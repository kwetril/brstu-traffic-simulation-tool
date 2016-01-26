package by.brstu.tst.osmparsing;

import de.topobyte.osm4j.core.access.OsmIterator;
import de.topobyte.osm4j.core.model.iface.*;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwetril on 1/26/16.
 */
public class OsmXmlParser {
    public List<OsmNode> parse(OsmIterator iterator) {
        List<OsmNode> nodes = new ArrayList<>();
        for (EntityContainer container : iterator) {
            switch (container.getType()) {
                case Node:
                    System.out.println("Node found");
                    OsmNode node = (OsmNode) container.getEntity();
                    System.out.format("Node info: id=%s, lon=%s, lat=%s\n",
                            node.getId(), node.getLongitude(), node.getLatitude());
                    nodes.add(node);
                    break;
                case Relation:
                    System.out.println("Relation found");
                    OsmRelation relation = (OsmRelation) container.getEntity();
                    System.out.format("Relation info: id=%s, numOfMembers=%s\n",
                            relation.getId(), relation.getNumberOfMembers());
                    break;
                case Way:
                    System.out.println("Way found");
                    OsmWay way = (OsmWay) container.getEntity();
                    System.out.format("Way info: id=%s, numOfNodes=%s\n",
                            way.getId(), way.getNumberOfNodes());
                    break;
            }
        }

        OsmNode firstNode = nodes.get(0);
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
        return nodes;
    }
}
