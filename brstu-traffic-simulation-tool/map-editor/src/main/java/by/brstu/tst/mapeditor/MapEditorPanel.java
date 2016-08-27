package by.brstu.tst.mapeditor;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.elements.BaseRoadElementVisitor;
import by.brstu.tst.core.map.primitives.MapRectangle;

import java.awt.*;

/**
 * Created by kwetril on 7/11/16.
 */
public class MapEditorPanel extends TransformableCanvas {
    private Map map;
    private MapEditorFrame parentFrame;

    public MapEditorPanel(MapEditorFrame parentFrame) {
        super(-3, 4, 0, 2.0);
        this.parentFrame = parentFrame;
        addMouseScalingTool();
        addMouseMovingTool();
    }

    public void showMap(Map map) {
        this.map = map;
        FindMapBoundsVisitor mapBoundsVisitor = new FindMapBoundsVisitor();
        map.visitElements(mapBoundsVisitor);
        MapRectangle bounds = mapBoundsVisitor.getMapBounds();
        showBounds(bounds.getMinX(), bounds.getMinY(),
                bounds.getMaxX(), bounds.getMaxY());
    }

    @Override
    public void update(Graphics graphics) {
        super.update(graphics);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (map != null) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            BaseRoadElementVisitor mapDrawingVisitor = new RoadElementDrawVisitor(graphics2D);
            map.visitElements(mapDrawingVisitor);
        }
        parentFrame.setStatus(String.format("Scale: %s; Scale power: %s; Translate X: %s; Translate Y: %s; (W, H)=%s,%s",
                transformState.getScale(), transformState.getScalePower(),
                transformState.getTranslate(), transformState.getTranslateY(),
                getWidth(), getHeight()));
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
    }
}
