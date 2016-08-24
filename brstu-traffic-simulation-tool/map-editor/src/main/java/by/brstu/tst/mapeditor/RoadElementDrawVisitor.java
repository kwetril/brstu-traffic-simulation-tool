package by.brstu.tst.mapeditor;

import by.brstu.tst.core.map.elements.*;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Created by kwetril on 8/24/16.
 */
public class RoadElementDrawVisitor implements IRoadElementVisitor {
    private Graphics2D graphics;

    public RoadElementDrawVisitor(Graphics2D graphics) {
        this.graphics = graphics;
    }

    @Override
    public void visit(BaseRoadElement roadElement) {
        System.out.println("BaseRoadElement " + roadElement.getName());
    }

    @Override
    public void visit(NodeRoadElement roadElement) {
        System.out.println("NodeRoadElement " + roadElement.getName());
        MapPoint point = roadElement.getBasePoint();
        graphics.drawOval((int) point.getLon(), (int) point.getLat(), 3, 3);
    }

    @Override
    public void visit(EdgeRoadElement roadElement) {
        System.out.println("EdgeRoadElement " + roadElement.getName());
    }

    @Override
    public void visit(SourceElement roadElement) {
        System.out.println("SourceRoadElement " + roadElement.getName());
        visit((NodeRoadElement) roadElement);

    }

    @Override
    public void visit(DestinationElement roadElement) {
        System.out.println("DestinationRoadElement " + roadElement.getName());
        visit((NodeRoadElement) roadElement);
    }

    @Override
    public void visit(Intersection roadElement) {
        System.out.println("IntersectionRoadElement " + roadElement.getName());
        visit((NodeRoadElement) roadElement);
    }

    @Override
    public void visit(DirectedRoad roadElement) {
        System.out.println("DirectedRoad " + roadElement.getName());
        MapPoint from = roadElement.getStartNode().getBasePoint();
        MapPoint to = roadElement.getEndNode().getBasePoint();
        Line2D line = new Line2D.Float(from.getLon(), from.getLat(), to.getLon(), to.getLat());
        Stroke stroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(2));
        graphics.draw(line);
        graphics.setStroke(stroke);
    }
}
