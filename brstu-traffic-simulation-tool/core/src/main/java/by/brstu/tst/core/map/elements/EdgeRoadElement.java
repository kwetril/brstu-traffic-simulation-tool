package by.brstu.tst.core.map.elements;

/**
 * Created by kwetril on 8/16/16.
 */
public abstract class EdgeRoadElement extends BaseRoadElement {
    NodeRoadElement firstNodeElement;
    NodeRoadElement secondNodeElement;

    public EdgeRoadElement(String name, NodeRoadElement firstNodeElement, NodeRoadElement secondNodeElement) {
        super(name);
        this.firstNodeElement = firstNodeElement;
        this.secondNodeElement = secondNodeElement;
    }

    public abstract DirectedRoad getDirectedRoadByStartNode(NodeRoadElement startNode);

    @Override
    public void accept(BaseRoadElementVisitor visitor) {
        visitor.visit(this);
    }
}
