package by.brstu.tst.mapeditor;

import java.awt.*;

/**
 * Created by kwetril on 7/12/16.
 */
public class TransformState {
    private int minScalePower;
    private int maxScalePower;
    private int scalePower;
    private double scaleFactor;
    private Point translation;
    private double rotationAngle;

    public TransformState(int minScalePower, int maxScalePower, int initScalePower, double scaleFactor) {
        this(minScalePower, maxScalePower, initScalePower, scaleFactor, new Point(0, 0), 0);
    }

    public TransformState(int minScalePower, int maxScalePower, int initScalePower, double scaleFactor,
                          Point offset, double angle) {
        this.minScalePower = minScalePower;
        this.maxScalePower = maxScalePower;
        this.scalePower = initScalePower;
        this.scaleFactor = scaleFactor;
        this.translation = offset;
        this.rotationAngle = angle;
    }

    public double getScale() {
        return Math.pow(scaleFactor, scalePower);
    }

    public double getScalePower() {
        return scalePower;
    }

    public int getScaleIndex() {
        return scalePower - minScalePower;
    }

    public Point getTranslation() {
        return translation;
    }

    public void updateScalePower(int value) {
        scalePower = Math.min(Math.max(scalePower + value, minScalePower), maxScalePower);
    }

    public void updateTranslation(double dx, double dy) {
        dx = dx / getScale();
        dy = dy / getScale();
        translation.setLocation(translation.getX() + dx, translation.getY() + dy);
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void updateRotationAngle(double angle) {
        rotationAngle += angle;
    }
}
