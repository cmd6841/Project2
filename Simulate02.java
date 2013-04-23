import edu.rit.numeric.Series;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

public class Simulate02 {
	private static double jobExecTime = 10.0;
	private static double interArrivalTime = 5.0;
	private static int maximumJobs = 100;
	private static long seed = 0756467427;
	private static Random rand;
	private static Simulation sim;
	private static ComputeServer server1;
	private static ComputeServer server2;
	private static ComputeServer server3;
	private static ComputeServer server4;
	private static JobAdder jobAdder;
	private static JobQueue jobQueue;

	public static void main(String[] args) {
		rand = Random.getInstance(seed);
		sim = new Simulation();
		jobQueue = new JobQueue();
		server1 = new ComputeServer(1, sim, rand, jobExecTime, jobQueue);
		server2 = new ComputeServer(2, sim, rand, jobExecTime, jobQueue);
		ComputeServer[] servers = new ComputeServer[2];
		servers[0] = server1;
		servers[1] = server2;
		jobAdder = new JobAdder(sim, servers, rand, interArrivalTime,
				maximumJobs, jobQueue);
		sim.run();
//		System.out.println("Jobs executed by " + server1 + ": "
//				+ server1.jobsExecuted);
//		System.out.println("Jobs executed by " + server2 + ": "
//				+ server2.jobsExecuted);

		Series.Stats stats = jobAdder.getRespTimeSeries().stats();
		jobAdder.getRespTimeSeries().print();
		System.out.printf("Response time mean   = %.3f%n", stats.mean);
		System.out.printf("Response time stddev = %.3f%n", stats.stddev);

		Job.resetCurrentId();

		System.out.println();
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
		jobAdder = new JobAdder(sim, servers, rand, interArrivalTime,
				maximumJobs, jobQueue);
		sim.run();
//		System.out.println("Jobs executed by " + server1 + ": "
//				+ server1.jobsExecuted);
//		System.out.println("Jobs executed by " + server2 + ": "
//				+ server2.jobsExecuted);
//		System.out.println("Jobs executed by " + server3 + ": "
//				+ server3.jobsExecuted);
//		System.out.println("Jobs executed by " + server4 + ": "
//				+ server4.jobsExecuted);
		stats = jobAdder.getRespTimeSeries().stats();
		jobAdder.getRespTimeSeries().print();
		System.out.printf("Response time mean   = %.3f%n", stats.mean);
		System.out.printf("Response time stddev = %.3f%n", stats.stddev);
	}

}
