import java.util.LinkedList;
import java.util.Queue;

/**
 * Class JobQueue is a representation of the job queue used in the Grid
 * Computing System. The job queue is common to all the Compute Servers present
 * in a system.
 * 
 * @author Chinmay Dani
 * 
 */
public class JobQueue {
	// A queue object that contains Job objects.
	private Queue<Job> queue;

	/**
	 * Constructs a new JobQueue object.
	 * 
	 */
	public JobQueue() {
		this.queue = new LinkedList<Job>();
	}

	/**
	 * Adds a Job object to the end of the job queue.
	 * 
	 * @param job
	 *            the Job object
	 * @return true if Job added successfully, false otherwise
	 */
	public boolean addJob(Job job) {
		// System.out.printf("%.3f %s added to %s%n", sim.time(), job,
		// "Job Queue");
		return this.queue.add(job);
	}

	/**
	 * Returns the Job object at the head of the job queue.
	 * 
	 * @return the head object of the queue
	 */
	public Job getJob() {
		return this.queue.remove();
	}

	/**
	 * States whether the job queue is empty or not.
	 * 
	 * @return true if the job queue is empty, false otherwise
	 */
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}
}
