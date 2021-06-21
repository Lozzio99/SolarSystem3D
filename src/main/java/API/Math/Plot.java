package API.Math;

import API.Math.ADT.Point;
import API.Math.Functions.Function;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.StrictMath.abs;
import static java.util.stream.IntStream.range;
import static API.Math.ADT.Point.ORIGIN;

public class Plot extends Canvas {
    private final static Dimension screen = new Dimension(500, 500);
    private final JFrame frame;
    private final boolean drawAxis = true;
    private String title;
    private WindowEvent listen;
    private Point[] p1, p2, p3;
    private double[] f1, f2, f3;
    private Line2D.Double x, y;
    private boolean calculated;
    private boolean plot1, plot2, plot3;
    private String label1, label2, label3;


    public Plot() {
        this.frame = new JFrame();
        this.frame.setSize(screen);
        plot1 = plot2 = plot3 = calculated = false;
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                listen = new WindowEvent(frame, 201);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(listen);
                System.exit(0);
            }
        });
        ImageIcon img = new ImageIcon(
                Objects.requireNonNull(this.getClass().getClassLoader()
                        .getResource("Icons/death.png")).getFile());
        this.frame.setIconImage(img.getImage());
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.add(this);
        this.title = "";
        this.frame.setTitle(this.title);
        this.frame.setVisible(true);
        Point.setScreen(screen);
        ORIGIN = new Point();
        if (drawAxis) this.axis();
        this.start();
    }

    public Plot(String title) {
        this();
        this.frame.setTitle(this.title = title);
    }

    /**
     * sorts the arrays by reference
     *
     * @param xs the arrays which will give the sorting order
     * @param ys the xs index - related values
     */
    public static void sortingForDrawing(double[] xs, double[] ys) {
        int n = xs.length;
        for (int j = 1; j < n; j++) {
            double key = xs[j], y_key = ys[j];
            int i = j - 1;
            while ((i > -1) && (xs[i] > key)) {
                xs[i + 1] = xs[i];
                ys[i + 1] = ys[i];
                i--;
            }
            xs[i + 1] = key;
            ys[i + 1] = y_key;
        }
    }

    public static void sort(double[] xs) {
        int n = xs.length;
        for (int j = 1; j < n; j++) {
            double key = xs[j];
            int i = j - 1;
            while ((i > -1) && (xs[i] > key)) {
                xs[i + 1] = xs[i];
                i--;
            }
            xs[i + 1] = key;
        }
    }

    @Contract(pure = true)
    public static double @NotNull [] linX(double a, double b, int n) {
        double[] xs = new double[n];
        double h = (b - a) / n;
        for (int i = 0; i < n; i++) {
            xs[i] = a += h;
        }
        return xs;
    }

    @Contract(pure = true)
    public static double @NotNull [] linXSorted(double @NotNull [] sortedX, int n) {
        return linX(sortedX[0], sortedX[sortedX.length - 1], n);
    }

    public Plot plot(double[] xs, double[] ys, int PLOT, String label) {
        switch (PLOT) {
            case 1 -> {
                label1 = label;
                return this.plot(xs, ys);
            }
            case 2 -> {
                label2 = label;
                return this.plot2(xs, ys);
            }
            case 3 -> {
                label3 = label;
                return this.plot3(xs, ys);
            }
            default -> throw new IllegalArgumentException();
        }
    }

    public Plot plot(double[] xs, double[] ys) {
        if (plot1) throw new IllegalStateException("Plot id not available");
        if (xs.length != ys.length) throw new IllegalArgumentException("drawX and drawY must have the same size");
        if (xs.length > 1000) throw new UnsupportedOperationException("need to implement multiple points arrays");
        calculated = false;
        double[] x, y;
        x = Arrays.copyOf(xs, xs.length);
        y = Arrays.copyOf(ys, ys.length);
        sortingForDrawing(x, y);
        this.p1 = new Point[xs.length];
        for (int i = 0; i < this.p1.length; i++) {
            this.p1[i] = new Point(x[i], y[i]);
        }
        this.f1 = y;
        plot1 = true;
        calculated = true;
        return this.start();
    }

    public Plot plot2(double[] xs, double[] ys) {
        if (plot2) throw new IllegalStateException("Plot id not available");
        calculated = false;
        plot2 = true;
        double[] x, y;
        x = Arrays.copyOf(xs, xs.length);
        y = Arrays.copyOf(ys, ys.length);
        sortingForDrawing(x, y);
        this.p2 = new Point[xs.length];
        for (int i = 0; i < this.p2.length; i++) {
            this.p2[i] = new Point(x[i], y[i]);
        }
        calculated = true;
        return this.start();
    }

    public Plot plot3(double[] xs, double[] ys) {
        calculated = false;
        if (plot3) throw new IllegalStateException("Plot id not available");
        plot3 = true;
        double[] x, y;
        x = Arrays.copyOf(xs, xs.length);
        y = Arrays.copyOf(ys, ys.length);
        sortingForDrawing(x, y);
        this.p3 = new Point[xs.length];
        for (int i = 0; i < this.p3.length; i++) {
            this.p3[i] = new Point(x[i], y[i]);
        }
        calculated = true;
        return this.start();
    }

    public Plot plotLinF(Function<Double, Double> f, double[] xs, int PLOT) {
        double min = Arrays.stream(xs).min().orElse(-100);
        double max = Arrays.stream(xs).max().orElse(100);
        double[] x = linX(min, max, (int) (abs(max) + abs(min)));
        double[] y = new double[x.length];
        for (int i = 0; i < x.length; i++)
            y[i] = f.apply(x[i]);
        switch (PLOT) {
            case 1 -> {
                f1 = y;
                if (!plot1) return this.plot(x, y);
            }
            case 2 -> {
                f2 = y;
                if (!plot2) return this.plot2(x, y);
            }
            case 3 -> {
                f3 = y;
                if (!plot3) return this.plot3(x, y);
            }
            default -> throw new IllegalArgumentException("PLOT NOT AVAILABLE" + PLOT);
        }
        throw new IllegalStateException("PLOT not valid or unavailable");
    }

    public double[] getEvaluation(int PLOT) {
        switch (PLOT) {
            case 1 -> {
                return f1 == null ? new double[0] : f1;
            }
            case 2 -> {
                return f2 == null ? new double[0] : f2;
            }
            case 3 -> {
                return f3 == null ? new double[0] : f3;
            }
            default -> throw new IllegalArgumentException("PLOT not valid or not evaluated");
        }
    }

    private void axis() {
        Point px = new Point(-400, 0), py = new Point(0, -400), px1 = new Point(400, 0), py1 = new Point(0, 400);
        this.x = new Line2D.Double(px.getX(), px.getY(), px1.getX(), px1.getY());
        this.y = new Line2D.Double(py.getX(), py.getY(), py1.getX(), py1.getY());
    }

    @Contract(" -> this")
    private Plot start() {
        Timer t = new Timer(100, (e) -> render());
        t.start();
        return this;
    }

    public Plot translate(double x, double y) {
        ORIGIN.xOff += x;
        ORIGIN.yOff += y;
        if (drawAxis) this.axis();
        return this;
    }

    public Plot scale(double scale) {
        Point.scale += scale;
        return this;
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics graphics = bs.getDrawGraphics();
        Graphics2D g = (Graphics2D) graphics;
        g.setColor(new Color(76, 76, 76, 190));
        g.fill3DRect(0, 0, screen.width, screen.height, true);
        draw(g);
        g.dispose();
        bs.show();
    }

    private void draw(Graphics2D g) {
        if (drawAxis) {
            g.setColor(new Color(255, 57, 57, 255));
            g.draw(this.x);
            g.draw(this.y);
        }
        if (!calculated) return;
        g.setStroke(new BasicStroke(0.5f));
        g.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 10));
        if (plot1) {
            g.setColor(new Color(92, 156, 255, 255));
            g.drawString(label1 == null ? "plot1" : label1, 10, 13);
            range(0, this.p1.length - 1)
                    //.filter(f -> p1[f] != null && p1[f + 1] != null)
                    .mapToObj(i -> new Line2D.Double(
                            p1[i + 1].getX(), p1[i + 1].getY(),
                            p1[i].getX(), p1[i].getY()))
                    .forEach(g::draw);
        }
        if (plot2) {
            g.setColor(new Color(85, 255, 79, 255));
            g.drawString(label2 == null ? "plot2" : label2, 10, 25);
            range(0, this.p2.length - 1)
                    //.filter(i -> p2[i + 1] != null && p2[i] != null)
                    .mapToObj(i -> new Line2D.Double(
                            p2[i + 1].getX(), p2[i + 1].getY(),
                            p2[i].getX(), p2[i].getY()))
                    .forEach(g::draw);
        }
        if (plot3) {
            g.setColor(new Color(255, 178, 80, 255));
            g.drawString(label3 == null ? "plot3" : label3, 10, 37);
            range(0, this.p3.length - 1)
                    //.filter(i -> p3[i + 1] != null && p3[i] != null)
                    .mapToObj(i -> new Line2D.Double(
                            p3[i + 1].getX(), p3[i + 1].getY(),
                            p3[i].getX(), p3[i].getY()))
                    .forEach(g::draw);
        }
    }

    public Plot fit(Function<Double, Double> l, double[] xs, double[] ys, int n, int plot) {
        double[] x1 = Arrays.copyOf(xs, xs.length);
        sort(x1);
        double[] x2 = linXSorted(x1, n), y2;
        y2 = new double[n];
        Arrays.setAll(y2, i -> l.apply(x1[i]));
        switch (plot) {
            case 1 -> {
                f1 = y2;
                if (!plot1) return plot(x2, y2);
            }
            case 2 -> {
                f2 = y2;
                if (!plot2) return plot2(x2, y2);
            }
            case 3 -> {
                f3 = y2;
                if (!plot3) return plot3(x2, y2);
            }
            default -> throw new IllegalArgumentException("PLOT not valid or unavailable");
        }
        throw new IllegalStateException("Plot not valid or unavailable");
    }
}
