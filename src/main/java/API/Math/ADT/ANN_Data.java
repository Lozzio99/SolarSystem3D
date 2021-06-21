package API.Math.ADT;
import org.jetbrains.annotations.Contract;
public record ANN_Data(double[] input, double[] output) {

    @Contract(pure = true)
    public double[] getInput() {
        return input;
    }

    @Contract(pure = true)
    public double[] getOutput() {
        return output;
    }
}
