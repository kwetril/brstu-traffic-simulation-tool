package by.brstu.tst.mapeditor;

import by.brstu.tst.core.map.elements.*;
import by.brstu.tst.core.map.primitives.MapPoint;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Created by kwetril on 8/24/16.
 */
public class RoadElementDrawVisitor extends BaseRoadElementVisitor {
    private Graphics2D graphics;

    public RoadElementDrawVisitor(Graphics2D graphics) {
        this.graphics = graphics;
    }

    @Override
    public void visit(NodeRoadElement roadElement) {
        MapPoint point = roadElement.getBasePoint();
        graphics.drawOval((int) point.getLon(), (int) point.getLat(), 3, 3);
    }

    @Override
    public void visit(DirectedRoad roadElement) {
        MapPoint from = roadElement.getStartNode().getBasePoint();
        MapPoint to = roadElement.getEndNode().getBasePoint();
        Line2D line = new Line2D.Float(from.getLon(), from.getLat(), to.getLon(), to.getLat());
        Stroke stroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2));
        graphics.draw(line);
        graphics.setStroke(stroke);
    }
}
