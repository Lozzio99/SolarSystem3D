package phase3.Graphics.Scenes;

import API.Graphics.MouseInput;
import API.Graphics.Scene;
import API.Math.ADT.Vector2DConverter;
import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.Simulation.SimulationInterface;
import API.System.StateInterface;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static API.Config.*;
import static API.Graphics.GraphicsInterface.screen;
import static API.Math.ADT.Vector2DConverter.convertVector;
import static API.Math.ADT.Vector2DConverter.getScale;

public class PendulumScene extends Scene {

    static Vector3dInterface vx, vy, vx2, vy2;
    private static double r1;
    private static double r2;
    private final Color[] colors = new Color[]{
            new Color(255, 255, 255, 149),
            new Color(255, 55, 55),
            new Color(255, 99, 99, 195),
            new Color(113, 255, 115),
            new Color(135, 255, 136, 174)
    };
    private final Vector3dInterface origin = new Vector3D(0, 0, 0), p1 = new Vector3D(-100, -200, 0), p2 = new Vector3D(-100, -400, 0);
    private final SimulationScene.Bag[] trajectories = new SimulationScene.Bag[2];

    public PendulumScene(SimulationInterface s) {
        super(s);
    }

    protected static void drawTrajectories(Graphics2D g, int i, SimulationScene.Bag[] trajectories) {
        for (int k = trajectories[i].getInsert(); k < trajectories[i].getTrajectories().length - 1; k++) {
            if (trajectories[i].getTrajectories()[k + 1] == null)
                break;
            g.draw(new Line2D.Double(
                    Vector2DConverter.convertVector(trajectories[i].getTrajectories()[k]),
                    Vector2DConverter.convertVector(trajectories[i].getTrajectories()[k + 1])));
        }
        for (int k = 0; k < trajectories[i].getInsert() - 1; k++) {
            if (trajectories[i].getTrajectories()[k + 1] == null)
                break;
            g.draw(new Line2D.Double(
                    Vector2DConverter.convertVector(trajectories[i].getTrajectories()[k]),
                    Vector2DConverter.convertVector(trajectories[i].getTrajectories()[k + 1])));
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        if (MAKE_GIF){
            BufferedImage image = new BufferedImage(screen.width, screen.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();
            createImage(g);
            saveImage("src/main/resources/Gifs/pendulum" + (GIF_INDEX) + ".png", image);
            if (GIF_INDEX == END_GIF) System.exit(0);
        } else {
            Graphics2D g = (Graphics2D) graphics;
            createImage(g);
        }
    }
    private void createImage(Graphics2D g) {
        g.setColor(Color.black);
        g.fill3DRect(0, 0, screen.width, screen.height, false);
        g.setColor(colors[0]);
        g.setStroke(new BasicStroke(1f));
        g.draw(X);
        g.draw(Y);
        Point2D.Double po = convertVector(origin),
                p1 = convertVector(this.p1),
                p2 = convertVector(this.p2);
        g.draw(new Line2D.Double(po, p1));
        g.draw(new Line2D.Double(p1, p2));
        g.draw(planetShape(po, 10 * getScale()));
        g.setColor(colors[1]);
        g.fill(planetShape(p1, r1 * getScale()));
        g.setColor(colors[2]);
        drawTrajectories(g, 0, trajectories);
        g.setColor(colors[3]);
        g.fill(planetShape(p2, r2 * getScale()));
        g.setColor(colors[4]);
        drawTrajectories(g, 1, trajectories);
    }


    @Override
    public void init() {
        //super.init();
        for (int i = 0; i < 25; i++) Vector2DConverter.zoomOut();
        mouseSensitivity = 2;
        TRAJECTORY_LENGTH = 600;
        this.state = simulation.getSystem().getState();
        this.trajectories[0] = new SimulationScene.Bag();
        this.trajectories[1] = new SimulationScene.Bag();
        updateBodies();
        vx = new Vector3D(1000, 0, 0);
        vx2 = new Vector3D(-1000, 0, 0);
        vy = new Vector3D(0, 1000, 0);
        vy2 = new Vector3D(0, -1000, 0);
        X = new Line2D.Double(Vector2DConverter.convertVector(vx), Vector2DConverter.convertVector(vx2));
        Y = new Line2D.Double(Vector2DConverter.convertVector(vy), Vector2DConverter.convertVector(vy2));
    }


    @Override
    public synchronized void update(StateInterface<Vector3dInterface> v) {
        super.update(v);
        updateBodies();
        X.setLine(Vector2DConverter.convertVector(vx), Vector2DConverter.convertVector(vx2));
        Y.setLine(Vector2DConverter.convertVector(vy), Vector2DConverter.convertVector(vy2));
        super.resetMouse();
    }

    protected void updateBodies() {
        r1 = state.get()[0].getX()>50?50 : state.get()[0].getX() ;
        r2 = state.get()[1].getX()>50?50 : state.get()[1].getX() ;
        double l1 = state.get()[0].getY();
        double l2 = state.get()[1].getY();
        double a1 = state.get()[0].getZ();
        double a2 = state.get()[1].getZ();
        p1.setX(l1 * sin(a1));
        p1.setY(l1 * cos(a1));
        p2.setX(p1.getX() + l2 * sin(a2));
        p2.setY(p1.getY() + l2 * cos(a2));
        this.trajectories[0].add(p1.clone());
        this.trajectories[1].add(p2.clone());
    }

    @Override
    public void addMouseControl(MouseInput mouse) {
        this.mouse = mouse;
    }
}
