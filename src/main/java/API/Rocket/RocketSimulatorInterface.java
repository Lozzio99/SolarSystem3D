package API.Rocket;

import API.Math.ADT.Vector3dInterface;
import API.Rocket.ProbeSimulatorInterface;

public interface RocketSimulatorInterface extends ProbeSimulatorInterface {
    double evaluateLoss(Vector3dInterface desiredVelocity, Vector3dInterface actualVelocity);

    void updateMass(double propellantConsumed);

    double getFuelMass();
}
