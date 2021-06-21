package API.Math.Noise;


import org.jetbrains.annotations.Contract;

import static java.lang.StrictMath.*;

/**
 * Implementation Source :
 * Copyright (c) Flow Powered <https://github.com/flow>
 * Copyright (c) SpongePowered <https://github.com/SpongePowered>
 * Copyright (c) contributors
 * Copyright (c) noise <https://github.com/SpongePowered/noise>
 */
public class Noise3D extends Noise {

    private static final int X_NOISE_GEN = 1619;
    private static final int Y_NOISE_GEN = 31337;
    private static final int Z_NOISE_GEN = 6971;
    private static final int SEED_NOISE_GEN = 1013;
    private static final int SHIFT_NOISE_GEN = 8;

    private double perlinFreq = 0.06,
            lacunarity =1e-4,
            persistence = 0.04;

    private int seed = -11, OCTAVE_COUNT = 30;
    private double sphereFreq = 0.002;


    /**
     * Generates a gradient-coherent-noise value from the coordinates of a
     * three-dimensional input value.
     *
     * <p>The return value ranges from 0 to 1.</p>
     *
     * <p>For an explanation of the difference between <i>gradient</i> noise
     * and <i>value</i> noise, see the comments for the
     * {@link #gradientNoise3D(double, double, double, int, int, int, int)} function.</p>
     *
     * @param x    The {@code x} coordinate of the input value.
     * @param y    The {@code y} coordinate of the input value.
     * @param z    The {@code z} coordinate of the input value.
     * @param seed The random number seed.
     * @return The generated gradient-coherent-noise value.
     */
    public static double gradientCoherentNoise3D(double x, double y, double z, int seed) {

        // Create a unit-length cube aligned along an integer boundary.  This cube
        // surrounds the input point.

        int x0 = ((x > 0.0) ? (int) x : (int) x - 1);
        int x1 = x0 + 1;

        int y0 = ((y > 0.0) ? (int) y : (int) y - 1);
        int y1 = y0 + 1;

        int z0 = ((z > 0.0) ? (int) z : (int) z - 1);
        int z1 = z0 + 1;

        // Map the difference between the coordinates of the input value and the
        // coordinates of the cube's outer-lower-left vertex onto an S-curve.
        double xs, ys, zs;
        xs = sCurve3(x - (double) x0);
        ys = sCurve3(y - (double) y0);
        zs = sCurve3(z - (double) z0);

        // Now calculate the noise values at each vertex of the cube.  To generate
        // the coherent-noise value at the input point, interpolate these eight
        // noise values using the S-curve value as the interpolant (tri-linear
        // interpolation.)

        double iy0 = generateVector(x, y, z, seed, x0, x1, y0, y1, z0, xs, ys);
        double iy1 = generateVector(x, y, z, seed, x0, x1, y0, y1, z1, xs, ys);
        return linearInterp(iy0, iy1, zs);
    }

    private static double generateVector(double x, double y, double z, int seed, int x0, int x1, int y0, int y1, int z1, double xs, double ys) {
        double n0, n1, ix0, ix1;
        n0 = gradientNoise3D(x, y, z, x0, y0, z1, seed);
        n1 = gradientNoise3D(x, y, z, x1, y0, z1, seed);
        ix0 = linearInterp(n0, n1, xs);
        n0 = gradientNoise3D(x, y, z, x0, y1, z1, seed);
        n1 = gradientNoise3D(x, y, z, x1, y1, z1, seed);
        ix1 = linearInterp(n0, n1, xs);
        return linearInterp(ix0, ix1, ys);
    }

    /**
     * Generates a gradient-noise value from the coordinates of a
     * three-dimensional input value and the integer coordinates of a
     * nearby three-dimensional value.
     *
     * <p>The difference between fx and ix must be less than or equal to one.
     * The difference between {@code fy} and {@code iy} must be less than or equal to one.
     * The difference between {@code fz} and {@code iz} must be less than
     * or equal to one.</p>
     *
     * <p>A <i>gradient</i>-noise function generates better-quality noise than a
     * <i>value</i>-noise function. Most noise modules use gradient noise for
     * this reason, although it takes much longer to calculate.</p>
     *
     * <p>The return value ranges from 0 to 1.</p>
     *
     * <p>This function generates a gradient-noise value by performing the
     * following steps:</p>
     * <ol>
     * <li>It first calculates a random normalized vector based on the nearby
     * integer value passed to this function.</li>
     * <li>It then calculates a new value by adding this vector to the nearby
     * integer value passed to this function.</li>
     * <li>It then calculates the dot product of the above-generated value and
     * the floating-point input value passed to this function.</li>
     * </ol>
     *
     * <p>A noise function differs from a random-number generator because it
     * always returns the same output value if the same input value is passed
     * to it.</p>
     *
     * @param fx   The floating-point {@code x} coordinate of the input value.
     * @param fy   The floating-point {@code y} coordinate of the input value.
     * @param fz   The floating-point {@code z} coordinate of the input value.
     * @param ix   The integer {@code x} coordinate of a nearby value.
     * @param iy   The integer {@code y} coordinate of a nearby value.
     * @param iz   The integer {@code z} coordinate of a nearby value.
     * @param seed The random number seed.
     * @return The generated gradient-noise value.
     */
    @Contract(pure = true)
    public static double gradientNoise3D(double fx, double fy, double fz, int ix, int iy, int iz, int seed) {
        // Randomly generate a gradient vector given the integer coordinates of the
        // input value.  This implementation generates a random number and uses it
        // as an index into a normalized-vector lookup table.
        int vectorIndex = (X_NOISE_GEN * ix + Y_NOISE_GEN * iy + Z_NOISE_GEN * iz + SEED_NOISE_GEN * seed);
        vectorIndex ^= (vectorIndex >> SHIFT_NOISE_GEN);
        vectorIndex &= 0xff;

        double xvGradient = RANDOM_VECTORS_PERLIN3D[(vectorIndex << 2)];
        double yvGradient = RANDOM_VECTORS_PERLIN3D[(vectorIndex << 2) + 1];
        double zvGradient = RANDOM_VECTORS_PERLIN3D[(vectorIndex << 2) + 2];

        // Set up us another vector equal to the distance between the two vectors
        // passed to this function.
        double xvPoint = (fx - ix);
        double yvPoint = (fy - iy);
        double zvPoint = (fz - iz);

        // Now compute the dot product of the gradient vector with the distance
        // vector.  The resulting value is gradient noise.  Apply a scaling and
        // offset value so that this noise value ranges from 0 to 1.
        return ((xvGradient * xvPoint) + (yvGradient * yvPoint) + (zvGradient * zvPoint)) + 0.5;
    }

    @Contract(pure = true)
    public static double intValueNoise3D(int x, int y, int z, int seed) {
        // All constants are primes and must remain prime in order for this noise
        // function to work correctly.
        int n = (X_NOISE_GEN * x + Y_NOISE_GEN * y + Z_NOISE_GEN * z + SEED_NOISE_GEN * seed) & 0x7fffffff;
        n = (n >> 13) ^ n;
        int m = (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;
        return m / 2147483647.0;
    }

    public double getPerlinFreq() {
        return perlinFreq;
    }

    public double getOctaveCount() {
        return this.OCTAVE_COUNT;
    }

    public void setPerlinFreq(double perlinFreq) {
        this.perlinFreq = perlinFreq;
    }

    public double getLacunarity() {
        return lacunarity;
    }

    public void setLacunarity(double lacunarity) {
        this.lacunarity = lacunarity;
    }

    public double getPersistence() {
        return persistence;
    }

    public void setPersistence(double persistence) {
        this.persistence = persistence;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public double getSphereFreq() {
        return sphereFreq;
    }

    public void setSphereFreq(double sphereFreq) {
        this.sphereFreq = sphereFreq;
    }

    public void setOctaveCount(int octaveCount) {
        this.OCTAVE_COUNT = max(min(octaveCount, 30), 1);
    }

    public double perlinNoise3DValue(double x, double y, double z) {
        double x1 = x;
        double y1 = y;
        double z1 = z;
        double value = 0.0;
        double signal;
        double curPersistence = 1.0;
        double nx, ny, nz;
        int seed;

        x1 *= perlinFreq;
        y1 *= perlinFreq;
        z1 *= perlinFreq;

        for (int curOctave = 0; curOctave < OCTAVE_COUNT; curOctave++) {

            // Make sure that these floating-point values have the same range as a 32-
            // bit integer so that we can pass them to the coherent-noise functions.
            nx = makeInt32Range(x1);
            ny = makeInt32Range(y1);
            nz = makeInt32Range(z1);

            // Get the coherent-noise value from the input value and add it to the
            // final result.
            seed = (this.seed + curOctave);
            signal = gradientCoherentNoise3D(nx, ny, nz, seed);
            value += signal * curPersistence;

            // Prepare the next octave.
            x1 *= lacunarity;
            y1 *= lacunarity;
            z1 *= lacunarity;
            curPersistence *= persistence;
        }

        return value;
    }

    public double sphereNoiseValue(double x, double y, double z) {
        double x1 = x;
        double y1 = y;
        double z1 = z;
        x1 *= sphereFreq;
        y1 *= sphereFreq;
        z1 *= sphereFreq;
        double c = sqrt(x1 * x1 + y1 * y1 + z1 * z1);
        int floor = (int) c;
        double ds1 = c - (c < floor ? floor - 1 : floor);
        double ds2 = 1.0 - ds1;
        double nearestDist = min(ds1, ds2);
        return 1.0 - (nearestDist * 2.0);
    }

    /**
     * Returns the maximum value the perlin module can output in its current configuration
     *
     * @return The maximum possible value for perlinNoise3DValue(double, double, double) to return
     */
    public double getMaxValue() {
        /*
         * Each successive octave adds persistence ^ current_octaves to max possible output.
         * So (p = persistence, o = octave): Max(perlin) = p + p*p + p*p*p + ... + p^(o-1).
         * Using geometric series formula we can narrow it down to this:
         */
        return (pow(getPersistence(), getOctaveCount()) - 1) / (getPersistence() - 1);
    }

    @Override
    public double getValue(double x, double y, double z) {
        double v = perlinNoise3DValue(x, y, z);
        double u = sphereNoiseValue(x, y, z);
        double w = intValueNoise3D((int) x, (int) y, (int) z, seed);
        // return linearInterp(v, u, 0.3);
        return v;
    }
}
