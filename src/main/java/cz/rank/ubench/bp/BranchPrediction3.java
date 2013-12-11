package cz.rank.ubench.bp;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Karel Rank
 */
public class BranchPrediction3 {
    public static final int MAX_INTS = 1000000;
    public static final int ITERATIONS = 100;

    private final int[] randomizedInts = new int[MAX_INTS];
    private final int[] sortedInts = new int[MAX_INTS];
    private long totalSorted;
    private long totalRandomized;
    private int totalOverHalf;

    private BranchPrediction3() {
        final Random random = new Random(System.nanoTime());

        final int[] ints = new int[MAX_INTS];

        for (int i = 0; i < MAX_INTS; i++) {
            ints[i] = random.nextInt(MAX_INTS);
        }

        System.arraycopy(ints, 0, randomizedInts, 0, MAX_INTS);

        System.arraycopy(ints, 0, sortedInts, 0, MAX_INTS);
        Arrays.sort(sortedInts);
    }

    private void performTest() {
        totalSorted = 0;
        totalRandomized = 0;
        totalOverHalf = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            iterateRandomized();

            iterateSorted();
        }

        System.out.format("Randomized total=%,12d ns; avg=%,12d ns\n", totalRandomized, totalRandomized / ITERATIONS);
        System.out.format("Sorted total=%,12d ns; avg=%,12d ns\n", totalSorted, totalSorted / ITERATIONS);
        System.out.println("Over half total=" + totalOverHalf); // Avoid BCE
    }

    private void iterateSorted() {
        long t1;
        long t2;
        t1 = System.nanoTime();
        totalOverHalf += iterateOver(sortedInts);
        t2 = System.nanoTime();

        totalSorted += t2 - t1;
    }

    private void iterateRandomized() {
        long t1 = System.nanoTime();
        totalOverHalf += iterateOver(randomizedInts);
        long t2 = System.nanoTime();

        totalRandomized += t2 - t1;
    }

    private void warmUp() {
        for (int i = 0; i < ITERATIONS*ITERATIONS; i++) {
            iterateRandomized();

            iterateSorted();
        }
    }

    /**
     * @return ints greater than {@link #MAX_INTS} / 2
     * @param ints
     */
    private int iterateOver(int[] ints) {
        int greaterInts = 0;

        for (int i : ints) {
            if (i > MAX_INTS / 2) {
                greaterInts++;
            }
        }

        return greaterInts;
    }

    public static void main(String[] args) {
        BranchPrediction3 bp = new BranchPrediction3();

        bp.warmUp();
        bp.performTest();
    }
}
