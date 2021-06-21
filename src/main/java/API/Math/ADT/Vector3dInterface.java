/*
 * @author Pieter Collins, Christof Seiler, Katerina Stankova, Nico Roos, Katharina Schueller
 * @version 0.99.0
 *
 * This interface serves as the API for students in phase 1.
 */

package API.Math.ADT;


/**
 * The interface Vector 3 d interface.
 */
public interface Vector3dInterface {


    /**
     * Gets x.
     *
     * @return the x value
     */
    double getX();

    /**
     * set the x value
     *
     * @param x the x
     */
    void setX(double x);

    /**
     * Gets y.
     *
     * @return the y value
     */
    double getY();

    /**
     * set the y value
     *
     * @param y the y
     */
    void setY(double y);

    /**
     * Gets z.
     *
     * @return the z value
     */
    double getZ();

    /**
     * set the z value
     *
     * @param z the z
     */
    void setZ(double z);

    /**
     * Sum the other vector and the current one
     *
     * @param other the vector to be added
     * @return a new Vector3D
     */
    Vector3dInterface add(Vector3dInterface other);

    /**
     * Subtract the other vector from the current one
     *
     * @param other the vector to be subtracted
     * @return a new Vector3D
     */
    Vector3dInterface sub(Vector3dInterface other);

    Vector3dInterface add(double other);

    Vector3dInterface sub(double other);

    /**
     * Abs sub vector 3 d interface.
     *
     * @param other the other
     * @return the vector 3 d interface
     */
    Vector3dInterface absSub(Vector3dInterface other);


    /**
     * Multiply the current vector by a scalar
     *
     * @param scalar the scalar to be multiplied
     * @return a new Vector3D
     */
    Vector3dInterface mul(double scalar);

    /**
     * Divide the current vector by a scalar
     *
     * @param scalar the scalar to be divided
     * @return a new Vector3D
     */
    Vector3dInterface div(double scalar);


    /**  Scalar x vector multiplication, followed by an addition
     *   @param scalar the double used in the multiplication step
     *   @param other  the vector used in the multiplication step
     *   @return the result of the multiplication step added to this vector,
     *   for example:
     *   Vector3d a = Vector();
     *   double h = 2;
     *   Vector3d b = Vector();
     *   ahb = a.addMul(h, b);
     *   * ahb should now contain the result of this mathematical operation:
     *   a+h*b
     */
    Vector3dInterface addMul(double scalar, Vector3dInterface other);

    /**
     * Norm double.
     *
     * @return the Euclidean norm of a vector
     */
    double norm();

    double[] getVal();

    /**
     * Dist double.
     *
     * @param other the other
     * @return the Euclidean distance between two vectors
     */
    double dist(Vector3dInterface other);

    /**
     * @return A string in this format:
     * Vector3d(-1.0, 2, -3.0) should print out (-1.0,2.0,-3.0)
     */
    String toString();

    /**
     * safe clone of the current vector
     *
     * @return a new Vector3D with given x,y,z positions
     */
    Vector3dInterface clone();


    /**
     * Heading double.
     *
     * @return the double
     */
    double heading();

    /**
     * Is zero boolean
     *
     * @return the boolean
     */
    boolean isZero();

    boolean isLessThan(double delta);

    @Override
    int hashCode();

    /**
     * Div vector 3 d interface.
     *
     * @param vector3dInterface the vector 3 d interface
     * @return the vector 3 d interface
     */
    Vector3dInterface div(Vector3dInterface vector3dInterface);

    Vector3dInterface multiply(Vector3dInterface other);

    Vector3dInterface sumOf(Vector3dInterface... k2);

    void set(Vector3dInterface v);
}
