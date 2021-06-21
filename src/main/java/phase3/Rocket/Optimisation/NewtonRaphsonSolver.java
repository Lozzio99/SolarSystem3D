package phase3.Rocket.Optimisation;

import API.Math.ADT.Matrix;
import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.Math.Functions.PartialDerivative;
import API.Rocket.ObjectiveFunction;
import org.jetbrains.annotations.Contract;

import static phase3.Rocket.Optimisation.RocketSimulatorModel.*;

/**
 * The Newton raphson solver.
 */
public class NewtonRaphsonSolver {


    public static double ERROR = 100.0;

    /**
     * Launching position for the Rocket from Earth (00:00:00 01/04/2020)
     */
    public static Vector3dInterface ROCKET_STARTING_POS = new Vector3D(-1.4717856245318698E11, -2.861154627637646E10, 8032437.618829092);

    /**
     * Intermediate point near Titan to move to Earth (06:00:00 16/11/2023)
     */
    public static Vector3dInterface INTERMEDIATE_START = new Vector3D(1.3585811106122769E12, -6.058172170455222E11, -4.8541143460416466E10);

    /**
     * Target point near the Earth (00:00:00 01/09/2026)
     */
    public static Vector3dInterface EARTH_TARGET = new Vector3D(1.403938718949137E11, -5.650895144135213E10, 4.5059332345759265E7);

    /**
     * Target point near Titan (12:00:00 09/10/2023)
     */
    public static Vector3dInterface TITAN_TARGET = new Vector3D(1.3210553440774814E12, -6.208533970972291E11, -4.14051317342377E10);

    /**
     * The constant initPos.
     */
    public static Vector3dInterface initPos;

    /**
     * The constant endTime.
     */
    public static double endTime;

    /**
     * The constant waitTime
     */
    public static double waitTime;

    /**
     * The Log iteration.
     */
    public final boolean LOG_ITERATION = true;
    private int i = 0;
    private final ObjectiveFunction fX;
    /**
     * The Init sol.
     */
    public Vector3dInterface initSol;
    /**
     * The Target sol.
     */
    public Vector3dInterface targetSol;

    /**
     * Constructor of a basic Newton-Raphson solver to approximate solution of a function
     *
     * @param fX 3-d vector function
     */
    @Contract(pure = true)
    public NewtonRaphsonSolver(ObjectiveFunction fX) {
        this.fX = fX;
    }

    /**
     * Constructor of Newton-Raphson solver for the rocket-simulationType problem
     *
     * @param initPos   initial position of a rocket
     * @param targetPos target position of a rocket
     * @param endTime   fixed time at which rocket must reach its target
     */
    public NewtonRaphsonSolver(Vector3dInterface initPos, Vector3dInterface targetPos, double endTime) {
        this.fX = pF;
        NewtonRaphsonSolver.initPos = initPos;
        targetPosition = targetPos.clone();
        NewtonRaphsonSolver.endTime = endTime;
    }

    /**
     * Constructor of Newton-Raphson solver for the rocket-simulationType problem with the delay
     * Rocket is simulated after the wait time
     *
     * @param initPos   initial position of a rocket
     * @param targetPos target position of a rocket
     * @param endTime   fixed time at which rocket must reach its target
     * @param waitTime  delay for rocket simulationType
     */
    public NewtonRaphsonSolver(Vector3dInterface initPos, Vector3dInterface targetPos, double endTime, double waitTime) {
        this.fX = pFDelay;
        NewtonRaphsonSolver.initPos = initPos;
        targetPosition = targetPos.clone();
        NewtonRaphsonSolver.endTime = endTime;
        NewtonRaphsonSolver.waitTime = waitTime;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        //### Approximation of rocket mission

        double FixedTime1 = 111153600;
        double FixedTime2 = 3261600;
        double FixedTime3 = 88106400;
        double FixedTime4 = 202521600;

        //### Path from Earth to Titan
        NewtonRaphsonSolver nrSolver = new NewtonRaphsonSolver(ROCKET_STARTING_POS, TITAN_TARGET, FixedTime1);
        Vector3dInterface aprxVel = nrSolver.NewtRhapSolution(new Vector3D(5053.8397726433905, -42680.243107188355, -2383.2465825823965), new Vector3D(0.0, 0.0, 0.0), 360);

        //### Path from Titan to Earth
        NewtonRaphsonSolver nrSolver2 = new NewtonRaphsonSolver(INTERMEDIATE_START, EARTH_TARGET, FixedTime4, FixedTime1 + FixedTime2);
        Vector3dInterface aprxVel2 = nrSolver2.NewtRhapSolution(new Vector3D(-8413.49799899527, 3774.5102109077566, 347.1890518838117), new Vector3D(0.0, 0.0, 0.0), 360.0);

    }

    /**
     * Method finds point on surface of Earth for launching position to the target point
     *
     * @param vector3dInterface target point
     * @return result
     */
    public static Vector3dInterface findPointOnEarth(Vector3dInterface vector3dInterface) {
        //https://math.stackexchange.com/questions/1784106/how-do-i-compute-the-closest-points-on-a-sphere-given-a-point-outside-the-sphere
        Vector3D v1 = new Vector3D(-1.471858828790318E11, -2.8610694436326927E10, 8164251.808805363);
        Vector3D v2 = (Vector3D) vector3dInterface.clone();
        double disV1V2 = v1.dist(v2);
        Vector3D unitV = new Vector3D(
                (v2.getX() - v1.getX()) / disV1V2,
                (v2.getY() - v1.getY()) / disV1V2,
                (v2.getZ() - v1.getZ()) / disV1V2
        );
        Vector3D startV1 = new Vector3D(
                v1.getX() + (6.371e6 + 1e6) * unitV.getX(),
                v1.getY() + (6.371e6 + 1e6) * unitV.getY(),
                v1.getZ() + (6.371e6 + 1e6) * unitV.getZ()
        );
        return startV1;
    }

    /**
     * Approximation of a solution to a function using Newton-Rhapson method
     *
     * @param initSol   initial solution
     * @param targetSol target solution of the model function
     * @param h         step-size
     * @return approximated solution i.e. fX(aprxSol) ~ targetSol
     */
    public Vector3dInterface NewtRhapSolution(Vector3dInterface initSol, Vector3dInterface targetSol, double h) {
        this.initSol = initSol.clone();
        this.targetSol = targetSol.clone();
        Vector3dInterface aprxSol = initSol.clone();
        // iteratively apply newton-raphson
        while (ERROR > 0.000001) {
            aprxSol = NewtRhapStep(aprxSol, h);
            if (LOG_ITERATION) {
                System.out.println("Result - Iteration #" + (i) + ": " + aprxSol.toString());
            }
            i++;
        }
        i = 0;
        return aprxSol;
    }

    /**
     * Newton-raphson step to approximate the velocity
     *
     * @param vector interim velocity value
     * @param h      step-size
     * @return approximated velocity
     */
    public Vector3dInterface NewtRhapStep(Vector3dInterface vector, double h) {
        double[][] D = PartialDerivative.getJacobianMatrix(fX, (Vector3D) vector.clone(), h); // Compute matrix of partial derivatives D
        Matrix DI = new Matrix(Matrix.inverse(D)); // Compute DI inverse of D
        Vector3dInterface modelVector = fX.modelFx(vector);
        if (LOG_ITERATION) {
            System.out.println("Error - Iteration #" + i + ": " + modelVector);
        }
        ERROR = modelVector.norm();
        Vector3dInterface DIProd = DI.multiplyVector(modelVector);
        return vector.sub(DIProd); // V1 = velocity - DI * function(velocity)
    }

}
