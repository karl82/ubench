package cz.rank.ubench.osr;

/**
 * Jednoduchy benchmark pro porovnani rychlostnich
 * rozdilu mezi pouzitim synchronizovanych metod
 * a synchronizovanych bloku.
 */
public class SynchronizationTest4 {
    // dostatecny pocet iteraci pro spusteni JITu
    private final static int ITERATIONS = 5000000;

    // zamezime tomu, aby se do casu behu benchmarku
    // zapocital i cas JITu
    private final static int WARMUP = 3;

    long sumTestX = 0, sumTestY = 0, sumTestZ = 0, sumTestV = 0;

    private int x = 0;
    private int y = 0;
    private int z = 0;
    private volatile int v = 0;

    // test s nesynchronizovanou metodou
    private void testX() {
        x++;
    }

    // test se synchronizovanou metodou
    private synchronized void testY() {
        y++;
    }

    // test s metodou se synchronizovanym blokem
    private void testZ() {
        synchronized (this) {
            z++;
        }
    }

    // test s metodou vyuzivajici volatilni atribut
    private void testV() {
        v++;
    }

    // spusteni benchmarku
    public static void main(String[] args) {
        SynchronizationTest4 test = new SynchronizationTest4();

        // provest zadany pocet testu
        for (int i = 0; i < 10; i++) {
            test.round(i);

        }

        test.printResults();
    }

    private void printResults() {
        // vypis vsech ctyr kumulativnich casu
        System.out.format("Cumulative time for testX(): %,12d ns\n", sumTestX);
        System.out.format("Cumulative time for testY(): %,12d ns\n", sumTestY);
        System.out.format("Cumulative time for testZ(): %,12d ns\n", sumTestZ);
        System.out.format("Cumulative time for testV(): %,12d ns\n", sumTestV);
        System.out.println("X=" + x);
    }

    private void round(int i) {
        doTestX(i);
        doTextY(i);
        doTestZ(i);
        doTestV(i);
    }

    private void doTestV(int i) {
        long t1;
        long t2;
        long delta_t;// provest test a zmerit cas behu testu
        t1 = System.nanoTime();
        for (int j = 0; j < ITERATIONS; j++) {
            testV();
        }
        t2 = System.nanoTime();
        delta_t = t2 - t1;
        if (i > WARMUP) {
            sumTestV += delta_t;
        }

        // vypis casu pro treti test
        System.out.format("Round #%2d testV() time: %,12d ns\n", i, delta_t);
    }

    private void doTestZ(int i) {
        long t1;
        long t2;
        long delta_t;// provest test a zmerit cas behu testu
        t1 = System.nanoTime();
        for (int j = 0; j < ITERATIONS; j++) {
            testZ();
        }
        t2 = System.nanoTime();
        delta_t = t2 - t1;
        if (i > WARMUP) {
            sumTestZ += delta_t;
        }

        // vypis casu pro treti test
        System.out.format("Round #%2d testZ() time: %,12d ns\n", i, delta_t);
    }

    private void doTextY(int i) {
        long t1;
        long t2;
        long delta_t;// provest test a zmerit cas behu testu
        t1 = System.nanoTime();
        for (int j = 0; j < ITERATIONS; j++) {
            testY();
        }
        t2 = System.nanoTime();
        delta_t = t2 - t1;
        if (i > WARMUP) {
            sumTestY += delta_t;
        }

        // vypis casu pro druhy test
        System.out.format("Round #%2d testY() time: %,12d ns\n", i, delta_t);
    }

    private void doTestX(int i) {
        long t1;
        long t2;
        long delta_t;
        t1 = System.nanoTime();
        for (int j = 0; j < ITERATIONS; j++) {
            testX();
        }
        t2 = System.nanoTime();
        delta_t = t2 - t1;
        if (i > WARMUP) {
            sumTestX += delta_t;
        }

        // vypis casu pro prvni test
        System.out.format("Round #%2d testX() time: %,12d ns\n", i, delta_t);
    }

}
