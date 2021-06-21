package phase3.Forces;

import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.Math.Functions.ODEFunctionInterface;
import API.Math.Noise.Noise;
import API.Math.Noise.Noise2D;
import API.Math.Noise.Noise3D;
import org.jetbrains.annotations.Contract;
import phase3.Simulation.State.RateOfChange;

import static java.lang.StrictMath.abs;
import static API.Config.NOISE_DIMENSIONS;

public final class WindModel implements WindInterface {

    private Noise noise;
    private static double mid;

    @Contract(pure = true)
    public WindModel() {

    }

    @Override
    public void init() {
        switch (NOISE_DIMENSIONS) {
            case BI_DIMENSIONAL -> {
                this.noise = new Noise2D();
                Noise.OCTAVE_COUNT = 8;
                Noise.SCALING_BIAS = 1.4f;
                Noise2D.randomizedAlgorithm();
            }
            case TRI_DIMENSIONAL -> {
                this.noise = new Noise3D();
                ((Noise3D) this.noise).setPerlinFreq(0.7);
                ((Noise3D) this.noise).setLacunarity(.812);
                ((Noise3D) this.noise).setPersistence(0.03);
                ((Noise3D) this.noise).setOctaveCount(30);
                mid = ((Noise3D) this.noise).getMaxValue() / 2.;
            }
        }

    }

    @Override
    public ODEFunctionInterface<Vector3dInterface> getWindFunction() {
        return (t, y) -> {
            Vector3dInterface v = y.get()[0];
            double acc;
            if (NOISE_DIMENSIONS == Noise.NoiseDim.BI_DIMENSIONAL) {
                acc = this.noise.getValue(v.getX(), v.getY(), t);
            } else {
                acc = this.noise.getValue(v.getX(), v.getY(), t);
            }
            double dir = abs(acc - mid);
            acc = dir * (acc > mid ? 1 : -1);
            //System.out.println(acc * 10);
            return new RateOfChange<>(y.get()[1], new Vector3D(acc * 10, 0, 0));
        };
    }

    @Override
    public Noise getNoise() {
        return this.noise;
    }

}
