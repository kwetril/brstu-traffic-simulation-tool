package by.brstu.tst.osmparsing;

import de.topobyte.osm4j.core.access.OsmIterator;
import de.topobyte.osm4j.core.model.iface.*;

/**
 * Created by kwetril on 1/26/16.
 */
public class OsmXmlParser {
    public void parse(OsmIterator iterator) {
        for (EntityContainer container : iterator) {
            switch (container.getType()) {
                case Node:
                    System.out.println("Node found");
                    OsmNode node = (OsmNode) container.getEntity();
                    System.out.format("Node info: id=%s, lon=%s, lat=%s\n",
                            node.getId(), node.getLongitude(), node.getLatitude());
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
    }
}
