package phase3.Simulation.Bodies;

import API.Math.ADT.Vector3D;
import API.System.CelestialBody;

import java.awt.*;

/**
 * The type Satellite.
 */
public class Satellite extends CelestialBody {

    SatellitesEnum name;

    /**
     * Instantiates a new Satellite.
     * Instantiates a new Planet.
     * Repository Class for satellites used in the Simulation
     * @param name the name
     */
    public Satellite(SatellitesEnum name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.name();
    }

    @Override
    public void initProperties() {
        switch (name) {
            case MOON -> {
                this.setMASS(7.349e22);
                this.setRADIUS(1.7371e6);
                this.setColour(Color.gray);
                this.setVectorLocation(new Vector3D(-1.472343904597218e+11, -2.822578361503422e+10, 1.052790970065631e+07));
                this.setVectorVelocity(new Vector3D(4433.121575882713, -29484.53616031408, 88.96594867343843));
            }
            case TITAN -> {
                this.setMASS(1.34553e23);
                this.setRADIUS(2575.5e3);
                this.setColour(Color.gray);
                this.setVectorLocation(new Vector3D(6.332873118527889e+11, -1.357175556995868e+12, -2.134637041453660e+09));
                this.setVectorVelocity(new Vector3D(3056.87794615761, 6125.612917224867, -952.3587319894634));
            }
        }
    }

    /**
     * The enum Satellites enum.
     */
    public enum SatellitesEnum {
        /**
         * Moon satellites enum.
         */
        MOON,
        /**
         * Titan satellites enum.
         */
        TITAN,
        /**
         * Asteroid satellites enum.
         */
        ASTEROID
    }
}
