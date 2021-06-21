package phase3.Simulation.Bodies;

import API.Math.ADT.Vector3D;
import API.System.CelestialBody;

import java.awt.*;

/**
 * The type Particle.
 */
public class Particle extends CelestialBody {
    /**
     * The Id.
     */
    int id;

    private static final Color[] c = new Color[]{
            new Color(255, 55, 55),
            new Color(255, 157, 53),
            new Color(255, 194, 55),
            new Color(174, 255, 48),
            new Color(3, 255, 25),
            new Color(53, 255, 231),
            new Color(55, 114, 255),
            new Color(184, 63, 255),
            new Color(242, 15, 250),
            new Color(253, 253, 253)
    };

    /**
     * Instantiates a new Particle.
     *
     * @param id the id
     */
    public Particle(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PARTICLE_" + this.id;
    }

    @Override
    public void initProperties() {
        this.setRADIUS(0.5);
        /*
        double x = Math.max(7e8,new Random().nextInt());
        double y = Math.max(7e8,new Random().nextInt());
        double z = Math.max(7e8,new Random().nextInt());
        if (new Random().nextDouble() < 0.5) x *= -1;
        if (new Random().nextDouble() < 0.5) y *= -1;
        if (new Random().nextDouble() < 0.5) z *= -1;
        this.setVectorLocation(new Vector3D(x, y, z));
         */
        this.setVectorLocation(new Vector3D(id % 31, id % 31, id % 13));
        this.getVectorLocation().setX(
                this.getVectorLocation().getX() == this.getVectorLocation().getY() ?
                        this.getVectorLocation().getX() + 2 : this.getVectorLocation().getX());
        this.setVectorVelocity(new Vector3D(0,0,0));
        this.setColour(c[id % c.length]);
    }
}
