package phase3.Simulation.Bodies;


import API.Math.ADT.Vector3D;
import API.System.CelestialBody;

import java.awt.*;

/**
 * The type Planet.
 */
public class Planet extends CelestialBody {
    /**
     * The Planet.
     */
    PlanetsEnum planet;

    /**
     * Instantiates a new Planet.
     * Repository Class for planets used in the Simulation
     * Data from Project Manual
     *
     * @param planet the planet
     */
    public Planet(PlanetsEnum planet) {
        this.planet = planet;
    }

    @Override
    public String toString() {
        return planet.name();
    }

    @Override
    public void initProperties() {
        switch (planet) {
            case MERCURY -> {
                this.setMASS(3.302e+23);
                this.setRADIUS(2.4397e6);
                this.setColour(new Color(92, 87, 87, 233));
                this.setVectorLocation(new Vector3D(6.047855986424127e+06, -6.801800047868888e+10, -5.702742359714534e+09));
                this.setVectorVelocity(new Vector3D(38925.85164132107, 2978.342227951605, -3327.96413011577));
            }
            case VENUS -> {
                this.setMASS(4.8685e24);
                this.setRADIUS(6.0518e6);
                this.setColour(new Color(172, 75, 1, 232));
                this.setVectorLocation(new Vector3D(-9.435345478592035e+10, 5.350359551033670e+10, 6.131453014410347e+09));
                this.setVectorVelocity(new Vector3D(-17264.04276675418, -30734.32498568155, 574.1783348533565));
            }
            case EARTH -> {
                this.setMASS(5.97219e24);
                this.setRADIUS(6.371e6);
                this.setColour(new Color(16, 54, 167, 232));
                this.setVectorLocation(new Vector3D(-1.471922101663588e+11, -2.860995816266412e+10, 8.278183193596080e+06));
                this.setVectorVelocity(new Vector3D(5427.193371063864, -29310.56603506259, 0.6575428116074852));
            }
            case MARS -> {
                this.setMASS(6.4171e23);
                this.setRADIUS(3.3895e6);
                this.setColour(new Color(173, 77, 39, 237));
                this.setVectorLocation(new Vector3D(-3.615638921529161e+10, -2.167633037046744e+11, -3.687670305939779e+09));
                this.setVectorVelocity(new Vector3D(24815.51959239763, -1816.367993839315, -646.7321577627249));
            }
            case JUPITER -> {
                this.setMASS(1.89813e27);
                this.setRADIUS(6.9911e7);
                this.setColour(new Color(234, 161, 71, 228));
                this.setVectorLocation(new Vector3D(1.781303138592156E11, -7.551118436250294E11, -8.532838524470329E8));
                this.setVectorVelocity(new Vector3D(12558.52546626294, 3622.680182938116, -295.8620385189845));
            }
            case SATURN -> {
                this.setMASS(5.6834e26);
                this.setRADIUS(5.8232e7);
                this.setColour(new Color(165, 134, 107, 208));
                this.setVectorLocation(new Vector3D(6.328646641500651e+11, -1.358172804527507e+12, -1.578520137930810e+09));
                this.setVectorVelocity(new Vector3D(8220.8421339415, 4052.137353045929, -397.6224693819078));
            }
            case URANUS -> {
                this.setMASS(8.6813e25);
                this.setRADIUS(2.5362e7);
                this.setColour(new Color(201, 238, 238, 226));
                this.setVectorLocation(new Vector3D(2.395195786685187e+12, 1.744450959214586e+12, -2.455116324031639e+10));
                this.setVectorVelocity(new Vector3D(-4059.468609332644, 5187.467321685034, 71.82516190869795));
            }
            case NEPTUNE -> {
                this.setMASS(1.02413e26);
                this.setRADIUS(2.4622e7);
                this.setColour(new Color(0, 71, 179, 240));
                this.setVectorLocation(new Vector3D(4.382692942729203e+12, -9.093501655486243e+11, -8.227728929479486e+10));
                this.setVectorVelocity(new Vector3D(1051.98951225787, 5358.319815712676, -134.4948707739122));
            }
        }
    }


    /**
     * The enum Planets enum.
     */
    public enum PlanetsEnum {
        /**
         * Mercury planets enum.
         */
        MERCURY,
        /**
         * Venus planets enum.
         */
        VENUS,
        /**
         * Earth planets enum.
         */
        EARTH,
        /**
         * Mars planets enum.
         */
        MARS,
        /**
         * Jupiter planets enum.
         */
        JUPITER,
        /**
         * Saturn planets enum.
         */
        SATURN,
        /**
         * Uranus planets enum.
         */
        URANUS,
        /**
         * Neptune planets enum.
         */
        NEPTUNE
    }

}