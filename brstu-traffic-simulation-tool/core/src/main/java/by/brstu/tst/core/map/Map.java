package by.brstu.tst.core.map;

import by.brstu.tst.core.map.elements.BaseRoadElementVisitor;
import by.brstu.tst.core.map.elements.EdgeRoadElement;
import by.brstu.tst.core.map.elements.NodeRoadElement;

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

    public void visitElements(BaseRoadElementVisitor visitor) {
        for (NodeRoadElement nodes : nodeElements.values()) {
            nodes.accept(visitor);
        }
        for (EdgeRoadElement edge : edgeElements.values()) {
            edge.accept(visitor);
        }
    }

    public void visitNode(String name, BaseRoadElementVisitor visitor) {
        nodeElements.get(name).accept(visitor);
    }

    public void visitEdge(String name, BaseRoadElementVisitor visitor) {
        edgeElements.get(name).accept(visitor);
    }
}
