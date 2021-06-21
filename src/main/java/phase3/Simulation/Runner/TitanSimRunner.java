package phase3.Simulation.Runner;

import API.Simulation.RunnerInterface;
import org.jetbrains.annotations.Contract;
import API.Math.ADT.Vector3dInterface;
import phase3.Forces.ModuleFunction;
import phase3.Forces.WindInterface;
import phase3.Forces.WindModel;
import API.Math.Functions.ODEFunctionInterface;
import API.Math.Solvers.ODESolverInterface;
import phase3.Rocket.Controllers.DecisionMaker;
import API.Simulation.SimulationInterface;
import API.System.StateInterface;

import static API.Config.*;


public class TitanSimRunner implements RunnerInterface {
    private final SimulationInterface simulation;
    private ODESolverInterface<Vector3dInterface> solver;
    private ODEFunctionInterface<Vector3dInterface> gravityFunction;
    private ModuleFunction moduleFunction;
    private ODEFunctionInterface<Vector3dInterface> windFunction;
    private DecisionMaker moduleController;

    @Contract(pure = true)
    public TitanSimRunner(SimulationInterface simulation) {
        this.simulation = simulation;
    }

    @Override
    public ModuleFunction getControls() {
        return this.moduleFunction;
    }

    @Override
    public ODESolverInterface<Vector3dInterface> getSolver() {
        return this.solver;
    }


    @Override
    public void init() {
        WindInterface wind = new WindModel();
        wind.init();
        this.windFunction = wind.getWindFunction();
        this.moduleController = new DecisionMaker(CONTROLLER);
        this.moduleFunction = new ModuleFunction();
        this.gravityFunction = this.moduleFunction.evaluateAcceleration();
        this.solver = initSolver(this.gravityFunction);
    }

    @Override
    public SimulationInterface simInstance() {
        return simulation;
    }

    @Override
    public synchronized void loop() {
        // draw graphics
        // update module gravityFunction
        StateInterface<Vector3dInterface> currentState = simulation.getSystem().getState();
        if (currentState.get()[0].getY() < 0.0001) {
            System.out.println("\nTime: " + CURRENT_TIME);
            System.out.println("Position: " + currentState.get()[0]);
            System.out.println("Velocity: " + currentState.get()[1]);
            System.exit(0);
        }

        simulation.getGraphics().start(currentState);
        moduleFunction.setControls(moduleController.getControls(CURRENT_TIME, currentState));

        StateInterface<Vector3dInterface> afterGravity = solver.step(gravityFunction, CURRENT_TIME, currentState, MODULE_STEP_SIZE);

        if (WIND) {
            StateInterface<Vector3dInterface> afterWind = solver.step(windFunction, CURRENT_TIME, afterGravity, MODULE_STEP_SIZE);
            simulation.getSystem().updateState(afterWind);
        } else {
            simulation.getSystem().updateState(afterGravity);
        }

        CURRENT_TIME += MODULE_STEP_SIZE;

        /*

        New state 1 = solver.step(state 0);
        New state 2 = moduleControllerOpen.makeDecision(New State 2);

        New state 3 = wind.modify(New State 1);
        New state 3 = moduleControllerClosed.optimizeDecision(New State 2);

        this state = New State 3;
        */


        // decision-thrusts    ] -> LOOP
        // evaluate next state ]
        // perturbation        ]
        //     *fix decision   ]
        //
        // update state
    }

}
