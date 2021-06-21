package phase3.Forces;

import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.Math.Functions.ODEFunctionInterface;
import phase3.Simulation.State.RateOfChange;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public final class PendulumOscillation {

    private static final double g = -9.8;
    private static final double damp = 0.993;
    static double m1, m2, r1, r2; //constants
    private final ODEFunctionInterface<Vector3dInterface> oscillation = (t, y) -> {
        Vector3dInterface
                vel1 = y.get()[2],
                vel2 = y.get()[3],
                acc1 = new Vector3D(0, 0, 0),
                acc2 = new Vector3D(0, 0, 0);

        double a1 = y.get()[0].getZ(), a2 = y.get()[1].getZ();
        double a1_v = vel1.getZ(), a2_v = vel2.getZ();
        double num1 = -g * (2 * m1 + m2) * sin(a1);
        double num2 = -m2 * g * sin(a1-2*a2);
        double num3 = -2*sin(a1-a2)*m2;
        double num4 = a2_v*a2_v*r2+a1_v*a1_v*r1*cos(a1-a2);
        double den = r1 * (2*m1+m2-m2*cos(2*a1-2*a2));
        double a1_a = (num1 + num2 + num3*num4) / den;

        num1 = 2 * sin(a1-a2);
        num2 = (a1_v*a1_v*r1*(m1+m2));
        num3 = g * (m1 + m2) * cos(a1);
        num4 = a2_v*a2_v*r2*m2*cos(a1-a2);
        den = r2 * (2*m1+m2-m2*cos(2*a1-2*a2));
        double a2_a = (num1*(num2+num3+num4)) / den;


        acc1.setZ(a1_a * damp);
        acc2.setZ(a2_a * damp);
        return new RateOfChange<>(y.get()[2], y.get()[3], acc1, acc2);
    };

    public PendulumOscillation(double[] masses, double[] lengths) {
        m1 = masses[0];
        m2 = masses[1];
        r1 = lengths[0];
        r2 = lengths[1];
    }

    public ODEFunctionInterface<Vector3dInterface> getOscillation() {
        return oscillation;
    }
}
