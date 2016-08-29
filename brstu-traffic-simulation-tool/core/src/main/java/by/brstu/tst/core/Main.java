package by.brstu.tst.core;

import by.brstu.tst.core.config.Configuration;
import by.brstu.tst.core.config.ConfigurationReader;
import by.brstu.tst.core.map.Map;
import by.brstu.tst.core.map.MapBuilder;
import by.brstu.tst.core.map.primitives.MapPoint;

/**
 * Created by kwetril on 8/16/16.
 */
public class Main {
    public static void main(String[] args) {
        ConfigurationReader configReader = new ConfigurationReader();
        Configuration config = configReader.read();
        MapBuilder mapBuilder = new MapBuilder(config);

        mapBuilder
                .addSourceElement("src-1", new MapPoint(2637872.9f, 6815772.77f))
                .addDestinationElement("dst-1", new MapPoint(2639162.77f, 6816176.45f))
                .addSourceElement("src-2", new MapPoint(2639081.56f, 6816202.73f))
                .addDestinationElement("dst-2", new MapPoint(2637791.68f, 6815801.43f))
                .addSourceElement("src-3", new MapPoint(2638369.74f, 6816678.07f))
                .addDestinationElement("dst-3", new MapPoint(2638728.04f, 6815366.7f))
                .addSourceElement("src-4", new MapPoint(2638785.37f, 6815385.81f))
                .addDestinationElement("dst-4", new MapPoint(2638417.51f, 6816666.13f))
                .addIntersection("crs-1", new MapPoint(2638577.55f, 6815992.52f));

        mapBuilder
                .addRoad("rd-1", "src-1", new MapPoint(2637872.9f, 6815772.77f),
                        "crs-1", new MapPoint(2638577.55f, 6815992.52f), 3)
                .addRoad("rd-2", "src-2", new MapPoint(2639081.56f, 6816202.73f),
                        "crs-1", new MapPoint(2638577.55f, 6815992.52f), 3)
                .addRoad("rd-3", "src-3", new MapPoint(2638369.74f, 6816678.07f),
                        "crs-1", new MapPoint(2638577.55f, 6815992.52f), 3)
                .addRoad("rd-4", "src-4", new MapPoint(2638785.37f, 6815385.81f),
                        "crs-1", new MapPoint(2638577.55f, 6815992.52f), 3)
                .addRoad("rd-5", "crs-1", new MapPoint(2638577.55f, 6815992.52f),
                        "dst-1", new MapPoint(2639162.77f, 6816176.45f), 3)
                .addRoad("rd-6", "crs-1", new MapPoint(2638577.55f, 6815992.52f),
                        "dst-2", new MapPoint(2637791.68f, 6815801.43f), 3)
                .addRoad("rd-7", "crs-1", new MapPoint(2638577.55f, 6815992.52f),
                        "dst-3", new MapPoint(2638728.04f, 6815366.7f), 3)
                .addRoad("rd-8", "crs-1", new MapPoint(2638577.55f, 6815992.52f),
                        "dst-4", new MapPoint(2638417.51f, 6816666.13f), 3);

        Map map = mapBuilder.build("map-1");
    }
}
