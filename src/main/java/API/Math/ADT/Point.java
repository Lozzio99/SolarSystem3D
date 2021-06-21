package API.Math.ADT;

import java.awt.*;


public class Point {
    public static double scale = 1.05;
    public static Dimension screen;
    public static Point ORIGIN;
    public double xOff;
    public double yOff;
    double x, y;

    public Point(double x, double y) {
        this.x = ORIGIN.getX() + (x * scale);
        this.y = ORIGIN.getY() - (y * scale);
    }

    public Point() {
        this.x = screen.width / 2. + xOff;
        this.y = screen.height / 2. - yOff;
    }

    public static void setScreen(Dimension s) {
        screen = s;
    }

    public double getX() {
        double x = (this.x + (xOff));
        return ((x > screen.width ? screen.width : x) < 0 ? 0 : x);
    }

    public double getY() {
        double y = (this.y + (yOff));
        return ((y > screen.width ? screen.width : y) < 0 ? 0 : y);
    }

    @Override
    public String toString() {
        return "{" +
                "drawX=" + x +
                ",drawY=" + y +
                '}';
    }
}

