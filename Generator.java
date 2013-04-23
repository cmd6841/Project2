import edu.rit.numeric.ExponentialPrng;
import edu.rit.numeric.ListSeries;
import edu.rit.numeric.Series;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

public abstract class Generator
	{
	protected Simulation sim;
	protected ExponentialPrng treqPrng;
	protected int nreq;

	protected ListSeries respTimeSeries;
	protected int n;

	/**
	 * Create a new request generator.
	 *
	 * @param  sim     Simulation.
	 * @param  treq    Request mean interarrival time.
	 * @param  nreq    Number of requests.
	 * @param  prng    Pseudorandom number generator.
	 */
	public Generator
		(Simulation sim,
		 double treq,
		 int nreq,
		 Random prng)
		{
		this.sim = sim;
		this.treqPrng = new ExponentialPrng (prng, 1.0/treq);
		this.nreq = nreq;

		respTimeSeries = new ListSeries();
		n = 0;
		}

	/**
	 * Generate the next request.
	 */
	protected abstract void generateRequest();

	/**
	 * Returns a data series containing the response time statistics of the
	 * generated requests.
	 *
	 * @return  Response time series.
	 */
	public Series responseTimeSeries()
		{
		return respTimeSeries;
		}

	/**
	 * Returns the response time statistics of the generated requests.
	 *
	 * @return  Response time statistics (mean, standard deviation, variance).
	 */
	public Series.Stats responseTimeStats()
		{
		return respTimeSeries.stats();
		}

	/**
	 * Returns the drop fraction of the generated requests.
	 */
	public double dropFraction()
		{
		return (double)(nreq - respTimeSeries.length())/(double)nreq;
		}
	}
