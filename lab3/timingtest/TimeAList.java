package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> testCount = new AList<>();
        for(int i = 1; i <= 128; i *= 2) {
            testCount.addLast(i * 1000);
        }
        AList<Double> testTimes = new AList<>();
        for(int i = 0; i < testCount.size(); i += 1) {
            AList<Integer> testCase = new AList<>();
            long start = System.currentTimeMillis();
            for(int n = 0; n < testCount.get(i); n += 1) {
                testCase.addLast(100);
            }
            testTimes.addLast((System.currentTimeMillis() - start) / 1000.0);
        }
        printTimingTable(testCount, testTimes, testCount);
    }
}
