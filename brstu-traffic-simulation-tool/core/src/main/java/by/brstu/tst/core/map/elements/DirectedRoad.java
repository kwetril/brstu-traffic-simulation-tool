package by.brstu.tst.core.map.elements;

import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwetril on 8/17/16.
 */
public class DirectedRoad extends EdgeRoadElement {
    private RoadSegment[] segments;
    private int numLanes;
    private float laneWidth;

    public DirectedRoad(String name, NodeRoadElement fromNode, NodeRoadElement toNode,
                        List<BezierCurve> roadCenterCurvePath, int numLanes, float laneWidth) {
        super(name, fromNode, toNode);
        fromNode.getOutputElements().add(this);
        toNode.getInputElements().add(this);
        this.numLanes = numLanes;
        this.laneWidth = laneWidth;
        segments = new RoadSegment[roadCenterCurvePath.size()];
        for (int i = 0; i < roadCenterCurvePath.size(); i++) {
            segments[i] = new RoadSegment(this, i, roadCenterCurvePath.get(i), numLanes);
        }
    }

    public NodeRoadElement getStartNode() {
        return firstNodeElement;
    }

    public NodeRoadElement getEndNode() {
        return secondNodeElement;
    }

    public MapPoint getStartPoint() {
        return segments[0].getCenterCurve().getPoints()[0];
    }

    public MapPoint getEndPoint() {
        return segments[segments.length - 1].getCenterCurve().getPoints()[3];
    }

    public RoadSegment[] getSegments() {
        return segments;
    }

    @Override
    public DirectedRoad getDirectedRoadByStartNode(NodeRoadElement startNode) {
        if (startNode.getName().equals(this.firstNodeElement.getName())) {
            return this;
        }
        return null;
    }

    @Override
    public DirectedRoad getDirectedRoadByEndNode(NodeRoadElement endNode) {
        try {
            if (endNode.getName().equals(this.secondNodeElement.getName())) {
                return this;
            }
            return null;
        }
        catch (Exception ex) {
            System.out.println(this.getName());
            System.out.println(endNode.getName());
            return null;
        }
    }

    @Override
    public void accept(BaseRoadElementVisitor visitor) {
        visitor.visit(this);
    }

    public int getNumLanes() {
        return numLanes;
    }

    public float getLaneWidth() {
        return laneWidth;
    }
}
