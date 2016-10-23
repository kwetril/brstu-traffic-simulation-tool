package by.brstu.tst.core.simulation.control.autonomous;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by a.klimovich on 22.10.2016.
 *
 * This class should implement algorithm to find clique in graph with maximal weight of vertices.
 *
 * Possible implementation: Deniss Kumlander's article
 */
public class WeightedGraph {
    private HashMap<RoadConnectorDescription, Integer> connectorToId;
    private HashMap<Integer, RoadConnectorDescription> idToConnector;
    boolean[][] graph;
    double[] weights;

    public WeightedGraph(List<WeightedConnectorDescription> connectors,
                         HashMap<RoadConnectorDescription, HashSet<RoadConnectorDescription>> nonConflictConnectors) {
        connectorToId = new HashMap<>();
        idToConnector = new HashMap<>();
        graph = new boolean[connectorToId.size()][connectorToId.size()];
        weights = new double[connectorToId.size()];
        int currentId = 0;
        for (WeightedConnectorDescription weightedConnector : connectors) {
            connectorToId.put(weightedConnector.getConnectorDescription(), currentId);
            idToConnector.put(currentId, weightedConnector.getConnectorDescription());
            weights[currentId] = weightedConnector.getWeight();
            currentId++;
        }
        for (WeightedConnectorDescription weightedConnector : connectors) {
            int id = connectorToId.get(weightedConnector.getConnectorDescription());
            HashSet<RoadConnectorDescription> nonConflict = nonConflictConnectors.get(
                    weightedConnector.getConnectorDescription());
            for (WeightedConnectorDescription anotherConnector : connectors) {
                if (nonConflict.contains(anotherConnector.getConnectorDescription())) {
                    int anotherId = connectorToId.get(anotherConnector.getConnectorDescription());
                    graph[id][anotherId] = true;
                }
            }
        }
    }

    public List<RoadConnectorDescription> getBestConnectors() {
        //current implementation returns only pair of directions
        int first = 0, second = 1;
        double bestWeight = 0;
        for (int i = 0; i < weights.length; i++) {
            for (int j = i + 1; j < weights.length; j++) {
                if (graph[i][j]) {
                    double currentWeight = weights[i] + weights[j];
                    if (currentWeight > bestWeight) {
                        bestWeight = currentWeight;
                        first = i;
                        second = j;
                    }
                }
            }
        }
        List<RoadConnectorDescription> result = new ArrayList<>();
        result.add(idToConnector.get(first));
        result.add(idToConnector.get(second));
        return result;
    }
}
