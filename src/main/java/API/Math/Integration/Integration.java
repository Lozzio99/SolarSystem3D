package API.Math.Integration;

import API.Math.Functions.Function;

@FunctionalInterface
public interface Integration {

    double integrate(Function<Double, Double> fX, double h, double a, double b);

}
