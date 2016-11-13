package by.brstu.tst.core.simulation.control.autonomous.algorithm.laning;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;

/**
 * Created by a.klimovich on 13.11.2016.
 */
public abstract class BaseLaneChoosingAlgorithm {
    protected Intersection intersection;

    public BaseLaneChoosingAlgorithm(Intersection intersection) {
        this.intersection = intersection;
    }

    public abstract void addVehicle(String vehicleId, DirectedRoad roadFrom, DirectedRoad roadTo);

    public abstract void removeVehicle(String vehicleId);

    public abstract double[] getLanePriorities(DirectedRoad roadFrom, DirectedRoad roadTo);

    protected int getTurnDirection(DirectedRoad roadFrom, DirectedRoad roadTo) {
        BezierCurve connector = intersection.getConnector(roadFrom, 0, roadTo, 0);
        MapPoint[] points = connector.getPoints();
        Vector fromVector = new Vector(points[0], points[1]);
        Vector toVector = new Vector(points[2], points[3]);
        double angle = fromVector.angleClockwise(toVector);
        int direction = 0;
        if (angle > Math.PI / 4 && angle < 2 * Math.PI - Math.PI / 4) {
            if (angle < Math.PI) {
                direction = 1; //right turn
            } else {
                direction = -1; //left turn
            }
        }
        return direction;
    }
}
