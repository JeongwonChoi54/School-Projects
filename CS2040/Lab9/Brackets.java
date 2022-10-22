/**
 * Name: Jeongwon Choi
 * Matric. No: A0219728U
 */

import java.util.*;

class Operation{
	public Stack<Long> sequence;
	public int open_bracket;
	
	public Operation(){
		sequence = new Stack<>();
		open_bracket = 0;
	}

	public void put(String value){
		if (value.equals("(")){
			open_bracket+= 1;
			sequence.push(Long.valueOf(-1));
		}
		else if (value.equals(")")){
			calculate();		
		}
		else{
			sequence.push(Long.parseLong(value));
		}
	}

	public void calculate(){
		long output = 0;
		if (open_bracket % 2 == 0){
			while (!sequence.isEmpty() && sequence.peek() != -1){
				output = (output + sequence.pop()) % 1_000_000_007;
			}
		}

		else{
			output = 1;
			while (!sequence.isEmpty() && sequence.peek() != -1){
				output = (output * sequence.pop()) % 1_000_000_007;
			}
		}
		if (!sequence.isEmpty()){
			sequence.pop();
			open_bracket -= 1;
		}
		sequence.push(output);
	}

	public void result(){
		long output = 0;
		while (!sequence.isEmpty()){
			output = (output + sequence.pop()) % 1_000_000_007;
		}
		System.out.println(output);
	}	
}
		
					

public class Brackets {
	public static void main(String args[]) {
    	Kattio io = new Kattio(System.in);
		Operation equation = new Operation();
		int n = io.getInt();
		for (int i = 0; i < n; i++){
			equation.put(io.getWord());
		}
		equation.result();
	}
}
