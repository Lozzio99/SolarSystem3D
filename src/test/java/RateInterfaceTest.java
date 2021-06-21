import org.junit.jupiter.api.Test;
import API.System.RateInterface;
import phase3.Simulation.State.RateOfChange;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateInterfaceTest {

    @Test
    void get() {
        RateInterface<Double> rate = new RateOfChange<Double>(2.0,3.0);
        assertEquals(rate.get()[0],2.0);
        assertEquals(rate.get()[1],3.0);
    }
}