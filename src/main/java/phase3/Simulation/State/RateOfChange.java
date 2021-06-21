package phase3.Simulation.State;

import API.System.RateInterface;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;

public class RateOfChange<E> implements RateInterface<E> {
    final E[] dy;

    @Contract(pure = true)
    @SafeVarargs
    public RateOfChange(E... y) {
        this.dy = y;
    }

    @Override
    public E[] get() {
        return this.dy;
    }

    @Override
    public String toString() {
        return Arrays.toString(dy);
    }
}
