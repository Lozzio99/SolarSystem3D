package API.Math.Noise;

import org.jetbrains.annotations.Contract;

import java.util.Random;

public class Noise2D extends Noise {
    static {
        randomize();
        randomizedAlgorithm();
    }

    public static void randomize() {
        for (int i = 0; i < RANDOM_VECTORS2D.length; i++) RANDOM_VECTORS2D[i] = new Random().nextDouble();
    }

    @Contract(pure = true)
    public static double perlinNoise2D(int x, int y) {
        int x1 = x % N_COUNT;
        int y1 = y % N_COUNT;
        return PERLIN_VECTORS2D[y1 * N_COUNT + x1];
    }

    // Generate2d Perlin Noise array

    public static void randomizedAlgorithm() {
        for (int x = 0; x < N_COUNT; x++)

            for (int y = 0; y < N_COUNT; y++) {
                float noise = 0.0f;
                float sumScaling = 0.0f;
                float scaling = 1.0f;

                //iterate for octaves
                for (int octave = 0; octave < OCTAVE_COUNT; octave++) {
                    int padding = N_COUNT >> octave;  //half pitch by size and by 2 for each octave

                    //take neighbour samples for interpolation
                    int x1 = (x / padding) * padding;  //move around array by padding thanks to int phase division
                    int y1 = (y / padding) * padding;

                    int x2 = (x1 + padding) % N_COUNT;   //paired values
                    int y2 = (y1 + padding) % N_COUNT;

                    //distance for blending factor
                    float xBlend = (float) (x - x1) / (float) padding;   //how far the sample is with padding units
                    float yBlend = (float) (y - y1) / (float) padding;

                    //direct linear interpolation between samples
                    double xSample, ySample;
                    xSample = linearInterp(RANDOM_VECTORS2D[y1 * N_COUNT + x1], RANDOM_VECTORS2D[y1 * N_COUNT + x2], xBlend);
                    ySample = linearInterp(RANDOM_VECTORS2D[y2 * N_COUNT + x1], RANDOM_VECTORS2D[y2 * N_COUNT + x2], xBlend);


                    sumScaling += scaling;  //accumulate scaling factor
                    noise += (yBlend * (ySample - xSample) + xSample) * scaling;  //update noise value
                    scaling = scaling / SCALING_BIAS;  //can blur the blending by some factor at each octave
                }

                PERLIN_VECTORS2D[y * N_COUNT + x] = noise / sumScaling;  //divide by the scaling accumulated in all octaves
                // contain values in [0,1]
            }
    }

    @Override
    public double getValue(double x, double y, double z) {
        return perlinNoise2D(((int) x), ((int) y));  //2d
    }
}
