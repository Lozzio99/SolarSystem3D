package API;

import API.Errors.ErrorPrint;
import API.Math.Noise.Noise;
import API.Errors.ErrorData;
import API.System.Clock;

public class Config {
    /*
     * AVAILABLE SIMULATION TYPES
     */
    public static final int FLIGHT_TO_TITAN = 0;
    public static final int LANDING_ON_TITAN = 1;
    public static final int LORENTZ_ATTRACTOR = 2;
    public static final int DOUBLE_PENDULUM = 3;
    public static final double LORENZ_STEP_SIZE = 1e-2;
    public static int SIMULATION = FLIGHT_TO_TITAN;


    /*
     * TIME SETTINGS
     */
    public static final double MODULE_STEP_SIZE = 0.01;
    public static final double SS_STEP_SIZE = Clock.HOUR;
    public static final double PENDULUM_STEP_SIZE = 0.5;
    public static double CURRENT_TIME = 0;
    public static int EXECUTION_SPEED_MS = 3;
    public static final Clock SIMULATION_CLOCK = new Clock();


    /*
     * SOLVERS SETTINGS
     */
    public static final int EULER = 0;
    public static final int RK4 = 1;
    public static final int VERLET_STD = 2;
    public static final int VERLET_VEL = 3;
    public static final int MIDPOINT = 4;
    public static int SOLVER = RK4;




    /*
     * MODULE CONTROLLERS SETTINGS
     */
    public static final int OPEN = 0;
    public static final int CLOSED = 2;
    public static int CONTROLLER = 0;








    /*
     * SIMULATION SETTINGS
     */
    public static boolean WIND = false;
    public static int GIF_INDEX = 0,END_GIF = 300;
    public static boolean MAKE_GIF = false;
    public static final int N_PARTICLES = 500;
    public static final String MODEL_PATH = "";
    public static Noise.NoiseDim NOISE_DIMENSIONS = Noise.NoiseDim.TRI_DIMENSIONAL;
    public static final boolean DEBUG = false;
    public static final boolean NAMES = true;
    public static final boolean DRAW_TRAJECTORIES = true;
    public static int TRAJECTORY_LENGTH = EXECUTION_SPEED_MS * 100;



    /*
     * ERRORS SETTINGS
     */
    public static final ErrorPrint OUT_ERROR = ErrorPrint.CONSOLE;
    public static final ErrorData[] ORIGINAL_DATA = new ErrorData[13];
    public static boolean ERROR_EVALUATION = false;
    public static int ERROR_MONTH_INDEX = -1; // clock will check first and increment it to 0 -> first month evaluation



}
