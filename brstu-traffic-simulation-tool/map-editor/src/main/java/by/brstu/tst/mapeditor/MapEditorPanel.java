package by.brstu.tst.mapeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Created by kwetril on 7/11/16.
 */
public class MapEditorPanel extends JPanel {
    private ShapesContainer container;
    private double scale = 1.0;
    private int startX = 0;
    private int startY = 0;

    public MapEditorPanel() {
        MovingAdapter ma = new MovingAdapter();
        addMouseMotionListener(ma);
        addMouseListener(ma);
        addMouseWheelListener(new ScaleHandler());
    }

    public void showMap(ShapesContainer container) {
        this.container = container;
        repaint();
    }

    @Override
    public void update(Graphics graphics) {
        super.update(graphics);
        System.out.println("Update");
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (container != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            System.out.printf("Scale: %s\n", scale);
            graphics2D.scale(scale, scale);
            graphics2D.translate(startX, startY);

            graphics2D.drawOval((int)container.point1.getX(), (int)container.point1.getY(), 3, 3);

            graphics2D.fill(container.rectangle);
        }
        System.out.println("PaintComponent");
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        System.out.println("Paint");
    }

    class MovingAdapter extends MouseAdapter {

        private int x;

        private int y;

        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            super.mouseReleased(mouseEvent);
            startX -= mouseEvent.getX() - x;
            startY -= mouseEvent.getY() - y;
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            /*
            if (container == null) {
                return;
            }

            int dx = e.getX() - x;
            int dy = e.getY() - y;

            if (container.rectangle.getBounds2D().contains(x, y)) {
                container.rectangle.x += dx;
                container.rectangle.y += dy;
                repaint();
            }
            x += dx;
            y += dy;
            */
        }
    }

    class ScaleHandler implements MouseWheelListener {
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (container == null) {
                return;
            }

            int x = e.getX();
            int y = e.getY();

            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

                if (container.rectangle.getBounds2D().contains(x, y)) {
                    float amount = e.getWheelRotation() * 5f;
                    container.rectangle.width += amount;
                    container.rectangle.height += amount;
                    repaint();
                }
                else {
                    double amount = Math.pow(1.4, e.getWheelRotation());
                    scale *= amount;
                    repaint();
                }
            }
        }
    }
}
