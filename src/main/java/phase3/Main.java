package phase3;

import API.Config;
import phase3.Simulation.Simulation;
import API.Simulation.SimulationInterface;

public class Main {
    public static SimulationInterface simulation;

    public static void main(String[] args) {
        simulation = new Simulation();
        simulation.init();
        simulation.start();
    }
}
