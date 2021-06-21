package phase3.Simulation.Systems;

import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.System.CelestialBody;
import API.System.SystemInterface;
import phase3.Simulation.Bodies.Particle;
import API.System.StateInterface;
import phase3.Simulation.State.SystemState;

import java.util.ArrayList;
import java.util.List;

import static API.Config.N_PARTICLES;

public class LorenzSystem implements SystemInterface {

    private final List<CelestialBody> bodies = new ArrayList<>();
    private StateInterface<Vector3dInterface> state;

    @Override
    public StateInterface<Vector3dInterface> getState() {
        return this.state;
    }

    @Override
    public List<CelestialBody> getCelestialBodies() {
        return this.bodies;
    }

    @Override
    public void init() {
        this.bodies.clear();
        Vector3dInterface[] pos = new Vector3dInterface[N_PARTICLES];
        for (int i = 0; i < pos.length; i++) {
            this.bodies.add(new Particle(i + 1));
            this.bodies.get(i).initProperties();
            pos[i] = this.bodies.get(i).getVectorLocation();
        }
        this.state = new SystemState<>(pos);

    }

    @Override
    public void updateState(StateInterface<Vector3dInterface> step) {
        this.state = step;
    }
}
