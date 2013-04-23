import edu.rit.numeric.ExponentialPrng;
import edu.rit.numeric.ListSeries;
import edu.rit.sim.Event;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Class JobAdder is a helper class in the Grid Computing System that performs
 * the task of adding new jobs in the job queue and prompts the compute servers
 * to execute the jobs accordingly.
 * 
 * @author Chinmay Dani
 * 
 */
public class JobAdder {
	// A Simultaion object.
	private Simulation sim;

	// An array that holds the ComputeServer objects in the system.
	private ComputeServer[] servers;

	// A random number generator for generating exponentially distributed
	// job inter-arrival times.
	private ExponentialPrng interArrivalTimes;

	// A series to accumulate the response times for each job in the job queue.
	private ListSeries respTimeSeries;

	// The maximum number of jobs the system will be executing.
	private int totalJobs;

	// The JobQueue object in the system.
	private JobQueue jobQueue;

	// The total number of jobs added in the job queue.
	private int jobsAdded;

	/**
	 * Constructs a new JobAdder object with the information provided to it
	 * about the current Grid Computing System and starts adding the jobs to the
	 * job queue along with notifying the server to execute the jobs.
	 * 
	 * @param sim
	 *            the Simultaion object
	 * @param servers
	 *            the array of all the ComputeServer objects
	 * @param rand
	 *            the random generator of inter-arrival times for jobs
	 * @param meanInterArrivalTime
	 *            the mean inter-arrival time
	 * @param maximumJobs
	 *            the maximum number of jobs that will be executed in the
	 *            current simulation
	 * @param jobQueue
	 *            the JobQueue in the system
	 */
	public JobAdder(Simulation sim, ComputeServer[] servers, Random rand,
			double meanInterArrivalTime, int maximumJobs, JobQueue jobQueue) {
		this.sim = sim;
		this.servers = servers;
		this.interArrivalTimes = new ExponentialPrng(rand,
				1.0 / meanInterArrivalTime);
		this.totalJobs = maximumJobs;
		this.jobQueue = jobQueue;
		this.respTimeSeries = new ListSeries();
		addJob();
	}

	/**
	 * Adds the jobs to the JobQueue in the simulation at times exponentially
	 * distributed along a given mean inter-arrival time. After adding a job, it
	 * also notifies a ComputeServer to start executing the job.
	 */
	private void addJob() {
		if (jobsAdded < totalJobs) {
			Job job = new Job(sim, respTimeSeries);
			jobQueue.addJob(job);
			for (ComputeServer server : servers) {
				server.executeJob();
			}
			sim.doAfter(interArrivalTimes.next(), new Event() {
				public void perform() {
					jobsAdded++;
					addJob();
				}
			});
		}
	}

	/**
	 * Returns the accumulated response times of jobs executed in from the job
	 * queue in the form of a ListSeries.
	 * 
	 * @return the response time series.
	 */
	public ListSeries getRespTimeSeries() {
		return respTimeSeries;
	}

}
