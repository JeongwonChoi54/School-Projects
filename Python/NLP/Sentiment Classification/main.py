import os
import re
import sys
import string
import argparse
import datetime

import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
from torch.utils.data import Dataset, DataLoader

torch.manual_seed(0)


class SentDataset(Dataset):
    """
    Define a pytorch dataset class that accepts a text path, and optionally label path and
    a vocabulary (depends on your implementation). This class holds all the data and implement
    a __getitem__ method to be used by a Python generator object or other classes that need it.

    DO NOT shuffle the dataset here, and DO NOT pad the tensor here.
    """
    def __init__(self, train_path, label_path=None, vocab=None):
        """
        Read the content of vocab and text_file
        Args:
            vocab (string): Path to the vocabulary file.
            text_file (string): Path to the text file.
        """

        f = open(train_path, 'r')
        self.texts = []
        for tokens in f.readlines():
            tokens = re.findall('[a-z]+', tokens.lower())
            bigrams = [(bigrams) for bigrams in zip(tokens[:-1], tokens[1:])]
            self.texts.append(bigrams)
        
        if label_path is not None:
            with open(label_path, 'r') as f:
                self.labels = [int(label) for label in f.readlines()]

        else:
            self.labels = [0] * len(self.texts)

        if vocab is not None:
            self.vocab = vocab
        
        else:
            voc = {}
            for i in range(len(self.texts)):
                for bigrams in self.texts[i]:
                    voc[(bigrams)] = voc.get((bigrams), 0) + 1
            
            self.vocab = {}
            self.vocab['<unk>'] = 1
            idx = 2
            for bigram, count in voc.items():
                if count > 5:
                    self.vocab[bigram] = idx
                    idx += 1


    def vocab_size(self):
        """
        A function to inform the vocab size. The function returns two numbers:
            num_vocab: size of the vocabulary
        """
        num_vocab = len(self.vocab)
        return num_vocab
    
    def __len__(self):
        """
        Return the number of instances in the data
        """
        return len(self.texts)

    def __getitem__(self, i):
        """
        Return the i-th instance in the format of:
            (text, label)
        Text and label should be encoded according to the vocab (word_id).

        DO NOT pad the tensor here, do it at the collator function.
        """
        text = [self.vocab.get(bigram, self.vocab['<unk>']) for bigram in self.texts[i]]
        label = self.labels[i]
        
        return text, label


class Model(nn.Module):
    """
    Define your model here
    """
    def __init__(self, num_vocab):
        super().__init__()
        # define your model attributes here
        self.embedding = nn.Embedding(num_vocab+1, 10)
        self.linear1 = nn.Linear(10, 5)
        self.dropout = nn.Dropout(0.3)
        self.linear2 = nn.Linear(5, 1)

    def forward(self, x):
        # define the forward function here
        x = self.embedding(x)
        x = x.mean(dim=1)
        x = self.linear1(x)
        x = F.relu(x)
        x = self.dropout(x)
        x = self.linear2(x)
        x = F.sigmoid(x)

        return x


def collator(batch):
    """
    Define a function that receives a list of (text, label) pair
    and return a pair of tensors:
        texts: a tensor that combines all the text in the mini-batch, pad with 0
        labels: a tensor that combines all the labels in the mini-batch
    """
    texts = [torch.tensor(text) for text, label in batch]
    texts = nn.utils.rnn.pad_sequence(texts, batch_first=True, padding_value=0)
    labels = torch.tensor([[label] for text, label in batch])
    
    return texts, labels


def train(model, dataset, batch_size, learning_rate, num_epoch, device='cpu', model_path=None):
    """
    Complete the training procedure below by specifying the loss function
    and optimizers with the specified learning rate and specified number of epoch.
    
    Do not calculate the loss from padding.
    """
    data_loader = DataLoader(dataset, batch_size=batch_size, collate_fn=collator, shuffle=True)

    # assign these variables
    criterion = F.binary_cross_entropy
    optimizer = optim.Adam(model.parameters(), lr=learning_rate)

    start = datetime.datetime.now()
    for epoch in range(num_epoch):
        model.train()
        running_loss = 0.0
        for step, data in enumerate(data_loader, 0):
            # get the inputs; data is a list of [inputs, labels]
            texts = data[0].to(device)
            labels = data[1].to(device)

            # zero the parameter gradients
            optimizer.zero_grad()

            # do forward propagation
            predictions = model(texts)

            # calculate the loss
            loss = criterion(predictions.float(), labels.float())

            # do backward propagation
            loss.backward()

            # do the parameter optimization
            optimizer.step()

            # calculate running loss value for non padding
            mask = (texts != 0).float()
            loss = (loss * mask).sum() / mask.sum()
            running_loss += loss.item()

            # print loss value every 100 iterations and reset running loss
            if step % 100 == 99:
                print('[%d, %5d] loss: %.3f' %
                    (epoch + 1, step + 1, running_loss / 100))
                running_loss = 0.0

    end = datetime.datetime.now()
    
    # define the checkpoint and save it to the model path
    # tip: the checkpoint can contain more than just the model
    checkpoint = {
        'vocab': dataset.vocab,
        'model_state_dict': model.state_dict()
    }
    torch.save(checkpoint, model_path)

    print('Model saved in ', model_path)
    print('Training finished in {} minutes.'.format((end - start).seconds / 60.0))


def test(model, dataset, thres=0.5, device='cpu'):
    model.eval()
    data_loader = DataLoader(dataset, batch_size=20, collate_fn=collator, shuffle=False)
    labels = []
    with torch.no_grad():
        for data in data_loader:
            texts = data[0].to(device)
            predictions = model(texts)
            predictions = (predictions > thres).int().squeeze().tolist()
            labels.extend([str(pred) for pred in predictions])

    return labels


def main(args):
    if torch.cuda.is_available():
        device_str = 'cuda:{}'.format(0)
    else:
        device_str = 'cpu'
    device = torch.device(device_str)
    
    assert args.train or args.test, "Please specify --train or --test"
    if args.train:
        assert args.label_path is not None, "Please provide the labels for training using --label_path argument"
        dataset = SentDataset(args.text_path, args.label_path)
        num_vocab = dataset.vocab_size()
        model = Model(num_vocab).to(device)
        
        # specify these hyper-parameters
        batch_size = 32
        learning_rate = 0.01
        num_epochs = 10

        train(model, dataset, batch_size, learning_rate, num_epochs, device, args.model_path)
    if args.test:
        assert args.model_path is not None, "Please provide the model to test using --model_path argument"
        
        # load the checkpoint
        checkpoint = torch.load(args.model_path)

        # create the test dataset object using SentDataset class
        dataset = SentDataset(args.text_path, vocab=checkpoint['vocab'])

        # initialize and load the model
        model = Model(dataset.vocab_size()).to(device)
        model.load_state_dict(checkpoint['model_state_dict'])

        # run the prediction
        preds = test(model, dataset, 0.5, device)

        # write the output
        with open(args.output_path, 'w', encoding='utf-8') as f:
            f.write('\n'.join(preds))
    print('\n==== All done ====')


def get_arguments():
    parser = argparse.ArgumentParser()
    parser.add_argument('--text_path', help='path to the text file')
    parser.add_argument('--label_path', default=None, help='path to the label file')
    parser.add_argument('--train', default=False, action='store_true', help='train the model')
    parser.add_argument('--test', default=False, action='store_true', help='test the model')
    parser.add_argument('--model_path', required=True, help='path to the model file during testing')
    parser.add_argument('--output_path', default='out.txt', help='path to the output file during testing')
    return parser.parse_args()

if __name__ == "__main__":
    args = get_arguments()
    main(args)