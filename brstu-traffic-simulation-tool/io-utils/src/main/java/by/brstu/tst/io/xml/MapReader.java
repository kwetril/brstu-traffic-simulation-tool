package by.brstu.tst.io.xml;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.MapBuilder;
import by.brstu.tst.core.map.primitives.MapPoint;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.*;
import java.io.File;
import java.util.List;

/**
 * Created by a.klimovich on 27.09.2016.
 */
public class MapReader {
    public Map readMap(String path) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document mapDom = docBuilder.parse(new File(path));
            MapBuilder mapBuilder = new MapBuilder();

            NodeList nodeList = mapDom.getElementsByTagName("node");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nodeXml = nodeList.item(i);
                processNodeElement(nodeXml, mapBuilder);
            }

            NodeList edgeList = mapDom.getElementsByTagName("edge");
            for (int i = 0; i < edgeList.getLength(); i++) {
                Node edgeXml = edgeList.item(i);
                processEdgeElement(edgeXml, mapBuilder);
            }

            String mapName = XmlUtils.getAttr(mapDom.getElementsByTagName("map")
                            .item(0).getAttributes(), "name");
            return mapBuilder.build(mapName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void processNodeElement(Node nodeXml, MapBuilder mapBuilder) {
        NamedNodeMap attributes = nodeXml.getAttributes();
        String name = XmlUtils.getAttr(attributes, "name");
        String type = XmlUtils.getAttr(attributes, "type");
        Node basePointXml = XmlUtils.filterNodesByTag(nodeXml.getChildNodes(), "basePoint").get(0);
        NamedNodeMap basePointAttrs = basePointXml.getAttributes();
        double x = XmlUtils.getDoubleAttr(basePointAttrs, "x");
        double y = XmlUtils.getDoubleAttr(basePointAttrs, "y");
        MapPoint basePoint = new MapPoint(x, y);
        switch (type) {
            case "source":
                mapBuilder.addSourceElement(name, basePoint);
                break;
            case "destination":
                mapBuilder.addDestinationElement(name,basePoint);
                break;
            case "intersection":
                mapBuilder.addIntersection(name, basePoint);
                break;
            default:
                System.out.printf("Node type not supported: %s\n", type);
        }
    }

    private void processEdgeElement(Node edgeXml, MapBuilder mapBuilder) {
        NamedNodeMap attributes = edgeXml.getAttributes();
        String name = XmlUtils.getAttr(attributes, "name");
        String type = XmlUtils.getAttr(attributes, "type");
        String fromNode = XmlUtils.getAttr(attributes, "fromNode");
        String toNode = XmlUtils.getAttr(attributes, "toNode");
        int numLanes = XmlUtils.getIntAttr(attributes, "lanes");
        float laneWidth = XmlUtils.getFloatAttr(attributes, "laneWidth");
        MapPoint fromConnectionPoint = null;
        MapPoint toConnectionPoint = null;

        List<Node> connectionPoints = XmlUtils.filterNodesByTag(edgeXml.getChildNodes(), "connectionPoint");
        for (Node connectionPoint : connectionPoints) {
            NamedNodeMap connectionPointAttrs = connectionPoint.getAttributes();
            String relatedNode = XmlUtils.getAttr(connectionPointAttrs, "node");
            double x, y;
            x = XmlUtils.getDoubleAttr(connectionPointAttrs, "x");
            y = XmlUtils.getDoubleAttr(connectionPointAttrs, "y");
            if (relatedNode.equals(fromNode)) {
                fromConnectionPoint = new MapPoint(x, y);
            } else if (relatedNode.equals(toNode)) {
                toConnectionPoint = new MapPoint(x, y);
            }
        }
        switch (type) {
            case "directed":
                mapBuilder.addRoad(name, fromNode, fromConnectionPoint,
                        toNode, toConnectionPoint, numLanes, laneWidth);
                break;
            default:
                System.out.printf("Edge type not supported: %s\n", type);
        }
    }
}
