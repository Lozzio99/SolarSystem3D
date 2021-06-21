package API.System;


/**
 * An interface representing the state of a system described by a differential equation.
 */
public interface StateInterface<E> {
    /**
     * Update a state to a new state computed by: this + step * rate
     *
     * @param step The time-step of the update
     * @param rate The average rate-of-change over the time-step. Has dimensions of [state]/[time].
     * @return The new state after the update. Required to have the same class as 'this'.
     */
    StateInterface<E> addMul(double step, RateInterface<E> rate);
    String toString();

    E[] get();

    StateInterface<E> copy();
}
