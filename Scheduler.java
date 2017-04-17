import java.util.*;				// Needed for various java util Classes.

/*
 * PROGRAM: Scheduler.java
 * @author: Moustapha Dieng
 * This program simulates an operating system
 * scheduler. It processes incoming jobs using
 * one or all the algorithms provided.
 **/
public abstract class Scheduler {

	// Field initialization.
	private ArrayList<Job> jobList = new ArrayList<>();
 	private Queue<Job> sortedList = new LinkedList<>();
	private Job[] job;
	protected Job ref;
	private int[] arrivalTime;
 
	/*
	 * No-arg Constructor
	 * @param: None.
	 */
	public Scheduler() {}

	/*
	 * Constructor: Initiliazes job list.
	 * @param: ArrayList of jobs.
	 */
	public Scheduler(ArrayList<Job> jobList) {
		this.jobList = jobList;
		job = new Job[jobList.size()];
		arrivalTime = new int[jobList.size()];
		for(int i = 0; i < jobList.size(); i++) {
			job[i] = jobList.get(i);
			arrivalTime[i] = job[i].getArrivalTime();
		}
		Arrays.sort(arrivalTime);
		jobSort();
	}
 
 	/*
	 * Method: jobSort
	 * @param: None.
	 * Sorts job by arrival time.
	 */
  	public void jobSort() {
		for(int i = 0; i < jobList.size(); i++) {
			for(int j = 0; j < jobList.size(); j++)
				if(arrivalTime[i] == job[j].getArrivalTime())
					sortedList.add(job[j]);
		}
	}

 	/*
	 * Method: getJobList
	 * @param: None.
	 * @return: jobList.
	 */
	public ArrayList<Job> getJobList() {
		return jobList;
	}

 	/*
	 * Method: getSortedList
	 * @param: None.
	 * @return: sortedList.
	 */
	public Queue<Job> getSortedList() {
		return sortedList;
	}

 	/*
	 * Method: padLeft
	 * @param: String s and length n.
	 * @return: Pads by n s.
	 */
	public String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}
}

/*
 * PROGRAM: FCFS.java
 * @author: Moustapha Dieng
 * Non-preemptive: First-Come-First-Serve
 **/
class FCFS extends Scheduler {
	// Field initialization.
	private ArrayList<Job> jobList = new ArrayList<>();
	private Queue<Job> sortedList = new LinkedList<>();
	private int timeElapsed = 0;

	/*
	 * Constructor: Initiliazes job list.
	 * @param: ArrayList of jobs.
	 */
	public FCFS(ArrayList<Job> jobList) {
		super(jobList);
		this.jobList = getJobList();
		this.sortedList = getSortedList();
		runJobs();
	}
 
 	/*
	 * Method: runJobs.
	 * @param: None.
	 * @return: Void.
	 * Processes jobs: Acts as select and run method.
	 * Uses queue to hold jobs based on arrival time.
	 */
	public void runJobs() {
		System.out.println("\n~~~~ FIRST COME FIRST SERVE ~~~~\n");
		while(true) {
			Job currentJob = sortedList.remove();
			if(timeElapsed != 0) 
				System.out.print(padLeft(" ", timeElapsed));
			currentJob.run();
			System.out.println();
			timeElapsed += currentJob.getServiceTime();
			if(sortedList.isEmpty())
				break;
		}
	}
}

/*
 * PROGRAM: SPN.java
 * @author: Moustapha Dieng
 * Non-preemptive: Shortest Process Next
 **/
class SPN extends Scheduler {
	// Field initialization.
	private ArrayList<Job> jobList = new ArrayList<>();
	private Queue<Job> sortedList = new LinkedList<>();
	private int timeElapsed = 0;

	/*
	 * Constructor: Initiliazes job list.
	 * @param: ArrayList of jobs.
	 */
	public SPN(ArrayList<Job> jobList) {
		super(jobList);
		for(int i = 0; i < jobList.size(); i++)
			this.jobList.add(jobList.get(i));
		this.sortedList = getSortedList();
		runJobs();
	}

 	/*
	 * Method: selectedJob.
	 * @param: None.
	 * @return: Selected job.
	 * Returns job with shortest service time.
	 */
	public Job selectJob() {
		Job min = jobList.get(0);
		for(int i = 0; i < jobList.size(); i++) {
			Job num = jobList.get(i);
			if(timeElapsed >= num.getArrivalTime() && num.getServiceTime() < min.getServiceTime()) min = num;
		}
		jobList.remove(min);
		return min;
	}

 	/*
	 * Method: runJobs.
	 * @param: None.
	 * @return: Void.
	 * Processes jobs.
	 */
	public void runJobs() {
		boolean firstRun = false;
		System.out.println("\n~~~~ SHORTEST PROCESS NEXT ~~~~\n");
		while(true) {
			Job currentJob;
			if(!firstRun) {
				currentJob = sortedList.remove();
				jobList.remove(currentJob);
				firstRun = true;
			}
			else currentJob = selectJob();
			if(timeElapsed != 0) 
				System.out.print(padLeft(" ", timeElapsed));
			currentJob.run();
			System.out.println();
			timeElapsed += currentJob.getServiceTime();
			if(jobList.isEmpty())
				break;
		}
	}
}

/*
 * PROGRAM: HRRN.java
 * @author: Moustapha Dieng
 * Non-preemptive: Highest Response Ration Next
 **/
class HRRN extends Scheduler {
	// Field initialization.
	private ArrayList<Job> jobList = new ArrayList<>();
	private Queue<Job> sortedList = new LinkedList<>();
	float[] r;
	float[] ref;
	private int timeElapsed = 0;
	
 	/*
	 * Constructor: Initiliazes job list.
	 * @param: ArrayList of jobs.
	 */
	public HRRN(ArrayList<Job> jobList) {
		super(jobList);
   		for(int i = 0; i < jobList.size(); i++)
			this.jobList.add(jobList.get(i));
		this.sortedList = getSortedList();
		r = new float[jobList.size()];
		ref = new float[jobList.size()];
		runJobs();
	}

 	/*
	 * Method: selectedJob.
	 * @param: None.
  	 * @return: Selected job.
	 * Returns job based on algorithm: (w+s)/s
	 */
 	public Job selectJob() {
		float highestR;
		Job max = jobList.get(0);;
		for(int i = 0; i < jobList.size(); i++) {
			if(timeElapsed >= jobList.get(i).getArrivalTime()) {
				r[i] = ((timeElapsed - jobList.get(i).getArrivalTime())	+ jobList.get(i).getServiceTime())
				/ (float)jobList.get(i).getServiceTime();
				ref[i] = r[i];
				Arrays.sort(ref);
				highestR = ref[ref.length - 1];
				if(r[i] == highestR)
					max = jobList.get(i);
			}
		}
		jobList.remove(max);
		return max;
	}

 	/*
	 * Method: runJobs.
	 * @param: None.
	 * @return: Void.
	 * Processes jobs.
	 */
	public void runJobs() {
		boolean firstRun = false;
		System.out.println("\n~~~~ HIGHEST RESPONSE RATIO NEXT ~~~~\n");
		while(true) {
			Job currentJob;
			if(!firstRun) {
				currentJob = sortedList.remove();
				jobList.remove(currentJob);
				firstRun = true;
			}
			else currentJob = selectJob();
			if(timeElapsed != 0) 
				System.out.print(padLeft(" ", timeElapsed));
			currentJob.run();
			System.out.println();
			timeElapsed += currentJob.getServiceTime();
			if(jobList.isEmpty())
				break;
		}
	}
}

/*
 * PROGRAM: RR.java
 * @author: Moustapha Dieng
 * Preemptive: Round Robin
 **/
class RR extends Scheduler {
	// Field initialization.
	private ArrayList<Job> jobList = new ArrayList<>();
	private Queue<Job> readyQueue = new LinkedList<>();
	private	Stack<Job> temp = new Stack<>();
	private int quantum;
	private int timeElapsed = 0;
	private int totalServiceTime = 0;
	private boolean[] inQueue;
	private	Job selectedJob;
	private String[] graph;

	/*
	 * Constructor: Initiliazes job list.
	 * @param: ArrayList of jobs.
	 */
	public RR(ArrayList<Job> jobList) {
		super(jobList);
		inQueue = new boolean[jobList.size()];
		graph = new String[jobList.size()];
		for(int i = 0; i < jobList.size(); i++) {
			this.jobList.add(jobList.get(i));
			totalServiceTime += jobList.get(i).getServiceTime();
			inQueue[i] = false;
			graph[i] = "";
		}
		selectedJob = null;
		runJobs();
	}

 	/*
	 * Method: selectedJob.
	 * @param: None.
	 * @return: Selected job.
	 * Returns job based on algorithm.
	 */
	public Job selectJob() {
		int arrivalTime;
		boolean preempt = false;
		for(int i = 0; i < jobList.size(); i++) {
			arrivalTime = jobList.get(i).getArrivalTime();
			if(!inQueue[i] && timeElapsed >= arrivalTime && jobList.get(i).getRunTime() == 0)  {
				readyQueue.add(jobList.get(i));
				inQueue[i] = true;
				preempt = true;
			}
		}
		if(preempt && !temp.empty() && selectedJob.getServiceTime() - selectedJob.getRunTime() > 0) 
			readyQueue.add(temp.pop());
		if(!preempt && selectedJob.getServiceTime() - selectedJob.getRunTime() > 0)
			readyQueue.add(selectedJob);
		selectedJob = readyQueue.remove();
		for(int i = 0; i < jobList.size(); i++) {
			int remainingTime = selectedJob.getServiceTime() - selectedJob.getRunTime();
			if(quantum > remainingTime)
				saveGraph(selectedJob, jobList.get(i), remainingTime);
			else 
				saveGraph(selectedJob, jobList.get(i), quantum);
		}
		if(selectedJob.getServiceTime() - selectedJob.getRunTime() > 0) 
			temp.push(selectedJob);
		return selectedJob;
	}

	/*
	 * Method: saveGraph.
	 * @param: Selected Job, one of the jobs in the list, remaining time or quantum.
	 * If current job is selected job then print name.
	 * Else pad left with a space.
	 * Runs for the quantum or remaining time argument received.
	 */
	public void saveGraph(Job runningJob, Job job, int timeSlice) {
		for(int i = 0; i < jobList.size(); i++) {
			if(job == jobList.get(i)) {
				for(int j = 0; j < timeSlice; j++) {
					if(runningJob != job) 
						graph[i] += padLeft(" ", 1);
					else 
						graph[i] += jobList.get(i).getName();
				}
			}
		}
	}

	/*
	 * Method: printGraph.
	 * @param: None.
	 * @return: Final graph output.
	 * Combines strings from all jobs output into one.
	 * Leaves a newline between each job.	
	 */
	public String printGraph() {
		String g = "";
		for(int i = 0; i < jobList.size(); i++)
			g += graph[i] + "\n";
		return g;
	}
		
 	/*
	 * Method: runJobs.
	 * @param: None.
	 * @return: Void.
	 * Processes jobs.
	 */
	public void runJobs() {
		Scanner keyboard = new Scanner(System.in);
		System.out.print("\nENTER QUANTUM FOR ROUND ROBIN: ");
		quantum = keyboard.nextInt();
		System.out.println("\n~~~~ ROUND ROBIN ~~~~\n");
		while(true) {
			Job currentJob;
			int remainingTime;
			currentJob = selectJob();
			remainingTime = currentJob.getServiceTime() - currentJob.getRunTime();
			if(quantum > remainingTime) { 
				currentJob.run(remainingTime);
				timeElapsed += remainingTime;
			}
			else {
				currentJob.run(quantum);
				timeElapsed += quantum;
			}
			if(timeElapsed == totalServiceTime)
				break;
		}
		System.out.println(printGraph());
	}
}

/*
 * PROGRAM: SRT.java
 * @author: Moustapha Dieng
 * Preemptive: Shortest Remaining Time
 **/
class SRT extends Scheduler {
	// Field initialization.
	private ArrayList<Job> jobList = new ArrayList<>();
	private Queue<Job> readyQueue = new LinkedList<>();
	private int quantum = 1;
	private int timeElapsed = 0;
	private int totalServiceTime = 0;
	private int[] remainingTime;
	private	Job selectedJob;
	private String[] graph;

	/*
	 * Constructor: Initiliazes job list.
	 * @param: ArrayList of jobs.
	 */
	public SRT(ArrayList<Job> jobList) {
		super(jobList);
		remainingTime = new int[jobList.size()];
		graph = new String[jobList.size()];
		for(int i = 0; i < jobList.size(); i++) {
			this.jobList.add(jobList.get(i));
			totalServiceTime += jobList.get(i).getServiceTime();
			graph[i] = "";
		}
		selectedJob = null;
		runJobs();
	}

 	/*
	 * Method: selectedJob.
	 * @param: None.
	 * @return: Selected job.
	 * Returns job based on algorithm.
	 */
	public Job selectJob() {
		ArrayList<Integer> rem = new ArrayList<>();
		boolean preempt = false;
		for(int i = 0; i < jobList.size(); i++) {
			int arrivalTime = jobList.get(i).getArrivalTime();
			int timeLeft = jobList.get(i).getServiceTime() - jobList.get(i).getRunTime();
			if(timeElapsed >= arrivalTime && timeLeft > 0) {
				rem.add(timeLeft);
			}
		}
		remainingTime = new int[rem.size()];
		for(int i = 0; i < rem.size(); i++) 
			remainingTime[i] = rem.get(i);
		Arrays.sort(remainingTime);
		for(int i = 0; i < jobList.size(); i++) {
			int arrivalTime = jobList.get(i).getArrivalTime();
			int timeLeft = jobList.get(i).getServiceTime() - jobList.get(i).getRunTime();
			int runtime = jobList.get(i).getRunTime();
			if(!preempt && timeElapsed >= arrivalTime && timeLeft > 0 && timeLeft == remainingTime[0]) {
				readyQueue.add(jobList.get(i));
				preempt = true;
			}
		}
		selectedJob = readyQueue.remove();
		for(int i = 0; i < jobList.size(); i++) 
			saveGraph(selectedJob, jobList.get(i), quantum);
		return selectedJob;
	}

	/*
	 * Method: saveGraph.
	 * @param: Selected Job, one of the jobs in the list, remaining time or quantum.
	 * If current job is selected job then print name.
	 * Else pad left with a space.	
	 * Runs for the quantum or remaining time argument received.
	 */
	public void saveGraph(Job runningJob, Job job, int timeSlice) {
		for(int i = 0; i < jobList.size(); i++) {
			if(job == jobList.get(i)) {
				for(int j = 0; j < timeSlice; j++) {
					if(runningJob != job) 
						graph[i] += padLeft(" ", 1);
					else graph[i] += jobList.get(i).getName();
				}
			}
		}
	}

	/*
	 * Method: printGraph.
	 * @param: None.
	 * @return: Final graph output.
	 * Combines strings from all jobs output into one.
	 * Leaves a newline between each job.
	 */
	public String printGraph() {
		String g = "";
		for(int i = 0; i < jobList.size(); i++)
			g += graph[i] + "\n";
		return g;
	}

 	/*
	 * Method: runJobs.
	 * @param: None.
	 * @return: Void.
	 * Processes jobs.
	 */
	public void runJobs() {
		System.out.println("\n~~~~ SHORTEST REMAINING TIME ~~~~\n");
		while(true) {
			Job currentJob;
			currentJob = selectJob();
			currentJob.run(quantum);
			timeElapsed += quantum;
			if(timeElapsed == totalServiceTime)
				break;
		}
		System.out.println(printGraph());
	}
}

/*
 * PROGRAM: MLF.java
 * @author: Moustapha Dieng
 * Preemptive: Multilevel Feedback
 **/
class MLF extends Scheduler {
	// Field initialization.
	private ArrayList<Job> jobList = new ArrayList<>();
	private Queue<Job> readyQueue = new LinkedList<>();
	private Queue<Job> queue1 = new LinkedList<>();
	private Queue<Job> queue2 = new LinkedList<>();
	private int quantum = 1;
	private int timeElapsed = 0;
	private int x = 0;
	private int totalServiceTime = 0;
	private	Job selectedJob;
	private String[] graph;

	/*
	 * Constructor: Initiliazes job list.
	 * @param: ArrayList of jobs.
	 */
	public MLF(ArrayList<Job> jobList) {
		super(jobList);
		graph = new String[jobList.size()];
		for(int i = 0; i < jobList.size(); i++) {
			this.jobList.add(jobList.get(i));
			totalServiceTime += jobList.get(i).getServiceTime();
			graph[i] = "";
		}
		selectedJob = null;
		runJobs();
	}
 
  	/*
	 * Method: selectedJob.
	 * @param: None.
	 * @return: Selected job.
	 * Returns job based on algorithm.
	 */
	public Job selectJob() {
		for(int i = 0; i < jobList.size(); i++) {
			int arrivalTime = jobList.get(i).getArrivalTime();
			int runtime = jobList.get(i).getRunTime();
			if(timeElapsed >= arrivalTime && runtime == 0) {
				readyQueue.add(jobList.get(i));
				x++;
			}
		}
		if(x == 1) {
			selectedJob = readyQueue.remove();
			int timeLeft = selectedJob.getServiceTime() - selectedJob.getRunTime();
			if(timeLeft > quantum) {
				readyQueue.add(selectedJob);
			}
		}
		else if(x == 2) {
			queue1.add(readyQueue.remove());
			selectedJob = readyQueue.remove();
			int timeLeft = selectedJob.getServiceTime() - selectedJob.getRunTime();
			if(timeLeft > quantum)
				queue1.add(selectedJob);
			x++;
		}
		else if(x > 2) {
			if(!readyQueue.isEmpty()) {
				selectedJob = readyQueue.remove();
				int timeLeft = selectedJob.getServiceTime() - selectedJob.getRunTime();
				if(timeLeft > quantum)
					queue1.add(selectedJob);
			}
			else if(!queue1.isEmpty()) {
				selectedJob = queue1.remove();
				int timeLeft = selectedJob.getServiceTime() - selectedJob.getRunTime();
				if(timeLeft > quantum)
					queue2.add(selectedJob);
			}
			else if(!queue2.isEmpty()) {
				selectedJob = queue2.remove();
				int timeLeft = selectedJob.getServiceTime() - selectedJob.getRunTime();
				if(timeLeft > quantum)
					queue2.add(selectedJob);
			}
		}
   		for(int i = 0; i < jobList.size(); i++) 
			saveGraph(selectedJob, jobList.get(i), quantum);
		return selectedJob;
	}

	/*
	 * Method: saveGraph.
	 * @param: Selected Job, one of the jobs in the list, remaining time or quantum.
	 * If current job is selected job then print name.
	 * Else pad left with a space.
	 * Runs for the quantum or remaining time argument received.
	 */
	public void saveGraph(Job runningJob, Job job, int timeSlice) {
		for(int i = 0; i < jobList.size(); i++) {
			if(job == jobList.get(i)) {
				for(int j = 0; j < timeSlice; j++) {
					if(runningJob != job) 
						graph[i] += padLeft(" ", 1);
					else graph[i] += jobList.get(i).getName();
				}
			}
		}
	}

	/*
	 * Method: printGraph.
	 * @param: None.
	 * @return: Final graph output.
	 * Combines strings from all jobs output into one.
	 * Leaves a newline between each job.
	 */
	public String printGraph() {
		String g = "";
		for(int i = 0; i < jobList.size(); i++)
			g += graph[i] + "\n";
		return g;
	}

 	/*
	 * Method: runJobs.
	 * @param: None.
	 * @return: Void.
	 * Processes jobs.
	 */		
	public void runJobs() {
		System.out.println("\n~~~~ MULTILEVEL FEEDBACK ~~~~\n");
		while(true) {
			Job currentJob;
			currentJob = selectJob();
			currentJob.run(quantum);
			timeElapsed += quantum;
			if(timeElapsed == totalServiceTime)
				break;
		}
		System.out.println(printGraph());
	}
}
