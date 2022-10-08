// Name: Jeongwon Choi
// Student No.: A0219728U

// "Pea Soup and Pancakes" question from kattis; https://nus.kattis.com/contests/w8xrzu/problems/peasoup
import java.util.*;

public class PeaSoup {

	public static void main(String[] args){

		Scanner sc = new Scanner (System.in);
		int n = Integer.parseInt(sc.nextLine());

		for (int i = 0; i < n; i++){

			int k = Integer.parseInt(sc.nextLine());
			String restaurant = sc.nextLine();
			boolean pea_soup = false;
			boolean pancakes = false;

			for (int j = 0; j < k; j++){
				String menu = sc.nextLine();

				if (menu.equals("pea soup")){
					pea_soup = true;
				}

				else if (menu.equals("pancakes")){
					pancakes = true;
				}
			}
			if (pea_soup && pancakes){
				System.out.println(restaurant);
				break;
				}
			else if (i == n-1){
				System.out.println("Anywhere is fine I guess");
			}}
	}
}