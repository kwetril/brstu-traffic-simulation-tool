package by.brstu.tst.core.map.elements;

import by.brstu.tst.core.map.primitives.MapPoint;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwetril on 8/17/16.
 */
public class DirectedRoad extends EdgeRoadElement {
    private MapPoint startPoint;
    private MapPoint endPoint;
    private List<MapPoint> innerPoints;
    private int numLanes;
    private float laneWidth;

    public DirectedRoad(String name, NodeRoadElement fromNode, MapPoint startPoint,
                        NodeRoadElement toNode, MapPoint endPoint,
                        int numLanes, float laneWidth) {
        this(name, fromNode, startPoint, toNode, endPoint, new ArrayList<>(), numLanes, laneWidth);
    }

    public DirectedRoad(String name, NodeRoadElement fromNode, MapPoint startPoint,
                        NodeRoadElement toNode, MapPoint endPoint,
                        List<MapPoint> innerPoints, int numLanes, float laneWidth) {
        super(fromNode, toNode);
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        fromNode.getOutputElements().add(this);
        toNode.getInputElements().add(this);
        this.innerPoints = innerPoints;
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
        return startPoint;
    }

    public MapPoint getEndPoint() {
        return endPoint;
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
