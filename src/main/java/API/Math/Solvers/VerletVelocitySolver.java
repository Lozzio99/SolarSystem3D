package API.Math.Solvers;


import API.Math.ADT.Vector3dInterface;
import API.Math.Functions.ODEFunctionInterface;
import API.System.RateInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import phase3.Simulation.State.RateOfChange;
import API.System.StateInterface;
import phase3.Simulation.State.SystemState;

/**
 * Verlet Velocity Solver (both positions and velocity, less round-off errors)
 * Second order Solver
 */
public record VerletVelocitySolver<E>(ODEFunctionInterface<E> f) implements ODESolverInterface<E> {

    /**
     * Instantiates a new Verlet velocity solver.
     *
     * @param f the f
     */
    @Contract(pure = true)
    public VerletVelocitySolver {
    }


    /**
     * Step of a Verlet Velocity Algorithm:
     * Consists of two equations:
     * x_n+1 = x_n + (v_n) *delta_t+1/2*(a_n)*(delta_t^2)
     * v_n+1 = v_n + 1/2*((a_n+1)+(a_n))*(delta_t)
     * <p>
     * next(0) = (y(0) + [y(1) * h])  + [1/2   * h^2 * (f.call(t,y))]
     * next(1) = (y(1))               + [1/2  * h^2 * (f.call(t+h,y) + f.call(t,y))/h]
     * <p>
     * x - position, v - velocity, delta_t - step size
     * Source: http://www.physics.udel.edu/~bnikolic/teaching/phys660/numerical_ode/node5.html
     *
     * @param f ordinary differential equations - function of acceleration
     * @param t current time
     * @param y current state;
     * @param h step size
     * @return new state
     */
    @Override
    @SuppressWarnings("{unchecked,unsafe}")
    public StateInterface<E> step(ODEFunctionInterface<E> f, double t, StateInterface<E> y, double h) {
        StateInterface<E> next = new RungeKuttaSolver<>(f).step(f, t, y, h);
        int s = y.get().length / 2; //take state symmetry
        RateInterface<E> a = f.call(t,y),a1 = f.call(t+h,next);
        RateInterface<E> nextDy;
        if (y.get() instanceof Vector3dInterface[]){
            StateInterface<Vector3dInterface> y2 = null;
            RateInterface<Vector3dInterface> dy2 = null;
            Vector3dInterface[] state = new Vector3dInterface[y.get().length],rate = new Vector3dInterface[y.get().length];
            for (int i = 0; i< s; i++){
                state[i] = ((Vector3dInterface) y.get()[i]).addMul(h, (Vector3dInterface) y.get()[i + s]);
                state[i+s] = (Vector3dInterface) y.get()[i+s];
                rate[i] = ((Vector3dInterface) a.get()[i + s]);
                rate[i+s] = ((Vector3dInterface) a1.get()[i + s]).add(((Vector3dInterface) a.get()[i + s])).div(h);
            }
            y2 = new SystemState<>(state);
            next = (SystemState<E>) y2;
            dy2 = new RateOfChange<>(rate);
            nextDy = (RateOfChange<E>) dy2;
        }
        else if (y.get() instanceof Double[]){
            StateInterface<Double> y2 = null;
            RateInterface<Double> dy2 = null;
            Double[] state = new Double[y.get().length],rate = new Double[y.get().length];
            for (int i = 0; i< s; i++){
                state[i] = (((Double) y.get()[0] + (h * (Double) y.get()[1])));
                state[i+s] = (Double) y.get()[i+s];
                rate[i] = ((Double) a.get()[i + s]);
                rate[i+s] =  ((Double) a1.get()[i + s] + (Double) a.get()[i + s]) / h;
            }
            y2 = new SystemState<>(state);
            next = (SystemState<E>) y2;
            dy2 = new RateOfChange<>(rate);
            nextDy = (RateOfChange<E>) dy2;
        } else throw new UnsupportedOperationException();
        return next.addMul((h*h)/2,nextDy);
    }

    @Contract(pure = true)
    @Override
    public ODEFunctionInterface<E> getFunction() {
        return f;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "VerletVelocitySolver";
    }
}