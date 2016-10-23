package by.brstu.tst.core.map.elements;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class BidirectedRoad extends EdgeRoadElement {

    public BidirectedRoad(String name, NodeRoadElement firstNodeElement, NodeRoadElement secondNodeElement) {
        super(name, firstNodeElement, secondNodeElement);
    }

    @Override
    public DirectedRoad getDirectedRoadByStartNode(NodeRoadElement startNode) {
        /**
         * TODO not implemented yet
         */
        return null;
    }

    @Override
    public DirectedRoad getDirectedRoadByEndNode(NodeRoadElement startNode) {
        /**
         * TODO not implemented yet
         */
        return null;
    }
}
