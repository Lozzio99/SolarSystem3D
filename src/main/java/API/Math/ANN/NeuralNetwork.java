package API.Math.ANN;

import API.Config;
import API.Math.ADT.Matrix;

import java.awt.event.KeyAdapter;
import java.io.File;

public class NeuralNetwork extends KeyAdapter {
    protected Matrix weights_ih;
    protected Matrix weights_ho;
    protected Matrix weights_hh;
    protected Matrix bias_hh;
    protected Matrix bias_ih;
    protected Matrix bias_ho;

    private double lk = 0.1;

    public NeuralNetwork() {
        loadModel();
    }

    public NeuralNetwork(int in_nodes, int hid_nodes, int out_nodes, boolean random, boolean export) {
        this.weights_ih = new Matrix(hid_nodes, in_nodes);
        this.weights_hh = new Matrix(hid_nodes, hid_nodes);
        this.weights_ho = new Matrix(out_nodes, hid_nodes);
        this.bias_ih = new Matrix(hid_nodes, 1);
        this.bias_hh = new Matrix(hid_nodes, 1);
        this.bias_ho = new Matrix(out_nodes, 1);
        if (random) {
            this.weights_ih = Matrix.randomize(this.weights_ih);
            this.weights_hh = Matrix.randomize(this.weights_hh);
            this.weights_ho = Matrix.randomize(this.weights_ho);
            this.bias_ih = Matrix.randomize(this.bias_ih);
            this.bias_hh = Matrix.randomize(this.bias_hh);
            this.bias_ho = Matrix.randomize(this.bias_ho);
        }
        if (export) exportModel();
    }


    protected void exportModel() {
        Matrix[] modelMatrixObject = new Matrix[6];
        modelMatrixObject[0] = weights_ih;
        modelMatrixObject[1] = weights_ho;
        modelMatrixObject[2] = bias_ih;
        modelMatrixObject[3] = bias_ho;
        modelMatrixObject[4] = weights_hh;
        modelMatrixObject[5] = bias_hh;
        ModelHandler.export(String.format("%s/model.mlp", Config.MODEL_PATH), modelMatrixObject);
    }

    private void loadModel() {
        if (!new File(String.format("%s/model.mlp", Config.MODEL_PATH)).exists()) {
            return;
        }
        Matrix[] modelMatrixObject = ModelHandler.loadMatrix(String.format("%s/model.mlp", Config.MODEL_PATH));
        if (modelMatrixObject == null) throw new IllegalStateException("Model not loaded");
        this.weights_ih = modelMatrixObject[0];
        this.weights_ho = modelMatrixObject[1];
        this.bias_ih = modelMatrixObject[2];
        this.bias_ho = modelMatrixObject[3];
        this.weights_hh = modelMatrixObject[4];
        this.bias_hh = modelMatrixObject[5];
    }

    public void setLearningRate(double lk) {
        this.lk = lk;
    }

    public void printAllWeights() {
        this.weights_ho.printMatrix();
        this.weights_ih.printMatrix();
        this.bias_ih.printMatrix();
        this.bias_ho.printMatrix();
    }

    public Matrix[] feedForward(final Matrix inputs) {
        //generating hidden output
        var hidden1 = Matrix.multiply(this.weights_ih, inputs);
        hidden1 = Matrix.add(hidden1, this.bias_ih);
        hidden1 = Matrix.map(hidden1);

        //generating 2nd hidden output
        var hidden2 = Matrix.multiply(this.weights_hh, hidden1);
        hidden2 = Matrix.add(hidden2, this.bias_hh);
        hidden2 = Matrix.map(hidden2);

        //generating final output   NEW MATRIX RESULTING FROM THE EVOLVING OUTPUT
        var outputs = Matrix.multiply(this.weights_ho, hidden2);
        outputs = Matrix.add(outputs, this.bias_ho);
        outputs = Matrix.map(outputs);
        return new Matrix[]{hidden1, hidden2, outputs};
    }

    public Matrix guess(final double[] inputs) {
        return feedForward(new Matrix(inputs))[2];
    }

    public void train(Matrix given, Matrix targets) {
        Matrix[] layers_out = feedForward(given);
        var tgt = new Matrix(targets.getMatrix());
        //calculate out error
        var out = Matrix.subtract(tgt, layers_out[2]);
        Matrix[] grad0 = error(out, layers_out[2], layers_out[1]);
        this.weights_ho = Matrix.add(this.weights_ho, grad0[1]);
        this.bias_ho = Matrix.add(this.bias_ho, grad0[0]);
        //calculate hidden error
        tgt = Matrix.transpose(this.weights_ho);
        out = Matrix.multiply(tgt, out);
        grad0 = error(out, layers_out[1], layers_out[0]);
        this.weights_hh = Matrix.add(this.weights_hh, grad0[1]);
        this.bias_hh = Matrix.add(this.bias_hh, grad0[0]);
        //calculate hidden error2
        tgt = Matrix.transpose(this.weights_hh);
        out = Matrix.multiply(tgt, grad0[2]);
        grad0 = error(out, layers_out[0], given);
        this.weights_ih = Matrix.add(this.weights_ih, grad0[1]);
        this.bias_ih = Matrix.add(this.bias_ih, grad0[0]);
    }
    //id like to refactor it a bit

    public Matrix[] error(final Matrix hidden1_er, final Matrix hidden, final Matrix in) {
        //calculate hidden gradient
        var h1_gradient = Matrix.dfunc(hidden);
        h1_gradient = h1_gradient.multiply(hidden1_er);
        h1_gradient = h1_gradient.multiply(lk);
        //calculate and adjust hidden weights
        final var inputs_T = Matrix.transpose(in);
        final var d_ih = Matrix.multiply(h1_gradient, inputs_T);
        return new Matrix[]{h1_gradient, d_ih, hidden1_er};
    }

    @Override
    public String toString() {
        return "NeuralNetwork{" +
                "weights_ih=" + weights_ih +
                ", weights_ho=" + weights_ho +
                ", weights_hh=" + weights_hh +
                ", bias_hh=" + bias_hh +
                ", bias_ih=" + bias_ih +
                ", bias_ho=" + bias_ho +
                ", lk=" + lk +
                '}';
    }

    public Matrix[] getWeights() {
        return new Matrix[]{
                this.weights_ih,
                this.bias_ih,
                this.weights_hh,
                this.bias_hh,
                this.weights_ho,
                this.bias_ho
        };
    }
}
