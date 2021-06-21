package phase3.Rocket;

import API.Math.ADT.Vector3D;

import java.awt.*;

import static API.Config.*;

/**
 * The type Rocket simulator.
 */
public class ModuleSimulator extends RocketSimulator {
    @Override
    public void initProperties() {
        this.setMASS(6e3);
        this.setRADIUS(1e1);
        this.setColour(Color.GREEN);
        if (CONTROLLER == OPEN) {
            this.setVectorLocation(new Vector3D(-5e2, 1e3, 0));
            this.setVectorVelocity(new Vector3D(30, 0.0, 0));
        } else if (CONTROLLER == CLOSED) {
            this.setVectorLocation(new Vector3D(2800, 2800, 0));
            this.setVectorVelocity(new Vector3D(-40, 0, 0));
        }

        this.fuelMass = this.startFuel;
        this.totalMass = this.startFuel + this.getMASS();
        this.setMASS(this.totalMass);
    }
}
