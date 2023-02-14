// Name: Jeongwon Choi
// Student No.: A0219728U

//"Best Relay Team" question from kattis; https://nus.kattis.com/contests/uinqsa/problems/bestrelayteam

import java.util.*;

class Runner {
	public String name;
	public double a, b;

	public Runner(String name, double a, double b) {
		this.name = name;
		this.a = a;
		this.b = b;
	}

	public String getName() {
		return name;
	}

	public double get1() {
		return a;
	}

	public double get2() {
		return b;
	}

	public String toString(){
		return name;
	}
}

class Team {
	public Runner[] order;
	public double time;

	public Team(Runner[] order, double time){
		this.order = order;
		this.time = time;
	}

	public Runner[] getOrder(){
		return order;
	}

	public double getTime(){
		return time;
	}


}

public class BestRelayTeam {

	public static void main(String[] args){

		//Create list from user input

		Scanner sc = new Scanner (System.in);
		int n = Integer.parseInt(sc.nextLine());
		Runner[] p = new Runner[n];

		for (int i = 0; i < n; i++){
			p[i] = new Runner(sc.next(), sc.nextDouble(), sc.nextDouble());
		}

		List<Runner> list = Arrays.asList(p); 

		//Sort by 1st leg speed

		Collections.sort(list, Comparator.comparingDouble(Runner::get1));

		//Select 4 fastest by 1st leg and create array as 'leg1'

		Runner[] leg1 = new Runner[4];

		for (int i = 0; i < 4; i++){
			leg1[i] = list.get(i);
		}

		//Sort by 2nd leg speed

		Collections.sort(list, Comparator.comparingDouble(Runner::get2));

		//Select 4 fastest by 2nd leg and create array as 'leg2'

		Runner[] leg2 = new Runner[4];

		for (int i = 0; i < 4; i++){
			leg2[i] = list.get(i);
		}

		//Create Teams

		Team[] options = new Team[4];

		for (int i = 0; i<4; i++){
			Runner[] members = new Runner[4];
			members[0] = leg1[i];
			double total_time = leg1[i].get1();
			int k = 1;
			
			for (int j = 0; k<4; j++){ 
				if (leg2[j] == members[0]){
					continue;
				
				}

				else{
					members[k] = leg2[j];
					total_time += leg2[j].get2();
					k++;
				}

			}

			options[i] = new Team(members, total_time);
		}

		List<Team> list_of_teams = Arrays.asList(options);
		Collections.sort(list_of_teams, Comparator.comparingDouble(Team::getTime));

		Team fastest = list_of_teams.get(0);

		System.out.println(fastest.getTime());
		for (int i = 0; i<4; i++){
			System.out.println(fastest.getOrder()[i]);
		}




		








	}
}