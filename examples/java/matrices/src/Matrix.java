import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Matrix {
    private final double[][] values;

    public Matrix(double[][] values) {
        this.values = values;
    }

    public double sum() {
        var result = 0.0;
        for (double[] value : values) {
            result += addRow(value);
        }
        return result;
    }

    public double sumParallel() throws InterruptedException {
        var result = 0.0;
        List<MyThread<Double>> thread = new ArrayList<>();
        for (double[] value : values) {
            MyThread<Double> instance = new MyThread<>(()->addRow(value), 0);
            thread.add(instance);
            instance.start();
        }
        for (MyThread<Double> thread1 : thread) {
            thread1.join();
            result += thread1.result;
        }
        return result;
    }

    public Matrix addSerial(Matrix other) {
        double[][] result = new double[values.length][];
        for (int i = 0; i < values.length; i++) {
            int cols = values[i].length;
            var row = new double[cols];
            for (int j = 0; j < cols; j++) {
                row[j] = values[i][j] + other.values[i][j];
            }
            result[i] = row;
        }
        return new Matrix(result);
    }
    public Matrix addParallel(Matrix other) throws InterruptedException {

        double[][] result = new double[values.length][];
        List<MyThread<double[]>> thread = new ArrayList<>();
        for (int i = 0; i <values.length; i++) {
            int finalI = i;
            MyThread myThread = new MyThread(()->addRows(values[finalI], other.values[finalI]), i);
            thread.add(myThread);
            myThread.start();
        }

        for (MyThread<double[]> thread1 : thread) {
            thread1.join();
            result[thread1.getRow()] = thread1.result;
        }
        return new Matrix(result);
    }

    private double[] addRows(double[] row1, double[] row2) {
        var row = new double[row1.length];
        for (int j = 0; j < row1.length; j++) {
            row[j] = row1[j] + row2[j];
        }
        return row;
    }

    private class MyThread<T> extends Thread {
        private Supplier<T> supplier;
        private T result;
        private int row;

        public MyThread(Supplier<T> supplier, int row) {
            this.supplier = supplier;
            this.row = row;
        }


        @Override
        public void run() {
            result = supplier.get();
        }

        public T getResult() {
            return result;
        }

        public int getRow() {return row;}
    }

    private double addRow(double[] value) {
        var result = 0.0;
        for (double v : value) {
            result += v;
        }
        return result;
    }

    static class ThreadValue<T> extends Thread {
        private final Supplier<T> expression;
        private T result;

        public ThreadValue(Supplier<T> expression) {
            this.expression = expression;
        }
        public void run() { result = expression.get(); }
        public T getValue() { return result; }
    }


}