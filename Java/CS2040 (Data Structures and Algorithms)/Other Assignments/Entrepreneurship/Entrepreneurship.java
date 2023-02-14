/**
 * Name: Jeongwon Choi
 * Matric. No: A0219728U
 */

import java.lang.*;
import java.io.*;
import java.util.*;

class Order{
	double num_of_pizza;
	double price;

	public Order(double num_of_pizza, double price){
		this.num_of_pizza = num_of_pizza;
		this.price = price;
	}

	public double getNum_pizza(){
		return num_of_pizza;
	}

	public double getPrice(){
		return price;
	}
}

class Batch{
	Order[] single_batch;
	int num_of_order;
	int dir; //set dir as 1 if 'L', -1 if 'R'
	int cur;

	public Batch(int num_of_order, int dir){
		this.num_of_order = num_of_order;
		this.dir = dir;
		this.single_batch = new Order[num_of_order];

		if (dir == 1){
			this.cur = 0;
		}
		else{
			this.cur = num_of_order -1;
		}
	}

	public void setBatch(Order order_x){
		single_batch[cur] = order_x;
		cur += dir;
	}

	public int getNumofOrder(){
		return num_of_order;
	}

	public Order get_ith(int i){
		return single_batch[i];
	}
}

class OrderSheet{
	Batch[] all_order;
	int cur;
	double output;
	int max;

	public OrderSheet(int orders, int max){
		this.all_order = new Batch[orders];
		this.cur = 0;
		this.output = 0.0;
		this.max = max;
	}

	public void cancel(int num){
		cur -= num;
		cur = Math.max(cur, 0);
	}

	public void add(Batch batch){
		all_order[cur] = batch;
		cur += 1;
	}

	public void outcome(){
		double pizzas = 0;
		int ith_batch = 0;
		while (pizzas < max && ith_batch < cur){
			Batch orders = all_order[ith_batch];
			for (int i = 0; i < orders.getNumofOrder(); i++){
				Order pizza = orders.get_ith(i);
				double update = pizzas + pizza.getNum_pizza();
				if (update < max){
					pizzas = update;
					output += (pizza.getNum_pizza() * pizza.getPrice());
				}
				else if (update == max){	
					pizzas = update;	
					output += (pizza.getNum_pizza() * pizza.getPrice());
					break;
				}

				else{
					continue;
				}
			}
			ith_batch += 1;
		}
		String x = String.format("%.1f", output);
		System.out.println(x);

	}

}

public class Entrepreneurship {
	public static void main(String args[]) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer input = new StringTokenizer(br.readLine());
		int orders = Integer.parseInt(input.nextToken());
		int max = Integer.parseInt(input.nextToken());	
		
		if (orders == 0 || max == 0){
			System.out.println(0.0);
    		}
		
		else{
			OrderSheet ordersheet = new OrderSheet(orders, max);
	    		for (int i = 0; i < orders; i++){
	    			input = new StringTokenizer(br.readLine());	

	    			if (input.countTokens()==3){
					input.nextToken();
		    			int num_pizza = Integer.parseInt(input.nextToken());
		    			String dir = input.nextToken();
					Batch batch;
					input = new StringTokenizer(br.readLine());	
		    
		    			if (dir.equals("L")){
			    			batch = new Batch(num_pizza, 1);
		    			}
		    			else{
						batch = new Batch(num_pizza, -1);
		    			}

		    			for (int k = 0; k < num_pizza; k++){
						Order order = new Order(Double.parseDouble(input.nextToken()), 
								Double.parseDouble(input.nextToken()));
			    			batch.setBatch(order);
		    			}
		    			ordersheet.add(batch);
	    			}

	    			else{
					input.nextToken();
		    			ordersheet.cancel(Integer.parseInt(input.nextToken()));
	    			}
    			}

    			ordersheet.outcome();
		}
	}
}
