package API.Math.ANN;

import API.Math.ADT.Matrix;
import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import phase3.Forces.ModuleFunction;
import API.Math.Solvers.ODESolverInterface;
import API.Simulation.RunnerInterface;
import phase3.Simulation.Simulation;
import API.System.StateInterface;
import phase3.Simulation.State.SystemState;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static API.Config.*;
import static phase3.Main.simulation;


public class TrainingArea {

    private static final long TIMEOUT_MINUTES = 5;
    private static final int POPULATION_SIZE = 100;
    private static final ModuleANN[] population = new ModuleANN[POPULATION_SIZE];
    private static final double END_TIME = 5000;
    private static double time;
    private static Matrix[] genePrevGeneration;
    private static ModuleFunction moduleFunction;
    private static double BEST_FITNESS = 0;


    /*
    public static void main(String[] args) throws InterruptedException {
        SOLVER = RK4;
        simulation = new Simulation() {
            @Override
            public void init() {
                this.getRunner().init();
                moduleFunction = this.getRunner().getControls();
            }
        };
        simulation.init();
        populateRandom();
        //populateFromModel();
        initRandomState();
        runSimulation();
    }
     */


    private static void runSimulation() throws InterruptedException {
        RunnerInterface runner = simulation.getRunner();
        final int[] i = new int[]{0};
        ScheduledFuture<?> f = runner.getExecutor().scheduleWithFixedDelay(
                () -> {
                    simulationLoop();
                    //next generation
                    System.out.println("Generation " + (i[0]++));
                }, 15, 15, MILLISECONDS);
        try {
            f.get(TIMEOUT_MINUTES, SECONDS);
        } catch (ExecutionException | TimeoutException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static void populateRandom() {
        int nInput = 8, nHidden = 16, nOut = 2;
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = new ModuleANN(nInput, nHidden, nOut, true, false);
        }
        if (genePrevGeneration == null) genePrevGeneration = population[0].getWeights();
    }

    private static void initRandomState() {
        StateInterface<Vector3dInterface> random = randomState();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i].setState(new SystemState<>(random.get()[0].clone(), random.get()[1].clone()));
        }
    }

    private static StateInterface<Vector3dInterface> randomState() {
        return new SystemState<>(
                new Vector3D(
                        new Random().nextInt(200),
                        new Random().nextInt(200),
                        new Random().nextDouble()),
                new Vector3D(
                        new Random().nextDouble(),
                        new Random().nextDouble(),
                        new Random().nextDouble()
                ));
    }

    private static void populateFromModel() {
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = new ModuleANN();
            population[i].setFitness(0);
        }
    }

    private static void simulationLoop() {
        ODESolverInterface<Vector3dInterface> solver = simulation.getRunner().getSolver();
        time = 0;
        Matrix[] smartBrainGen = null;
        while ((time + MODULE_STEP_SIZE) <= END_TIME) {
            for (ModuleANN rocket : population) {
                final Matrix output = rocket.guess(inputFromState(rocket.getState()));
                moduleFunction.setControls(output.matrix[0][0], output.matrix[1][0]);
                rocket.setState(solver.step(
                        solver.getFunction(),
                        time,
                        rocket.getState(),
                        MODULE_STEP_SIZE));
                smartBrainGen = checkIndividual(rocket); //will return null if fitness is lower or same
            }
            time += MODULE_STEP_SIZE;
        }
        nextGeneration(smartBrainGen);  //will reproduce the last gen
        Runtime.getRuntime().gc();
    }

    private static double[] inputFromState(final StateInterface<Vector3dInterface> moduleState) {
        return new double[8];
        //maybe here distance from (0,0,0),(0,0,0)
        //btw this needs to match with the number of input in the ANN
        //@see populateRandom()
    }

    private static Matrix[] checkIndividual(final ModuleANN individual) {
        if (individual.isOnTarget()) return individual.getWeights();
        if (individual.getFitness() > BEST_FITNESS) {
            BEST_FITNESS = individual.getFitness();
            genePrevGeneration = individual.getWeights();
        }
        return genePrevGeneration;
    }

    private static void nextGeneration(final Matrix[] smartBrain) {
        if (smartBrain != null)   //this got on target so might do something special
            createFromSmart(smartBrain);
        else      //still something
            mutatePopulation(genePrevGeneration);
    }

    private static void createFromSmart(final Matrix[] smartBrain) {
        for (ModuleANN brain : population) {
            brain.setWeights(smartBrain);
        }
        mutatePopulation(smartBrain);
    }


    private static void mutatePopulation(final Matrix[] previousGenome) {
        for (ModuleANN brain : TrainingArea.population) {
            brain.setWeights(previousGenome);
        }
        /*  Might do something more here */
    }


}
