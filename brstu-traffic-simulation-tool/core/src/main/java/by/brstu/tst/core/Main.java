package by.brstu.tst.core;

import by.brstu.tst.core.map.elements.Map;
import by.brstu.tst.core.map.elements.MapBuilder;
import by.brstu.tst.core.map.elements.MapPoint;

/**
 * Created by kwetril on 8/16/16.
 */
public class Main {
    public static void main(String[] args) {
        MapBuilder mapBuilder = new MapBuilder();

        mapBuilder
            .addSourceElement("src-1", new MapPoint(0.0f, 9.0f))
            .addDestinationElement("dst-1", new MapPoint(20.0f, 9.0f))
            .addSourceElement("src-2", new MapPoint(20.0f, 11.0f))
            .addDestinationElement("dst-2", new MapPoint(0.0f, 11.0f))
            .addSourceElement("src-3", new MapPoint(9.0f, 0.0f))
            .addDestinationElement("dst-3", new MapPoint(9.0f, 20.0f))
            .addSourceElement("src-4", new MapPoint(11.0f, 20.0f))
            .addDestinationElement("dst-4", new MapPoint(11.0f, 0.0f))
            .addIntersection("crs-1", new MapPoint(10.0f, 10.0f));

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
