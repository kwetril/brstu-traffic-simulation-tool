package by.brstu.tst.osmparsing;

import com.vividsolutions.jts.geom.*;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.Envelope2D;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.*;
import org.geotools.swing.JMapFrame;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kwetril on 1/26/16.
 */
public class MapRenderer {
    void render(List<OsmNode> nodes) throws Exception {
        MapContent map = new MapContent();


        SimpleFeatureType TYPE = DataUtilities.createType("location","geom:Point,name:String");

        ArrayList<SimpleFeature> list = new ArrayList<SimpleFeature>();
        GeometryFactory geometryFactory = new GeometryFactory();
        for (OsmNode node : nodes) {
            list.add( SimpleFeatureBuilder.build( TYPE, new Object[]{ geometryFactory.createPoint(new Coordinate(node.getLatitude(), node.getLongitude())), "name1"}, null) );
        }
        SimpleFeatureCollection collection = new ListFeatureCollection(TYPE,list);

        // O(N) access
        SimpleFeatureSource source = DataUtilities.source( collection );
        SimpleFeatureCollection features = source.getFeatures();
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
        StyleBuilder sb = new StyleBuilder();
        FilterFactory2 ff = sb.getFilterFactory();

        Mark testMark = sb.createMark(ff.literal("circle"), sb.createFill(Color.RED, 0.85), null);

        Graphic graph = sb.createGraphic(null, // An external graphics if needed
                new Mark[] { testMark }, // a Mark if not an external graphics
                null, // aSymbol
                ff.literal(1), // opacity
                ff.literal(3), // read from feature "size" attribute
                ff.literal(0)); // rotation, here read into the feature
        PointSymbolizer aPointSymbolizer = sb.createPointSymbolizer(graph);

        // creation of the style
        Style style = sb.createStyle(aPointSymbolizer);

        Layer mainLayer = new FeatureLayer(features, style);
        map.addLayer(mainLayer);


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
        Coordinate positionCoo = new Coordinate(nodes.get(0).getLatitude(), nodes.get(0).getLongitude());
        env.setFrameFromDiagonal(positionCoo.x - increment, positionCoo.y
                - increment, positionCoo.x + increment, positionCoo.y
                + increment);
        frame.getMapPane().setDisplayArea(env);

        // Finally display the map frame.
        // When it is closed the app will exit.
        frame.setVisible(true);
    }
}
