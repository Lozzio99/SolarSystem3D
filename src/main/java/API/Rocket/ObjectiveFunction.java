package API.Rocket;

import API.Math.ADT.Vector3dInterface;

/**
 * The interface Newt raph function.
 */
public interface ObjectiveFunction {

    Vector3dInterface modelFx(Vector3dInterface vector);

}
