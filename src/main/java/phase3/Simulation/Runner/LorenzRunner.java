package phase3.Simulation.Runner;

import API.Math.ADT.Vector3dInterface;
import phase3.Forces.LorenzGravityODE1;
import phase3.Forces.ModuleFunction;
import API.Math.Functions.ODEFunctionInterface;
import API.Math.Solvers.ODESolverInterface;
import API.Simulation.RunnerInterface;
import API.Simulation.SimulationInterface;

import static API.Config.LORENZ_STEP_SIZE;

public class LorenzRunner implements RunnerInterface {

    private final SimulationInterface simulation;
    private final ODEFunctionInterface<Vector3dInterface> function;
    private ODESolverInterface<Vector3dInterface> solver;

    public LorenzRunner(SimulationInterface simulation) {
        this.simulation = simulation;
        this.function = new LorenzGravityODE1().getVelocityFunction();
        step_size[0] = LORENZ_STEP_SIZE;
    }

    @Override
    public ModuleFunction getControls() {
        return null;
    }

    @Override
    public ODESolverInterface<Vector3dInterface> getSolver() {
        return this.solver;
    }

    @Override
    public void init() {
        this.solver = initSolver(this.function);
    }

    @Override
    public SimulationInterface simInstance() {
        return simulation;
    }
}
