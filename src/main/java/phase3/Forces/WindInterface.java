package phase3.Forces;

import API.Math.ADT.Vector3dInterface;
import API.Math.Functions.ODEFunctionInterface;
import API.Math.Noise.Noise;

public interface WindInterface {

    void init();

    ODEFunctionInterface<Vector3dInterface> getWindFunction();

    Noise getNoise();


}