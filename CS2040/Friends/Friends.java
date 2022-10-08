/**
 * Name: Jeongwon Choi
 * Matric. No: A0219728U
 */
import java.util.*;
import java.lang.*;

class Cafe{
	String name;
	ArrayList<Long> arrival;
	ArrayList<Long> departure;

	public Cafe(String name){
		this.name = name;
		arrival = new ArrayList<Long> ();
		departure = new ArrayList<Long> ();
	}

	public void setVisitors(long arriv, long depart){
		arrival.add(arriv);
		departure.add(depart);
	}
	 
}

public class Friends {
	public static void main(String args[]) {
		Kattio io = new Kattio(System.in);
		int numCafes = io.getInt();
		long tom = io.getLong();
		int max = 0;	
		ArrayList<String> cafes = new ArrayList<String>();

		for (int i=0; i<numCafes; i++){

			String name = io.getWord();
			int visitors = io.getInt();

			Cafe cafe = new Cafe(name);
		
			for (int j = 0; j < visitors; j++){
				cafe.setVisitors(io.getLong(), io.getLong());
			}

			Collections.sort(cafe.arrival);
			Collections.sort(cafe.departure);

			int max1 = 0;
			int max2 = 0;

			ArrayList<Long> people = new ArrayList<Long>();

			for (int k = 0; k < cafe.arrival.size(); k++){
				people.add(cafe.departure.get(k));
				max2++;
				while(people != null && people.get(0) < (cafe.arrival.get(k)-tom)){
					people.remove(0);
					max2--;
				}
				max1 = Math.max(max1, max2);
			}
			if (max1 > max){
				cafes.clear();
				cafes.add(name);
				max = max1;
			}
			else if (max1 == max){
				cafes.add(name);
			}
		}
		System.out.println(max);
		Collections.sort(cafes);
		for (int i = 0; i < cafes.size(); i++){
			System.out.println(cafes.get(i));
		}		
			    
  }
}
