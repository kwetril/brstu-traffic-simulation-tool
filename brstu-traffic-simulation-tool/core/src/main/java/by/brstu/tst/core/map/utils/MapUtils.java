package by.brstu.tst.core.map.utils;

import by.brstu.tst.core.map.primitives.MapPoint;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Created by a.klimovich on 27.08.2016.
 */
public class MapUtils {
    private GeodeticCalculator distanceCalculator;

    public MapUtils() {
        try {
            CoordinateReferenceSystem crs = CRS.decode("EPSG:3857");
            distanceCalculator = new GeodeticCalculator(crs);
        } catch (Exception ex) {
            distanceCalculator = null;
            return;
        }
    }

    /**
     * Returns distance between two points in meters.
     */
    double calculateDistance(MapPoint startPoint, MapPoint destinationPoint) {
        distanceCalculator.setStartingGeographicPoint(startPoint.getX(), startPoint.getY());
        distanceCalculator.setDestinationGeographicPoint(destinationPoint.getX(), destinationPoint.getY());
        return distanceCalculator.getOrthodromicDistance();
    }
}
