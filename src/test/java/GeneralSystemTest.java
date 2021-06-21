import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.Math.Functions.ODEFunctionInterface;
import API.Math.Solvers.*;
import phase3.Simulation.State.RateOfChange;
import API.System.StateInterface;
import phase3.Simulation.State.SystemState;

import java.util.Arrays;
import static java.lang.Math.sin;
import static java.lang.StrictMath.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static API.Config.*;
/**
 *Equation : y″ + 2y′ − 3y = 0
 *Evaluate with 4e^t + - 3e^(-3t)
 */
class GeneralSystemTest {
    static ODESolverInterface<Double> solver;
    private boolean isStep;
    private final double e = Math.exp(1);

    //y″ + 2y′ − 3y = 0, y(0) = 1, y′(0) = 13

    static StateInterface<Vector3dInterface> vectorState;
    static StateInterface<Double> doubleState;

    static double t, tf, stepSize;
    static ODEFunctionInterface<Vector3dInterface> dydx = (t, y) -> {
        Vector3dInterface position = y.get()[0];
        Vector3dInterface rateOfChange = y.get()[1];


        double acceleration = 3*position.getX()-2*rateOfChange.getX();
        double next_velocity = rateOfChange.getX() + stepSize*acceleration;
        return new RateOfChange<>(new Vector3D(next_velocity, 0, 0),new Vector3D(acceleration, 0, 0));

    };
    static ODEFunctionInterface<Double> dy = (t, y) -> {
        double x = y.get()[0];
        double dx = y.get()[1];
        double ddx = 3*x-2*dx;
        double next_dx = dx + ddx*stepSize;

        return new RateOfChange<>(next_dx,ddx);
    };

    @BeforeEach
    void setup() {

        stepSize = 0.00001;
        t = 0;
        tf = 0.2;
        double x1 = 1;
        double x2 = 13;
        vectorState = new SystemState<>(new Vector3D(x1, 0, 0),new Vector3D(x2,0,0));
        doubleState = new SystemState<>(x1, x2);

    }


    @Test
    @DisplayName("midPointSolve")
    void midPointSolve() {
        isStep = false;
        SOLVER = MIDPOINT;
        solver = initSolver(dy);
        setupSolve("MIDPOINT");
    }
    @Test
    @DisplayName("rkSolve")
    void rkSolve() {
        isStep = false;
        SOLVER = RK4;
        solver = initSolver(dy);
        setupSolve("RK");
    }
    @Test
    @DisplayName("StandardVerlet")
    void StandardVerlet() {
        isStep = false;
        SOLVER = VERLET_STD;
        solver = initSolver(dy);
        setupSolve("verletStd");
    }

    @Test
    @DisplayName("VerletVelocity")
    void VelocityVerlet() {
        isStep = false;
        SOLVER = VERLET_VEL;
        solver = initSolver(dy);
        setupSolve("verletVel");
    }

    @Test
    @DisplayName("eulerSolve")
    void eulerSolve() {
        isStep = false;
        SOLVER = EULER;
        solver = initSolver(dy);
        setupSolve("EULER");
    }
    @Test
    @DisplayName("midPointStep")
    void midPointStep() {
        isStep = true;
        SOLVER = MIDPOINT;
        solver = initSolver(dy);
        setupSolve("MIDPOINT");
    }
    @Test
    @DisplayName("rkStep")
    void rkStep() {
        isStep = true;
        SOLVER = RK4;
        solver = initSolver(dy);
        setupSolve("RK");
    }

    @Test
    @DisplayName("eulerStep")
    void eulerStep() {
        isStep = true;
        SOLVER = EULER;
        solver = initSolver(dy);
        setupSolve("EULER");
    }

    private void setupSolve(String solverName){
        if(isStep){
            step(solverName);
        } else {
            this.solveDouble(solverName);
        }
    }


    double checkAtTime(double t){
        return 4*pow(e,t)-3*pow(e,-3*t);
    }

    private void printDoubleResults(String solver, StateInterface<Double> y) {
        System.out.println(solver + " : " + Arrays.toString(y.get()));
        System.out.println(solver + " ABS ERROR: " + (checkAtTime(t) - y.get()[0]));
        assertTrue(() -> abs(checkAtTime(t) - y.get()[0]) < 1);
    }

    private ODESolverInterface<Double> initSolver(ODEFunctionInterface<Double> function) {
        switch (SOLVER) {
            case EULER -> {
                return new EulerSolver<>(function);
            }
            case RK4 -> {
                return new RungeKuttaSolver<>(function);
            }
            case VERLET_STD -> {
                return new StandardVerletSolver<>(function);
            }
            case VERLET_VEL -> {
                return new VerletVelocitySolver<>(function);
            }
            case MIDPOINT -> {
                return new MidPointSolver<>(function);
            }
            default -> throw new IllegalStateException();
        }
    }

    private StateInterface<Double> solveDouble(String solverName) {
        while (t <= tf + stepSize) {
            doubleState = solver.step(dy, t, doubleState, stepSize);
            t += stepSize;
        }
        printDoubleResults(solverName, doubleState);
        return doubleState;
    }
    private StateInterface<Double> step(String solverName) {
        doubleState = solver.step(dy, stepSize, doubleState, stepSize);
        printDoubleResults(solverName, doubleState);
        return doubleState;
    }

}
