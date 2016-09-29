package by.brstu.tst.io.xml;

import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.MapBuilder;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;
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

        List<Node> segmentsXml = XmlUtils.filterNodesByTag(
                XmlUtils.filterNodesByTag(edgeXml.getChildNodes(), "segments").get(0).getChildNodes(), "curve");
        List<BezierCurve> segments = new ArrayList<>();
        for (int i = 0; i < segmentsXml.size(); i++) {
            Node segmentXml = segmentsXml.get(i);
            List<Node> pointsXml = XmlUtils.filterNodesByTag(segmentXml.getChildNodes(), "point");
            NamedNodeMap point1attrs = pointsXml.get(0).getAttributes();
            double x0 = XmlUtils.getDoubleAttr(point1attrs, "x");
            double y0 = XmlUtils.getDoubleAttr(point1attrs, "y");
            NamedNodeMap point2attrs = pointsXml.get(1).getAttributes();
            double x1 = XmlUtils.getDoubleAttr(point2attrs, "x");
            double y1 = XmlUtils.getDoubleAttr(point2attrs, "y");
            NamedNodeMap point3attrs = pointsXml.get(2).getAttributes();
            double x2 = XmlUtils.getDoubleAttr(point3attrs, "x");
            double y2 = XmlUtils.getDoubleAttr(point3attrs, "y");
            NamedNodeMap point4attrs = pointsXml.get(3).getAttributes();
            double x3 = XmlUtils.getDoubleAttr(point4attrs, "x");
            double y3 = XmlUtils.getDoubleAttr(point4attrs, "y");
            segments.add(new BezierCurve(new MapPoint(x0, y0), new MapPoint(x1, y1),
                    new MapPoint(x2, y2), new MapPoint(x3, y3)));
        }

        switch (type) {
            case "directed":
                mapBuilder.addRoad(name, fromNode, fromConnectionPoint,
                        toNode, toConnectionPoint, segments, numLanes, laneWidth);
                break;
            default:
                System.out.printf("Edge type not supported: %s\n", type);
        }
    }
}
