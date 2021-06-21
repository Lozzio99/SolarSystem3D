package API.Rocket;

import API.Math.ADT.Vector3dInterface;
import API.Rocket.ObjectiveFunction;

public interface OptimisationInterface {

    /**
     * Minimisation function
     *
     * @param initVector initial vector to be optimised
     * @param fX         function, which value is to be minimised (ideal fX(V)~0)
     * @return optimal vector
     */
    Vector3dInterface minimiseVector(Vector3dInterface initVector, ObjectiveFunction fX);

}
