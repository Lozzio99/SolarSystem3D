package API.Math.ADT;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static java.lang.StrictMath.*;


/**
 * The type Vector 3 d.
 */
public class Vector3D implements Vector3dInterface {
    /**
     * having this final will make collector garbage references faster
     */
    final private double[] val;

    public Vector3D() {
        this.val = new double[3];
        this.val[0] = 0;
        this.val[1] = 0;
        this.val[2] = 0; //the angle directly initialised
    }

    public Vector3D(double x, double y) {
        this.val = new double[3];
        this.val[0] = x;
        this.val[1] = y;
        this.val[2] = 0; //the angle directly initialised
    }

    /**
     * Instantiates a new Vector 3 d.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public Vector3D(double x, double y, double z) {
        this.val = new double[3];
        this.val[0] = x;
        this.val[1] = y;
        this.val[2] = z;
    }

    /**
     * Normalises the vector.
     *
     * @param v the v
     * @return vector 3 d interface
     */
    public static Vector3dInterface normalize(final Vector3dInterface v) {
        Vector3dInterface v2 = new Vector3D(0, 0, 0);
        double magnitude = sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ());
        v2.setX(v.getX() / magnitude);
        v2.setY(v.getY() / magnitude);
        v2.setZ(v.getZ() / magnitude);
        return v2;
    }

    @Override
    public Vector3dInterface sumOf(final Vector3dInterface... k) {
        Vector3dInterface res = new Vector3D(getX(), getY(), getZ());
        for (Vector3dInterface vector3dInterface : k) {
            res = res.add(vector3dInterface);
        }
        return res;
    }

    @Override
    public void set(final Vector3dInterface v) {
        setX(v.getX());
        setY(v.getY());
        setZ(v.getZ());
    }

    /**
     * Gets the distance between two vectors.
     *
     * @param first the first
     * @param other the other
     * @return double double
     */
    public static double dist(final Vector3dInterface first,final  Vector3dInterface other) {
        double v1 = pow(other.getX() - first.getX(), 2);
        double v2 = pow(other.getY() - first.getY(), 2);
        double v3 = pow(other.getZ() - first.getZ(), 2);
        return sqrt(v1 + v2 + v3);
    }

    /**
     * Gets the unit vector between two vectors.
     *
     * @param from the from
     * @param to   the to
     * @return vector 3 d interface
     */
    public static Vector3dInterface unitVectorDistance(final Vector3dInterface from,final Vector3dInterface to) {
        return normalize(new Vector3D(
                from.getX() - to.getX(),
                from.getY() - to.getY(),
                from.getZ() - to.getZ()));
    }

    /**
     * Returns the dot product of two vectors.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @return double double
     */
    public static double dot(final Vector3dInterface v1,final Vector3dInterface v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    /**
     * Returns the cross of two vectors.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @return vector 3 d interface
     */
    @Contract("_, _ -> new")
    public static @NotNull Vector3dInterface cross(final @NotNull Vector3dInterface v1,final @NotNull Vector3dInterface v2) {
        return new Vector3D(
                v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
                v1.getZ() * v2.getX() - v1.getX() * v2.getZ(),
                v1.getX() * v2.getY() - v1.getY() * v2.getX());
    }


    /**
     * Adds another vector onto this vector.
     *
     * @return a new Vector3d
     */
    @Override
    public Vector3dInterface add(final Vector3dInterface other) {
        return new Vector3D(
                getX() + other.getX(),
                getY() + other.getY(),
                getZ() + other.getZ());
    }

    /**
     * Subtracts this vector from another vector.
     *
     * @return a new Vector3d
     */
    @Override
    public Vector3dInterface sub(final Vector3dInterface other) {
        return new Vector3D(
                getX() - other.getX(),
                getY() - other.getY(),
                getZ() - other.getZ());
    }

    @Override
    public Vector3dInterface add(double other) {
        return new Vector3D(
                getX() + other,
                getY() + other,
                getZ() + other);
    }

    @Override
    public Vector3dInterface sub(double other) {
        return new Vector3D(
                getX() - other,
                getY() - other,
                getZ() - other);
    }

    /**
     * @return the result of |this-other|
     */
    public Vector3dInterface absSub(final Vector3dInterface other) {
        return new Vector3D(
                abs(getX() - other.getX()),
                abs(getY() - other.getY()),
                abs(getZ() - other.getZ()));
    }

    /**
     * Multiplies this vector by a scalar.
     *
     * @return a new Vector3d
     */
    @Override
    public Vector3dInterface mul(double scalar) {
        return new Vector3D(
                getX() * scalar,
                getY() * scalar,
                getZ() * scalar);
    }

    @Override
    public Vector3dInterface div(final Vector3dInterface other) {
        return new Vector3D(
                getX() / other.getX(),
                getY() / other.getY(),
                getZ() / other.getZ());
    }

    @Override
    public Vector3dInterface multiply(final Vector3dInterface other) {
        return new Vector3D(
                getX() * other.getX(),
                getY() * other.getY(),
                getZ() * other.getZ());
    }

    /**
     * Divides this vector by a scalar.
     *
     * @return a new Vector3d
     */
    @Override
    public Vector3dInterface div(double scalar) {
        return new Vector3D(
                getX() / scalar,
                getY() / scalar,
                getZ() / scalar);
    }

    /**
     * Adds another vector onto this one after it is multiplied by a scalar.
     *
     * @return a new Vector3d
     */
    @Override
    public Vector3dInterface addMul(double scalar,final  Vector3dInterface other) {
        Vector3dInterface newV = other.mul(scalar);
        return this.add(newV);
    }

    /**
     * Gets the norm of a vector.
     */
    @Override
    public double norm() {
        double sum = Arrays.stream(this.val).map(x -> pow(x, 2)).sum();
        return sqrt(sum);
    }

    @Override
    public double[] getVal() {
        return this.val;
    }

    /**
     * Gets the distance between this and another vector.
     */
    @Override
    public double dist(final Vector3dInterface other) {
        double sum = 0.0;
        int bound = val.length;
        for (int i = 0; i < bound; i++) {
            double pow = pow(other.getVal()[i] - val[i], 2);
            sum += pow;
        }
        return sqrt(sum);
    }

    /**
     * Converts a vector to a string.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("(");
        for (int i = 0; i < this.val.length; i++) {
            s.append(this.val[i]);
            if (i != this.val.length - 1)
                s.append(",");
        }
        return s + ")";
    }



    /**
     * Clones a Vector3dInterface.
     */
    @Override
    public Vector3dInterface clone() {
        return new Vector3D(getX(), getY(), getZ());
    }


    /**
     * Radian mode
     *
     * @return Angle of rotation for 2D vectors
     */
    @Override
    public double heading() {
        return Math.atan2(this.getY(), this.getX()) * (180 / PI);
    }

    @Override
    public double getX() {
        return this.val[0];
    }

    @Override
    public void setX(double x) {
        this.val[0] = x;
    }

    @Override
    public double getY() {
        return this.val[1];
    }

    @Override
    public void setY(double y) {
        this.val[1] = y;
    }

    @Override
    public double getZ() {
        return this.val[2];
    }

    @Override
    public void setZ(double z) {
        this.val[2] = z;
    }

    @Override
    public boolean isZero() {
        for (double x : this.val)
            if (x != 0)
                return false;
        return true;
    }

    @Override
    public boolean isLessThan(double delta) {
        for (double x : this.val)
            if (x > delta)
                return false;
        return true;
    }


    /**
     * Implementation of the equals and HashCode methods for the scheduling purpose.
     * Vector decisions are stored in an hashmap linked to a Clock as a key or a StateInterface,
     * we therefore want the key to be mapped and retrieved in an approximate situation, to avoid
     * such an uniqueness in the hashcode of the Vectors there we therefore
     * decided to hash the first 8 significant figures and to compare vectors by an accuracy of 10^-8
     */
    @Override
    public int hashCode() {
        int hash = 11;
        for (int i = 0; i < 3; i++) {
            hash = 31 * hash + Double.hashCode(this.val[i]);
        }
        return hash;
    }

    /**
     * Actual accuracy is 10^-8 here too
     *
     * @param o the object to be compared to
     * @return true if the vector is equal up to a certain accuracy
     */

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Vector3dInterface v) {
            return v.getX() == this.val[0] &&
                    v.getY() == this.val[1] &&
                    v.getZ() == this.val[2];
        }
        return false;
    }

}
