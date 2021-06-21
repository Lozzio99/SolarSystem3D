package API.Math.ANN;

import API.Math.ADT.Matrix;
import API.Math.ADT.Vector3dInterface;
import API.System.StateInterface;

public class ModuleANN extends NeuralNetwork {
    private double fitness;
    private boolean onTarget;
    private StateInterface<Vector3dInterface> state;


    public ModuleANN() {
        super();
    }

    public ModuleANN(int in, int hid, int out, boolean random, boolean save) {
        super(in, hid, out, random, save);
        fitness = 0;
        onTarget = false;
    }

    public StateInterface<Vector3dInterface> getState() {
        return state;
    }

    public void setState(StateInterface<Vector3dInterface> state) {
        this.state = state;
    }

    public boolean isOnTarget() {
        return onTarget = (this.state.get()[0].isZero() && this.state.get()[1].isLessThan(0.1));
    }

    public void setOnTarget(boolean onTarget) {
        this.onTarget = onTarget;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setWeights(Matrix[] weights) {
        this.weights_ih = weights[0];
        this.bias_ih = weights[1];
        this.weights_hh = weights[2];
        this.bias_hh = weights[3];
        this.weights_ho = weights[4];
        this.bias_ho = weights[5];
    }
}
