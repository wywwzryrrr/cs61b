package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> testCount = new AList<>();
        for(int n = 1; n <= 128; n *= 2) {
            testCount.addLast(n * 1000);
        }
        AList<Double> testTimes = new AList<>();
        AList<Integer> testOpCounts = new AList<>();
        for(int i = 0; i < testCount.size(); i += 1) {
            testOpCounts.addLast(10000);
        }
        for(int i = 0; i < testCount.size(); i += 1) {
            SLList<Integer> testDemo = new SLList<>();
            for(int n = 0; n < testCount.get(i); n += 1) {
                testDemo.addLast(n);
            }
            long start = System.currentTimeMillis();
            for(int m = 0; m < testOpCounts.get(i); m += 1) {
                int last = testDemo.getLast();
            }
            testTimes.addLast((System.currentTimeMillis() - start) / 1000.0);
        }
        printTimingTable(testCount, testTimes, testOpCounts);
    }
}
