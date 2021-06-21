package API.Graphics;


import phase3.Graphics.Scenes.SimulationScene;
import API.Math.ADT.Vector3D;
import API.Math.ADT.Vector3dInterface;
import API.Simulation.SimulationInterface;
import API.System.StateInterface;
import phase3.Simulation.State.SystemState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static API.Config.*;
import static API.Config.MAKE_GIF;
import static API.Graphics.GraphicsInterface.screen;
import static phase3.Graphics.GraphicsManager._2D;
import static phase3.Graphics.GraphicsManager._3D;
import static API.Math.ADT.Vector3DConverter.*;

/**
 * Abstract class to Render a scene
 * to implement a Scene inherit from this class like this :
 * class MyScene extends Scene{
 * }
 * <p>
 * it is also possible to delegate the background painting
 * to the superclass with a call to the super.paintComponent(graphics)
 * {@link #paintComponent(Graphics)}
 * or to update the bodies positions and the mouse updates by a call
 * to the {@link #update(StateInterface v)} and a consequent call to  {@link #resetMouse()}
 */
public abstract class Scene extends JPanel {
    /**
     * The constant mouseSensitivity.
     */
    protected static double mouseSensitivity = 8;
    private final static int UNIT_SIZE = 1410;//*(int) scale;
    public static Line2D.Double X, Y;
    /**
     * The constant initialX.
     */
    protected static int initialX, initialY, x, y, xDif, yDif, totalXDif, totalYDif;
    protected static int initialX2, initialY2, x2, y2, xDif2, yDif2, totalXDif2, totalYDif2;
    protected static double scale = 3e8;
    protected static double deltaX2;
    protected static double deltaY2;
    /**
     * The constant mouse.
     */
    public MouseInput mouse;
    protected double radiusMag = 4e2;
    protected Vector3dInterface left = new Vector3D(-UNIT_SIZE, 0, 0),
            right = new Vector3D(UNIT_SIZE, 0, 0),
            top = new Vector3D(0, UNIT_SIZE, 0),
            bottom = new Vector3D(0, -UNIT_SIZE, 0),
            front = new Vector3D(0, 0, UNIT_SIZE),
            rear = new Vector3D(0, 0, -UNIT_SIZE);


    protected StateInterface<Vector3dInterface> state = new SystemState<>(new Vector3D(0, 0, 0));
    /**
     * The Graphics object
     */
    private Graphics2D g;
    /**
     * The background flightImage
     */
    protected BufferedImage flightImage;
    protected BufferedImage landingImage;
    protected boolean IMAGE_FAILED = false;
    protected final SimulationInterface simulation;

    protected Scene(SimulationInterface s) {
        simulation = s;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        //super.paintComponent(graphics);
        g = (Graphics2D) graphics;
        if (flightImage == null || landingImage == null) {
            create();
            setHints(g);
        }
    }

    public void paintImage(Graphics2D g, BufferedImage image) {
        if (!IMAGE_FAILED) {
            g.drawImage(image, 0, 0, screen.width,
                    screen.height, 0, 0, image.getWidth(), image.getHeight(), new Color(0, 0, 0, 111), this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, simulation.getGraphics().getFrame().getWidth(),
                    simulation.getGraphics().getFrame().getHeight());
        }
    }
    public static void saveImage(String file, BufferedImage image) {
        try {
            File f = new File(file);
            if (!f.mkdirs()) throw new IllegalStateException("failed to create " + f.getPath());
            GIF_INDEX++;
            ImageIO.write(image, "gif", f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected static void updateTrajectories(int i, double dx, double dy, boolean running, SimulationScene.Bag[] trajectories, Vector3dInterface[] planetsPositions) {
        if (running) trajectories[i].add(planetsPositions[i]);
        if (dx == 0 && dy == 0) return;
        for (int k = 0; k < trajectories[i].getTrajectories().length; k++) {
            if (trajectories[i].getTrajectories()[k] == null) {
                break;
            }
            //most pleasant "bug" of my life - change delta x and y to be xdiff and ydiff - rotate the scene
            rotateAxisY(trajectories[i].getTrajectories()[k], false, dx);
            rotateAxisX(trajectories[i].getTrajectories()[k], false, dy);
        }
    }

    protected static void drawTrajectories(Graphics2D g, int i, SimulationScene.Bag[] trajectories) {
        if (DRAW_TRAJECTORIES) {
            for (int k = trajectories[i].getInsert(); k < trajectories[i].getTrajectories().length - 1; k++) {
                if (trajectories[i].getTrajectories()[k + 1] == null)
                    break;
                g.draw(new Line2D.Double(
                        convertVector(trajectories[i].getTrajectories()[k]),
                        convertVector(trajectories[i].getTrajectories()[k + 1])));
            }
            for (int k = 0; k < trajectories[i].getInsert() - 1; k++) {
                if (trajectories[i].getTrajectories()[k + 1] == null)
                    break;
                g.draw(new Line2D.Double(
                        convertVector(trajectories[i].getTrajectories()[k]),
                        convertVector(trajectories[i].getTrajectories()[k + 1])));
            }
        }
    }

    /**
     * Improves the rendering performance as well as the scaling and coloring
     * interpolations
     *
     * @param g the graphics object to configure
     */
    public void setHints(Graphics2D g) {
        this.doLayout();
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
    }

    /**
     * Loads the background flightImage from resources
     */
    void create() {
        try {
            File titan = new File(Objects.requireNonNull(Scene.class.getClassLoader().getResource("Icons/titan2.png")).getFile());
            //URL resourse = new URL("https://cdn.mos.cms.futurecdn.net/k3FmsfkjnEQao2Ci2AtEUK-1200-80.jpg");
            File flight = new File(Objects.requireNonNull(Scene.class.getClassLoader().getResource("Icons/milky-way4k.jpg")).getFile());
            flightImage = ImageIO.read(flight);
            landingImage = ImageIO.read(titan);
        } catch (NullPointerException | IOException e) {
            IMAGE_FAILED = true;
            flightImage = null;
            System.err.println(e.getMessage() + "\nPossible fix : make sure 'milky-way4k.jpg' is in resources");
        }
    }

    /**
     * Planet shape ellipse 2 d . double.
     *
     * @param position the position
     * @param radius   the radius
     * @return the ellipse 2 d . double
     */
    protected Ellipse2D.Double planetShape(Vector3dInterface position, double radius) {
        Point2D.Double p = convertVector(position);
        return new Ellipse2D.Double(p.getX() - radius, p.getY() - radius, radius * 2, radius * 2);
    }

    protected Ellipse2D.Double planetShape(Point2D.Double p, double radius) {
        return new Ellipse2D.Double(p.getX() - radius, p.getY() - radius, radius * 2, radius * 2);
    }

    /**
     * Init.
     */
    public void init(){
        this.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    MAKE_GIF = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    MAKE_GIF = false;
                    GIF_INDEX = 0;
                }
            }
        });
        this.setEnabled(true);
        this.setFocusable(true);
        System.err.println("PRESS SPACE TO START GIF");
        System.err.println("PRESS ENTER TO END GIF");
    }

    /**
     * updates the mouse tracking variables to update the bodies if
     * any mouse event was generated
     */
    public synchronized void update(final StateInterface<Vector3dInterface> v) {
        this.state = v;
        //xDif = xDif2 = yDif = yDif2 = 0;

        if (mouse.getScene() == _2D) {
            x = mouse.getX();
            y = mouse.getY();
            if (mouse.getButton() == MouseInput.ClickType.LeftClick) {
                xDif = x - initialX;
                translate(xDif / mouseSensitivity, 0);
                yDif = y - initialY;
                translate(0, yDif / mouseSensitivity);
            } else if (mouse.isScrollingUp()) {
                zoomIn();
            } else if (mouse.isScrollingDown()) {
                zoomOut();
            }
            totalXDif += xDif;
            totalYDif += yDif;
        } else if (mouse.getScene() == _3D) {
            x2 = mouse.getX();
            y2 = mouse.getY();
            if (mouse.getButton() == MouseInput.ClickType.LeftClick) {
                deltaX2 = xDif2 = x2 - initialX2;
                totalXDif2 += xDif2;
                this.rotateOnAxisX(false, xDif2 / mouseSensitivity);
            } else if (mouse.getButton() == MouseInput.ClickType.RightClick) {
                deltaY2 = yDif2 = y2 - initialY2;
                totalYDif2 += yDif2;
                this.rotateOnAxisY(false, yDif2 / mouseSensitivity);
            } else if (mouse.isScrollingUp()) {
                zoomIn();
            } else if (mouse.isScrollingDown()) {
                zoomOut();
            }
        }
        //call this from the subclass
        //this.resetMouse();
    }

    /**
     * add the Mouse listener object to the scene
     *
     * @param mouse the mouse
     */
    public abstract void addMouseControl(MouseInput mouse);

    /**
     * Reset the mouse events generated, after the update call,
     * in order to let them awake for the next frame,
     * avoiding such a call will lead to interminable mouse events such as
     * continuous spinning or zooming
     */
    public void resetMouse() {
        mouse.resetScroll();
        //mouse.resetButton();
        initialX = x;
        initialY = y;
        initialX2 = x2;
        initialY2 = y2;
        deltaX2 = 0;
        deltaY2 = 0;
    }

    /**
     * Rotates over the Y axis
     *
     * @param cw true if clockwise , false otherwise
     * @param y  the degree of rotation
     */
    public void rotateOnAxisX(boolean cw, double y) {
        rotateAxisY(top, cw, y);
        rotateAxisY(bottom, cw, y);
        rotateAxisY(left, cw, y);
        rotateAxisY(right, cw, y);
        rotateAxisY(rear, cw, y);
        rotateAxisY(front, cw, y);
    }

    /**
     * Rotates over the X axis
     *
     * @param cw true if clockwise , false otherwise
     * @param x  the degree of rotation
     */
    void rotateOnAxisY(boolean cw, double x) {
        rotateAxisX(top, cw, x);
        rotateAxisX(bottom, cw, x);
        rotateAxisX(left, cw, x);
        rotateAxisX(right, cw, x);
        rotateAxisX(rear, cw, x);
        rotateAxisX(front, cw, x);
    }

    /**
     * Rotates over the Z axis
     *
     * @param cw true if clockwise , false otherwise
     * @param z  the degree of rotation
     */
    void rotateOnAxisZ(boolean cw, double z) {
        rotateAxisZ(top, cw, z);
        rotateAxisZ(bottom, cw, z);
        rotateAxisZ(left, cw, z);
        rotateAxisZ(right, cw, z);
        rotateAxisZ(rear, cw, z);
        rotateAxisZ(front, cw, z);
    }

    /**
     * enum for the scenes implementations
     */
    public enum SceneType {
        /**
         * Landing module
         */
        MODULE_SCENE,
        STARTING_SCENE,
        FLIGHT_SCENE,
        LORENZ_SCENE,
        PENDULUM_SCENE
    }

}
