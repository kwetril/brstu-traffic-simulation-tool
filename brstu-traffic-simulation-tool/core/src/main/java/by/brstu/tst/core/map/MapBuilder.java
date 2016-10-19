package by.brstu.tst.core.map;

import by.brstu.tst.core.map.elements.*;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kwetril on 8/16/16.
 */
public class MapBuilder {
    private HashMap<String, NodeRoadElement> nodeElements;
    private HashMap<String, EdgeRoadElement> edgeElements;

    public MapBuilder() {
        nodeElements = new HashMap<>();
        edgeElements = new HashMap<>();
    }

    public MapBuilder addSourceElement(String name, MapPoint basePoint) {
        addNodeElement(name, new SourceElement(name, basePoint));
        return this;
    }

    public MapBuilder addIntersection(String name, MapPoint basePoint) {
        addNodeElement(name, new Intersection(name, basePoint));
        return this;
    }

    public MapBuilder addDestinationElement(String name, MapPoint basePoint) {
        addNodeElement(name, new DestinationElement(name, basePoint));
        return this;
    }

    private boolean checkNameUniquness(String name) {
        return  !(nodeElements.containsKey(name) || edgeElements.containsKey(name));
    }

    private void addNodeElement(String name, NodeRoadElement element) {
        if (!checkNameUniquness(name)) {
            throw new RuntimeException("Name of the road element should be unique");
        }
        nodeElements.put(name, element);
    }

    public MapBuilder addRoad(String name, String fromName, String toName,
                              List<BezierCurve> roadSegments, int numLanes, float laneWidth) {
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
        DirectedRoad road = new DirectedRoad(name, fromNode, toNode, roadSegments, numLanes, laneWidth);
        edgeElements.put(name, road);
        return this;
    }

    public Map build(String name) {
        return new Map(name, nodeElements, edgeElements);
    }
}
