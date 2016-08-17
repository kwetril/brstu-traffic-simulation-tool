package by.brstu.tst.core.by.brstu.tst.core.map.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kwetril on 8/16/16.
 */
public class MapBuilder {
    HashMap<String, NodeRoadElement> nodeElements;
    HashMap<String, EdgeRoadElement> edgeElements;
    HashMap<String, List<String>> connections;

    public MapBuilder() {
        nodeElements = new HashMap<>();
        edgeElements = new HashMap<>();
    }

    public MapBuilder addSourceElement(String name) {
        addNodeElement(name, new SourceElement(name));
        return this;
    }

    public MapBuilder addIntersection(String name) {
        addNodeElement(name, new Intersection(name));
        return this;
    }

    public MapBuilder addDestinationElement(String name) {
        addNodeElement(name, new DestinationElement(name));
        return this;
    }

    private boolean checkNameUniquness(String name) {
        return  (nodeElements.containsKey(name) || edgeElements.containsKey(name));
    }

    private void addNodeElement(String name, NodeRoadElement element) {
        if (!checkNameUniquness(name)) {
            throw new RuntimeException("Name of the road element should be unique");
        }
        nodeElements.put(name, element);
    }

    public MapBuilder addRoad(String name, String fromName, String toName) {
        if (!checkNameUniquness(name)) {
            throw new RuntimeException("Name of the road element should be unique");
        }
        if (fromName.equals(toName)) {
            throw new RuntimeException("Road ends should be different");
        }
        if (!nodeElements.containsKey(fromName)) {
            throw new RuntimeException(String.format("Element with name %s was not found", fromName));
        }
        if (!nodeElements.containsKey(toName)) {
            throw new RuntimeException(String.format("Element with name %s was not found", toName));
        }
        NodeRoadElement fromNode = nodeElements.get(fromName);
        NodeRoadElement toNode = nodeElements.get(toName);
        DirectedRoad road = new DirectedRoad(name, fromNode, toNode);
        edgeElements.put(name, road);
        if (connections.containsKey(fromName)) {
            connections.get(fromName).add(toName);
        } else {
            List<String> nodeConectinons = new ArrayList<>();
            nodeConectinons.add(toName);
            connections.put(fromName, nodeConectinons);
        }
        return this;
    }

    public Map build(String name) {
        return new Map(name, nodeElements, edgeElements, connections);
    }
}
