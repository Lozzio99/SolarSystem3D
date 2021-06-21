package API.System;

import API.Math.ADT.Vector3dInterface;

import java.util.List;

import static API.Config.SIMULATION_CLOCK;

public interface SystemInterface {
    StateInterface<Vector3dInterface> getState();

    List<CelestialBody> getCelestialBodies();

    void init();

    void updateState(StateInterface<Vector3dInterface> step);

    default Clock getClock() {
        return SIMULATION_CLOCK;
    }
}
