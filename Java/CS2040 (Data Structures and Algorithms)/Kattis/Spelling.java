// Name: Jeongwon Choi
// Student No.: A0219728U

// "T9 Spelling" question from Kattis; https://nus.kattis.com/contests/jnqnar/problems/t9spelling

import java.util.*;

public class Spelling{

	public static void main(String[] args){

		Scanner sc = new Scanner (System.in);
		int n = Integer.parseInt(sc.nextLine());

		for (int i = 1; i < n+1; i++){

			String output = "Case #" + Integer.toString(i) + ": ";
			String text = sc.nextLine();

			for (int j = 0; j<text.length(); j++){

				char letter = text.charAt(j);
				int digit = 0;
				int numOfDigit = 1;

				if (String.valueOf(letter).equals(" ")){
				}

				else if (String.valueOf(letter).equals("z")){
					digit = 9;
					numOfDigit = 4;
				}

				else if (String.valueOf(letter).equals("s")){
					digit = 7;
					numOfDigit = 4;
				}

				else {
					int ascii = letter;

					if (ascii < 115){
						ascii -= 91;
					}

					else {
						ascii -= 92;
					}
					digit = ascii/3;
					numOfDigit = ascii%3 + 1;
				}

				if (String.valueOf(output.charAt(output.length()-1)).equals(" ")){
					}

					else if (Integer.parseInt(String.valueOf(output.charAt(output.length()-1))) == digit){
						output = output.concat(" ");
					}

					for (int k = 0; k < numOfDigit; k++){
						output+= Integer.toString(digit);
					}

			}
			System.out.println(output);
		}
	}
}