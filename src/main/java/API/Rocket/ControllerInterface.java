package API.Rocket;

import API.Math.ADT.Vector3dInterface;
import API.System.StateInterface;

public interface ControllerInterface {

    /**
     * Main controller function; adjusts acceleration and torque
     *
     * @return new vector state
     */
    double[] controlFunction(double t, StateInterface<Vector3dInterface> y);

}
