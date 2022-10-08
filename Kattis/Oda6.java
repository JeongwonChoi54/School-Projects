// Name: Jeongwon Choi
// Student No.: A0219728U

// "Kattis's Quest" question from Kattis; https://nus.kattis.com/problems/kattissquest

import java.util.*;

public class Oda6{
	
	public static void main(String[] args){

		Scanner sc = new Scanner(System.in);
		int n = Integer.parseInt(sc.next());

		TreeMap<Integer, PriorityQueue<Integer>> map = new TreeMap<Integer, PriorityQueue<Integer>>();

		for (int i = 0; i < n; i++){
			String k = sc.next();

			if (k.equals("add")){

				int x = Integer.parseInt(sc.next());

				if (map.get(x) == null){
					PriorityQueue<Integer> priority = new PriorityQueue<>(Collections.reverseOrder());
					priority.add(Integer.parseInt(sc.next()));
					map.put(x, priority);
				}
				else{
					PriorityQueue<Integer> a = map.get(x);
					a.add(Integer.parseInt(sc.next()));
					map.replace(x, a);

				}
			}

			else{
				long gold = 0;

				int q = Integer.parseInt(sc.next());

				while(map.floorKey(q) != null){

					int a = map.floorKey(q);

					gold+= map.get(a).poll();
					q -= a;

					if (map.get(a).peek() == null){
						map.remove(a);
					}
				}

				System.out.println(gold);
			}

		}
	}
}



