package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import by.brstu.tst.core.map.utils.RoadConnectorDescription;
import by.brstu.tst.core.simulation.control.autonomous.WeightedConnectorDescription;

import java.util.*;

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
        graph = new boolean[connectors.size()][connectors.size()];
        weights = new double[connectors.size()];
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
        if (connectorToId.size() == 0) {
            return Collections.EMPTY_LIST;
        } else if (connectorToId.size() == 1) {
            return new ArrayList<>(connectorToId.get(0));
        }
        //current implementation returns only pair of directions
        boolean answerFound = false;
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
                        answerFound = true;
                    }
                }
            }
        }
        List<RoadConnectorDescription> result = new ArrayList<>();
        if (answerFound) {
            result.add(idToConnector.get(first));
            result.add(idToConnector.get(second));
        }
        return result;
    }
}
