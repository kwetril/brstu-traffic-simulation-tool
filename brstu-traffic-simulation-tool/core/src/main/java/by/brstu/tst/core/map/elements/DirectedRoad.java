package by.brstu.tst.core.map.elements;

import by.brstu.tst.core.map.primitives.MapPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwetril on 8/17/16.
 */
public class DirectedRoad extends EdgeRoadElement {
    private NodeRoadElement fromNode;
    private NodeRoadElement toNode;
    private List<MapPoint> innerPoints;
    private int numLanes;
    private float laneWidth;

    public DirectedRoad(String name, NodeRoadElement fromNode, NodeRoadElement toNode,
                        int numLanes, float laneWidth) {
        this(name, fromNode, toNode, new ArrayList<>(), numLanes, laneWidth);
    }

    public DirectedRoad(String name, NodeRoadElement fromNode, NodeRoadElement toNode,
                        List<MapPoint> innerPoints, int numLanes, float laneWidth) {
        this.name = name;
        this.fromNode = fromNode;
        this.toNode = toNode;
        fromNode.getOutputElements().add(this);
        toNode.getInputElements().add(this);
        this.innerPoints = innerPoints;
        this.numLanes = numLanes;
        this.laneWidth = laneWidth;
    }

    public NodeRoadElement getStartNode() {
        return fromNode;
    }

    public NodeRoadElement getEndNode() {
        return toNode;
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
