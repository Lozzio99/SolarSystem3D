package phase3.Rocket.Controllers;

import API.Math.ADT.Vector3dInterface;
import API.Rocket.ControllerInterface;
import API.System.StateInterface;
import org.jetbrains.annotations.Contract;

import static java.lang.Math.*;
import static API.Config.MODULE_STEP_SIZE;
import static phase3.Forces.ModuleFunction.G;
import static phase3.Forces.ModuleFunction.V_MAX;

public class OpenLoopManualNewController implements ControllerInterface {
    private boolean ROTATION;
    private boolean HORIZONTAL;
    private boolean VERTICAL;
    private boolean initRotationFlag;
    private boolean initHorizontalMoveFlag;
    private boolean init = true;

    StateInterface<Vector3dInterface> initState;

    private double x_0;
    private double y_0;
    private double theta_0;

    private double x_0_dot;
    private double y_0_dot;
    private double theta_0_dot;

    private double u = 0;
    private double v = 0;

    private double rotation = 1.0;
    private double hMvTime;
    private double hCoMvTime;
    private double rtTime;
    private double tempT;

    @Contract(pure = true)
    public OpenLoopManualNewController() {

    }

    @Override
    public double[] controlFunction(double t, StateInterface<Vector3dInterface> y) {
        if (init) initBounds(y);
        updateControls(t);
        return new double[]{getU(), getV()};
    }

    private void initBounds(StateInterface<Vector3dInterface> y) {
        setStateValues(y);
        init = false;
        ROTATION = true;
        HORIZONTAL = true;
        VERTICAL = false;
        initRotationFlag = true;
        initHorizontalMoveFlag = true;
        initState = y.copy();

        if (x_0 < 0.0) rotation = -1.0;

        hMvTime = Double.MAX_VALUE;
        hCoMvTime = Double.MAX_VALUE;
        rtTime = Double.MAX_VALUE;
    }

    /**
     * Main method that updates control values
     *
     * @param t current time
     */
    private void updateControls(double t) {
        // adjust horizontal position/velocity
        if (HORIZONTAL) {
            if (startHorizontalMove(t)) {
                HORIZONTAL = false;
                VERTICAL = true;
                initHorizontalMoveFlag = true;
            }
        }
        // adjust vertical position/velocity
        else if (VERTICAL) startVerticalMove();

        if (y_0 < 1e-5) DefaultPhase();

        // update local module simulationType
        updateState();
    }

    private boolean startHorizontalMove(double t) {
        HORIZONTAL = true;
        if (ROTATION) startRotation(PI / 4.0, t);
        else {
            if (initHorizontalMoveFlag) initHorizontalMove(t);
            else if (t > hMvTime) correctHorizontalMove(t);
            else if (t > hCoMvTime) {
                return startRotation(PI / 4.0, t);
            }
        }
        return false;
    }

    private void correctHorizontalMove(double t) {
        u = abs(x_0_dot/(sin(theta_0)));
        hMvTime = Double.MAX_VALUE;
        hCoMvTime = t + 1.0;
    }

    private void initHorizontalMove(double t) {
        setControls(abs(pow(x_0_dot, 2) / (2 * x_0 * sin(theta_0))), 0.0);
        hMvTime = t + abs((2 * x_0) / x_0_dot) - MODULE_STEP_SIZE;
        initHorizontalMoveFlag = false;
    }

    private boolean startRotation(double degrees, double t) {
        if (initRotationFlag) initRotation(degrees, t);
        if (t < tempT + rtTime) {
            //setControls(abs(G / cos(theta_0)), V_MAX * rotation);
            setControls(0.0, V_MAX * rotation);
        } else if (t > tempT + rtTime && t < tempT + rtTime * 2.0 + MODULE_STEP_SIZE) {
            //setControls(abs(G / cos(theta_0)), -V_MAX * rotation);
            setControls(0.0, -V_MAX * rotation);
        } else {
            ROTATION = false;
            initRotationFlag = true;
            rotation *= -1.0;
            setControls(0.0, 0.0);
            return true;
        }
        return false;
    }

    private void initRotation(double degrees, double t) {
        rtTime = sqrt((abs(degrees % (2 * PI))) / V_MAX);
        rotation = -1.0*(abs(x_0_dot)/x_0_dot);
        tempT = t;
        initRotationFlag = false;
    }

    private void startVerticalMove() {
        VERTICAL = false;
        u = abs(pow(y_0_dot, 2) / (2 * y_0)) + G;
    }

    private void DefaultPhase() {
        setControls(abs(G / cos(theta_0)), 0.0);
    }

    @Contract(pure = true)
    private double getU() {
        return u;
    }

    @Contract(pure = true)
    private double getV() {
        return v;
    }

    @Contract(mutates = "this")
    private void setControls(double u, double v) {
        this.u = u;
        this.v = v;
    }

    private void setStateValues(StateInterface<Vector3dInterface> state) {
        x_0 = state.get()[0].getX();
        y_0 = state.get()[0].getY();
        theta_0 = state.get()[0].getZ();

        x_0_dot = state.get()[1].getX();
        y_0_dot = state.get()[1].getY();
        theta_0_dot = state.get()[1].getZ();
    }

    private void updateState() {
        x_0 += x_0_dot * MODULE_STEP_SIZE + 0.5 * u * sin(theta_0) * MODULE_STEP_SIZE * MODULE_STEP_SIZE;
        x_0_dot += u * sin(theta_0) * MODULE_STEP_SIZE;

        y_0 += y_0_dot * MODULE_STEP_SIZE + 0.5 * (u * cos(theta_0) - G) * MODULE_STEP_SIZE * MODULE_STEP_SIZE;
        y_0_dot += (u * cos(theta_0) - G) * MODULE_STEP_SIZE;

        theta_0 += theta_0_dot * MODULE_STEP_SIZE + 0.5 * v * MODULE_STEP_SIZE * MODULE_STEP_SIZE;
        theta_0_dot += v * MODULE_STEP_SIZE;
    }

}
