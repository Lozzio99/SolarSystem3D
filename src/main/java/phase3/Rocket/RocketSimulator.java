package phase3.Rocket;

import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.Rocket.RocketSimulatorInterface;
import API.System.CelestialBody;
import API.System.StateInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.awt.Color.GREEN;
import static API.Config.SS_STEP_SIZE;
import static phase3.Main.simulation;

public class RocketSimulator extends CelestialBody implements RocketSimulatorInterface {
    // Variables
    double startFuel = 2e4;

    double fuelMass;

    double totalMass;

    double exhaustVelocity = 2e4;

    double maxThrust = 3e7;

    /**
     * Evaluate loss double.
     * <p>
     * source : https://www.narom.no/undervisningsressurser/sarepta/rocket-theory/rocket-engines/the-rocket-equation/
     * <p>
     * M(t)=M_(start )−m ̇  ;m ̇=  Δm/Δt
     * M(t)∗a(t)= m ̇∗V_e=F_max;
     * a(t)=  F_max/(Mstart−m ̇∗t);
     * ∫a(t)dt=v(t); V_e∗ln(Mstart/M(t) ;
     * Δm=M_start∗(1 − e^(−Δv/V_e ));
     * Δm - Propellant Consumed for One Step
     *
     * @param desiredVelocity the desired velocity
     * @param actualVelocity  the actual velocity
     * @return Δm, propellant consumed
     */
    @Override
    public double evaluateLoss(Vector3dInterface desiredVelocity, Vector3dInterface actualVelocity) {
        double deltaV = desiredVelocity.sub(actualVelocity).norm();

        double propellantConsumed = (fuelMass * deltaV) / (this.exhaustVelocity + deltaV);

        if (exhaustVelocity * (propellantConsumed / SS_STEP_SIZE) > maxThrust) {
            System.out.println("Max Thrust exceeded!!!");
        }
        updateMass(propellantConsumed);
        return propellantConsumed;
    }

    /**
     * Update mass.
     *
     * @param propellantConsumed the propellant consumed
     */
    @Override
    public void updateMass(double propellantConsumed) {
        if (this.getFuelMass() - propellantConsumed >= 0) {
            this.setMASS(this.totalMass - propellantConsumed);
            this.fuelMass = (this.fuelMass - propellantConsumed);
        } else {
            System.out.println("Out of fuel!");
        }
    }

    @Override
    public double getFuelMass() {
        return this.fuelMass;
    }


    @Override
    public String toString() {
        return "ROCKET";
    }

    @Override
    public void initProperties() {
        this.setMASS(7.8e4);
        this.setRADIUS(1e2);
        this.setColour(GREEN);
        this.setVectorLocation(new Vector3D(-1.4717856245318698E11, -2.861154627637646E10, 8032437.618829092)); //earth
        this.setVectorVelocity(new Vector3D(0, 0, 0));
        this.fuelMass = this.startFuel;
        this.totalMass = this.startFuel + this.getMASS();
        this.setMASS(this.totalMass);
    }

    /**
     * Info string.
     *
     * @return the string
     */
    public String info() {
        return "Dry Mass: " + this.getMASS() + "\n" +
                "Fuel Mass: " + this.fuelMass + "\n";
    }

    @Override
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double[] ts) {
        assert (simulation.getSystem() != null);
        StateInterface<Vector3dInterface>[] seq = simulation.getRunner().getSolver().solve(
                simulation.getRunner().getSolver().getFunction(),
                simulation.getSystem().getState(),
                ts);

        return getSequence(seq);
    }

    @Contract(pure = true)
    private Vector3dInterface @NotNull [] getSequence(StateInterface<Vector3dInterface> @NotNull [] seq) {
        Vector3dInterface[] trajectory = new Vector3dInterface[seq.length];
        for (int i = 0; i < trajectory.length; i++) {
            trajectory[i] = (Vector3dInterface) seq[i];
        }
        return trajectory;
    }

    @Override
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double tf, double h) {
        assert (simulation.getSystem() != null);
        StateInterface<Vector3dInterface>[] seq = simulation.getRunner().getSolver().solve(
                simulation.getRunner().getSolver().getFunction(),
                simulation.getSystem().getState(),
                tf, h);
        return getSequence(seq);
    }

}
