package phase3.Rocket.Controllers;

import API.Math.ADT.Vector3dInterface;
import API.Rocket.ControllerInterface;
import API.System.StateInterface;

import static API.Config.CLOSED;
import static API.Config.OPEN;

public class DecisionMaker {

    private ControllerInterface controller;

    public DecisionMaker(int loopType) {
        switch (loopType) {
            case OPEN -> {
                controller = new OpenLoopManualNewController();
            }
            case CLOSED -> {
                controller = new ClosedLoopController();
            }
        }
    }

    public double[] getControls(double t, StateInterface<Vector3dInterface> y) {
        return controller.controlFunction(t, y);
    }
}
