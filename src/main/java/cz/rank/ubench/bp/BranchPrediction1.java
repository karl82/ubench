package cz.rank.ubench.bp;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Karel Rank
 */
public class BranchPrediction1 {
    public static final int MAX_INTS = 1000000;
    public static final int ITERATIONS = 100;

    private final int[] randomizedInts = new int[MAX_INTS];
    private final int[] sortedInts = new int[MAX_INTS];

    private BranchPrediction1() {
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
        long totalSorted = 0;
        long totalRandomized = 0;
        int totalOverHalf = 0;

        for (int i = 0; i < ITERATIONS; i++) {
            long t1 = System.nanoTime();
            totalOverHalf += iterateOverRandomized();
            long t2 = System.nanoTime();

            totalRandomized += t2 - t1;

            t1 = System.nanoTime();
            totalOverHalf += iterateOverSorted();
            t2 = System.nanoTime();

            totalSorted += t2 - t1;
        }

        System.out.format("Randomized total=%,12d ns; avg=%,12d ns\n", totalRandomized, totalRandomized / ITERATIONS);
        System.out.format("Sorted total=%,12d ns; avg=%,12d ns\n", totalSorted, totalSorted / ITERATIONS);
        System.out.println("Over half total=" + totalOverHalf); // Avoid BCE
    }

    private void warmUp() {
        for (int i = 0; i < ITERATIONS*ITERATIONS; i++) {
            iterateOverRandomized();

            iterateOverSorted();
        }
    }

    /**
     * @return ints greater than {@link #MAX_INTS} / 2
     */
    private int iterateOverRandomized() {
        int greaterInts = 0;

        for (int i : randomizedInts) {
            if (i > MAX_INTS / 2) {
                greaterInts++;
            }
        }

        return greaterInts;
    }

    /**
     * @return ints greater than {@link #MAX_INTS} / 2
     */
    private int iterateOverSorted() {
        int greaterInts = 0;

        for (int i : sortedInts) {
            if (i > MAX_INTS / 2) {
                greaterInts++;
            }
        }

        return greaterInts;
    }

    public static void main(String[] args) {
        BranchPrediction1 bp = new BranchPrediction1();

        bp.warmUp();
        bp.performTest();
    }
}
