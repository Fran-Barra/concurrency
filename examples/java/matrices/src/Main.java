public class Main {
    public static void main(String[] args) throws InterruptedException {
        Matrix m = create(10, 10_000_000);

        System.out.println("Sum = " + m.sum());

       // Matrix r = m.addSerial(m);

        System.out.println("Sum Parallel = " + m.sumParallel());

        testSum(m);
       // testAdd(m, m);
    }


    private static void testAdd(Matrix m1, Matrix m2) throws InterruptedException {
        var t0 = System.nanoTime();
        var m3 = m1.addSerial(m2);
        var t1 = System.nanoTime();
        System.out.println("Serial 'add': " + (t1 - t0) / 1_000 + " micros");

        var m4 = m1.addParallel(m2);
        var t2 = System.nanoTime();
        System.out.println("Parallel 'add': " + (t2 - t1) / 1_000 + " ms");
    }

    private static void testSum(Matrix m1) throws InterruptedException {
        var t0 = System.nanoTime();
        var m3 = m1.sum();
        var t1 = System.nanoTime();
        System.out.println("Serial 'sum': " + (t1 - t0) / 1_000 + " ms");

        var m4 = m1.sumParallel();
        var t2 = System.nanoTime();
        System.out.println("Parallel 'sum': " + (t2 - t1) / 1_000 + " ms");
    }


    private static Matrix create(int rows, int cols) {
        double[][] result = new double[rows][];
        for (int i = 0; i < rows; i++) {
            var row = new double[cols];
            for (int j = 0; j < cols; j++) {
                row[j] = 1.0;
            }
            result[i] = row;
        }
        return new Matrix(result);
    }
}
