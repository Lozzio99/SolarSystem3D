/*
 * @author Pieter Collins, Christof Seiler, Katerina Stankova, Nico Roos, Katharina Schueller
 * @version 0.99.0
 *
 * This interface serves as the API for students in phase 1.
 */

package API.Rocket;

import API.Math.ADT.Vector3dInterface;

/**
 * An interface for simulating a probe launched from Earth
 */
public interface ProbeSimulatorInterface {
    /**
     * Trajectory vector 3 d interface [ ].
     *
     * @param p0 the p 0
     * @param v0 the v 0
     * @param ts the ts
     * @return the vector 3 d interface [ ]
     */
    /*
     * Simulate the solar system, including a probe fired from Earth at 00:00h on 1 April 2020.
     *
     * @param   p0      the starting position of the probe, relative to the earth's position.
     * @param   v0      the starting velocity of the probe, relative to the earth's velocity.
     * @param   ts      the times at which the states should be output, with ts[0] being the initial time.
     * @return  an array of size ts.length giving the position of the probe at each time stated,
     *          taken relative to the Solar System barycentre.
     */
    Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double[] ts);

    /*
     * Simulate the solar system with steps of an equal size.
     * The final step may have a smaller size, if the step-size does not exactly divide the solution time range.
     *
     * @param   tf      the final time of the evolution.
     * @param   h       the size of step to be taken
     * @return  an array of size round(tf/h)+1 giving the position of the probe at each time stated,
     *          taken relative to the Solar System barycentre
     */

    /**
     * Trajectory vector 3d interface [ ].
     *
     * @param p0 the p 0
     * @param v0 the v 0
     * @param tf the tf
     * @param h  the h
     * @return the vector 3 d interface [ ]
     */
    Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double tf, double h);
}
