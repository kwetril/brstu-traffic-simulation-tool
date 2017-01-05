package by.brstu.tst.ui.utils;

import by.brstu.tst.core.map.elements.BaseRoadElementVisitor;
import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.NodeRoadElement;
import by.brstu.tst.core.map.elements.RoadSegment;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.map.utils.MapUtils;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
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

        if (points.size() > 1) {
            graphics.setStroke(new BasicStroke(3.5f * 3));
            graphics.setColor(roadColor);
            sortPointsAroundBasePoint(points, point);
            Path2D.Float path = new Path2D.Float();
            path.moveTo(points.get(0).getX(), points.get(0).getY());
            points.stream().skip(1).forEach(pt -> path.lineTo(pt.getX(), pt.getY()));
            path.closePath();
            graphics.fill(path);
            graphics.draw(path);
        }
        //graphics.setColor(nodeElementColor);
        //graphics.setStroke(roadEdgeStroke);
        //graphics.draw(ShapeUtils.circleFromPoint(point, 4));
    }

    @Override
    public void visit(DirectedRoad road) {
        for (RoadSegment segment : road.getSegments()) {
            BezierCurve curve = segment.getCenterCurve();
            /*for (MapPoint pt : curve.getPoints()) {
                graphics.setColor(nodeElementColor);
                graphics.setStroke(roadEdgeStroke);
                graphics.draw(ShapeUtils.circleFromPoint(pt, 2));
            }*/

            int numLanes = road.getNumLanes();
            float laneWidth = road.getLaneWidth();
            BasicStroke roadStroke = new BasicStroke(numLanes * laneWidth,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            graphics.setStroke(roadStroke);
            graphics.setColor(roadColor);

            Path2D.Float path = new Path2D.Float();
            ShapeUtils.updatePathWithBezierCurve(path, curve);
            graphics.draw(path);

            graphics.setColor(roadMarkupColor);
            graphics.setStroke(roadEdgeStroke);
            path = new Path2D.Float();
            BezierCurve tmp = MapUtils.moveCurveLeft(curve, laneWidth * numLanes / 2.0f);
            ShapeUtils.updatePathWithBezierCurve(path, tmp);
            graphics.draw(path);

            path = new Path2D.Float();
            tmp = MapUtils.moveCurveRight(curve, laneWidth * numLanes / 2.0f);
            ShapeUtils.updatePathWithBezierCurve(path, tmp);
            graphics.draw(path);

            graphics.setStroke(roadLaneMarkupStroke);
            float deltaLane;
            if (numLanes % 2 == 0) {
                path = new Path2D.Float();
                ShapeUtils.updatePathWithBezierCurve(path, curve);
                deltaLane = 0.0f;
            } else {
                deltaLane = 0.5f;
            }
            for (int i = 0; i < numLanes / 2; i++) {
                path = new Path2D.Float();
                tmp = MapUtils.moveCurveLeft(curve, (i + deltaLane) * laneWidth);
                ShapeUtils.updatePathWithBezierCurve(path, tmp);
                graphics.draw(path);

                if (i > 0 || deltaLane > 0) {
                    path = new Path2D.Float();
                    tmp = MapUtils.moveCurveRight(curve, (i + deltaLane) * laneWidth);
                    ShapeUtils.updatePathWithBezierCurve(path, tmp);
                    graphics.draw(path);
                }
            }
        }
    }

    private void sortPointsAroundBasePoint(List<MapPoint> points, MapPoint basePoint) {
        Vector baseVector = new Vector(basePoint, new MapPoint(0, 0));
        points.sort((pt1, pt2) -> {
            Vector firstVector = new Vector(basePoint, pt1);
            Vector secondVector = new Vector(basePoint, pt2);
            double firstAngle = baseVector.angleClockwise(firstVector);
            double secondAngle = baseVector.angleClockwise(secondVector);
            if (firstAngle < secondAngle) {
                return -1;
            } else if (firstAngle > secondAngle) {
                return 1;
            }
            return 0;
        });
    }
}
