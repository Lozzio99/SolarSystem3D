package API.Math.Solvers;

import API.Math.Functions.ODEFunctionInterface;
import API.System.StateInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The type Mid point solver.
 * Second order Solver
 */
public record MidPointSolver<E>(ODEFunctionInterface<E> f) implements ODESolverInterface<E> {
    /**
     * Instantiates a new Mid point solver.
     *
     * @param f the f
     */
    @Contract(pure = true)
    public MidPointSolver {
    }

    /**
     * @param f the function representing Newton's Gravitational law
     * @param y the instance  of the Simulation
     * @param h stepSize
     * @return the next state of the simulationType based on A Midpoint Step
     */
    @Override
    public StateInterface<E> step(ODEFunctionInterface<E> f, double t, StateInterface<E> y, double h) {
        return y.addMul(h, f.call(t + (h / 2), y.addMul(h / 2, f.call(t, y))));
    }

    @Contract(pure = true)
    @Override
    public ODEFunctionInterface<E> getFunction() {
        return this.f;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "MidPointSolver";
    }
}
