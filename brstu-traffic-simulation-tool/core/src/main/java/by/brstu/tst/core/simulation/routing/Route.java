package by.brstu.tst.core.simulation.routing;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.NodeRoadElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class Route {
    List<NodeRoadElement> nodes;
    List<DirectedRoad> roads;
    HashMap<String, NodeRoadElement> nextNodeMap;
    HashMap<String, DirectedRoad> nextRoadMap;

    /**
     * @param map
     * @param routeElements - list of road elements names,
     *                      it should start from source element name,
     *                      finish with destination element name,
     *                      and names of nodes and edges should turn repeatedly.
     */
    public Route(Map map, String[] routeElements) {
        nodes = new ArrayList<>();
        roads = new ArrayList<>();
        nextNodeMap = new HashMap<>();
        nextRoadMap = new HashMap<>();
        String prevName = null; //no edge before source node
        NodeRoadElement node = null;
        for (int i = 0; i < routeElements.length; i++) {
            if (i % 2 == 0) {
                node = map.getNode(routeElements[i]);
                nodes.add(node);
                nextNodeMap.put(prevName, node);
                prevName = node.getName();
            } else {
                DirectedRoad edge = map.getEdge(routeElements[i]).getDirectedRoadByStartNode(node);
                roads.add(edge);
                nextRoadMap.put(prevName, edge);
                prevName = edge.getName();
            }
        }
        nextRoadMap.put(prevName, null); //no edge after destination node
    }

    public NodeRoadElement getSource() {
        return nodes.get(0);
    }

    public NodeRoadElement getDestination() {
        return nodes.get(nodes.size() - 1);
    }

    public NodeRoadElement getNextNode(DirectedRoad edge) {
        return nextNodeMap.get(edge.getName());
    }

    public DirectedRoad getNextRoad(NodeRoadElement node) {
        return nextRoadMap.get(node.getName());
    }
}
