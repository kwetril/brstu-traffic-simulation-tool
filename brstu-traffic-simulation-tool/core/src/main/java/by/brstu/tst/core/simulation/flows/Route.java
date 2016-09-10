package by.brstu.tst.core.simulation.flows;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.elements.BaseRoadElement;
import by.brstu.tst.core.map.elements.EdgeRoadElement;
import by.brstu.tst.core.map.elements.NodeRoadElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class Route {
    List<NodeRoadElement> routeNodes;
    List<EdgeRoadElement> routeEdges;

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
        for (int i = 0; i < routeElements.length; i++) {
            if (i % 2 == 0) {
                routeNodes.add(map.getNode(routeElements[i]));
            } else {
                routeEdges.add(map.getEdge(routeElements[i]));
            }
        }
    }

    public NodeRoadElement getSource() {
        return routeNodes.get(0);
    }

    public NodeRoadElement getDestination() {
        return routeNodes.get(routeNodes.size() - 1);
    }
}
