package phase3.Simulation.Runner;

import API.Math.ADT.Vector3dInterface;
import phase3.Forces.ModuleFunction;
import phase3.Forces.PendulumOscillation;
import API.Math.Functions.ODEFunctionInterface;
import API.Math.Solvers.ODESolverInterface;
import API.Simulation.RunnerInterface;
import API.Simulation.SimulationInterface;

import static API.Config.PENDULUM_STEP_SIZE;

public class PendulumRunner implements RunnerInterface {
    private final SimulationInterface simulation;

    private ODESolverInterface<Vector3dInterface> solver;

    public PendulumRunner(SimulationInterface s) {
        this.simulation = s;
        step_size[0] = PENDULUM_STEP_SIZE;
    }

    @Override
    public ModuleFunction getControls() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ODESolverInterface<Vector3dInterface> getSolver() {
        return this.solver;
    }

    @Override
    public void init() {
        ODEFunctionInterface<Vector3dInterface> oscillation = new PendulumOscillation(
                new double[]{
                        simulation.getSystem().getState().get()[0].getX(),
                        simulation.getSystem().getState().get()[1].getX()
                },
                new double[]{
                        simulation.getSystem().getState().get()[0].getY(),
                        simulation.getSystem().getState().get()[1].getY()
                }
        ).getOscillation();
        this.solver = initSolver(oscillation);
    }

    @Override
    public SimulationInterface simInstance() {
        return this.simulation;
    }
}
