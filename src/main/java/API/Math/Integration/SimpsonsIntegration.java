package API.Math.Integration;

import API.Math.Functions.Function;

public class SimpsonsIntegration implements Integration {

    public double integrate(Function<Double, Double> fX, double h, double a, double b) {
        int N = (int) ((b - a) / h);
        double sum = (fX.apply(a) + fX.apply(b));
        for (int i = 1; i < N; i += 2) {
            sum += fX.apply(a + h * i) * 4.0;
        }
        for (int i = 2; i < N; i += 2) {
            sum += fX.apply(a + h * i) * 2.0;
        }
        return h * sum / 3.0;
    }

}
