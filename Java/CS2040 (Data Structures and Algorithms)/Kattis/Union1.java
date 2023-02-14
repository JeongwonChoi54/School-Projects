// Name: Jeongwon Choi
// Student No.: A0219728U

// "Almost Union-Find" question from Kattis; https://nus.kattis.com/problems/almostunionfind

import java.util.*;
import java.io.*;

class UnionFind{
	public int[] p;
	public HashMap<Integer, HashMap<Integer, Integer>> sets;

	public UnionFind(int n) {
		sets = new HashMap<Integer, HashMap<Integer, Integer>>();
		p = new int[n+1];
		for (int i = 1; i < (n+1); i++){
			p[i] = i;
			HashMap<Integer, Integer> val = new HashMap<Integer, Integer>();
			val.put(i, 1);
			sets.put(i, val);
		}
	}

	public Boolean isSameSet(int i, int j){
		return p[i] == p[j];
	}

	public void UnionSet(int i, int j){
		if (!isSameSet(i, j)){
			int x = sets.get((Object) p[i]).size();
			int y = sets.get((Object) p[j]).size();

			if (x>= y){
				HashMap<Integer, Integer> val = sets.get((Object) p[i]);
				for (int key: sets.get((Object) p[j]).keySet()){
					val.put(key, 1);
					p[key] = p[i];
				}
				sets.put(p[i], val);
			}

			else {
				HashMap<Integer, Integer> val = sets.get((Object) p[j]);
				for (int key: sets.get((Object) p[i]).keySet()){
					val.put(key, 1);
					p[key] = p[j];
				}
				sets.put(p[j], val);
			}

		}

	}

	public void Move(int i, int j){
		if (!isSameSet(i, j)){
			HashMap<Integer, Integer> val = sets.get((Object) p[j]);
			val.put(i, 1);
			sets.put(p[j], val);

			HashMap<Integer, Integer> val1 = sets.get((Object) p[i]);
			val1.remove((Object) i);
			sets.put(p[i], val1);

			p[i] = p[j];
		}


	}

	public int Num(int i){
		return sets.get((Object) p[i]).size();
	}

	public long Sum(int i){
		long sum = 0;
		for (int key: sets.get((Object) p[i]).keySet()){
			sum += key;
		}

		return sum;
	}
}

public class Union1{

	public static void main(String[] args) throws IOException{

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String input = "";

		while((input = br.readLine()) != null){

			StringTokenizer st = new StringTokenizer(input);

			int n = Integer.parseInt(st.nextToken());

			UnionFind almost = new UnionFind(n);

			int m = Integer.parseInt(st.nextToken());

			for (int i = 0; i<m; i++){
				StringTokenizer str = new StringTokenizer(br.readLine());
				int k = Integer.parseInt(str.nextToken());

				if (k == 1){
					int x = Integer.parseInt(str.nextToken());
					int y = Integer.parseInt(str.nextToken());

					almost.UnionSet(x, y);
				}

				else if (k == 2){
					int x = Integer.parseInt(str.nextToken());
					int y = Integer.parseInt(str.nextToken());

					almost.Move(x, y);
				}

				else{
					int x = Integer.parseInt(str.nextToken());
					int num = almost.Num(x);
					long sum = almost.Sum(x);
					System.out.println(num + " "+ sum);

				}
			}
		}



	}
}