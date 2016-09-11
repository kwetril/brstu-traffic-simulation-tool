package by.brstu.tst.core.simulation.flows;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.elements.BaseRoadElement;
import by.brstu.tst.core.map.elements.EdgeRoadElement;
import by.brstu.tst.core.map.elements.NodeRoadElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class Route {
    List<NodeRoadElement> routeNodes;
    List<EdgeRoadElement> routeEdges;
    HashMap<String, NodeRoadElement> nextNodeMap;
    HashMap<String, EdgeRoadElement> nextEdgeMap;

    /**
     * @param map
     * @param routeElements - list of road elements names,
     *                      it should start from source element name,
     *                      finish with destination element name,
     *                      and names of nodes and edges should turn repeatedly.
     */
    public Route(Map map, String[] routeElements) {
        routeNodes = new ArrayList<>();
        routeEdges = new ArrayList<>();
        nextNodeMap = new HashMap<>();
        nextEdgeMap = new HashMap<>();
        String prevName = null; //no edge before source node
        for (int i = 0; i < routeElements.length; i++) {
            if (i % 2 == 0) {
                NodeRoadElement node = map.getNode(routeElements[i]);
                routeNodes.add(node);
                nextNodeMap.put(prevName, node);
                prevName = node.getName();
            } else {
                EdgeRoadElement edge = map.getEdge(routeElements[i]);
                routeEdges.add(edge);
                nextEdgeMap.put(prevName, edge);
                prevName = edge.getName();
            }
        }
        nextEdgeMap.put(prevName, null); //no edge after destination node
    }

    public NodeRoadElement getSource() {
        return routeNodes.get(0);
    }

    public NodeRoadElement getDestination() {
        return routeNodes.get(routeNodes.size() - 1);
    }

    public NodeRoadElement getNextNode(EdgeRoadElement edge) {
        return nextNodeMap.get(edge.getName());
    }

    public EdgeRoadElement getNextEdge(NodeRoadElement node) {
        return nextEdgeMap.get(node.getName());
    }
}
