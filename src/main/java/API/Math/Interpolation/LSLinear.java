package API.Math.Interpolation;

import org.jetbrains.annotations.Contract;
import API.Math.Functions.Function;

import static java.lang.Math.pow;

public class LSLinear {
    protected double[] xs, ys;
    protected double xsum, ysum, x2sum, xysum;
    protected double XAvg, YAvg, XYAvg, X2Avg;
    private final Function<Double, Double> g = (x) -> a0() + a1() * x;
    protected boolean OUTPUT = false;
    protected boolean computed;

    @Contract(pure = true)
    public LSLinear() {

    }

    public LSLinear(double[] xs, double[] ys) {
        if (xs.length != ys.length) throw new IllegalArgumentException("X,Y must have the same size");
        computed = false;
        this.xs = xs;
        this.ys = ys;
    }

    protected void compute() {
        xsum = ysum = x2sum = xysum = 0;
        XAvg = YAvg = XYAvg = X2Avg = 0;
        for (int i = 0; i < this.xs.length; i++) {
            xsum += this.xs[i];
            ysum += this.ys[i];
            x2sum += pow(this.xs[i], 2);
            xysum += (this.xs[i] * this.ys[i]);
        }
        XAvg = xsum / this.xs.length;
        YAvg = ysum / this.xs.length;
        X2Avg = x2sum / this.xs.length;
        XYAvg = xysum / this.xs.length;
        computed = true;
    }

    @Contract(pure = true)
    private double a0() {
        return YAvg - a1() * XAvg;
    }

    //ax + b =
    @Contract(pure = true)
    private double a1() {
        return (XYAvg - (XAvg * YAvg)) / (X2Avg - pow(XAvg, 2));
    }

    public double g(double x) {
        if (!computed) compute();
        return g.apply(x);
    }

    public double[] g(double[] xs) {
        if (!computed) compute();
        double[] x = new double[xs.length];
        for (int i = 0; i < x.length; i++)
            x[i] = g(xs[i]);
        return x;
    }

    protected void makeMatrix() {
    }

}