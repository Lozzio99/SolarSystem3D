package API.Simulation;

import API.Graphics.GraphicsInterface;
import API.System.SystemInterface;

public interface SimulationInterface {
    GraphicsInterface getGraphics();

    SystemInterface getSystem();

    RunnerInterface getRunner();

    void init();

    void start();

    boolean isRunning();


}
