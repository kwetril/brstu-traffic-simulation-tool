package by.brstu.tst.osmparsing;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Created by kwetril on 1/28/16.
 */
public class OsmNodeToFeatureTransformer {
    private SimpleFeatureType nodeFeatureType;
    private SimpleFeatureBuilder nodeFeatureBuilder;
    private GeometryFactory pointFactory;

    public OsmNodeToFeatureTransformer(SimpleFeatureType nodeFeatureType) {
        this.nodeFeatureType =nodeFeatureType;
        nodeFeatureBuilder = new SimpleFeatureBuilder(nodeFeatureType);
        pointFactory = new GeometryFactory();
    }


    public SimpleFeature transform(OsmNode node) {
        Coordinate coordinate = new Coordinate(node.getLatitude(), node.getLongitude());
        Point point = pointFactory.createPoint(coordinate);
        nodeFeatureBuilder.add(point);
        return nodeFeatureBuilder.buildFeature(String.valueOf(node.getId()));
    }
}
