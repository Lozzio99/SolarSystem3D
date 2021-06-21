/*
 * @author Pieter Collins, Christof Seiler, Katerina Stankova, Nico Roos, Katharina Schueller
 * @version 0.99.0
 *
 * This interface serves as the API for students in phase 1.
 */

package API.Math.Solvers;


import phase3.Forces.ModuleFunction;
import API.Math.Functions.ODEFunctionInterface;
import API.System.StateInterface;

/**
 * A class for solving a general differential equation dy/h = f(t,y)
 * y(t) describes the state of the system at time t
 * f(t,y(t)) defines the derivative of y(t) with respect to time t
 */
public interface ODESolverInterface<E> {

    /**
     * Solve state interface [ ].
     *
     * @param f  the f
     * @param y0 the y 0
     * @param ts the ts
     * @return the state interface [ ]
     */
    /*
     * Solve the differential equation by taking multiple steps.
     *
     * @param   f       the function defining the differential equation dy/h=f(t,y)
     * @param   y0      the starting state
     * @param   ts      the times at which the states should be output, with ts[0] being the initial time
     * @return  an array of size ts.length with all intermediate states along the path
     */
    default StateInterface<E>[] solve(ODEFunctionInterface<E> f, StateInterface<E> y0, double[] ts) {
        StateInterface<E>[] states = new StateInterface[ts.length];
        double endTime = ts[ts.length - 1];
        double currTime = ts[0];

        for (int i = 0; i < ts.length - 1; i++) {
            double h = ts[i + 1] - ts[i];
            states[i] = this.step(f, currTime, y0, h);
            y0 = states[i];
            currTime += h;
        }
        double h = ts[ts.length - 1] - ts[ts.length - 2];
        states[states.length - 1] = y0.addMul(currTime, f.call(h, y0));
        return states;
    }

    /**
     * Solve state interface [ ].
     *
     * @param f  the f
     * @param y0 the y 0
     * @param tf the tf
     * @param h  the h
     * @return the state interface [ ]
     */
    /*
     * Solve the differential equation by taking multiple steps of equal size, starting at time 0.
     * The final step may have a smaller size, if the step-size does not exactly divide the solution time range
     *
     * @param   f       the function defining the differential equation dy/h=f(t,y)
     * @param   y0      the starting state
     * @param   tf      the final time
     * @param   h       the size of step to be taken
     * @return  an array of size round(tf/h)+1 including all intermediate states along the path
     */
    default StateInterface<E>[] solve(ODEFunctionInterface<E> f, StateInterface<E> y0, double tf, double h) {
        StateInterface[] path = new StateInterface[(int) (Math.round(tf / h)) + 2];
        double currTime = 0;
        path[0] = y0;
        for (int i = 1; i < path.length - 1; i++) {
            path[i] = this.step(f, currTime, y0, h);
            y0 = path[i];
            currTime += h;
        }
        path[path.length - 1] = this.step(f, tf - currTime, y0, tf - currTime);
        return path;
    }

    /**
     * Step state interface.
     *
     * @param f the f
     * @param t the t
     * @param y the y
     * @param h the h
     * @return the state interface
     */
    /*
     * Update rule for one step.
     *
     * @param   f   the function defining the differential equation dy/h=f(t,y)
     * @param   t   the time
     * @param   y   the state
     * @param   h   the step size
     * @return  the new state after taking one step
     */
    StateInterface<E> step(ODEFunctionInterface<E> f, double t, StateInterface<E> y, double h);

    /**
     * For Higher CPU usages, should return MaxCPUSolver object
     * this is used to evaluate single bodies by individual threads,
     * therefore will be consequently implemented in different ways
     *
     * @return the function used for calculations
     * @see ModuleFunction
     */
    ODEFunctionInterface<E> getFunction();

    String toString();

}
