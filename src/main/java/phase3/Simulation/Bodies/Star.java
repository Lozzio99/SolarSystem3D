package phase3.Simulation.Bodies;

import API.Math.ADT.Vector3D;
import API.System.CelestialBody;

import java.awt.*;

public class Star extends CelestialBody {
    @Override
    public String toString() {
        return "SUN";
    }

    @Override
    public void initProperties() {
        this.setMASS(1.988500e+30);
        this.setRADIUS(6.96342e7);//TODO : set this to e8 for collision detector
        this.setColour(Color.yellow);
        this.setVectorLocation(new Vector3D(-6.806783239281648e+08, 1.080005533878725e+09, 6.564012751690170e+06));
        this.setVectorVelocity(new Vector3D(-14.20511660519414, -4.954714684919104, 0.399423759988592));
    }
}
