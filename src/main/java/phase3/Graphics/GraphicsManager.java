package phase3.Graphics;


import API.Graphics.GraphicsInterface;
import API.Graphics.MouseInput;
import API.Graphics.Scene;
import phase3.Graphics.Scenes.*;
import API.Math.ADT.Vector3dInterface;
import API.Simulation.SimulationInterface;
import API.System.StateInterface;
import phase3.Simulation.State.SystemState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static API.Config.*;


/**
 * The main graphics threads/scenes handler
 */
public class GraphicsManager extends Canvas implements GraphicsInterface {

    private final static String SS = "Flight to Titan", ML = "Landing on Titan", LA = "Lorenz Attractor", DP = "Double Pendulum";
    /**
     * The Main graphics thread.
     */
    protected final AtomicReference<Thread> mainGraphicsTh = new AtomicReference<>();
    /**
     * The Current scene.
     */
    protected Scene currentScene;
    /**
     * The Frames.
     */
    int frames = 0;
    /**
     * The T.
     */
    double t = System.currentTimeMillis();
    private JFrame frame;
    private WindowEvent listen;
    public static int _2D = 1, _3D = 2;
    private StateInterface<Vector3dInterface> state;
    private final SimulationInterface simulation;
    public int simulationType;

    public GraphicsManager(SimulationInterface simulation) {
        this.simulation = simulation;
    }


    @Override
    public void init() {
        this.frame = new JFrame(
                switch (SIMULATION) {
                    case FLIGHT_TO_TITAN -> SS;
                    case LANDING_ON_TITAN -> ML;
                    case LORENTZ_ATTRACTOR -> LA;
                    case DOUBLE_PENDULUM -> DP;
                    default -> "";
                }
        );
        this.frame.setSize(screen);
        this.frame.add(this);
        this.setWindowProperties();
        this.frame.setVisible(true);
        Cursor c = new Cursor(Cursor.MOVE_CURSOR);
        this.frame.setCursor(c);
        //this.frame.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        //this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

    /**
     * Sets window properties.
     */
    protected void setWindowProperties() {
        final var closed = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                listen = new WindowEvent(frame, 201);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(listen);
                System.out.println("System closed by user");
                System.exit(0);
            }
        };
        this.frame.addWindowListener(closed);
        this.frame.setResizable(true);
        this.frame.setIconImage(loadIcon("death.png").getImage());
        this.frame.setLocationRelativeTo(null);// Center window
        Scene.SceneType x = switch (SIMULATION) {
            case FLIGHT_TO_TITAN -> Scene.SceneType.FLIGHT_SCENE;
            case LANDING_ON_TITAN -> Scene.SceneType.MODULE_SCENE;
            case LORENTZ_ATTRACTOR -> Scene.SceneType.LORENZ_SCENE;
            case DOUBLE_PENDULUM -> Scene.SceneType.PENDULUM_SCENE;
            default -> throw new IllegalStateException();
        };
        this.changeScene(x);
    }

    public static ImageIcon loadIcon(String path) {
        ImageIcon i = new ImageIcon(Objects.requireNonNull(GraphicsManager.class.getClassLoader().getResource("icons/" + path)).getFile());
        Image scaled = i.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        i.setImage(scaled);
        return i;
    }


    @Override
    public synchronized void start(final StateInterface<Vector3dInterface> state) {
        this.state = new SystemState<>(state.get());
        this.mainGraphicsTh.set(new Thread(this, "Main Graphics"));
        this.mainGraphicsTh.get().setDaemon(true);
        this.mainGraphicsTh.get().start();
    }

    @Override
    public JFrame getFrame() {
        return this.frame;
    }

    @Override
    public Scene getScene() {
        return this.currentScene;
    }

    @Override
    public void changeScene(Scene.SceneType scene) {
        MouseInput mouse;
        switch (scene) {
            case MODULE_SCENE -> {
                this.currentScene = new ModuleScene(simulation);
                mouse = new MouseInput(_2D);
            }
            case STARTING_SCENE -> {
                this.currentScene = new StartingScene(simulation);
                mouse = new MouseInput(_3D);
            }
            case FLIGHT_SCENE -> {
                this.currentScene = new SimulationScene(simulation);
                mouse = new MouseInput(_3D);
            }
            case LORENZ_SCENE -> {
                this.currentScene = new LorenzScene(simulation);
                mouse = new MouseInput(_3D);
            }
            case PENDULUM_SCENE -> {
                this.currentScene = new PendulumScene(simulation);
                mouse = new MouseInput(_2D);
            }
            default -> throw new IllegalStateException();
        }
        if (this.frame.getComponentCount() >= 1)
            this.frame.remove(this.currentScene);
        this.currentScene.init();
        this.currentScene.addMouseControl(mouse);
        this.currentScene.addMouseListener(mouse);
        this.currentScene.addMouseWheelListener(mouse);
        this.currentScene.addMouseMotionListener(mouse);
        this.frame.add(this.currentScene);
        this.frame.setVisible(true);
        this.frame.repaint();  //call fast the ui
        //optional
    }

    @Override
    public synchronized void update() {
        this.currentScene.update(this.state);
    }

    @Override
    public void run() {
        if (DEBUG) {
            frames++;
            double v = System.currentTimeMillis();
            if (v - t > 1000) {
                double FPS = frames;
                System.out.println("FPS :: " + FPS);
                t = v;
                frames = 0;
            }
        }
        this.update();
        this.currentScene.repaint();
    }

    @Override
    public String toString() {
        return "GraphicsManager{" +
                "currentScene=" + currentScene.toString() +
                '}';
    }
}
