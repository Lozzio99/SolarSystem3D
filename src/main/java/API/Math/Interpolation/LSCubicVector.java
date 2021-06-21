package API.Math.Interpolation;

import API.Math.ADT.Matrix;
import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.Math.Functions.Function;

import java.util.Arrays;

import static java.lang.Math.pow;

public class LSCubicVector extends LSCubic {
    double V_XXsum, V_XYsum, V_XZsum;   //XY
    double V_X2Xsum, V_X2Ysum, V_X2Zsum;  //x2Y
    double V_X3Xsum, V_X3Ysum, V_X3Zsum;     //x3y
    double V_XAvg, V_YAvg, V_ZAvg;
    double V_XXAvg, V_XYAvg, V_XZAvg;
    double V_X2XAvg, V_X2YAvg, V_X2ZAvg;
    double V_X3XAvg, V_X3YAvg, V_X3ZAvg;

    Function<Vector3dInterface, Double> g = v -> {
        if (a == null) makeMatrix();
        return new Vector3D(
                p3x(v, a.matrix[0][0], a.matrix[1][0], a.matrix[2][0], a.matrix[3][0]),
                p3x(v, a.matrix[0][1], a.matrix[1][1], a.matrix[2][1], a.matrix[3][1]),
                p3x(v, a.matrix[0][2], a.matrix[1][2], a.matrix[2][2], a.matrix[3][2]));
    };
    double[] vx, vy, vz;

    public LSCubicVector(double[] xs, Vector3dInterface[] ys) {
        super(xs, new double[xs.length]);
        this.xs = xs;
        this.vx = new double[xs.length];
        this.vy = new double[xs.length];
        this.vz = new double[xs.length];
        for (int i = 0; i < xs.length; i++) {
            this.vx[i] = ys[i].getX();
            this.vy[i] = ys[i].getY();
            this.vz[i] = ys[i].getZ();
        }
        computed = false;
    }

    public double p3x(double x, double c0, double c1, double c2, double c3) {
        return c0 + (c1 * x) + (c2 * pow(x, 2)) + (c3 * pow(x, 3));
        // return c3 + (c2*x) + (c1 * pow(x,2)) + (c0 * pow(x,3));
    }

    @Override
    public double g(double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double[] g(double[] x) {
        throw new UnsupportedOperationException();
    }

    public Vector3dInterface gV(double v) {
        if (!computed) compute();
        return g.apply(v);
    }

    public Vector3dInterface[] gV(double[] v) {
        if (!computed) compute();
        Vector3dInterface[] x = new Vector3dInterface[xs.length];
        for (int i = 0; i < x.length; i++)
            x[i] = gV(v[i]);
        return x;
    }

    @Override
    protected void compute() {
        double n = this.xs.length;
        V_XXsum = V_XYsum = V_XZsum = 0;
        V_XXAvg = V_XYAvg = V_XZAvg = 0;
        V_X2Xsum = V_X2Ysum = V_X2Zsum = 0;
        V_X2XAvg = V_X2YAvg = V_X2ZAvg = 0;
        V_X3Xsum = V_X3Ysum = V_X3Zsum = 0;
        V_X3XAvg = V_X3YAvg = V_X3ZAvg = 0;

        XAvg = Arrays.stream(this.xs).sum() / n;
        X2Avg = Arrays.stream(this.xs).map(e -> pow(e, 2)).sum() / n;
        X3Avg = Arrays.stream(this.xs).map(e -> pow(e, 3)).sum() / n;
        X4Avg = Arrays.stream(this.xs).map(e -> pow(e, 4)).sum() / n;
        X5Avg = Arrays.stream(this.xs).map(e -> pow(e, 5)).sum() / n;
        X6Avg = Arrays.stream(this.xs).map(e -> pow(e, 6)).sum() / n;
        V_XAvg = Arrays.stream(this.vx).sum() / n;
        V_YAvg = Arrays.stream(this.vy).sum() / n;
        V_ZAvg = Arrays.stream(this.vz).sum() / n;


        for (int i = 0; i < this.xs.length; i++) {
            V_XXsum += this.xs[i] * this.vx[i];
            V_XYsum += this.xs[i] * this.vy[i];
            V_XZsum += this.xs[i] * this.vz[i];
            V_X2Xsum += pow(this.xs[i], 2) * this.vx[i];
            V_X2Ysum += pow(this.xs[i], 2) * this.vy[i];
            V_X2Zsum += pow(this.xs[i], 2) * this.vz[i];
            V_X3Xsum += (pow(this.xs[i], 3) * this.vx[i]);
            V_X3Ysum += (pow(this.xs[i], 3) * this.vy[i]);
            V_X3Zsum += (pow(this.xs[i], 3) * this.vz[i]);
        }
        V_XXAvg = V_XXsum / n;
        V_XYAvg = V_XYsum / n;
        V_XZAvg = V_XZsum / n;
        V_X2XAvg = V_X2Xsum / n;
        V_X2YAvg = V_X2Ysum / n;
        V_X2ZAvg = V_X2Zsum / n;
        V_X3XAvg = V_X3Xsum / n;
        V_X3YAvg = V_X3Ysum / n;
        V_X3ZAvg = V_X3Zsum / n;
        computed = true;
    }

    @Override
    protected void makeMatrix() {
        Matrix x = new Matrix(new double[][]{
                {1, XAvg, X2Avg, X3Avg},//x3
                {XAvg, X2Avg, X3Avg, X4Avg},//x4
                {X2Avg, X3Avg, X4Avg, X5Avg}, //x5
                {X3Avg, X4Avg, X5Avg, X6Avg}
        });      //x3    x4     x5     x6
        Matrix b = new Matrix(
                new double[][]{
                        {V_XAvg, V_YAvg, V_ZAvg},
                        {V_XXAvg, V_XYAvg, V_XZAvg},
                        {V_X2XAvg, V_X2YAvg, V_X2ZAvg},
                        {V_X3XAvg, V_X3YAvg, V_X3ZAvg}
                });


        this.a = Matrix.multiply(new Matrix(Matrix.inverse(x.getMatrix())), b);
        // A * x = b;
        // A = x^-1 * b

        if (OUTPUT) {
            System.out.println("X : ");
            x.printMatrix();
            System.out.println("B : ");
            b.printMatrix();
            System.out.println("--------------------");
            System.out.println("A :");
            this.a.printMatrix();
            System.out.println(" TRY : ");
        }
    }

}
