package API.Math.Noise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static API.Config.*;
import static API.Graphics.Scene.saveImage;
import static API.Math.Noise.Noise2D.*;

public class WindNoiseCanvas extends Canvas {

    private static final double endTime = 1000;
    private static final Noise2D noise2D = new Noise2D();
    private static final Dimension _3D = new Dimension(768, 512);
    private static final Dimension _2D = new Dimension(1024, 768);
    private static JFrame frame2D, frame3D;
    private static Noise3D noise3D;
    private static ScheduledExecutorService service;


    public static void main(String[] args) {
        //configure();
        service = new ScheduledThreadPoolExecutor(10);
        setup3D(frame3D = new JFrame(), new WindNoiseCanvas());
        //setup2D(frame2D = new JFrame(), new WindNoiseCanvas());
    }

    public static void configure() {
        System.err.println("Save gif Wind 3D images?");
        System.out.println("{Y,N}");
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        if (s.toLowerCase(Locale.ROOT).charAt(0) == 'y') MAKE_GIF = true;
        else {
            if (s.toLowerCase(Locale.ROOT).charAt(0) == 'n') MAKE_GIF = false;
            else System.err.println("Unknown answer " + s + " -> saving set to false.");
        }

        System.out.println("Press key {SPACE} to generate new RANDOM_VECTORS");
        System.out.println("Press key {UP,DOWN} to increase/decrease SCALING");
        System.out.println("Press key {+,-} to increase/decrease OCTAVE_COUNT");
    }

    public static void setup2D(JFrame frame, Canvas canvas) {
        frame.setSize(_2D);
        frame.setResizable(false);
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setVisible(true);
        canvas.setFocusable(true);
        canvas.setEnabled(true);
        setMouse(canvas);
        frame.setTitle("Octaves : " + OCTAVE_COUNT + " , scaling : " + SCALING_BIAS);
        canvas.createBufferStrategy(3);
        service.scheduleWithFixedDelay(() -> {
            noise2DIteration(canvas);
        }, 30, 16, TimeUnit.MILLISECONDS);
    }
    private static void setMouse(Canvas canvas) {
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean update = false;
                if (e.getKeyCode() == KeyEvent.VK_PLUS) {
                    update = true;
                    OCTAVE_COUNT++;
                }
                if (e.getKeyCode() == KeyEvent.VK_MINUS) {
                    update = true;
                    OCTAVE_COUNT--;
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    update = true;
                    SCALING_BIAS += 0.2;
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    update = true;
                    SCALING_BIAS -= 0.2;
                }

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    update = true;
                    randomize();
                }

                if (update) {
                    OCTAVE_COUNT = Math.min(OCTAVE_COUNT, MAX_OCTAVE);
                    OCTAVE_COUNT = Math.max(OCTAVE_COUNT, MIN_OCTAVE);
                    SCALING_BIAS = Math.max(SCALING_BIAS, 0.2f);
                    randomizedAlgorithm();
                    frame2D.setTitle("Octaves : " + OCTAVE_COUNT + " , scaling : " + SCALING_BIAS);
                }
            }
        });

    }
    public static void setup3D(JFrame frame, Canvas canvas) {
        noise3D = new Noise3D();
        frame.setSize(_3D);
        frame.setResizable(false);
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.createBufferStrategy(3);
        final double[] time = {0};
        ScheduledFuture<?> f;
        Runnable iteration = () -> {
            while (time[0] <= endTime) {
                noise3DIteration(canvas, time[0]);
                time[0] += 10;
            }
        };
        f = service.scheduleWithFixedDelay(iteration, 30, 16, TimeUnit.MILLISECONDS);
        Runnable stop = () -> {
            if (time[0] == endTime) {
                f.cancel(true);
                frame2D.dispose();
                frame3D.dispose();
                service.shutdown();
            }
        };
        service.scheduleWithFixedDelay(stop, 30, 16, TimeUnit.MILLISECONDS);

    }
    public static void noise2DIteration(Canvas canvas) {
        BufferStrategy bs = canvas.getBufferStrategy();
        Graphics2D drawGraphics = (Graphics2D) bs.getDrawGraphics();
        for (int i = 0; i < _2D.width; i += 2) {
            for (int k = 0; k < _2D.height; k += 2) {
                int x = (int) (perlinNoise2D(i, k) * 255);
                paintTile(i, k, 2, new Color(x, x, x), drawGraphics);
            }
        }
        bs.show();
    }
    public static synchronized void noise3DIteration(Canvas canvas, double time) {
        frame3D.setTitle("iteration " + time);
        if (MAKE_GIF){
            BufferedImage save = new BufferedImage(_3D.width, _3D.height, BufferedImage.TYPE_INT_RGB);
            WritableRaster raster = save.getRaster();
            writeRaster(raster,time);
            if (GIF_INDEX <= END_GIF) saveImage("src/main/resources/Gifs/Wind" + GIF_INDEX + ".png", save);
            else System.exit(0);
        }else{
            BufferStrategy bs = canvas.getBufferStrategy();
            Graphics2D drawGraphics = (Graphics2D) bs.getDrawGraphics();
            createImage(drawGraphics,time);
            bs.show();
        }
    }

    private static void createImage(Graphics2D g,double time){
        for (int i = 0; i < _3D.width; i++) {
            for (int k = 0; k < _3D.height; k++) {
                int noise = getNoise(noise3D.getValue(i , k , time));
                paintTile(i, k, 1, new Color(noise, noise, noise), g);
            }
        }
    }
    private static void writeRaster(WritableRaster raster,double time){
        for (int i = 0; i < _3D.width; i++) {
            for (int k = 0; k < _3D.height; k++) {
                int noise = getNoise(noise3D.getValue(i * 3, k * 10, time));
                raster.setPixel(i, k, new int[]{noise, noise, noise});
            }
        }
    }


    private static int getNoise(double v) {
        int noise = (int) (255 * (v / 2));
        noise = Math.min(noise, 255);
        noise = Math.max(noise, 0);
        return noise;
    }
    private static void paintTile(int x, int y, int tileSize, Color c, Graphics2D g) {
        g.setColor(c);
        g.fill(new Rectangle(x, y, tileSize, tileSize));
    }
}
