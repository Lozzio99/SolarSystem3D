package API.Rocket;

import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.System.Clock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static API.Config.SIMULATION_CLOCK;

/**
 * Schedules Shifts/Thrusts of Rocket based on Newton-Raphson
 */
public class RocketSchedule {

    /**
     * The Shift at time.
     */
    Map<Clock, Vector3dInterface> shiftAtTime;


    /**
     * Init.
     */
    public void init() {
        this.shiftAtTime = new ConcurrentHashMap<>();
        this.preparePlan();
    }

    /**
     * Preparing the scheduling in advance.
     * Link to desired keys the corresponding vector decisions,
     * which will be then applied, whenever one of these keys will
     * be "present" in the system.
     * Example: by linking a Clock with a specified date and time to
     * a certain decision,it is then possible to retrieve this when the
     * clock instance in the system will return true from the implemented
     * equals(Object o) method.
     */
    public void preparePlan() {
        this.addToPlan(new Clock().setInitialDay(1, 4, 2020).setInitialTime(0, 0, 0), new Vector3D(5053.8397726433905, -42680.243107188355, -2383.2465825823965));
        this.addToPlan(new Clock().setInitialDayAndTime(0, 0, 6, 16, 11, 2023), new Vector3D(-8466.431731294904, 3689.7962745433374, 363.6276188737569));
    }


    public Vector3dInterface shift() {
        return this.getDesiredVelocity(SIMULATION_CLOCK);
    }

    /**
     * Gets desired velocity.
     *
     * @param clock the clock
     * @return the desired velocity
     */
    public Vector3dInterface getDesiredVelocity(Clock clock) {
        if (clock == null) return new Vector3D();
        Vector3dInterface v = shiftAtTime.getOrDefault(clock, new Vector3D());
        return v == null ? new Vector3D() : v;
    }


    /**
     * Add to plan.
     *
     * @param clock    the clock
     * @param decision the decision
     */
    public void addToPlan(Clock clock, Vector3dInterface decision) {
        Vector3dInterface v = this.shiftAtTime.put(clock, decision);
        if (v != null) this.shiftAtTime.put(clock, v.add(decision));
    }

}
