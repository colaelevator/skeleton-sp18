package hw2;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private int n;
    private int t;
    private double mean;
    private double deviation;
    private double[] threshold;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        n = N;
        t = T;
        threshold = new double[T];
        for (int i = 0; i < T; ++i) {
            threshold[i] = MonteCarloSimulation.simulation(pf.make(N), n);  // 返回的是比值
        }
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return deviation;
    }

    public double confidenceLow() {
        return (mean - (1.96 * deviation / Math.sqrt((double) t)));
    }

    public double confidenceHigh() {
        return (mean + (1.96 * deviation / Math.sqrt((double) t)));
    }
}
