package by.brstu.tst.mapeditor;

import by.brstu.tst.core.map.elements.*;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by kwetril on 8/24/16.
 */
public class RoadElementDrawVisitor extends BaseRoadElementVisitor {
    private Graphics2D graphics;
    private BasicStroke roadLaneMarkupStroke;
    private BasicStroke roadEdgeStroke;
    private Color roadColor;
    private Color roadMarkupColor;
    private Color nodeElementColor;

    public RoadElementDrawVisitor(Graphics2D graphics) {
        this.graphics = graphics;
        roadLaneMarkupStroke = new BasicStroke(0.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                0.0f, new float[] {1.0f, 2.0f}, 0.0f);
        roadEdgeStroke = new BasicStroke(0.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        roadColor = Color.BLACK;
        roadMarkupColor = Color.WHITE;
        nodeElementColor = Color.BLUE;
    }

    @Override
    public void visit(NodeRoadElement roadElement) {
        MapPoint point = roadElement.getBasePoint();
        List<MapPoint> points = new ArrayList<>();
        roadElement.getInputElements()
                .stream()
                .map(edgeRoadElement -> ((DirectedRoad) edgeRoadElement).getEndPoint())
                .collect(Collectors.toCollection(() -> points));
        roadElement.getOutputElements()
                .stream()
                .map(edgeRoadElement -> ((DirectedRoad) edgeRoadElement).getStartPoint())
                .collect(Collectors.toCollection(() -> points));
        Path2D.Float path = new Path2D.Float();
        path.moveTo(points.get(0).getX(), points.get(0).getY());
        points.stream().skip(1).forEach(pt -> path.lineTo(pt.getX(), pt.getY()));
        path.closePath();
        graphics.setColor(nodeElementColor);
        graphics.fill(path);
        graphics.draw(new Ellipse2D.Float(point.getX() - 4, point.getY() - 4, 8, 8));
    }

    @Override
    public void visit(DirectedRoad roadElement) {
        MapPoint from = roadElement.getStartPoint();
        MapPoint to = roadElement.getEndPoint();
        Line2D line = new Line2D.Float(from.getX(), from.getY(), to.getX(), to.getY());
        int numLanes = roadElement.getNumLanes();
        float laneWidth = roadElement.getLaneWidth();
        BasicStroke roadStroke = new BasicStroke(numLanes * laneWidth,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        graphics.setStroke(roadStroke);
        graphics.setColor(roadColor);
        graphics.draw(line);

        graphics.setColor(roadMarkupColor);
        graphics.setStroke(roadEdgeStroke);
        Vector roadVector = new Vector(from, to);
        roadVector = roadVector.turnLeft().setLength(laneWidth * numLanes / 2.0f);
        MapPoint edgeFrom = roadVector.addToPoint(from);
        MapPoint edgeTo = roadVector.addToPoint(to);
        Line2D markupLine = new Line2D.Float(edgeFrom.getX(), edgeFrom.getY(),
                edgeTo.getX(), edgeTo.getY());
        graphics.draw(markupLine);

        roadVector = roadVector.multiply(-1);
        edgeFrom = roadVector.addToPoint(from);
        edgeTo = roadVector.addToPoint(to);
        markupLine = new Line2D.Float(edgeFrom.getX(), edgeFrom.getY(),
                edgeTo.getX(), edgeTo.getY());
        graphics.draw(markupLine);

        graphics.setStroke(roadLaneMarkupStroke);
        int startLane;
        float deltaLane;
        if (numLanes % 2 == 0) {
            graphics.draw(line);
            startLane = 1;
            deltaLane = 0.0f;
        } else {
            startLane = 0;
            deltaLane = 0.5f;
        }
        for (int lane = startLane; lane < numLanes / 2; lane++) {
            roadVector = roadVector.multiply(-1).setLength((lane + deltaLane) * laneWidth);
            edgeFrom = roadVector.addToPoint(from);
            edgeTo = roadVector.addToPoint(to);
            markupLine = new Line2D.Float(edgeFrom.getX(), edgeFrom.getY(),
                    edgeTo.getX(), edgeTo.getY());
            graphics.draw(markupLine);

            roadVector = roadVector.multiply(-1).setLength((lane + deltaLane) * laneWidth);
            edgeFrom = roadVector.addToPoint(from);
            edgeTo = roadVector.addToPoint(to);
            markupLine = new Line2D.Float(edgeFrom.getX(), edgeFrom.getY(),
                    edgeTo.getX(), edgeTo.getY());
            graphics.draw(markupLine);
        }
    }
}
