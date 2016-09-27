package by.brstu.tst.io.osm;

import de.topobyte.osm4j.core.model.iface.EntityType;
import org.geotools.data.DataUtilities;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Created by kwetril on 1/29/16.
 */
public class FeatureTypeFactory {
    SimpleFeatureType getType(EntityType type) {
        try {
            switch (type) {
                case Node:
                    return DataUtilities.createType("node", "geom:Point,name:String");
                case Way:
                    return DataUtilities.createType("way", "geom:LineString,name:String");
            }
        } catch (Exception ex) {
        }
        return null;
    }
}
