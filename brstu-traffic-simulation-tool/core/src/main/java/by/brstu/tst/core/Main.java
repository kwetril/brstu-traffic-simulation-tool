package by.brstu.tst.core;

import by.brstu.tst.core.by.brstu.tst.core.map.elements.Map;
import by.brstu.tst.core.by.brstu.tst.core.map.elements.MapBuilder;

/**
 * Created by kwetril on 8/16/16.
 */
public class Main {
    public static void main(String[] args) {
        MapBuilder mapBuilder = new MapBuilder();

        mapBuilder
            .addSourceElement("src-1")
            .addDestinationElement("dst-1")
            .addSourceElement("src-2")
            .addDestinationElement("dst-2")
            .addSourceElement("src-3")
            .addDestinationElement("dst-3")
            .addSourceElement("src-4")
            .addDestinationElement("dst-4")
            .addIntersection("crs-1");

        mapBuilder
            .addRoad("rd-1", "src-1", "crs-1")
            .addRoad("rd-2", "src-2", "crs-1")
            .addRoad("rd-3", "src-3", "crs-1")
            .addRoad("rd-4", "src-4", "crs-1")
            .addRoad("rd-5", "crs-1", "dst-1")
            .addRoad("rd-6", "crs-1", "dst-2")
            .addRoad("rd-7", "crs-1", "dst-3")
            .addRoad("rd-8", "crs-1", "dst-4");

        Map map = mapBuilder.build("map-1");
    }
}
