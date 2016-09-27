package by.brstu.tst.io.osm;

import de.topobyte.osm4j.core.model.iface.OsmEntity;
import de.topobyte.osm4j.core.model.iface.OsmTag;

import java.util.HashMap;

/**
 * Created by kwetril on 1/29/16.
 */
public class OsmEntityHelper {
    public static HashMap<String, String> getTags(OsmEntity entity) {
        HashMap<String, String> result = new HashMap<>();
        int numTags = entity.getNumberOfTags();
        for (int i = 0; i < numTags; ++i) {
            OsmTag tag = entity.getTag(i);
            result.put(tag.getKey(), tag.getValue());
        }
        return result;
    }
}
