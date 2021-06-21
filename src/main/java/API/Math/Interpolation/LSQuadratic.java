package API.Math.Interpolation;

import API.Math.ADT.Matrix;
import API.Math.Functions.Function;

import static java.lang.Math.pow;

public class LSQuadratic extends LSLinear {
    protected Matrix a;
    protected double x3sum, x4sum, x2ysum;
    protected double x2YAvg, X3Avg, X4Avg;
    private final Function<Double, Double> g = (x) -> {
        //ax^2 + bx + c
        if (a == null) this.makeMatrix();
        return a.matrix[0][0] + a.matrix[1][0] * x + a.matrix[2][0] * pow(x, 2);
    };

    public LSQuadratic(double[] x, double[] y) {
        super(x, y);
        computed = false;
    }

    public LSQuadratic() {

    }

    @Override
    protected void compute() {
        super.compute();
        x3sum = 0;
        x4sum = 0;
        x2ysum = 0;
        X3Avg = X4Avg = x2YAvg = 0;
        for (int i = 0; i < this.xs.length; i++) {
            x3sum += pow(this.xs[i], 3);
            x4sum += pow(this.xs[i], 4);
            x2ysum += (pow(this.xs[i], 2) * this.ys[i]);
        }
        X3Avg = x3sum / this.xs.length;
        X4Avg = x4sum / this.xs.length;
        x2YAvg = x2ysum / this.xs.length;
    }


    @Override
    protected void makeMatrix() {
        Matrix matrix = new Matrix(new double[][]{
                {1, XAvg, X2Avg},//x3
                {XAvg, X2Avg, X3Avg},//x4
                {X2Avg, X3Avg, X4Avg} //x5
        });      //x3    x4     x5     x6
        Matrix r = new Matrix(new double[]{YAvg, XYAvg, x2YAvg});
        if (OUTPUT) {
            System.out.println("Quadratic");
            matrix.printMatrix();
            r.printMatrix();
        }
        this.a = Matrix.multiply(new Matrix(Matrix.inverse(matrix.getMatrix())), r);
    }

    @Override
    public double g(double x) {
        if (!computed) compute();
        return this.g.apply(x);
    }

}
