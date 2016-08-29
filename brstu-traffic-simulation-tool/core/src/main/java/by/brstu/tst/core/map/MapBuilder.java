package by.brstu.tst.core.map;

import by.brstu.tst.core.config.Configuration;
import by.brstu.tst.core.map.elements.*;
import by.brstu.tst.core.map.primitives.MapPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kwetril on 8/16/16.
 */
public class MapBuilder {
    private HashMap<String, NodeRoadElement> nodeElements;
    private HashMap<String, EdgeRoadElement> edgeElements;
    private Configuration config;


    public MapBuilder(Configuration config) {
        nodeElements = new HashMap<>();
        edgeElements = new HashMap<>();
        this.config = config;
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

    public MapBuilder addRoad(String name, String fromName, MapPoint fromPoint,
                              String toName, MapPoint toPoint, List<MapPoint> innerPoints,
                              int numLanes) {
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
        if (fromPoint == null) {
            fromPoint = fromNode.getBasePoint();
        }
        if (toPoint == null) {
            toPoint = toNode.getBasePoint();
        }
        DirectedRoad road = new DirectedRoad(name, fromNode, fromPoint,
                toNode, toPoint,
                innerPoints, numLanes, config.roadWidth());
        edgeElements.put(name, road);
        return this;
    }

    public MapBuilder addRoad(String name, String fromName, MapPoint fromPoint,
                              String toName, MapPoint toPoint, int numLanes) {
        return addRoad(name, fromName, fromPoint, toName, toPoint, new ArrayList<>(), numLanes);
    }

    public Map build(String name) {
        return new Map(name, nodeElements, edgeElements);
    }
}
