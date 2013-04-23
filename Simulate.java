import java.awt.Color;
import java.text.DecimalFormat;
import edu.rit.numeric.AggregateXYSeries;
import edu.rit.numeric.ListSeries;
import edu.rit.numeric.Series;
import edu.rit.numeric.Statistics;
import edu.rit.numeric.plot.Plot;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Class Simulate is the class that sets up the parameters in a simulation
 * experiment and performs the actual simulation of two Grid Computing Systems:
 * one with 2 compute servers and another with 4 compute servers. It also
 * accumulates data and performs the t-statistical test on the mean response
 * time populations of these two systems.
 * 
 * @author Chinmay Dani
 * 
 */
public class Simulate {
	private static double jobExecTime = 1.0;
	private static int totalJobs = 10000;
	private static long seed = 142857;
	private static Random rand;
	private static Simulation sim;
	private static ComputeServer server1;
	private static ComputeServer server2;
	private static ComputeServer server3;
	private static ComputeServer server4;
	private static JobAdder jobAdder;
	private static JobQueue jobQueue;

	private static double minInterArrTime = 0.1;
	private static double maxInterArrTime = 5.0;
	private static double deltaInterArrTime = 0.1;

	private static ListSeries interArrTimes;
	private static ListSeries meanRespTimesA;
	private static ListSeries meanRespTimesB;

	public static void main(String[] args) {
		if (args.length != 6)
			usage();

		jobExecTime = Double.parseDouble(args[0]);
		minInterArrTime = Double.parseDouble(args[1]);
		maxInterArrTime = Double.parseDouble(args[2]);
		deltaInterArrTime = Double.parseDouble(args[3]);
		seed = Integer.parseInt(args[4]);
		totalJobs = Integer.parseInt(args[5]);

		rand = Random.getInstance(seed);
		interArrTimes = new ListSeries();
		meanRespTimesA = new ListSeries();
		meanRespTimesB = new ListSeries();
		double interArrTime;

		System.out.println("\t2 servers\t\t\t4 servers");
		System.out.println("\ttresp\t\ttresp\t\ttresp\t\ttresp");
		System.out.println("treq\tmean\t\tstddev\t\tmean\t\tstddev\t\tt\t\tp");

		for (interArrTime = minInterArrTime; interArrTime <= maxInterArrTime; interArrTime += deltaInterArrTime) {
			interArrTimes.add(interArrTime);
			System.out.printf("%.3f", interArrTime);
			sim = new Simulation();
			jobQueue = new JobQueue();
			server1 = new ComputeServer(1, sim, rand, jobExecTime, jobQueue);
			server2 = new ComputeServer(2, sim, rand, jobExecTime, jobQueue);
			ComputeServer[] servers = new ComputeServer[2];
			servers[0] = server1;
			servers[1] = server2;
			jobAdder = new JobAdder(sim, servers, rand, interArrTime,
					totalJobs, jobQueue);
			sim.run();
			ListSeries twoserverseries = jobAdder.getRespTimeSeries();
			Series.Stats stats = twoserverseries.stats();
			meanRespTimesA.add(stats.mean);
			System.out.printf("\t%.2f\t\t%.2f", stats.mean, stats.stddev);

			Job.resetCurrentId();

			sim = new Simulation();
			jobQueue = new JobQueue();
			server1 = new ComputeServer(1, sim, rand, jobExecTime, jobQueue);
			server2 = new ComputeServer(2, sim, rand, jobExecTime, jobQueue);
			server3 = new ComputeServer(3, sim, rand, jobExecTime, jobQueue);
			server4 = new ComputeServer(4, sim, rand, jobExecTime, jobQueue);
			servers = new ComputeServer[4];
			servers[0] = server1;
			servers[1] = server2;
			servers[2] = server3;
			servers[3] = server4;
			jobAdder = new JobAdder(sim, servers, rand, interArrTime,
					totalJobs, jobQueue);
			sim.run();
			ListSeries fourserverseries = jobAdder.getRespTimeSeries();
			stats = fourserverseries.stats();
			meanRespTimesB.add(stats.mean);
			System.out.printf("\t\t%.2f\t\t%.2f", stats.mean, stats.stddev);

			double[] ttest = Statistics.tTestUnequalVariance(twoserverseries,
					fourserverseries);
			System.out.printf("\t\t%.3f\t\t%.3f%n", ttest[0], ttest[1]);
		}

		System.out.println("2 servers: " + meanRespTimesA.stats().mean);
		System.out.println("4 servers: " + meanRespTimesB.stats().mean);

		double maxResp = Math.max(meanRespTimesA.maxX(), meanRespTimesB.maxX());
		Plot plot = new Plot();

		plot = new Plot()
				.rightMargin(36)
				.xAxisTitle("Mean interarrival time")
				.xAxisTickFormat(new DecimalFormat("0.0"))
				.yAxisTitle("Mean response time")
				.yAxisTickFormat(new DecimalFormat("0.0"))
				.seriesDots(null)
				.seriesColor(Color.BLUE)
				.xySeries(new AggregateXYSeries(interArrTimes, meanRespTimesA))
				.seriesColor(Color.RED)
				.xySeries(new AggregateXYSeries(interArrTimes, meanRespTimesB))
				.labelPosition(Plot.RIGHT)
				.labelOffset(5)
				.labelColor(Color.BLUE)
				.label("<B>_____ 2 servers<B>", maxInterArrTime * 0.6, maxResp)
				.labelColor(Color.RED)
				.label("<B>_____ 4 servers<B>", maxInterArrTime * 0.6,
						maxResp * 0.9);
		plot.getFrame().setVisible(true);

	}

	private static void usage() {
		System.err.println("Usage: java Simulate <jobExecTime> "
				+ "<minInterArrTime> <maxInterArrTime> <deltaInterArrTime> "
				+ "<seed> <totalJobs>");
		System.err.println("jobExecTime: the mean job execution time");
		System.err.println("minInterArrTime: the lower bound of the mean job "
				+ "inter-arrival time");
		System.err.println("maxInterArrTime: the higher bound of the mean job "
				+ "inter-arrival time");
		System.err.println("deltaInterArrTime: the delta of the mean job "
				+ "inter-arrival time");
		System.err.println("seed: seed for the pseudo random number generator");
		System.err
				.println("totalJobs: the total number of jobs to be executed "
						+ "in the simulation");
	}

}
