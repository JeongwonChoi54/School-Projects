// Name: Jeongwon Choi
// Student No.: A0219728U

// "Assigning Workstations" question from Kattis; https://nus.kattis.com/problems/workstations

import java.util.*;

class Researcher{
    public int arrival;
    public int stay;

    public void setArrival(int n){
        arrival = n;
    }

    public void setStay(int n){
        stay =n;
    }

    public int getArrival(){
        return arrival;
    }

    public int getStay(){
        return stay;
    }
}

public class Work{

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.next());
        int m = Integer.parseInt(sc.next());

        Researcher[] researcher = new Researcher[n];

        for (int i = 0; i < n; i++){

            Researcher r = new Researcher();
            r.setArrival(Integer.parseInt(sc.next()));
            r.setStay(Integer.parseInt(sc.next()));

            researcher[i] = r;

        }

        Arrays.sort(researcher, Comparator.comparing(Researcher::getArrival));

        PriorityQueue<Integer> station = new PriorityQueue<>();
        int output = 0;
        boolean x = false;

        for (int i = 0; i<n; i++){
        	while (station.peek()!= null){

				if (station.peek() + m < researcher[i].getArrival()){
					station.poll();
				}

				else if (station.peek() > researcher[i].getArrival()){
					station.offer(researcher[i].getArrival() + researcher[i].getStay());
					x = true;
					break;
				}
				else{
					station.poll();
					station.offer(researcher[i].getArrival() + researcher[i].getStay());
					output++;
					x = true;
					break;
				}      		
        	}
        	if (x == false){
        		station.offer(researcher[i].getArrival() + researcher[i].getStay());
        	}
        }

        System.out.println(output);
    }
}