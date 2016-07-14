package by.brstu.tst.mapeditor;

import java.awt.*;

/**
 * Created by kwetril on 7/11/16.
 */
public class MapEditorPanel extends TransformableCanvas {
    private ShapesContainer container;

    public MapEditorPanel() {
        super(-3, 2, 0, 2.0);
        addMouseScalingTool();
        addMouseMovingTool();
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

            graphics2D.drawOval((int)container.point1.getX(), (int)container.point1.getY(), 3, 3);

            graphics2D.draw(container.line1);
            graphics2D.draw(container.line2);

            graphics2D.fill(container.rectangle);
        }
        System.out.println("PaintComponent");
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        System.out.println("Paint");
    }
}
