package API.Simulation;

import API.Math.ADT.Vector3dInterface;
import phase3.Forces.ModuleFunction;
import API.Math.Functions.ODEFunctionInterface;
import API.Math.Solvers.*;
import API.System.StateInterface;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static API.Config.*;

public interface RunnerInterface {

    double[] currentTime = new double[]{0};
    double[] step_size = new double[]{0};
    ScheduledExecutorService service = Executors.newScheduledThreadPool(10, Executors.privilegedThreadFactory());

    ModuleFunction getControls();

    ODESolverInterface<Vector3dInterface> getSolver();

    default ScheduledExecutorService getExecutor() {
        return service;
    }

    default ODESolverInterface<Vector3dInterface> initSolver(ODEFunctionInterface<Vector3dInterface> function) {
        switch (SOLVER) {
            case EULER -> {
                return new EulerSolver<>(function);
            }
            case RK4 -> {
                return new RungeKuttaSolver<>(function);
            }
            case VERLET_STD -> {
                return new StandardVerletSolver<>(function);
            }
            case VERLET_VEL -> {
                return new VerletVelocitySolver<>(function);
            }
            case MIDPOINT -> {
                return new MidPointSolver<>(function);
            }
            default -> throw new IllegalStateException();
        }
    }

    void init();

    SimulationInterface simInstance();

    default void runSimulation() {
        if (simInstance().isRunning()) {
            service.scheduleWithFixedDelay(this::loop, 1200, EXECUTION_SPEED_MS, TimeUnit.MILLISECONDS);
        }
    }

    default void loop() {
        StateInterface<Vector3dInterface> currentState = simInstance().getSystem().getState();
        simInstance().getGraphics().start(currentState);
        simInstance().getSystem().updateState(
                getSolver().step(getSolver().getFunction(), currentTime[0], currentState, step_size[0]));
        currentTime[0] += step_size[0];
    }
}
