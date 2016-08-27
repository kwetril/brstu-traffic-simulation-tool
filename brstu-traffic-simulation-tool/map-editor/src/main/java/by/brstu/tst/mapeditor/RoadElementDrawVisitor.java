package by.brstu.tst.mapeditor;

import by.brstu.tst.core.map.elements.*;
import by.brstu.tst.core.map.primitives.MapPoint;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * Created by kwetril on 8/24/16.
 */
public class RoadElementDrawVisitor extends BaseRoadElementVisitor {
    private Graphics2D graphics;
    private BasicStroke roadStroke;

    public RoadElementDrawVisitor(Graphics2D graphics) {
        this.graphics = graphics;
        roadStroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    @Override
    public void visit(NodeRoadElement roadElement) {
        MapPoint point = roadElement.getBasePoint();
        graphics.draw(new Ellipse2D.Float(point.getX() - 4, point.getY() - 4, 8, 8));
    }

    @Override
    public void visit(DirectedRoad roadElement) {
        MapPoint from = roadElement.getStartNode().getBasePoint();
        MapPoint to = roadElement.getEndNode().getBasePoint();
        Line2D line = new Line2D.Float(from.getX(), from.getY(), to.getX(), to.getY());
        Stroke stroke = graphics.getStroke();
        graphics.setStroke(roadStroke);
        graphics.draw(line);
        graphics.setStroke(stroke);
    }
}
