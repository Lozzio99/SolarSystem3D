package phase3.Simulation.State;

import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.System.RateInterface;
import API.System.StateInterface;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public record SystemState<E>(E... y) implements StateInterface<E> {
    @Contract(pure = true)
    @SafeVarargs
    public SystemState {
    }

    /**
     * Note : we are not doing any safety check here, but it is assumed that the state object and the rate object
     * have the same dimensions.
     * Example,
     * State and rate for 2nd order differentiation
     * State = [ y   ,  dy/dx   ]
     * Rate = [dy/dx , d2y/dx2 ]
     * <p>
     * So that the involved operation will be to return a new state which is the result of
     * this.y + (rate.y * step) applied accordingly to the integration order
     */
    @Override
    public StateInterface<E> addMul(double step, RateInterface<E> rate) {
        StateInterface<E> y1;
        if (y instanceof Vector3dInterface[]) {
            StateInterface<Vector3dInterface> n = new SystemState<>(new Vector3D[y.length]);
            for (int i = 0; i < y.length; i++)
                n.get()[i] = ((Vector3dInterface) this.get()[i]).addMul(step, ((Vector3dInterface) rate.get()[i]));
            y1 = (SystemState<E>) n;
        } else if (y instanceof Double[]) {
            StateInterface<Double> n = new SystemState<>(new Double[y.length]);
            for (int i = 0; i < y.length; i++)
                n.get()[i] = ((Double) this.get()[i]) + (step * ((Double) rate.get()[i]));
            y1 = (SystemState<E>) n;
        } else
            throw new UnsupportedOperationException();
        return y1;
    }

    @Contract(pure = true)
    @Override
    public E[] get() {
        return y;
    }

    @Contract(" -> new")
    @Override
    public @NotNull StateInterface<E> copy() {
        return new SystemState<>(get().clone());
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "[ " + Arrays.toString(this.y) + " ]";
    }
}
