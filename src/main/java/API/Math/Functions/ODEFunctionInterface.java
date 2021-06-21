/*
 * @author Pieter Collins, Christof Seiler, Katerina Stankova, Nico Roos, Katharina Schueller
 * @version 0.99.0
 *
 * This interface serves as the API for students in phase 1.
 */

package API.Math.Functions;

import API.System.RateInterface;
import API.System.StateInterface;

/**
 * An interface for the function f that represents the
 * differential equation dy/h = f(t,y)
 */
@FunctionalInterface
public interface ODEFunctionInterface<E> {

    /*
     * This is an interface for the function f that represents the
     * differential equation dy/h = f(t,y).
     * You need to implement this function to represent to the laws of physics.
     *
     * For example, consider the differential equation
     *   dy[0]/h = y[1];  dy[1]/h=cos(t)-sin(y[0])
     * Then this function would be
     *   f(t,y) = (y[1],cos(t)-sin(y[0])).
     *
     * @param   t   the time at which to evaluate the function
     * @param   y   the state at which to evaluate the function
     * @return  The average rate-of-change over the time-step. Has dimensions of [state]/[time].
     */

    /**
     * Call rate interface.
     *
     * @param t the t
     * @param y the y
     * @return the rate interface
     */
    RateInterface<E> call(double t, StateInterface<E> y);

}
