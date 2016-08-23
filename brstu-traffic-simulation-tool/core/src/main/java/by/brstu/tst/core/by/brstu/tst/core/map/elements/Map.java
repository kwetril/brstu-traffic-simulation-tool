package by.brstu.tst.core.by.brstu.tst.core.map.elements;

import java.util.HashMap;

/**
 * Created by kwetril on 7/7/16.
 */
public class Map {
    private String name;
    private HashMap<String, NodeRoadElement> nodeElements;
    private HashMap<String, EdgeRoadElement> edgeElements;

    public Map(String name, HashMap<String, NodeRoadElement> nodeElements,
               HashMap<String, EdgeRoadElement> edgeElements) {
        this.name = name;
        this.nodeElements = nodeElements;
        this.edgeElements = edgeElements;
    }
}
