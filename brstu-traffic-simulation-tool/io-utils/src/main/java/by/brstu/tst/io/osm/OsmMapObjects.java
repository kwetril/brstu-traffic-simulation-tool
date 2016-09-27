package by.brstu.tst.io.osm;

import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmWay;

import java.util.HashMap;

/**
 * Created by kwetril on 1/28/16.
 */
public class OsmMapObjects {
    HashMap<Long, OsmNode> nodes = new HashMap<>();
    HashMap<Long, OsmWay> ways = new HashMap<>();
}
