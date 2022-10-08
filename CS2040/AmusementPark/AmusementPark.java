/**
 * Name : Jeongwon Choi
 * Matric. No : A0219728U
 */
import java.io.*;
import java.lang.*;
import java.util.*;

//LinkedList of all the Days; RideList as Nodes
class RideDay{
    public RideList firstday;
    public RideList today;

    public void RideDay(RideList each_day){
        if (today == null){
            firstday = each_day;
            today = each_day;
        }

        else {
            today.tom = each_day;
            today = each_day;
        }
    }

    public void output(){
        
        today = firstday;

        while (today != null){
            if (today.getSize() == 0){
                System.out.println("Day " + today.getDay() + ": []");
                today = today.tom;
            }

            else{
                System.out.print("Day "+ today.getDay() + ": [");
                today.outcome();
                today = today.tom;
            }
        }
    }
    
}

//DoubleTailedLinkedList of rides of each day; Ride as nodes
class RideList{
    public Ride head;
    public Ride tail;
    public int size = 0;
    public int day;
    public RideList tom;

    //Each Ride that will go inside the RideList as nodes
    class Ride{
        public int ride;
        Ride prev;
        Ride next;

        public Ride(int ride){
            this.ride = ride;
        }

        public int get(){
            return ride;
        }
    }

    //Initiate each day of ride, setting the first Ride node as head and tail
    //Pre-con: 0 < day, 0 < a where both are integers
    //Post-con: Create DoubleTailedLinkedList where 'a' becomes both head and tail
    public void start(int day, int a){
        Ride first = new Ride(a);
        head = first;
        tail = first;
        size = 1;
        this.day = day;
    }
    
    //Add Ride 'a' as next node of tail and 'a' becomes the tail node
    public void next(int a){
        Ride next_ride = new Ride(a);

        //All the rides have been removed and need to re-set the head and tail references
        if (size == 0){
            start(day, a);
        }

        //Case when head and tail references are referring to the same node
        else if (size == 1){
            head.next = next_ride;
            tail = next_ride;
            tail.prev = head;
            size += 1;
        }

        else{
            tail.next = next_ride;
            next_ride.prev = tail;
            tail = tail.next;
            size += 1;
        }
    }

    //Delete 'a' number of nodes from Front
    //Pre-con: 0 < a where 'a' is an integer
    //Post-con: 'a' number of rides are removed from the front
    public void delete_f(int a){
        //Number of rides that the brother wishes to remove is bigger than the
        //number of rides in the list
        if (a > Math.min(100, size)){
            System.out.println("Invalid command");
        }

        //All the rides are removed
        else if (a == size){
            head = null;
            tail = null;
            size = 0;
        }

        else {
            while (a > 0){
                head = head.next;
                size -= 1;
                a -= 1;
            }
        }
    }

    //Delete 'a' number of nodes from Back
    //Pre-con: 0 < a where 'a' is an integer
    //Post-con: 'a' number of rides are removed from the back
    public void delete_b(int a){
        //Number of rides that the brother wishes to remove is bigger than the
        //number of rides in the list
        if (a > Math.min(100, size)){
            System.out.println("Invalid command");
        }

        //All the rides are removed
        else if (a == size){
            head = null;
            tail = null;
            size = 0;
        }

        else {
            while (a > 0){
                tail = tail.prev;
                size -= 1;
                a -= 1;
            }
            tail.next = null;
        }
    }

    //Remove x number of Ride nodes from front and add node 'a' to the front
    //Pre-con: 0 < x, 0 < a where 'a' and x are integers
    //Post-con: 'a' number of rides are removed from front and 'a' ride is added to the front
    public void change_f(int x, int a){
        //Number of rides that the brother wishes to remove is bigger than the
        //number of rides in the list
        if (x > Math.min(100, size)){
            System.out.println("Invalid command");
        }

        //Remove all current rides and add 'a' as head & tail
        else if (x == size){
            Ride node = new Ride(a);
            head = node;
            tail = node;
            size = 1;
        }

        else{
            delete_f(x-1);
            Ride node = new Ride(a);
            node.next = head.next;
            head.next.prev = node;
            head = node;
        }
    }

    //Remove x number of Ride nodes from back and add node 'a' to the back
    //Pre-con: 0 < x, 0 < a where 'a' and x are integers
    //Post-con: 'a' number of rides are removed from back and 'a' ride is added to the back
    public void change_b(int x, int a){
        if (x > Math.min(100, size)){
            System.out.println("Invalid command");
        }

        //Remove all current rides and add 'a' as head & tail
        else if (x == size){
            Ride node = new Ride(a);
            head = node;
            tail = node;
            size = 1;
        }

        else {
            delete_b(x-1);
            Ride node = new Ride(a);
            node.prev = tail.prev;
            tail.prev.next = node;
            tail = node;
        }
    }

    public int getDay(){
        return day;
    }

    public int getSize(){
        return size;
    }

    public void outcome(){

        Ride cur = head;

        while (cur.next != null){
            System.out.print(cur.get() + ", ");
            cur = cur.next;
        }
        System.out.println(tail.get() + "]");

    }
}


public class AmusementPark {
  public static void main(String args[]) throws IOException{
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      int day = 1;
      String input;
      StringTokenizer string = new StringTokenizer("");
      RideDay days = new RideDay();

      //Terminate only when the input is "END" (1 word)
      while(string.countTokens() != 1){
        input = br.readLine();
        string = new StringTokenizer(input);

        //Case of input being just "END" 
        if (string.countTokens() == 1){
            break;
        }
        
        string.nextToken();
        string.nextToken();
        int first = Integer.parseInt(string.nextToken());
        RideList ride = new RideList();
        ride.start(day, first);

        input = br.readLine();
        string = new StringTokenizer(input);

        //Terminate only when the input is "END" or "NEXT DAY"
        while(string.countTokens() != 1 && string.countTokens() != 2){

            //Case of "NEXT RIDE: A"
            if (string.countTokens() == 3){
                string.nextToken();
                string.nextToken();
                ride.next(Integer.parseInt(string.nextToken()));
            }

            //Case of "DELETE FRONT/BACK RIDE: X"
            else if (string.countTokens() == 4){
                string.nextToken();
                StringTokenizer front_back = new StringTokenizer(string.nextToken(), "FB", true);
                string.nextToken();
                if (front_back.nextToken().equals("F")){
                    ride.delete_f(Integer.parseInt(string.nextToken()));
                }
                else{
                    ride.delete_b(Integer.parseInt(string.nextToken()));
                }
            }

            //Case of "CHANGE FRONT/BACK RIDE: X A"
            else{
                string.nextToken();
                StringTokenizer front_back = new StringTokenizer(string.nextToken(), "FB", true);
                string.nextToken();
                if (front_back.nextToken().equals("F")){
                    ride.change_f(Integer.parseInt(string.nextToken()), Integer.parseInt(string.nextToken()));
                }
                else{
                    ride.change_b(Integer.parseInt(string.nextToken()), Integer.parseInt(string.nextToken()));
                }
            }

            input = br.readLine();
            string = new StringTokenizer(input);
        }
        days.RideDay(ride);
        day += 1;


      }
      days.output();


  }
}