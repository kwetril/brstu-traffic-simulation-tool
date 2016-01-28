package by.brstu.tst.osmparsing;

import com.vividsolutions.jts.geom.Coordinate;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.Envelope2D;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.*;
import org.geotools.swing.JMapFrame;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by kwetril on 1/26/16.
 */
public class MapRenderer {
    public void render(OsmMapObjects mapObjects) throws Exception {
        MapContent map = new MapContent();

        Collection<OsmNode> nodes = mapObjects.nodes.values();
        Layer nodeLayer = createNodeLayer(nodes);
        map.addLayer(nodeLayer);

        Layer wayLayer = createWayLayer(mapObjects);
        map.addLayer(wayLayer);

        OsmNode node = nodes.iterator().next();
        showMapFrame(map, node.getLatitude(), node.getLongitude());
    }

    private Layer createNodeLayer(Collection<OsmNode> nodes) {
        FeatureTypeFactory featureTypeFactory = new FeatureTypeFactory();
        StyleFactory styleFactory = new StyleFactory();
        SimpleFeatureType nodeFeatureType = featureTypeFactory.getType(EntityType.Node);
        OsmNodeToFeatureTransformer nodeTransformer = new OsmNodeToFeatureTransformer(nodeFeatureType);
        ArrayList<SimpleFeature> listNodes = new ArrayList<>();
        for (OsmNode node : nodes) {
            listNodes.add(nodeTransformer.transform(node));
        }
        SimpleFeatureCollection features = new ListFeatureCollection(nodeFeatureType, listNodes);
        Style nodeStyle = styleFactory.getStyle(EntityType.Node);
        return new FeatureLayer(features, nodeStyle);
    }

    private Layer createWayLayer(OsmMapObjects mapObjects) {
        FeatureTypeFactory featureTypeFactory = new FeatureTypeFactory();
        StyleFactory styleFactory = new StyleFactory();
        SimpleFeatureType wayFeatureType = featureTypeFactory.getType(EntityType.Way);
        OsmWayToFeatureTransformer wayTransformaer = new OsmWayToFeatureTransformer(wayFeatureType, mapObjects.nodes);

        ArrayList<SimpleFeature> listWays = new ArrayList<>();
        for (OsmWay way : mapObjects.ways.values()) {
            listWays.add(wayTransformaer.transform(way));
        }
        SimpleFeatureCollection collectionLines = new ListFeatureCollection(wayFeatureType, listWays);
        Style wayStyle = styleFactory.getStyle(EntityType.Way);
        return new FeatureLayer(collectionLines, wayStyle);
    }

    private void showMapFrame(MapContent map, double latitude, double longitude) {
        JMapFrame frame = new JMapFrame(map);
        frame.setSize(800, 600);
        frame.enableStatusBar(true);
        //frame.enableTool(JMapFrame.Tool.ZOOM, JMapFrame.Tool.PAN, JMapFrame.Tool.RESET);
        frame.enableToolBar(true);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu menu = new JMenu("Raster");
        menuBar.add(menu);

        Envelope2D env = new Envelope2D();
        final double increment = 0.05d;
        Coordinate positionCoo = new Coordinate(latitude, longitude);
        env.setFrameFromDiagonal(positionCoo.x - increment, positionCoo.y
                - increment, positionCoo.x + increment, positionCoo.y
                + increment);
        frame.getMapPane().setDisplayArea(env);

        // Finally display the map frame.
        // When it is closed the app will exit.
        frame.setVisible(true);
    }
}
