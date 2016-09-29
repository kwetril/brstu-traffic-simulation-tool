package by.brstu.tst.core.map.elements;

import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwetril on 8/17/16.
 */
public class DirectedRoad extends EdgeRoadElement {
    private List<BezierCurve> roadSegments;
    private int numLanes;
    private float laneWidth;

    public DirectedRoad(String name, NodeRoadElement fromNode, NodeRoadElement toNode,
                        int numLanes, float laneWidth) {
        this(name, fromNode, toNode, new ArrayList<>(), numLanes, laneWidth);
    }

    public DirectedRoad(String name, NodeRoadElement fromNode, NodeRoadElement toNode,
                        List<BezierCurve> roadSegments, int numLanes, float laneWidth) {
        super(fromNode, toNode);
        this.name = name;
        fromNode.getOutputElements().add(this);
        toNode.getInputElements().add(this);
        this.roadSegments = roadSegments;
        this.numLanes = numLanes;
        this.laneWidth = laneWidth;
    }

    public NodeRoadElement getStartNode() {
        return firstNodeElement;
    }

    public NodeRoadElement getEndNode() {
        return secondNodeElement;
    }

    public MapPoint getStartPoint() {
        return roadSegments.get(0).getPoints()[0];
    }

    public MapPoint getEndPoint() {
        return roadSegments.get(roadSegments.size() - 1).getPoints()[3];
    }

    public List<BezierCurve> getSegments() {
        return roadSegments;
    }

    @Override
    public DirectedRoad getDirectedRoadByStartNode(NodeRoadElement startNode) {
        if (startNode.getName().equals(this.firstNodeElement.getName())) {
            return this;
        }
        return null;
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

    public RoadLane getLane(int laneNumber) {
        return new RoadLane(this, laneNumber);
    }
}
