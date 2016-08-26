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
            transformState.updateScalePower(mouseWheelEvent.getWheelRotation(),
                    mouseWheelEvent.getX(), mouseWheelEvent.getY());
            repaint();
        });
    }

    public void addMouseMovingTool() {
        MouseAdapter adapter = new MouseAdapter() {
            private int x;
            private int y;

            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.printf("Mouse x: %s; y: %s\n", e.getX(), e.getY());
            }

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
        g2d.transform(transformState.getTransform());
    }

    protected void showBounds(float x0, float y0, float x1, float y1) {
        double lenX = x1 - x0;
        double lenY = y1 - y0;
        double preferedScaleX = getWidth() / lenX;
        double preferedScaleY = getHeight() / lenY;
        double preferedScale = Math.min(preferedScaleX, preferedScaleY);
        int preferedScalePower = (int) (Math.log(preferedScale) / Math.log(transformState.getScaleFactor()));
        transformState.setScalePower(preferedScalePower);

        double boundsCenterX = (x0 + x1) / 2 * transformState.getScale();
        double boundsCenterY = (y0 + y1) / 2 * transformState.getScale();
        double windowCenterX = getWidth() / 2;
        double windowCenterY = getHeight() / 2;
        transformState.setTranslation(windowCenterX - boundsCenterX, windowCenterY - boundsCenterY);

        repaint();
    }
}
