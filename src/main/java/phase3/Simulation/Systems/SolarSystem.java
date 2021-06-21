package phase3.Simulation.Systems;

import API.Math.ADT.Vector3dInterface;
import API.System.SystemInterface;
import phase3.Rocket.RocketSimulator;
import API.Errors.ErrorData;
import API.Errors.ErrorReport;
import API.System.CelestialBody;
import phase3.Simulation.Bodies.Planet;
import phase3.Simulation.Bodies.Satellite;
import phase3.Simulation.Bodies.Star;
import API.System.StateInterface;
import phase3.Simulation.State.SystemState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static API.Config.ERROR_EVALUATION;
import static API.Config.ERROR_MONTH_INDEX;
import static phase3.Simulation.Bodies.Planet.PlanetsEnum.*;
import static phase3.Simulation.Bodies.Satellite.SatellitesEnum.MOON;
import static phase3.Simulation.Bodies.Satellite.SatellitesEnum.TITAN;

public class SolarSystem implements SystemInterface {
    private volatile StateInterface<Vector3dInterface> state;
    private List<CelestialBody> bodies;

    @Override
    public StateInterface<Vector3dInterface> getState() {
        return state;
    }

    @Override
    public List<CelestialBody> getCelestialBodies() {
        return bodies;
    }

    @Override
    public void init() {
        this.bodies = new ArrayList<>();
        this.bodies.add(new Star());
        this.bodies.add(new Planet(MERCURY));
        this.bodies.add(new Planet(VENUS));
        this.bodies.add(new Planet(EARTH));
        this.bodies.add(new Satellite(MOON));
        this.bodies.add(new Planet(MARS));
        this.bodies.add(new Planet(JUPITER));
        this.bodies.add(new Planet(SATURN));
        this.bodies.add(new Satellite(TITAN));
        this.bodies.add(new Planet(URANUS));
        this.bodies.add(new Planet(NEPTUNE));
        this.bodies.add(new RocketSimulator());

        this.getClock().setLaunchDay();
        Vector3dInterface[] state = new Vector3dInterface[this.bodies.size() * 2];
        int bound = this.bodies.size();
        for (int i = 0; i < bound; i++) {
            this.bodies.get(i).initProperties();
            state[i] = this.bodies.get(i).getVectorLocation();
            state[i + this.bodies.size()] = this.bodies.get(i).getVectorVelocity();
        }

        this.state = new SystemState<>(state);
        if (ERROR_EVALUATION)
            new ErrorReport(new ErrorData(this.state), ERROR_MONTH_INDEX).start();
    }

    @Override
    public synchronized void updateState(StateInterface<Vector3dInterface> step) {
        this.state = step;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("SolarSystem{" +
                "positions =\n");
        IntStream.range(0, this.bodies.size()).forEachOrdered(i -> {
            s.append(this.bodies.get(i)).append(" : ");
            s.append(this.state.get()[i]).append("\n");
        });
        s.append("velocities =\n");
        IntStream.range(0, this.bodies.size()).forEachOrdered(i -> {
            s.append(this.bodies.get(i)).append(" : ");
            s.append(this.state.get()[i + this.bodies.size()]).append("\n");
        });
        return s.toString();
    }
}
