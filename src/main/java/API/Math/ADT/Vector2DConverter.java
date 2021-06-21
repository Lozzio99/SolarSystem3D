package API.Math.ADT;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.Point2D;

import static phase3.Graphics.GraphicsManager.screen;

/**
 * The type Point 3 d converter.
 */
public class Vector2DConverter {
    protected static final double ZoomFactor = 1.05;
    private static final Point2D.Double ORIGIN = new Point2D.Double(screen.width / 2., screen.height / 2.);
    protected static double scale = 1.05d;

    /**
     * Zooms in by a factor.
     */
    public static void zoomIn() {
        scale *= ZoomFactor;
    }

    /**
     * Zooms out by a factor.
     */
    public static void zoomOut() {
        scale /= ZoomFactor;
    }

    /**
     * Returns the scale.
     *
     * @return scale scale
     */
    @Contract(pure = true)
    public static double getScale() {
        return scale;
    }

    /**
     * Converts a point with respect to the scale.
     *
     * @param point3D the point 3 d
     * @return java . awt . point
     */
    @Contract("_ -> new")
    public static Point2D.@NotNull Double convertVector(final @NotNull Vector3dInterface point3D) {
        return new Point2D.Double(ORIGIN.x + (point3D.getX() * (scale)), ORIGIN.y - (point3D.getY() * scale));
    }

    @Contract("_ -> param1")
    public static Point2D.@NotNull Double convertVector(final Point2D.@NotNull Double point3D) {
        point3D.setLocation(ORIGIN.x + (point3D.getX() * scale), ORIGIN.y - (point3D.getY() * scale));
        return point3D;
    }


    public static void translate(final double dx, final double dy) {
        ORIGIN.x += dx;
        ORIGIN.y += dy;
    }


}
