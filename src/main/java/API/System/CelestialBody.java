package API.System;

import API.Math.ADT.Vector3dInterface;
import org.jetbrains.annotations.Contract;

import java.awt.*;

/**
 * Represents a Celestial Body, with fields for all physics attributes
 * as well as GUI attributes
 */
public abstract class CelestialBody {
    private Color color;

    private double MASS;
    private double RADIUS;
    private double DENSITY;


    private Vector3dInterface vectorVelocity;
    private Vector3dInterface vectorLocation;

    private boolean collided;

    /**
     * Instantiates a new Celestial body.
     */
    @Contract(pure = true)
    public CelestialBody() {
        collided = false;
    }

    // --- Set-Methods ---

    /**
     * Sets the density.
     *
     * @param DENSITY the density
     */
    public void setDENSITY(double DENSITY) {
        this.DENSITY = DENSITY;
    }

    /**
     * Gets the mass.
     *
     * @return mass mass
     */
    public double getMASS() {
        return this.MASS;
    }

    /**
     * Sets the mass.
     *
     * @param mass the mass
     */
    public void setMASS(double mass) {
        this.MASS = mass;
    }

    /**
     * Gets the radius.
     *
     * @return radius radius
     */
    public double getRADIUS() {
        return this.RADIUS;
    }

    // --- Get-Methods ---

    /**
     * Sets the radius.
     *
     * @param RADIUS the radius
     */
    public void setRADIUS(double RADIUS) {
        this.RADIUS = RADIUS;
    }

    /**
     * Gets the density.
     *
     * @return the density
     */
    public double getDensity() {
        return this.DENSITY;
    }

    /**
     * Returns the colour of a body.
     *
     * @return colour colour
     */
    public Color getColour() {
        return color;
    }

    /**
     * Sets the colour of a body.
     *
     * @param col the col
     */
    public void setColour(Color col) {
        this.color = col;
    }

    /**
     * Converts the body to a string.
     */
    public abstract String toString();

    /**
     * Init properties.
     */
    public abstract void initProperties();

    /**
     * Is collided boolean.
     *
     * @return the boolean
     */
    public boolean isCollided() {
        return collided;
    }

    /**
     * Sets collided.
     *
     * @param bool the bool
     */
    public void setCollided(boolean bool) {
        this.collided = bool;
    }

    /**
     * Gets vector location.
     *
     * @return the vector location
     */
    public Vector3dInterface getVectorLocation() {
        return this.vectorLocation;
    }

    /**
     * Sets vector location.
     *
     * @param vector3D the vector 3 d
     */
    public void setVectorLocation(Vector3dInterface vector3D) {
        this.vectorLocation = vector3D;
    }

    /**
     * Gets vector velocity.
     *
     * @return the vector velocity
     */
    public Vector3dInterface getVectorVelocity() {
        return this.vectorVelocity;
    }

    /**
     * Sets vector velocity.
     *
     * @param vector3D the vector 3 d
     */
    public void setVectorVelocity(Vector3dInterface vector3D) {
        this.vectorVelocity = vector3D;
    }
}