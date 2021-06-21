package phase3.Simulation.Systems;

import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.System.CelestialBody;
import API.System.StateInterface;
import API.System.SystemInterface;
import phase3.Simulation.State.SystemState;

import java.util.List;

import static java.lang.Math.PI;

public class PendulumSystem implements SystemInterface {
    private StateInterface<Vector3dInterface> state;

    @Override
    public StateInterface<Vector3dInterface> getState() {
        return state;
    }

    @Override
    public List<CelestialBody> getCelestialBodies() {
        return null;
    }

    @Override
    public void init() {
        Vector3dInterface[] state = new Vector3dInterface[4];
        state[0] = new Vector3D(20, 600, PI / 4); //mass,length,angle
        state[1] = new Vector3D(80, 400, PI / 3);
        state[2] = new Vector3D(0, 0, 0); //dMass, dLength, dAngle
        state[3] = new Vector3D(0, 0, 0);
        this.state = new SystemState<>(state);
    }

    @Override
    public void updateState(StateInterface<Vector3dInterface> step) {
        this.state = step;
    }
}
