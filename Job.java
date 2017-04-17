/*
 * PROGRAM: Job.java
 * @author: Moustapha Dieng
 * This program simulates a process with
 * attributes: name, arrival time and service time.
 **/
public class Job {

	// Field initialization.
	private String name;
	private int arrivalTime;
	private int serviceTime;
	private int runtime;
	private int quantum = 0;
	
 	/*
	 * No-arg Constructor
	 * @param: None.
	 */
	public Job() {}

	/*
	 * Constructor: Initiliazes job.
	 * @param:name, arrival time and service time.
	 */
	public Job(String name, int arrivalTime, int serviceTime) {
		this.name = name;
		this.arrivalTime = arrivalTime;
		this.serviceTime = serviceTime;
		this.runtime = 0;
	}

 	/*
	 * Method: getName.
	 * @param: None.
	 * @return: Job name.
	 */
	public String getName() {
		return name;
	}

 	/*
	 * Method: getName.
	 * @param: None.
	 * @return: Job name.
	 */
	public int getArrivalTime() {
		return arrivalTime;
	}

 	/*
	 * Method: getServiceTime.
	 * @param: None.
	 * @return: Job service time.
	 */
	public int getServiceTime() {
		return serviceTime;
	}

 	/*
	 * Method: getRunTime.
	 * @param: None.
	 * @return: Job current runtime.
	 */
	public int getRunTime() {
		return runtime;
	}

 	/*
	 * Method: run.
	 * @param: None.
	 * @return: Print job name for service time.
	 */
	public void run() {
		for(int i = 0; i < serviceTime; i++)
			System.out.print(name);
	}

 	/*
	 * Method: run.
	 * @param: None.
	 * @return: Keeps track of current runtime.
	 */
	public void run(int timeSlice) {
		quantum = timeSlice;
		runtime += timeSlice;
	}
}
