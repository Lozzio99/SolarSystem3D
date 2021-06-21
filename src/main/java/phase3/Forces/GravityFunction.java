package phase3.Forces;

import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.Math.Functions.ODEFunctionInterface;
import phase3.Simulation.State.RateOfChange;
import API.System.StateInterface;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

public final class GravityFunction {

    private static final double G = 6.67408e-11;
    private static double[] BODIES_MASS;
    /**
     * Function that returns the rate of change of a state at a point in time
     */
    private final ODEFunctionInterface<Vector3dInterface> gravityAcceleration = (t, y) -> {
        int s = y.get().length / 2;
        final Vector3dInterface[] rate = new Vector3dInterface[y.get().length];
        for (int i = 0; i < s; i++) {
            rate[i] = y.get()[i + s];
            rate[i + s] = velocityFromAcceleration(i, y);
        }
        return new RateOfChange<>(rate);
    };

    public GravityFunction(double[] m) {
        BODIES_MASS = m;
    }

    public final double[] getMASSES() {
        return BODIES_MASS;
    }

    private synchronized Vector3dInterface velocityFromAcceleration(int i, final StateInterface<Vector3dInterface> y) {
        Vector3dInterface totalAcc = new Vector3D(0, 0, 0);
        for (int k = 0; k < y.get().length / 2; k++) {
            if (i != k) {
                Vector3dInterface acc = y.get()[i].clone(); //position a
                double squareDist = pow(y.get()[i].dist(y.get()[k]), 2);
                acc = y.get()[k].sub(acc); // Get the force position vector
                double den = sqrt(squareDist); //square distance
                if (den != 0) {
                    acc = acc.div(den); // Normalise to length 1
                    acc = acc.mul((G * BODIES_MASS[k]) / squareDist); // Convert force to acceleration
                } else {
                    System.exit(1);
                }
                totalAcc = totalAcc.add(acc); //sum to all forces acting on the body
            }
        }
        return totalAcc;
    }


    /**
     * Gets evaluate rate of change.
     *
     * @return the evaluate rate of change lambda function
     */
    public final ODEFunctionInterface<Vector3dInterface> evaluateAcceleration() {
        return this.gravityAcceleration;
    }

}