package by.brstu.tst.core.by.brstu.tst.core.map.elements;

/**
 * Created by kwetril on 8/17/16.
 */
public class DirectedRoad extends EdgeRoadElement {
    private NodeRoadElement fromNode;
    private NodeRoadElement toNode;

    public DirectedRoad(String name, NodeRoadElement fromNode, NodeRoadElement toNode) {
        this.name = name;
        this.fromNode = fromNode;
        this.toNode = toNode;
    }
}
