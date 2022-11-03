/**
 * Name: Jeongwon Choi
 * Matric. No: A0219728U
 */

import java.util.*;
import java.lang.*;


//Entries related to a participant with unknown status is stored
//unclear_indegree stores names of participants of unknown status that wrote this participant down
//unclear_outdegree stores names of participants of unknown status that this participant wrote down
//stu is a boolean value of whether a student wrote this participant down
//not_stu is a boolean value of whether this participant wrote down person that is not student
class Participant{
	public String name;
	public HashSet<String> unclear_indegree;
	public HashSet<String> unclear_outdegree;
	public boolean stu;
	public boolean not_stu;

	public Participant(String name){
		this.name = name;
		this.unclear_indegree = new HashSet<>();
		this.unclear_outdegree = new HashSet<>();
		this.stu = false;
		this.not_stu = false;
	}

	public void addIn(String entry_from){
		unclear_indegree.add(entry_from);
	}

	public void addOut(String entry_to){
		unclear_outdegree.add(entry_to);
	}

	public void hasStu(){
		stu = true;
	}

	public void hasNot_Stu(){
		not_stu = true;
	}

	public void In_to_Stu(String entry_from){
		unclear_indegree.remove(entry_from);
		stu = true;
	}

	public void Out_to_NotStu(String entry_to){
		unclear_outdegree.remove(entry_to);
		not_stu = true;
	}

	public HashSet<String> getIn(){
		return unclear_indegree;
	}

	public HashSet<String> getOut(){
		return unclear_outdegree;
	}

	//when there are no unclear entries and there exists entry made by a student to this participant and
	//this participant made an entry to a person that is not a student, by the rule of the contest everyone wins
	//; s -> ? -> n since if this participant is a student, s -> s -> n and if not student, s -> n -> n
	//both cases would allow for a case where a student writes down a person that is not a student
	public boolean check_win(){
		if (stu && not_stu && unclear_indegree.size() == 0 && unclear_outdegree.size() == 0){
			return true;
		}
		else {
			return false;
		}
	}

	public String getName(){
		return name;
	}

	public boolean boolStu(){
		return stu;
	}

	public boolean boolNotStu(){
		return not_stu;
	}


}

//Comparator that sorts <Participant>s by 'the number of entries made by the participant to person of unknown status'
class OutdegreeComp implements Comparator<Participant>{
	public int compare(Participant p1, Participant p2){
		return p1.getOut().size() - p2.getOut().size();
	}
}

//Stores each participant's name into corresponding hashset of one's status; student, not_student, unknown
//Stores hashset where key refers to the name and value refers to the corresponding Participant of the participant in unclear_edges
//keeping track of entries in case the one who made the entry, the one who has been written down or both of their status is unclear
//Stores integer, output, with default value of -1; change to 1 for victory if there exists entry from student to not student
class Contest{
	public HashSet<String> student;
	public HashSet<String> not_student;
	public HashSet<String> unknown;
	public HashMap<String, Participant> unclear_edges;
	public int output;

	public Contest(){
		this.student = new HashSet<>();
		this.not_student = new HashSet<>();
		this.unknown = new HashSet<>();
		this.unclear_edges = new HashMap<>();
		this.output = -1;
	}

	//Pre-Cond: Input of two strings
	//          First string refers to the name of the participant upto 10 English alphabets and had not been added previously
	//          Second string refers to the status of the participant; "s" = student, "n" = "not student", "?" = unknown
	//Post-Cond: Name of the participant added to the corresponding hashset of their status
	public void put(String name, String student_status){
		if (student_status.equals("s")){
			student.add(name);
		}
		else if (student_status.equals("n")){
			not_student.add(name);
		}
		else{
			unknown.add(name);
		}
	}

	//Pre-Cond: Input of two strings where first one is the name of the person who made the entry
	//          second being the name of the person whose name has been written down
	//          names must have already been added with their status from put(name, status)
	//Post-Cond: If first person is a student and second is not a student; s -> n, value of output updated to 1
	//           when either one of the person's status is unknown, the unknown person is added to unclear_edges as key with
	//           it's <Participant> as value
	public void entry(String participant, String entry){
		if (student.contains(participant)){
			// s -> n
			if (not_student.contains(entry)){
				output = 1;
			}
			// s -> ?
			// If second person is already added to unclear_edges, just check there exists entry made from a student to the second person
			// in it's <Participant>
			// Else, create <Participant> of second person, set boolean value stu to true,
			// add to unclear_edges as <second person's name, <Participant> of the second person>
			else if (unknown.contains(entry)){
				if (unclear_edges.get(entry) == null){
					unclear_edges.put(entry, new Participant(entry));
				}
				Participant cur_entry = unclear_edges.get(entry);
				cur_entry.hasStu();
				unclear_edges.put(entry, cur_entry);
			}
		}

		else if (unknown.contains(participant)){
			// ? -> ?
			// Add <person's name, <Participant> of the corresponding person> of both person that is not already in unclear_edge
			// For the first person, add second person's name to unclear_outdegree for the <Participant>
			// For the second person, add first person's name to unclear_indegree for the <Participant>
			if (unknown.contains(entry)){
				if (unclear_edges.get(participant) == null){
					unclear_edges.put(participant, new Participant(participant));
				}
				if (unclear_edges.get(entry) == null){
					unclear_edges.put(entry, new Participant(entry));
				}
				Participant cur_participant = unclear_edges.get(participant);
				cur_participant.addOut(entry);
				unclear_edges.put(participant, cur_participant);
				Participant cur_entry = unclear_edges.get(entry);
				cur_entry.addIn(participant);
				unclear_edges.put(entry, cur_entry);
			}
			// ? -> n
			// Add <first person's name, <Participant> of first person> if not already in unclear_edge
			// for the first person's <Participant> set boolean value of not_stu to true; noting there exists entry to not student
			// made by the first person
			else if (not_student.contains(entry)){
				if (unclear_edges.get(participant) == null){
					unclear_edges.put(participant, new Participant(participant));
				}
				Participant cur_participant = unclear_edges.get(participant);
				cur_participant.hasNot_Stu();
				unclear_edges.put(participant, cur_participant);
			}
		}
	}

	//Pre-Cond: outcome is unclear and unclear_edges is not empty
	//Post-Cond: return 1 if win is found, else 0
	public int check_unclear(){
		PriorityQueue<Participant> sort = new PriorityQueue<>(new OutdegreeComp());
		for (Map.Entry<String, Participant> edge : unclear_edges.entrySet()){
			Participant participant = edge.getValue();
			if (participant.check_win()){
				return 1;
			}
			//Add only <Participant>s with no unclear incoming edges, has incoming edge from student and outcoming edge exists
			//;there aren't any unclear incoming edges, safe to assume the status of the person as student
			//since if assume as not student, immediately a victory where entry made by student to not student occurs
			//added to 'sort' priority queue from smaller number of unclear outdegree edges
			if (participant.boolStu() && participant.getIn().size() == 0){
				if (participant.getOut().size() != 0 || participant.boolNotStu()){
					sort.add(participant);
				}
			}
		}

		//loop until there are no more cases to check; 'sort' is empty or victory is found and loop breaks, returning 1
		while (!sort.isEmpty()){
			Participant cur_participant = sort.poll();
			if (cur_participant.check_win()){
				return 1;
			}
			//As the status of person with only the indegree edges from student can be assumed as student as well,
			//check all the outdegree edges of the person and update the corresponding <Participant> in outdegree edges by
			//removing the person from the <Participant>'s unclear_indegree and check there exists indegree edge from student
			//check if case of win is made
			//if the updated <Participant>'s unclear_indegree is also empty, add to sort priorityqueue and repeat the process
			for (String entry : cur_participant.getOut()){
				Participant cur_entry = unclear_edges.get(entry);
				cur_entry.In_to_Stu(cur_participant.getName());
				if (cur_entry.check_win()){
					return 1;
				}
				if (cur_entry.getIn().size() == 0){
					if (cur_entry.getOut().size() != 0 || cur_entry.boolNotStu()){
						sort.add(cur_entry);
					}
				}
			}
		}
		return 0;
	}

	//If output had been updated to 1 in the process of entry(name1, name2) as student wrote down not student, print VICTORY
	//If output still remains 0 and there are no unclear entries that can change the outcome to win; unclear_edges empty, print EVERYONE LOSES
	//Else run check_unclear() to see whether it is unclear or a win
	public void outcome(){
		if (output == 1){
			System.out.println("VICTORY");
		}
		else if (unclear_edges.size() == 0){
			System.out.println("EVERYONE LOSES");
		}
		else{
			int output_val = check_unclear();
			if (output_val == 1){
				System.out.println("VICTORY");
			}
			else{
				System.out.println("OUTCOME UNCLEAR");
			}
		}
	}

}

public class Students {
	public static void main(String args[]) {
    	Kattio io = new Kattio(System.in);
    	//Number of participants a integer; 1 <= participants <= 10^5
		int participants = io.getInt();
		//Number of entries made a integer; 0 <= number of entries <= min(participants x (participants-1)/2, 2 x 10^5)
		int entries = io.getInt();
		//Initialize class Contest
		Contest contest= new Contest();
		for (int i = 0; i < participants; i++){
			//First word the name of the participant and the second word the status of participant(s/n/?)
			contest.put(io.getWord(), io.getWord());
		}

		for (int i = 0; i < entries; i++){
			//Input given inthe format of 'Name1 -> Name2' where Name1 and Name2 have already been given in contest.put(Name, status)
			String participant = io.getWord();
			io.getWord();
			String cur_entry = io.getWord();
			contest.entry(participant, cur_entry);
		}
		contest.outcome();

	}
}