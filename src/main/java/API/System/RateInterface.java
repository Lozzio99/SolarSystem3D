package API.System;

/**
 * An interface representing the time-derivative (rate-of-change) of the state of a system.
 */
public interface RateInterface<E> {
    E[] get();
}
