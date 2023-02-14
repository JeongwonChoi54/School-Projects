// Name: Jeongwon Choi
// Student No.: A0219728U

// "Sort of Sorting" question from Kattis; https://nus.kattis.com/contests/ppqrri/problems/sortofsorting

import java.util.*;

public class Sorting {

	public static void main(String[] args){

		Scanner sc = new Scanner (System.in);
		int n = sc.nextInt();

		while (n != 0){
			String[] names = new String[n];
			for (int i = 0; i<n; i++){
				names[i] = sc.next();
			}
			Arrays.sort(names, (name1, name2) -> name1.charAt(1) - name2.charAt(1));
			Arrays.sort(names, (name1, name2) -> name1.charAt(0) - name2.charAt(0));
			for (int j = 0; j < n; j++){
				System.out.print(names[j].toString() + '\n');
			}
			System.out.print('\n');
			n = sc.nextInt();
		}

	}
}
