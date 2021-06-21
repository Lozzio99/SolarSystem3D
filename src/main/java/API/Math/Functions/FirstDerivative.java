package API.Math.Functions;

import API.Math.ADT.Vector3dInterface;

public class FirstDerivative {
    public static Vector3dInterface _5PForwardStep
            (double h, Vector3dInterface fx, Vector3dInterface fxh, Vector3dInterface fx2h, Vector3dInterface fx3h, Vector3dInterface fx4h) {
        return fx.mul(-25).add(fxh.mul(48)).sub(fx2h.mul(36)).add(fx3h.mul(16)).sub(fx4h.mul(12)).div(12 * h);
    }

    public static Vector3dInterface _5PBackwardStep
            (double h, Vector3dInterface f4hx, Vector3dInterface f3hx, Vector3dInterface f2hx, Vector3dInterface fhx, Vector3dInterface fx) {
        return f4hx.mul(-25).sub(f3hx.mul(48)).add(f2hx.mul(36)).sub(fhx.mul(16)).sub(fx.mul(12)).div(12 * h);
    }

    public static Vector3dInterface _5PCentredStep
            (double h, Vector3dInterface f2hx, Vector3dInterface fhx, Vector3dInterface fxh, Vector3dInterface fx2h) {
        return f2hx.sub(fhx.mul(8)).add(fxh.mul(8)).sub(fx2h).div(12 * h);
    }

}
