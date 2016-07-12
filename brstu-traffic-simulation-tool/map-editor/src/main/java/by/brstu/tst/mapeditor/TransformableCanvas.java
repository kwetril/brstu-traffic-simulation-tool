package by.brstu.tst.mapeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by kwetril on 7/12/16.
 */
public class TransformableCanvas extends JPanel {
    protected TransformState transformState;

    public TransformableCanvas(int minScalePower, int maxScalePower, int initScalePower, double scaleFactor) {
        this.transformState = new TransformState(minScalePower, maxScalePower, initScalePower, scaleFactor);
    }

    public void addMouseScalingTool() {
        addMouseWheelListener(mouseWheelEvent -> {
            transformState.updateScalePower(mouseWheelEvent.getWheelRotation());
            repaint();
        });
    }

    public void addMouseMovingTool() {
        MouseAdapter adapter = new MouseAdapter() {
            private int x;
            private int y;

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                x = mouseEvent.getX();
                y = mouseEvent.getY();
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                int dx = x - mouseEvent.getX();
                int dy = y - mouseEvent.getY();
                transformState.updateTranslation(dx, dy);
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                int dx = mouseEvent.getX() - x;
                int dy = mouseEvent.getY() - y;
                x = mouseEvent.getX();
                y = mouseEvent.getY();
                transformState.updateTranslation(dx, dy);
                repaint();
            }
        };
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        double scale = transformState.getScale();
        g2d.scale(scale, scale);
        Point translation = transformState.getTranslation();
        g2d.translate(translation.getX(), translation.getY());
        g2d.rotate(transformState.getRotationAngle());
    }
}
