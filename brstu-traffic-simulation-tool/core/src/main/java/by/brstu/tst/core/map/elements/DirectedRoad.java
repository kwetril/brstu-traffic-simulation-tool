package by.brstu.tst.core.map.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwetril on 8/17/16.
 */
public class DirectedRoad extends EdgeRoadElement {
    private NodeRoadElement fromNode;
    private NodeRoadElement toNode;
    private List<MapPoint> innerPoints;

    public DirectedRoad(String name, NodeRoadElement fromNode, NodeRoadElement toNode) {
        this(name, fromNode, toNode, new ArrayList<>());
    }

    public DirectedRoad(String name, NodeRoadElement fromNode, NodeRoadElement toNode, List<MapPoint> innerPoints) {
        this.name = name;
        this.fromNode = fromNode;
        this.toNode = toNode;
        fromNode.getOutputElements().add(this);
        toNode.getInputElements().add(this);
        this.innerPoints = innerPoints;
    }

    public NodeRoadElement getStartNode() {
        return fromNode;
    }

    public NodeRoadElement getEndNode() {
        return toNode;
    }

    @Override
    public void accept(IRoadElementVisitor visitor) {
        visitor.visit(this);
    }
}
