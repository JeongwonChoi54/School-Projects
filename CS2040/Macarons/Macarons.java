/**
 * Name: Jeongwon Choi
 * Matric. No: A0219728U
 */

import java.util.*;

public class Macarons {
	public static void main(String args[]) {
		Kattio io = new Kattio(System.in);
		int macarons = io.getInt();
		int divisor = io.getInt();
	
		//Stores the <(sum of macarons)%d, number of occurrence>
		HashMap<Integer, Integer> sum = new HashMap<>();
	
		int macaronsum = 0;
	
		//PreCond: the number of macaron to be added is an integer, 1 <= added macarons <= 10^9
		//		   added to macaronsum from io.getInt()
		//PostCond: update the hashmap sum which stores the integer that is the 
		//			(sum of all the macarons that had been added so far)%divisor as key
		// 			and the total number of each occurrences as its value
		// -> Need to find the number of contiguous subsequences of maracons where sum of the numbers of
		//    maracon is divisible by divisor
		//    Implementing the idea where given an array of numbers, if set array[i] as the sum upto ith item,
		//    sum from (i+1)th to jth item would be array[j]-array[i],
		//    Case 1. if the (sum upto macarons in ith box)%divisor == (sum upto macarons in jth box)%divisor,
		//    		  sum of macarons from (i+1)th to jth input would be divisible by d
		//	  Case 2. if the (sum upto macarons in ith box)%divisor == 0, it fulfills the condition
 
		for (int i = 0; i<macarons; i++){
			macaronsum = (macaronsum + (io.getInt())%divisor) % divisor;
			sum.put(macaronsum, sum.getOrDefault(macaronsum, 0)+1);
		}

		int output = 0;

		// Case 2. from above

		output += sum.getOrDefault(0, 0);
		// Case 1. : adding cases of each occurrences > 1
		//			 since two of (sum upto macarons in ith box)%divisor need to be same,
		//			 calculate by adding all possible cases of choosing two indexes from the occurrences
		for (int occurrence : sum.values()){
			if (occurrence > 1){
				//Prevents integer overflow
				if (occurrence % 2 == 0){
					output += (occurrence/2)*(occurrence-1);
				}
				else{
					output += (occurrence-1)/2*(occurrence);
				}
			}
		}

		System.out.println(output);

  }
}
