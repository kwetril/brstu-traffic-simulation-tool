package by.brstu.tst.core.osmparsing;

import de.topobyte.osm4j.core.model.iface.EntityType;
import org.geotools.styling.*;
import org.opengis.filter.FilterFactory2;

import java.awt.*;

/**
 * Created by kwetril on 1/29/16.
 */
public class StyleFactory {
    Style getStyle(EntityType type) {
        switch (type) {
            case Node:
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
                return sb.createStyle(aPointSymbolizer);
            case Way:
                return SLD.createLineStyle(Color.RED, 2.0f);
        }
        return null;
    }
}
