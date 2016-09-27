package by.brstu.tst.io.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.klimovich on 27.09.2016.
 */
public class XmlUtils {
    public static String getAttr(NamedNodeMap attributes, String attrName) {
        return attributes.getNamedItem(attrName).getNodeValue();
    }

    public static double getDoubleAttr(NamedNodeMap attributes, String attrName) {
        return Double.parseDouble(getAttr(attributes, attrName));
    }

    public static float getFloatAttr(NamedNodeMap attributes, String attrName) {
        return Float.parseFloat(getAttr(attributes, attrName));
    }

    public static int getIntAttr(NamedNodeMap attributes, String attrName) {
        return Integer.parseInt(getAttr(attributes, attrName));
    }


    public static List<Node> filterNodesByTag(NodeList nodes, String tagName) {
        ArrayList<Node> result = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals(tagName)) {
                result.add(node);
            }
        }
        return result;
    }
}
