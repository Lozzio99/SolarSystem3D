package phase3.Rocket.Controllers;

import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.Rocket.ControllerInterface;
import API.System.StateInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a feedback controller for a module to land on a surface safely.
 */
public class ClosedLoopController implements ControllerInterface {

    private double u;
    private double v;

    public ClosedLoopController() {
        Vector3dInterface v1 = new Vector3D(0, 0, 0);
    }

    private void updateControls(double t, StateInterface<Vector3dInterface> y) {
        // Variables
        double xVel = y.get()[1].getX();
        double yPos = y.get()[0].getY();
        double yVel = y.get()[1].getY();
        double zPos = y.get()[0].getZ();
        double zVel = y.get()[1].getZ();

        // Modify for different behaviour:
        double turnSensitivity = 20;    // Higher value means faster turning
        double turnDampening = 100;      // Dampens the rotational acceleration to not overshoot the target angle
        double thrustFactor = 10;       // Factor for the amount of thrust applied
        double hoverThrust = 1.351;     // Required thrust to let the module hover
        double descentFactor = 5;      // Higher value means faster descent
        double posVelRatioFactor = 0.1; // Relation between vertical position and vertical velocity

        // --- Calculate rotational acceleration ---
        double rotationalAcceleration = getRotationalAcceleration(xVel, yPos, yVel, zPos, zVel, turnSensitivity, turnDampening);
        Vector3D rotVector = new Vector3D(0, 0, rotationalAcceleration);
        setV(rotationalAcceleration);

        // --- Calculate thrust ---
        double burnAmount = getBurnAmount(yPos, yVel, thrustFactor, hoverThrust, descentFactor, posVelRatioFactor);
        Vector3D thrustVector = burn(burnAmount, y);
        setU(burnAmount);
    }

    /**
     * Ensures that a rocket is only able to burn in the direction that the thruster is pointing at.
     * @param amount
     * @param state
     * @return
     */
    @Contract("_, _ -> new")
    private @NotNull Vector3D burn (double amount, @NotNull StateInterface<Vector3dInterface> state) {
        return  new Vector3D(amount * Math.sin(state.get()[0].getZ() * Math.PI / 180),
                amount * Math.cos(state.get()[0].getZ() * Math.PI / 180),
                0);
    }

    /**
     * This function computes the rotational acceleration of the module
     * Angles are calculated in degrees.
     * @param xVel              current x-velocity
     * @param yVel              current y-velocity
     * @param zPos              current z-position (radians)
     * @param zVel              current z-velocity (radians)
     * @param turnSensitivity   higher value means faster turning
     * @param turnDampening     dampens the rotational acceleration to not overshoot the target angle
     * @return
     */
    @Contract(pure = true)
    private double getRotationalAcceleration(double xVel, double yPos, double yVel, double zPos, double zVel, double turnSensitivity, double turnDampening) {

        double targetAngle;
        if (yPos < 10)
            targetAngle = -90;
        else
            targetAngle = Math.atan2(yVel, xVel) * 180 / Math.PI; // module velocity

        double currentAngle = -90 - zPos ; // module orientation
        double rotationalVelocity = -zVel;

        double angleDifference = currentAngle - targetAngle;

        double rotationalAcceleration = (angleDifference * turnSensitivity) + (rotationalVelocity * turnDampening);

        // Rotational thruster restriction
        double rotThrustLimit = 2.32;

        if (rotationalAcceleration > rotThrustLimit)
            rotationalAcceleration = rotThrustLimit;

        if (rotationalAcceleration < -rotThrustLimit)
            rotationalAcceleration = -rotThrustLimit;

        return rotationalAcceleration;
    }

    /**
     * Function to calculate the amount of thrust that is being applied.
     * @param yPos                  current y-position
     * @param yVel                  current y-velocity
     * @param thrustFactor          factor for the amount of thrust applied
     * @param hoverThrust           required thrust to let the module hover
     * @return
     */
    @Contract(pure = true)
    private double getBurnAmount(double yPos, double yVel, double thrustFactor, double hoverThrust, double descentFactor, double posVelRatioFactor) {

        double targetDecentRate = Math.sqrt(descentFactor * yPos) * posVelRatioFactor;
        double burnAmount = ((-yVel - targetDecentRate) * thrustFactor) + (hoverThrust / (yPos + 1));

        // Main thruster restriction
        double thrusterThreshold = 7.5;

        if (burnAmount > thrusterThreshold)
            burnAmount = thrusterThreshold;

        // Makes sure the thrust is not negative
        if (burnAmount < 0)
            burnAmount = 0;

        return burnAmount;
    }

    @Override
    public double[] controlFunction(double t, StateInterface<Vector3dInterface> y) {
        updateControls(t, y);
        return new double[]{getU(), getV()};
    }

    public double getU() {
        return u;
    }

    public double getV() {
        return v;
    }

    public void setU(double u) {
        this.u = u;
    }

    public void setV(double v) {
        this.v = v;
    }
}
