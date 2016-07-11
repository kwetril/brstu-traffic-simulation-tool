package by.brstu.tst.core.osmparsing;

import de.topobyte.osm4j.core.access.OsmIterator;
import de.topobyte.osm4j.core.model.iface.*;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import java.util.HashMap;

/**
 * Created by kwetril on 1/26/16.
 */
public class OsmXmlParser {
    public OsmMapObjects parse(OsmIterator iterator) {
        OsmMapObjects mapObjects = new OsmMapObjects();
        for (EntityContainer container : iterator) {
            switch (container.getType()) {
                case Node:
                    System.out.println("Node found");
                    OsmNode node = (OsmNode) container.getEntity();
                    System.out.format("Node info: id=%s, lon=%s, lat=%s\n",
                            node.getId(), node.getLongitude(), node.getLatitude());
                    printTags(node);
                    mapObjects.nodes.put(node.getId(), node);
                    break;
                case Relation:
                    System.out.println("Relation found");
                    OsmRelation relation = (OsmRelation) container.getEntity();
                    System.out.format("Relation info: id=%s, numOfMembers=%s\n",
                            relation.getId(), relation.getNumberOfMembers());
                    printTags(relation);
                    break;
                case Way:
                    System.out.println("Way found");
                    OsmWay way = (OsmWay) container.getEntity();
                    System.out.format("Way info: id=%s, numOfNodes=%s\n",
                            way.getId(), way.getNumberOfNodes());
                    printTags(way);
                    HashMap<String, String> tags = OsmEntityHelper.getTags(way);
                    if (tags.containsKey("highway")) {
                        mapObjects.ways.put(way.getId(), way);
                    }
                    break;
            }
        }

        OsmNode firstNode = mapObjects.nodes.values().iterator().next();
        try {
            CoordinateReferenceSystem source = CRS.decode("EPSG:4326");
            CoordinateReferenceSystem target = CRS.decode("EPSG:3857");
            MathTransform transform = CRS.findMathTransform(source, target, false);
            DirectPosition2D position = new DirectPosition2D(firstNode.getLatitude(), firstNode.getLongitude());
            GeneralDirectPosition transformedPosition = (GeneralDirectPosition) transform.transform(position, null);
            System.out.printf("%s, %s\n", transformedPosition.getOrdinate(0), transformedPosition.getOrdinate(1));

            double test[] = new double[] {firstNode.getLatitude(), firstNode.getLongitude()};
            double res[] = new double[2];
            transform.transform(test, 0, res, 0, 1);
            System.out.printf("%s, %s\n", res[0], res[1]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return mapObjects;
    }

    public void printTags(OsmEntity entity) {
        int numOfTags = entity.getNumberOfTags();
        for (int i = 0; i < numOfTags; ++i) {
            OsmTag tag = entity.getTag(i);
            System.out.printf("%s=%s ", tag.getKey(), tag.getValue());
        }
        System.out.println();
    }
}
