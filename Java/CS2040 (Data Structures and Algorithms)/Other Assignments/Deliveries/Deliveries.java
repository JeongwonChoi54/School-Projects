/**
 * Name: Jeongwon Choi
 * Matric. No: A0219728U
 */

import java.util.*;

/*
Pre-Con: integer deadline (1<= deadline <= 2419200) and integer money (1<= money<= 100000)
Post-Con: 'Request' where it's deadline and amount of money paid for the delivery is stored
		  respectively
 */
class Request{
	public int deadline;
	public int money;

	public Request(int deadline, int money){
		this.deadline = deadline;
		this.money = money;
	}

	public int getDeadline(){
		return this.deadline;
	}

	public int getMoney(){
		return this.money;
	}

}

// Comparator to sort priority queue of Requests from fastest to slowest deadline
class DeadLineComparator implements Comparator<Request>{
	public int compare(Request request1, Request request2){
		return (request1.getDeadline() - request2.getDeadline());
	}
}

// Comparator to sort priority queue of Requests from lowest to highest amount of money paid 
class MoneyComparator implements Comparator<Request>{
	public int compare(Request request1, Request request2){
		return (request1.getMoney() - request2.getMoney());
	}
}

// output stores all the requests that can be fulfilled upto the current added Requests
// and the size keeps track of the number of requests in the priorityqueue, output
class FinalOutput{
	public PriorityQueue<Request> output;
	public int size;

	//Store Request in output pq by the amount of money paid for each Request from lowest to highest
	public FinalOutput(){
		this.output = new PriorityQueue<>(new MoneyComparator());
		this.size = 0; 
	}

	/* 
	Pre-Con: output pq storing the requests that would lead to max amount of money Tom can get from them 
			 and a new Request which could be added to pq to maximize Tom's profit
	Post-Con: 1) The deadline of newly added request is later than the requests in the output pq(<size)
				 => request is simply added to the output pq
			  2) output pq of requests is already filled upto the deadline of the newly added request
				 however, switching the new request with the request in output pq of lowest amount paid;
				 first item in output pq as it is stored from lowest to highest paying requests,
				 would increase the amount of money Tom can make
				 => remove the first request in output pq and add the new request to output pq 

	*/
	public void add(Request request){
		if (size < request.getDeadline()){
			output.add(request);
			size++;
		}
		else if (output.peek().getMoney() < request.getMoney()){
			output.remove();
			output.add(request);
		}
	}

	// Prints the amount of money Tom can make from the requests in output pq
	public void getEarnings(){
		long earnings = 0;
		while (!output.isEmpty()){
			earnings += output.poll().getMoney();
		}
		System.out.println(earnings);
	}

}

public class Deliveries {
	public static void main(String args[]) {
    	Kattio io = new Kattio(System.in);
		PriorityQueue<Request> delivery = new PriorityQueue<>(new DeadLineComparator());
		int num_requests = io.getInt();
		
		//Stores requests in delivery pq from fastest to slowest deadline
		for (int i = 0; i < num_requests; i++){
			Request request = new Request(io.getInt(), io.getInt());
			delivery.add(request);
		}
		
		FinalOutput delivery_request = new FinalOutput();
		while(!delivery.isEmpty()){
			delivery_request.add(delivery.poll());	
		}
		
		delivery_request.getEarnings();
		
	}
}
