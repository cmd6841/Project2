import edu.rit.numeric.ExponentialPrng;
import edu.rit.sim.Event;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Class ComputeServer represents a Compute Server in the Grid Computing System.
 * The task of a compute server is to pull a job out of a common job queue when
 * and start executing it and continue doing so until there are no jobs in the
 * job queue.
 * 
 * @author Chinmay Dani
 * 
 */
public class ComputeServer {
	// A unique identifier for the ComputeServer.
	private int id;

	// A simultaion object.
	private Simulation sim;

	// A random number generator for generating exponentially distributed
	// job execution times.
	private ExponentialPrng jobExectimes;

	// A job queue to pull out the job for executing.
	private JobQueue jobQueue;

	// A flag which is true when the server is idle.
	public boolean idle;

	/**
	 * Constructs a new ComputeServer object and assigns it an id and a job
	 * queue.
	 * 
	 * @param id
	 *            the id
	 * @param sim
	 *            the simulation object
	 * @param rand
	 *            the random number generator for generating random job
	 *            execution times
	 * @param meanExecTime
	 *            mean job execution time
	 * @param jobQueue
	 *            the job queue object
	 */
	public ComputeServer(int id, Simulation sim, Random rand,
			double meanExecTime, JobQueue jobQueue) {
		this.id = id;
		this.sim = sim;
		this.jobQueue = jobQueue;
		this.jobExectimes = new ExponentialPrng(rand, 1 / meanExecTime);
		this.idle = true;
	}

	/**
	 * Executes the first job fetched from the job queue provided the server is
	 * currently idle and the job queue is not empty.
	 */
	public void executeJob() {
		if (idle && !jobQueue.isEmpty()) {
			idle = false;
			final Job job;
			job = jobQueue.getJob();
			// System.out.printf("%.3f %s starts processing %s%n", sim.time(),
			// this,
			// job);
			sim.doAfter(jobExectimes.next(), new Event() {

				@Override
				public void perform() {
					finishJob(job);
				}

			});
		}
	}

	/**
	 * Finishes the job being executed and continues to execute the next job
	 * present in the job queue.
	 * 
	 * @param job
	 *            the job object currently being executed.
	 */
	private void finishJob(Job job) {
		// System.out.printf("%.3f %s finishes processing %s%n", sim.time(),
		// ComputeServer.this, job);
		job.finish();
		this.idle = true;
		if (!jobQueue.isEmpty()) {
			executeJob();
		}
	}

	/**
	 * Returns the string representation of the current server.
	 * 
	 * @return string identifier
	 */
	public String toString() {
		return "Compute Server " + Integer.toString(this.id);
	}

}
