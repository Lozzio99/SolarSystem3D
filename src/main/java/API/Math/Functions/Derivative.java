package API.Math.Functions;

@FunctionalInterface
public interface Derivative<E, T> {
    E derivative(T x, Function<E, T> f);

    enum Scheme {
        CENTRED,
        BACKWARD,
        FORWARD,
    }

}

