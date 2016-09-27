package by.brstu.tst.io.osm;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.util.HashMap;

/**
 * Created by kwetril on 1/29/16.
 */
public class OsmWayToFeatureTransformer {

    private HashMap<Long, OsmNode> nodes;
    private SimpleFeatureType wayFeatyreType;
    private SimpleFeatureBuilder wayFeatureBuilder;
    private GeometryFactory lineFactory;

    public OsmWayToFeatureTransformer(SimpleFeatureType wayFeatyreType, HashMap<Long, OsmNode> nodes) {
        this.nodes = nodes;
        this.wayFeatyreType = wayFeatyreType;
        wayFeatureBuilder = new SimpleFeatureBuilder(wayFeatyreType);
        lineFactory = new GeometryFactory();
    }

    public SimpleFeature transform(OsmWay way) {
        Coordinate[] coordinates = new Coordinate[way.getNumberOfNodes()];
        for (int i = 0; i < coordinates.length; ++i) {
            OsmNode node = nodes.get(way.getNodeId(i));
            coordinates[i] = new Coordinate(node.getLatitude(), node.getLongitude());
        }
        LineString line = lineFactory.createLineString(coordinates);
        wayFeatureBuilder.add(line);
        return wayFeatureBuilder.buildFeature(String.valueOf(way.getId()));
    }
}
