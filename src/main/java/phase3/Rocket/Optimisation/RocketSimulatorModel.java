package phase3.Rocket.Optimisation;

import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.Math.Solvers.ODESolverInterface;
import API.Rocket.ObjectiveFunction;
import API.System.StateInterface;
import API.System.SystemInterface;
import org.jetbrains.annotations.Contract;

import static API.Config.MODULE_STEP_SIZE;
import static phase3.Main.simulation;

/**
 * The type Rocket sim model.
 */
public class RocketSimulatorModel {
    /**
     * The constant targetPosition.
     */
    public static Vector3dInterface targetPosition;
    /**
     * The constant pF.
     */

    public static ObjectiveFunction pFDelay = vector -> {
        Vector3D aprxPos = new Vector3D();//(Vector3D) RocketSimulatorModel.stateFx(NewtonRaphsonSolver.initPos, vector, NewtonRaphsonSolver.endTime, NewtonRaphsonSolver.waitTime);
        return targetPosition.sub(aprxPos);
    };
    /**
     * The Solver.
     */

    static ODESolverInterface<Vector3dInterface> solver;

    /**
     * The constant pF.
     */

    public static ObjectiveFunction pF = vector -> {
        Vector3dInterface aprxPos = RocketSimulatorModel.stateFx(NewtonRaphsonSolver.initPos, vector, NewtonRaphsonSolver.endTime);
        return targetPosition.sub(aprxPos);
    };
    /**
     * The Initial state.
     */
    static StateInterface<Vector3dInterface> initialState;


    /**
     * Create system system interface.
     *
     * @param initLocation the init location
     * @param initVelocity the init velocity
     * @return the system interface
     */
    @Contract(pure = true)
    public static SystemInterface createSystem(Vector3dInterface initLocation, Vector3dInterface initVelocity) {
        final SystemInterface[] system = new SystemInterface[1];


        return system[0];
    }

    /**
     * State fx vector 3 d interface.
     * Simulates position of the rocket with given parameters starting on 00:00:00 01/04/2020.
     *
     * @param initPos      the init pos
     * @param initVelocity the init velocity
     * @param timeFinal    the time final
     * @return the vector 3 d interface
     */
    public static Vector3dInterface stateFx(Vector3dInterface initPos, Vector3dInterface initVelocity, double timeFinal) {
        // init parameters
        SystemInterface system = createSystem(initPos, initVelocity);
        initialState = system.getState();
        solver = simulation.getRunner().getSolver();

        // solve trajectory
        StateInterface<Vector3dInterface>[] solution = solver.solve(solver.getFunction(), initialState, timeFinal, MODULE_STEP_SIZE);
        assert (simulation.getSystem().getCelestialBodies().size() > 10 &&
                !simulation.getSystem().getCelestialBodies().get(11).isCollided());
        return (Vector3dInterface) solution[solution.length - 1];
    }


}
