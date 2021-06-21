package API.Math.Integration;

import API.Math.Functions.Function;

public class TrapezoidIntegration implements Integration {

    public double integrate(Function<Double, Double> fX, double h, double a, double b) {
        int N = (int) ((b - a) / h);
        double sum = (fX.apply(a) + fX.apply(b)) * 0.5;
        for (int i = 1; i < N; i++) {
            sum += fX.apply(a + h * i);
        }
        return h * sum;
    }

}
