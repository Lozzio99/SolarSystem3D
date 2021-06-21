package phase3.Simulation;

import API.Graphics.GraphicsInterface;
import API.Simulation.RunnerInterface;
import API.Simulation.SimulationInterface;
import API.System.SystemInterface;
import phase3.Graphics.GraphicsManager;
import phase3.Simulation.Runner.*;
import phase3.Simulation.Systems.*;

import static API.Config.*;

public class Simulation implements SimulationInterface {

    private final GraphicsInterface graphics;
    private final SystemInterface system;
    private final RunnerInterface runner;
    private boolean running = false;

    public Simulation() {
        this.graphics = new GraphicsManager(this);
        switch (SIMULATION) {
            case FLIGHT_TO_TITAN -> {
                this.system = new SolarSystem();
                this.runner = new SolarSystemRunner(this);
            }
            case LANDING_ON_TITAN -> {
                this.system = new TitanSystem();
                this.runner = new TitanSimRunner(this);
            }
            case LORENTZ_ATTRACTOR -> {
                this.system = new LorenzSystem();
                this.runner = new LorenzRunner(this);
            }
            case DOUBLE_PENDULUM -> {
                this.system = new PendulumSystem();
                this.runner = new PendulumRunner(this);
            }
            default -> throw new IllegalStateException();
        }

    }

    @Override
    public GraphicsInterface getGraphics() {
        return this.graphics;
    }

    @Override
    public SystemInterface getSystem() {
        return this.system;
    }

    @Override
    public RunnerInterface getRunner() {
        return this.runner;
    }

    @Override
    public void init() {
        this.system.init(); //init system and give the runner the initial state ready
        this.graphics.init();
        this.runner.init();
    }

    @Override
    public void start() {
        this.running = true;
        this.runner.runSimulation();
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
