// Name: Jeongwon Choi
// Student No.: A0219728U

// "Problem A Conformity" question from Kattis; https://nus.kattis.com/contests/vxu5di/problems/conformity

import java.util.*;
import java.io.*;


public class ODA4{
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int n = Integer.parseInt(sc.next());
		Integer output = 0;
		int num = 0;
		HashMap<Long, Integer> combo = new HashMap<Long, Integer>();

		for (int i = 0; i < n; i++){
			int[] courses = new int[5];
			for (int k = 0; k < 5; k++){
				courses[k] = Integer.parseInt(sc.next());
			}
			Arrays.sort(courses);

			long c = 0;
			for (int j = 0; j<5; j++){
				c += courses[j] * Math.round(Math.pow(1000, j));

			}
			combo.put(c, combo.getOrDefault(c, 0)+1);
			if (output == combo.get(c)){
				num += 1;
			}
			else if (output < combo.get(c)){
				num = 1;
				output = combo.get(c);
			}
		}

		System.out.println(output * num);
	}
}


