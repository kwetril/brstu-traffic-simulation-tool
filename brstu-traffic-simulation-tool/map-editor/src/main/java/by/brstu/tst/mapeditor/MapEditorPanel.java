package by.brstu.tst.mapeditor;

import by.brstu.tst.core.map.elements.Map;
import by.brstu.tst.core.map.elements.IRoadElementVisitor;

import java.awt.*;

/**
 * Created by kwetril on 7/11/16.
 */
public class MapEditorPanel extends TransformableCanvas {
    private Map map;

    public MapEditorPanel() {
        super(-3, 2, 0, 2.0);
        addMouseScalingTool();
        addMouseMovingTool();
    }

    public void showMap(Map map) {
        this.map = map;
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
        if (map != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            IRoadElementVisitor mapDrawingVisitor = new RoadElementDrawVisitor(graphics2D);
            map.visitElements(mapDrawingVisitor);
        }
        System.out.println("PaintComponent");
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        System.out.println("Paint");
    }
}
