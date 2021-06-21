import API.System.StateInterface;
import API.Math.Functions.Function;
import API.Math.Functions.ODEFunctionInterface;
import API.Math.Solvers.*;
import phase3.Simulation.State.RateOfChange;
import phase3.Simulation.State.SystemState;
import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.pow;

public class ODE2Test {
    static StateInterface<Double> y;

    static Function<Double, Double> position = t -> {
        return (t * t * t) - (4 * t) + 3;   // y (t)
    };
    static Function<Double, Double> velocity = t -> {
        return 3 * t * t - 4;   // dy/h
    };
    static ODEFunctionInterface<Double> acceleration = (t, y) -> {
        // y =   [  y , dy ]
        return new RateOfChange<>(y.get()[1], 6 * t);
        //return [  dy, d2y ]
    };

    public static void main(String[] args) {
        double t = 0, tf = 2, stepSize = 0.1;
        for (int scale = 2; scale <= 10; scale += 2) {
            halfStepSize(new EulerSolver<>(acceleration), t, tf, stepSize, scale);
            halfStepSize(new MidPointSolver<>(acceleration), t, tf, stepSize, scale);
            halfStepSize(new RungeKuttaSolver<>(acceleration), t, tf, stepSize, scale);
            halfStepSize(new StandardVerletSolver<>(acceleration), t, tf, stepSize, scale);
            halfStepSize(new VerletVelocitySolver<>(acceleration), t, tf, stepSize, scale);
        }

    }

    public static StateInterface<Double> y0(double t) {
        return new SystemState<>(position.apply(t), velocity.apply(t));
    }

    public static void halfStepSize(ODESolverInterface<Double> solver, double t0, double tf, double stepSize, double scaling) {
        System.out.println(solver);
        double t = t0;
        double[] errorH, error_half_H;
        y = y0(t);
        while (t <= tf) {
            y = solver.step(acceleration, t, y, stepSize);
            t += stepSize;
        }
        print(stepSize, errorH = error(y, t));

        stepSize = stepSize / scaling;

        t = t0;
        y = y0(t);
        while (t <= tf) {
            y = solver.step(acceleration, t, y, stepSize);
            t += stepSize;
        }
        print(stepSize, error_half_H = error(y, t));

        testScalingError(errorH, error_half_H, solver.toString(), scaling);
        System.out.println("-------------------------------");
    }

    private static void testScalingError(double[] errorH, double[] error_half_h, String solver, double scaling) {
        int order = switch (solver) {
            case "EulerSolver" -> 1;
            case "StandardVerletSolver", "MidPointSolver", "VerletVelocitySolver" -> 2;
            case "RungeKutta4thSolver" -> 4;
            default -> 0;
        };
        //System.out.println(Arrays.toString(errorH));
        //System.out.println(Arrays.toString(error_half_h));
        for (int i = 0; i < errorH.length; i++) {
            // expect halfH error to be 1/(2^order) of H error
            //because step size has been divided by 2
            double expectedError = errorH[i] / (pow(scaling, order));
            double actualError = error_half_h[i];
            if (!(abs(expectedError - actualError) < 1 / pow(10, scaling)) && !(actualError < expectedError)) {
                System.err.println(solver + " ~ expected : " + expectedError + " ,actual : " + actualError + " ,scaling : " + scaling);
            }
        }
    }

    public static double[] error(StateInterface<Double> y, double time) {
        double absP, relP, absV, relV;
        absP = absolute(y.get()[0], position.apply(time));
        absV = absolute(y.get()[1], velocity.apply(time));
        relP = relative(y.get()[0], position.apply(time));
        relV = relative(y.get()[1], velocity.apply(time));
        return new double[]{absP, absV, relP, relV};
    }

    public static void print(double stepSize, double[] err) {
        System.out.println("StepSize : " + stepSize);
        System.out.println("ABSOLUTE\npos : " + err[0]);
        System.out.println("vel : " + err[1]);
        System.out.println("RELATIVE\npos : " + err[2]);
        System.out.println("vel : " + err[3]);
        System.out.println();
    }

    public static double absolute(double actual, double real) {
        return abs(actual - real);
    }

    public static double relative(double actual, double real) {
        return (absolute(actual, real)) / real;
    }

}
