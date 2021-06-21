package API.Math.Solvers;


import API.Math.Functions.ODEFunctionInterface;
import API.System.RateInterface;
import API.System.StateInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The type Runge kutta 4 th solver.
 * Fourth order solver for First Order ODE
 */

//OK around 10^-2/10^-3 error
public record RungeKuttaSolver<E>(ODEFunctionInterface<E> f) implements ODESolverInterface<E> {


    /**
     * Instantiates a new Runge kutta 4 th solver.
     *
     * @param f the function
     */
    @Contract(pure = true)
    public RungeKuttaSolver {
    }


    /**
     * k1=f(tn,yn)
     * k2=f(tn+h2,yn+h/2*k1)
     * k3=f(tn+h2,yn+h/2*k2)
     * k4=hf(tn+h,yn+h*k3)
     * kk = (k1 + 2*k2 + 2*k3 + k4)/6
     *
     * @param f the function representing Newton's Gravitational law
     * @param y the instance of the Simulation
     * @param h stepSize
     * @return the next state of the simulationType based on Runge-Kutta 4 Step
     */
    public StateInterface<E> step(ODEFunctionInterface<E> f, double t, final StateInterface<E> y, double h) {
        RateInterface<E> k1, k2, k3, k4;
        k1 = f.call(t, y);
        k2 = f.call(t + (h / 2), y.addMul(h * 0.5, k1));
        k3 = f.call(t + (h / 2), y.addMul(h * 0.5, k2));
        k4 = f.call(t + h, y.addMul(h, k3));
        return y.addMul(h / 6, k1)
                .addMul(h / 3, k2)
                .addMul(h / 3, k3)
                .addMul(h / 6, k4);
    }

    @Contract(pure = true)
    @Override
    public ODEFunctionInterface<E> getFunction() {
        return this.f;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "RungeKutta4thSolver";
    }


}
