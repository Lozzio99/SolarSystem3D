package API.Math.ANN;

import API.Math.ADT.ANN_Data;
import API.Math.ADT.Matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Xor {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;
    private static final int resolution = 10;
    private static final int cols = WIDTH / resolution;
    private static final int rows = HEIGHT / resolution;
    private static WindowEvent listen;
    private static JFrame frame;
    private static double learningRate = 0.01;
    private static int iteration = 1;
    private static Matrix[] in;
    private static Matrix[] tg;
    private static ANN_Data[] tests;


    public static void main(String[] args) {
        setup();
        initBatch();
        makeGraphics();
    }


    public static void makeGraphics() {
        XorWindow gfx = new XorWindow();
        frame.add(gfx);
        frame.setVisible(true);
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(gfx::evaluate, 30, 12, TimeUnit.MILLISECONDS);
    }

    public static void initBatch() {
        tests = new ANN_Data[4];
        // XOR                          inputs  v         targets v
        //              1       0
        //                                  -> not linearly separable
        //              0       1
        //
        tests[0] = new ANN_Data(new double[]{1, 1}, new double[]{0});
        tests[1] = new ANN_Data(new double[]{1, 0}, new double[]{1});
        tests[2] = new ANN_Data(new double[]{0, 1}, new double[]{1});
        tests[3] = new ANN_Data(new double[]{0, 0}, new double[]{0});
        in = new Matrix[tests.length];
        tg = new Matrix[tests.length];
        for (int i = 0; i < tests.length; i++) {
            in[i] = new Matrix(tests[i].getInput());
            tg[i] = new Matrix(tests[i].getOutput());
        }
    }

    public static void setup() {
        frame = new JFrame();
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.getContentPane();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowAdapter closed = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Xor.listen = new WindowEvent(Xor.frame, 201);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(Xor.listen);
                System.out.println("System closed by user");
                System.exit(0);
            }
        };
        frame.addWindowListener(closed);
        frame.setVisible(true);
        frame.pack();
    }

    public static class XorWindow extends JComponent {
        public NeuralNetwork brain;

        public XorWindow() {
            // the more hidden nodes the more it's accurate
            this.brain = new NeuralNetwork(2, 128, 1, true, false);
        }

        public synchronized void evaluate() {
            //give it a bit to run
            if (iteration % 100 == 0) {
                System.out.println(iteration + "  - learning rate : " + learningRate);
            }
            iteration++;
            int k = new Random().nextInt(tests.length);
            brain.train(in[k], tg[k]);
            //  brain.train(in[k],tg[k]);
            if (learningRate < 0.5 && learningRate <= 0.4) {
                brain.setLearningRate(learningRate += 1e-6);
            }
            repaint();
        }


        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            for (int i = 0; i < cols; i++) {
                for (int k = 0; k < rows; k++) {
                    double x1 = i / (double) cols;
                    double x2 = k / (double) rows;
                    double[] inputs = new double[]{x1, x2};
                    int y = (int) (this.brain.guess(inputs).getMatrix()[0][0] * 255);
                    Color r = new Color(255 - y, y, y / 2);
                    g2.setColor(r);
                    g2.fillRect(i * resolution, k * resolution, resolution, resolution);
                    g2.setColor(Color.white);
                    g2.drawRect(i * resolution, k * resolution, resolution, resolution);
                }
            }
            frame.repaint();

        }

    }


}
