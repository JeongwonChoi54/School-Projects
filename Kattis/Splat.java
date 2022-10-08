// Name: Jeongwon Choi
// Student No.: A0219728U

// "Coconut Splat" question from Kattis; https://nus.kattis.com/contests/fmai9h/problems/coconut

import java.util.*;

class Hand{
	public int player;
	public boolean folded;
	public boolean up;
	public Hand next;
	public Hand prev;

	public Hand(int player){
		this(player, true, true, null, null);
	}

	public Hand(int p, boolean f, boolean u, Hand n, Hand previous){
		player = p;
		folded = f;
		up = u;
		next = n;
		prev = previous;
	}

	public int getPlayer(){
		return player;
	}

	public boolean getFolded(){
		return folded;
	}

	public boolean getUp(){
		return up;
	}

	public Hand getNext(){
		return next;
	}

	public Hand getPrev(){
		return prev;
	}

	public void setNext(Hand n){
		next = n;
	}

	public void setPrev(Hand n){
		prev = n;
	}

	public void setFist(){
		folded = false;
	}

	public void setDown(){
		up = false;
	}

}

class CircularLinked{
	public Hand cur;
	public Hand head;
	public Hand tail;

	public int numHand;

	public int size(){
		return numHand;
	}

	public Hand getCur(){
		return cur;
	}

	public void initiallize(int NumPlayers){

		for (int i = 1; i < NumPlayers+1; i++){
			if (i == 1){
				head = new Hand(i);
				cur = head;
				tail = head;
			}

			else{
				Hand newHand = new Hand(i);
				tail.setNext(newHand);
				newHand.setPrev(tail);
				tail = newHand;

				if (i == NumPlayers){
					tail.setNext(head);
					head.setPrev(tail);
				}
			}

			numHand++;
		}
	}

	public void game(Hand cur, int Syllables){
		while (numHand >1){
			for (int i = 0; i < Syllables-1; i++){
				cur = cur.getNext();
			}
			if (cur.getFolded()){
				cur.setFist();
				Hand n = new Hand(cur.getPlayer(), false, true, cur.getNext(), cur);
				cur.getNext().setPrev(n);
				cur.setNext(n);
				numHand++;
			}

			else{
				if (cur.getUp()){
					cur.setDown();
					cur = cur.getNext();
				}
				else{
					cur = cur.getPrev();
					cur.setNext(cur.getNext().getNext());
					cur.getNext().setPrev(cur);
					cur = cur.getNext();
					numHand--;

				}
			}
		}

		System.out.println(cur.getPlayer());
	}


}



public class Splat {
	public static void main(String[] args){

		Scanner sc = new Scanner(System.in);

		int Syllables = sc.nextInt();
		int NumPlayers = sc.nextInt();

		if (NumPlayers == 1){
			System.out.println(NumPlayers);
		}

		else{
			CircularLinked list = new CircularLinked();
			list.initiallize(NumPlayers);
			list.game(list.getCur(), Syllables);

		}




    }
}