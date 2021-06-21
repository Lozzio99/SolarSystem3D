package phase3.Graphics.Scenes;

import API.Graphics.MouseInput;
import API.Graphics.Scene;
import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3DConverter;
import API.Math.ADT.Vector3dInterface;
import API.Simulation.SimulationInterface;
import API.System.StateInterface;
import org.jetbrains.annotations.Contract;
import phase3.Simulation.State.SystemState;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static API.Config.*;
import static API.Graphics.GraphicsInterface.screen;
import static API.Math.ADT.Vector3DConverter.*;

/**
 * The type Simulation scene.
 */
public class SimulationScene extends Scene {

    /**
     * The Planets positions.
     */
    Vector3dInterface[] planetsPositions;
    /**
     * The Radius.
     */
    double[] radius;
    /**
     * The Trajectories.
     */
    Bag[] trajectories;

    public SimulationScene(SimulationInterface s) {
        super(s);
    }


    @Override
    public void paintComponent(Graphics graphics) {
        if (MAKE_GIF){
            BufferedImage bi = new BufferedImage(screen.width,screen.height,BufferedImage.TYPE_INT_ARGB);
            Graphics2D save = bi.createGraphics();
            createImage(save);
            saveImage("src/main/resources/Gifs/Titan"+(GIF_INDEX)+".png",bi);
            if (GIF_INDEX == END_GIF) System.exit(0);
        } else {
            Graphics2D g = (Graphics2D) graphics;
            createImage(g);
        }
    }

    public void createImage(Graphics2D g){
        super.paintComponent(g);
        super.paintImage(g, flightImage);
        try {
            for (int i = 0; i < this.planetsPositions.length; i++) {
                final Point2D.Double p = Vector3DConverter.convertVector(this.planetsPositions[i]);
                if (NAMES) {
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Monospaced", Font.PLAIN, 10));
                    g.drawString(simulation.getSystem().getCelestialBodies().get(i).toString(), (int) p.getX(), (int) p.getY());
                }
                g.setColor(simulation.getSystem().getCelestialBodies().get(i).getColour());
                LorenzScene.drawTrajectories(g, i, this.trajectories);
                g.fill(planetShape(this.planetsPositions[i], this.radius[i]));

            }
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void init() {
        this.planetsPositions = new Vector3dInterface[simulation.getSystem().getState().get().length / 2];
        this.state = new SystemState<>(new Vector3D[this.planetsPositions.length]);
        this.radius = new double[this.planetsPositions.length];
        this.trajectories = new Bag[this.planetsPositions.length];
        for (int i = 0; i < this.planetsPositions.length; i++) {
            if (DRAW_TRAJECTORIES) this.trajectories[i] = new Bag();
            this.planetsPositions[i] = simulation.getSystem().getState().get()[i].div(scale);
            radius[i] = (simulation.getSystem().getCelestialBodies().get(i).getRADIUS() / scale) * getScale() * radiusMag;
        }
    }


    @Override
    public void addMouseControl(MouseInput mouse) {
        this.mouse = mouse;
    }

    /**
     * Get planet positions point 3 d [ ].
     *
     * @return the point 3 d [ ]
     */
    public Vector3dInterface[] getPlanetPositions() {
        return this.planetsPositions;
    }

    public void update(StateInterface<Vector3dInterface> state) {
        super.update(state);
        this.updateBodies();
        super.resetMouse();
    }

    /**
     * Update bodies.
     */
    public void updateBodies() {
        double x = totalXDif2 / mouseSensitivity, y = totalYDif2 / mouseSensitivity, dx = deltaX2 / mouseSensitivity, dy = deltaY2 / mouseSensitivity;
        for (int i = 0; i < this.planetsPositions.length; i++) {
            this.planetsPositions[i] = state.get()[i].div(scale);
            radius[i] = (simulation.getSystem().getCelestialBodies().get(i).getRADIUS() / scale) * getScale() * radiusMag;
            rotateAxisY(this.planetsPositions[i], false, x);
            rotateAxisX(this.planetsPositions[i], false, y);
            if (DRAW_TRAJECTORIES) {
                LorenzScene.updateTrajectories(i, dx, dy, simulation.isRunning(), this.trajectories, this.planetsPositions);
            }
        }

    }


    @Override
    public String toString() {
        return "SimulationScene{" +
                "planetsPositions=" + Arrays.toString(planetsPositions) +
                '}';
    }


    /**
     * The type Bag.
     */
    public static class Bag {
        /**
         * The Trajectories.
         */
        final Vector3dInterface[] trajectories;
        private int insert;

        /**
         * Instantiates a new Bag.
         */
        @Contract(pure = true)
        Bag() {
            this.trajectories = new Vector3dInterface[TRAJECTORY_LENGTH];
            this.insert = 0;
        }

        /**
         * Add.
         *
         * @param p the p
         */
        public void add(final Vector3dInterface p) {
            this.trajectories[this.getInsert()] = p;
            this.insert = this.getInsert() + 1;
            if (this.getInsert() == this.trajectories.length) {
                this.insert = 0;
            }
        }

        /**
         * Get trajectories point 3 d [ ].
         *
         * @return the point 3 d [ ]
         */
        public Vector3dInterface[] getTrajectories() {
            return this.trajectories;
        }

        /**
         * The Insert.
         */
        public int getInsert() {
            return insert;
        }
    }
}
