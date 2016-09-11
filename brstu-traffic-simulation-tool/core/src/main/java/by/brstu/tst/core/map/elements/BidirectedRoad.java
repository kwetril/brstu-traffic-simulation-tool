package by.brstu.tst.core.map.elements;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class BidirectedRoad extends EdgeRoadElement {

    public BidirectedRoad(NodeRoadElement firstNodeElement, NodeRoadElement secondNodeElement) {
        super(firstNodeElement, secondNodeElement);
    }

    @Override
    public DirectedRoad getDirectedRoadByStartNode(NodeRoadElement startNode) {
        /**
         * TODO not implemented yet
         */
        return null;
    }
}
