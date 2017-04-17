import java.io.*;				// Needed for File class.
import java.util.Scanner;			// Needed for Scanner class.
import java.util.ArrayList;			// Needed for ArrayList class.

/*
 * PROGRAM: OS.java
 * @author: Moustapha Dieng
 * This program simulates several operating system
 * scheduling algorithms. This driver creates the various
 * objects needed and present a selection menu allowing
 * the user to choose which algorithm to run.
 **/
public class OS {

	/*
	 * Main method
	 * @param: args as String array.
	 */	
	public static void main(String[] args) {
		// Creates File object and point to input file.
		File inFile = new File("jobs.txt"); 
		// Initializes the ArrayList to hold the list of jobs.
		ArrayList<Job> jobList = new ArrayList<>();				
		ArrayList<Job> jobList1 = new ArrayList<>();
		ArrayList<Job> jobList2 = new ArrayList<>();
		ArrayList<Job> jobList3 = new ArrayList<>();
		// To hold user input for algorithm selection.
		int input;						
		// Attempts to open file, read its contents and initialize the jobs and list of jobs.
		try {
			Scanner readFile = new Scanner(inFile);
			while(readFile.hasNext()) {
				String name = readFile.next();
				int arrivalTime = readFile.nextInt();
				int duration = readFile.nextInt();
				Job job = new Job(name, arrivalTime, duration);
				Job job1 = new Job(name, arrivalTime, duration);
				Job job2 = new Job(name, arrivalTime, duration);
				Job job3 = new Job(name, arrivalTime, duration);
				jobList.add(job);
				jobList1.add(job1);
				jobList2.add(job2);
				jobList3.add(job3);
			}
			readFile.close();
		} catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		// Menu selection.
		Scanner keyboard = new Scanner(System.in);
		System.out.println("SELECT SCHEDULING ALGORITHM");
		System.out.println("1 - First-Come-First-Serve");
		System.out.println("2 - Shortest Process Next");
		System.out.println("3 - Highest Response Ratio Next");
		System.out.println("4 - Round Robin");
		System.out.println("5 - Shortest Remaining Time");
		System.out.println("6 - Multilevel Feedback");
		System.out.println("7 - ALL");
		System.out.print("SELECTION: ");
		input = keyboard.nextInt();
		switch (input) {
			case 1:
				Scheduler fcfs  = new FCFS(jobList);
				break;
			case 2:
				Scheduler spn = new SPN(jobList);
				break;
			case 3:
				Scheduler hrrn = new HRRN(jobList);
				break;
			case 4:
				Scheduler rr = new RR(jobList1);
				break;
			case 5:
				Scheduler srt = new SRT(jobList2);
				break;
			case 6:
				Scheduler mlf = new MLF(jobList3);
				break;
			case 7:
				Scheduler fcfs1  = new FCFS(jobList);
				Scheduler spn1 = new SPN(jobList);
				Scheduler hrrn1 = new HRRN(jobList);
				Scheduler rr1 = new RR(jobList1);
				Scheduler srt1 = new SRT(jobList2);
				Scheduler mlf1 = new MLF(jobList3);
		}
	}
}
