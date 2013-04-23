import edu.rit.numeric.ListSeries;
import edu.rit.sim.Simulation;

/**
 * Class Job represents the unit of operation in the Grid computing system. Each
 * job has the information of its own start time and finish time.
 * 
 * @author Chinmay Dani
 * 
 */

public class Job {
	// A counter to generate a unique ID for current job.
	private static int currentId = 0;

	/**
	 * A method to reset the current id of jobs to 0.
	 */
	public static void resetCurrentId() {
		currentId = 0;
	}

	// A unique identifier of job.
	private int id;

	// An object of Simulation.
	private Simulation sim;

	// The simulation time at which a job is added to the job queue.
	private double startTime;

	// The simulation time at which a server finishes processing of the job.
	private double finishTime;

	// A series to accumulate the response time of the jobs.
	private ListSeries respTimeSeries;

	/**
	 * Constructs a new Job object, assigns a unique id to it and assigns the
	 * simulation time as its start time.
	 * 
	 * @param sim
	 *            the simulation object
	 * @param series
	 *            the repsonse time series
	 */
	public Job(Simulation sim, ListSeries series) {
		this.id = ++currentId;
		this.sim = sim;
		this.startTime = sim.time();
		this.respTimeSeries = series;
	}

	/**
	 * Finishes this job in the simulation by recording the current simulation
	 * time as its finish time and adds the response time to the series.
	 */
	public void finish() {
		this.finishTime = this.sim.time();
		if (this.respTimeSeries != null)
			this.respTimeSeries.add(responseTime());
	}

	/**
	 * Calculates the response time taken by a server to execute this job.
	 * 
	 * @return the response time for this job
	 */
	public double responseTime() {
		return this.finishTime - this.startTime;
	}

	/**
	 * Returns the current job id as a String.
	 * 
	 * @return the String identifier for this job
	 */
	public String toString() {
		return "Job " + id;
	}
}
