package API.Math.Functions;

import API.Math.ADT.Vector3dInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.pow;

public class SecondDerivative<E> {

    public static final double h = 1e-4;

    public static final Derivative.Scheme scheme = Derivative.Scheme.CENTRED;

    @Contract(pure = true)
    public static @NotNull Derivative<Double, Double> fivePointCentred(Function<Double, Double> fx) {
        return (x, f) -> (35 * fx.apply(x) - 104 * fx.apply(x + h) + 114 * fx.apply(x + 2 * h) - 56 * fx.apply(x + 3 * h) + 11 * fx.apply(x + 4 * h)) / (12 * pow(h, 2));
    }

    @Contract(pure = true)
    public static @NotNull Derivative<Double, Double> FivePointForward(Function<Double, Double> fx) {
        return (x, f) -> (-fx.apply(x - 2 * h) + 16 * fx.apply(x - h) - 30 * fx.apply(x) + 16 * fx.apply(x + h) - fx.apply(x + 2 * h)) / (12 * pow(h, 2));
    }

    @Contract(pure = true)
    public static @NotNull Derivative<Vector3dInterface, Double> FivePointAsymm(Function<Vector3dInterface, Double> fx) {
        return (x, f) -> fx.apply(x - h).mul(11).sub(fx.apply(x).mul(20)).add(fx.apply(x + h).mul(6)).add(fx.apply(x + 2 * h).mul(4)).sub(fx.apply(x + 3 * h)).div((12 * pow(h, 2)));
    }

    @Contract(pure = true)
    public static @NotNull Derivative<Vector3dInterface, Double> _3PCentredStep(double h, Function<Vector3dInterface, Double> f) {
        return (x, f1) -> f.apply(x - h).sub(f.apply(x).mul(2)).add(f.apply(x + h)).div(h * h);
    }

    public static Vector3dInterface _3PForwardStep(double h, Vector3dInterface fx, Vector3dInterface fxh, Vector3dInterface fx2h) {
        return fx.sub(fxh.mul(2)).add(fx2h).div(pow(h, 2));
    }

    public static Vector3dInterface _3PBackwardStep(double h, Vector3dInterface f2hx, Vector3dInterface fhx, Vector3dInterface fx) {
        return f2hx.sub(fhx.mul(2)).add(fx).div(pow(h, 2));
    }

    public static Vector3dInterface _3PCentredStep(double h, Vector3dInterface fhx, Vector3dInterface fx, Vector3dInterface fxh) {
        return fhx.sub(fx.mul(2)).add(fxh).div(pow(h, 2));
    }
}
