package phase3.Graphics.Scenes;

import API.Math.ADT.Vector2DConverter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static API.Config.*;

public class ModuleShape {
    private static final double ellipseWidth = 50;
    private static double prevTheta = 0;
    private static BufferedImage image;

    @Contract("_ -> new")
    public static @NotNull Shape createShape(final Point2D.@NotNull Double state) {
        //rescale to given position
        double r = ellipseWidth * Vector2DConverter.getScale();
        return new Ellipse2D.Double(state.x - (r / 2), state.y - (r / 2), r, r);
    }

    public static BufferedImage load() {
        try {
            File file = new File(Objects.requireNonNull(ModuleShape.class.getClassLoader().getResource("Icons/rocket.png")).getFile());
            image = ImageIO.read(file);
        } catch (IOException e) {
            try {
                URL resource = new URL("https://thumbnail.imgbin.com/2/20/23/imgbin-apollo-11-apollo-lunar-module-apollo-16-apollo-program-moon-moon-landing-pgu5fLwTgH3eyKaS17WweFtet_t.jpg");
                image = ImageIO.read(resource);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        }
        return image;
    }

    public static BufferedImage createImage(double theta) {
        if (theta == prevTheta) return image;
        if (CONTROLLER == OPEN) theta*=(180.0/Math.PI);
        prevTheta = theta;
        final double rads = Math.toRadians(theta);
        final double sin = Math.abs(Math.sin(rads));
        final double cos = Math.abs(Math.cos(rads));
        final int w = (int) Math.floor(image.getWidth() * cos + image.getHeight() * sin);
        final int h = (int) Math.floor(image.getHeight() * cos + image.getWidth() * sin);
        final BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        final AffineTransform at = new AffineTransform();
        at.translate(w / 2., h / 2.);
        at.rotate(rads, 0, 0);
        at.translate(-image.getWidth() / 2., -image.getHeight() / 2.);
        final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        rotateOp.filter(image, rotatedImage);
        return rotatedImage;
    }

}
