package API.Math.Functions;

/**
 * An interface for the function f that represents the equation y = f(x).
 *
 * @param <E> the type parameter returned.
 * @param <T> the type parameter evaluated.
 */
@FunctionalInterface
public interface Function<E, T> {
    /**
     * Apply f on T value.
     *
     * @param value the value of the T type
     * @return the E type value of the function
     */
    E apply(T value);
}

