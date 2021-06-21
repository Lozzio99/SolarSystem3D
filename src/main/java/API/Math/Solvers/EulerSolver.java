package API.Math.Solvers;


import API.Math.Functions.ODEFunctionInterface;
import API.System.StateInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The type Euler solver.
 * First Order Solver
 */
public record EulerSolver<E>(ODEFunctionInterface<E> f) implements ODESolverInterface<E> {

    /**
     * Instantiates a new Euler solver.
     *
     * @param f the f
     */
    @Contract(pure = true)
    public EulerSolver {
    }

    /**
     * Formula
     * y1 = y0 + h*f(t,y0);
     *
     * @param f the function representing Newton's Gravitational law
     * @param y the instance  of the Simulation
     * @return the next state of the simulationType based on a Euler Step
     */
    @Override
    public StateInterface<E> step(ODEFunctionInterface<E> f, double t, StateInterface<E> y, double h) {
        return y.addMul(h, f.call(t, y));
    }

    @Contract(pure = true)
    @Override
    public ODEFunctionInterface<E> getFunction() {
        return f;
    }


    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "EulerSolver";
    }


}
