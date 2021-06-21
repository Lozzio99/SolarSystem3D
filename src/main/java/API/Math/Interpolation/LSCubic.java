package API.Math.Interpolation;

import API.Math.ADT.Matrix;
import API.Math.Functions.Function;

import static java.lang.Math.pow;

public class LSCubic extends LSQuadratic {

    double x5sum, x6sum, x3ysum;
    double x3YAvg, X5Avg, X6Avg;
    private final Function<Double, Double> g = (x) -> {
        if (a == null) makeMatrix();
        return a.matrix[0][0] + a.matrix[1][0] * x + a.matrix[2][0] * pow(x, 2) + a.matrix[3][0] * pow(x, 3);
    };

    public LSCubic(double[] xs, double[] ys) {
        super(xs, ys);
        computed = false;
    }

    public LSCubic() {

    }

    @Override
    public double g(double x) {
        if (!computed) compute();
        return this.g.apply(x);
    }


    @Override
    protected void compute() {
        super.compute();
        x5sum = 0;
        x6sum = 0;
        x3ysum = 0;
        x3YAvg = X5Avg = X6Avg = 0;
        for (int i = 0; i < this.xs.length; i++) {
            x5sum += pow(this.xs[i], 5);
            x6sum += pow(this.xs[i], 6);
            x3ysum += (pow(this.xs[i], 3) * this.ys[i]);
        }
        x3YAvg = x3ysum / this.xs.length;
        X5Avg = x5sum / this.xs.length;
        X6Avg = x6sum / this.xs.length;

        computed = true;
    }

    @Override
    protected void makeMatrix() {
        Matrix matrix = new Matrix(new double[][]{
                {1, XAvg, X2Avg, X3Avg},//x3
                {XAvg, X2Avg, X3Avg, X4Avg},//x4
                {X2Avg, X3Avg, X4Avg, X5Avg}, //x5
                {X3Avg, X4Avg, X5Avg, X6Avg}
        });      //x3    x4     x5     x6
        Matrix r = new Matrix(
                new double[]{YAvg, XYAvg, x2YAvg, x3YAvg});  //V_X3YAvg
        this.a = Matrix.multiply(new Matrix(Matrix.inverse(matrix.getMatrix())), r);
        if (OUTPUT) {
            System.out.println("Cubic ");
            System.out.println("S : ");
            matrix.printMatrix();
            r.printMatrix();
            this.a.printMatrix();
            System.out.println("--------------------");
        }

    }
}
