package API.Math.Interpolation;

import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.Math.Functions.Derivative;
import API.Math.Functions.Function;
import API.Math.Plot;

import static java.lang.StrictMath.sin;
import static API.Math.Functions.SecondDerivative.h;

public class ApproximatePolynomial {

    static Vector3dInterface[] positions, velocities, accelerations;
    static Derivative<Vector3dInterface, Double> dy = (x, f) -> f.apply(x + h).sub(f.apply(x - h)).div(h + h);
    static Derivative<Vector3dInterface, Double> d2y = (x, f) -> f.apply(x - h).sub(f.apply(x).mul(2)).add(f.apply(x + h)).div(h * h);


    //time array
    static double[] time;
    static double startT = -200, endT = 200;
    static int length = 800;

    static double step;

    public static void main(String[] args) {
        initTime();
        positions = initPositions();
        LSCubicVector trial = new LSCubicVector(time, positions);
        Vector3dInterface[] solution = trial.gV(time);
        plot3Vector(solution, "POSITIONS");
        velocities = findDerivatives(trial::gV, dy);
        plot3Vector(velocities, "VELOCITIES");
        accelerations = findDerivatives(trial::gV, d2y);
        plot3Vector(accelerations, "ACCELERATIONS");
    }

    private static Vector3dInterface[] findDerivatives(Function<Vector3dInterface, Double> fx, Derivative<Vector3dInterface, Double> derivative) {
        Vector3dInterface[] velocities = new Vector3dInterface[length];
        for (int i = 0; i < velocities.length; i++) {
            velocities[i] = derivative.derivative(time[i], fx);
        }
        return velocities;
    }

    //{ 0    ,   25   ,   50  ,   75    ,  100   ,   125  ,  150   ,  175   ,   200   , 225   , 250  ,  275 , 300  }
    private static void initTime() {
        time = new double[length];
        double t = startT;
        step = (endT - startT) / length;
        for (int i = 0; i < length; i++)
            time[i] = (t += step);
    }

    private static Vector3dInterface[] initPositions(double[] vx, double[] vy, double[] vz) {
        if (vx.length != vy.length || vy.length != vz.length || vx.length != length) throw new IllegalStateException();
        Vector3dInterface[] positions = new Vector3dInterface[vx.length];
        for (int i = 0; i < positions.length; i++)
            positions[i] = new Vector3D(vx[i], vy[i], vz[i]);
        return positions;
    }

    //better strategy here maybe
    private static Vector3dInterface[] initPositions() {
        double[] vx = new double[length],
                vy = new double[length],
                vz = new double[length];
        Function<Double, Double> Fx = x -> x * x * x * x;
        Function<Double, Double> Fy = x -> sin(x * x) + x * x * x;
        Function<Double, Double> FTheta = x -> (10 * x * x * x - 30 * x);

        for (int i = 0; i < length; i++) {
            Double x = time[i];
            vx[i] = Fx.apply(x);
            vy[i] = Fy.apply(x);
            vz[i] = FTheta.apply(x);
        }
        return initPositions(vx, vy, vz);
    }


    private static void plot3Vector(Vector3dInterface[] solution, String title) {
        double[] solutionX = new double[time.length],
                solutionY = new double[time.length],
                solutionZ = new double[time.length];

        for (int i = 0; i < solution.length; i++) {
            solutionX[i] = solution[i].getX();
            solutionY[i] = solution[i].getY();
            solutionZ[i] = solution[i].getZ();
        }

        new Plot(title)
                //.translate(-150,0)
                //.scale(20)
                .plot(time, solutionX, 1, "x")
                .plot(time, solutionY, 2, "y")
                .plot(time, solutionZ, 3, "theta");
    }
}
