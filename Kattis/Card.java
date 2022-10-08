// Name: Jeongwon Choi
// Student No.: A0219728U

// "Card Trading" question from Kattis; https://nus.kattis.com/contests/r3qri9/problems/cardtrading

import java.util.*;


class Type {

	public int val;
	public int numOfCards;
	public int buy;
	public int sell;
	public int loss;

	public Type(int ival){
		val = ival;
	}

	public int getVal(){
		return val;
	}

	public int getNumofCards(){
		return numOfCards;
	}

	public int getBuy(){
		return buy;
	}

	public int getSell(){
		return sell;
	}

	public void SetBuy(int n){
		buy = n;
	}

	public void SetSell (int n){
		sell = n;
	}

	public void addNumofCards(){
		numOfCards++;
	}

	public void SetLoss(int n){
		loss = n;
	}

	public int getLoss(){
		return loss;
	}


}


public class Card {

	public static void main(String[] args){

		Scanner sc = new Scanner (System.in);
		int numOfDeck = sc.nextInt();
		int typesOfCards = sc.nextInt();
		int totalCombo = sc.nextInt();
		sc.nextLine();

		Type[] c = new Type[typesOfCards];

		//Create Type1, Type2, ..., Type(typeOfCards) as array c

		for (int i = 1; i < typesOfCards+1; i++){
			c[i-1] = new Type(i);

		}

		//Increase the numOfCards for each Type

		for (int i = 0; i < numOfDeck; i++){
			int eachCard = sc.nextInt();
			c[eachCard-1].addNumofCards();
		}

		sc.nextLine();

		//Set cost of each type of cards

		for (int i = 0; i < typesOfCards; i++){
			c[i].SetBuy(sc.nextInt());
			c[i].SetSell(sc.nextInt());
			//Calculate loss by adding coins that could've be gained by sell + coins needed to buy
			c[i].SetLoss((2-c[i].getNumofCards())*c[i].getBuy() + c[i].getNumofCards()*c[i].getSell());
			sc.nextLine();
		}

		List<Type> cardTypes = Arrays.asList(c);

		Collections.sort(cardTypes, Comparator.comparingInt(Type::getLoss));

		long profit = 0;

		for (int j = 0; j < typesOfCards; j++){
			if (j < totalCombo){
				profit -= (2-c[j].getNumofCards())*c[j].getBuy();
			}
			else {
				profit += c[j].getNumofCards()*c[j].getSell();
			}
		}

		System.out.println(profit);


	}

}