This project uses python flask and postgresql for app itself.
In order to deploy the app, I have used tmux and AWS EC2

Create EC2 instance; here, ami-082b1f4237bd816a1 was used.
For easier management, open VScode and connect to EC2.
The necessary command can be found under SSH client of the EC2 instance you have created.

First, from terminal, connect to postgresql

```
sudo -i -u postgres
```
  
If postgres is not already installed,
```
sudo apt -y install postgresql-14
```
Create database, here I just used database named "database"
```
psql database
```

Run the sql queries under sql folder,
first creating table by running queires schema.sql,
followed by adding company, property, users, transactions.
Here, the data are randomly generated using www.mockaroo.com

Next, install pip and the requirements
```
sudo apt install python3-pip
pip install -r requirements.txt
```

Create tmux session
```
tmux new -s Flask
```
Here I named the session 'Flask'

If tmux is not installed,
```
sudo apt-get install tmux
```

Run the app
```
sudo python3 app.py
```
Open http://54.255.148.131:5000/
Adjust accordingly with the public ip address of the EC2 you have created


You'll see that the app is running on the website and thanks to tmux, the app will run in the background even if you exit the instance.
