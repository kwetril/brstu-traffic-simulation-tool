package by.brstu.tst.core.simulation.routing.state;

import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;

/**
 * Created by a.klimovich on 08.10.2016.
 */
public abstract class RouteInnerState {
    protected BezierCurve curve;
    protected double curveParameter;
    protected final double EPS = 0.001;

    public abstract double updateState(double distance);
    public abstract RouteStateInfo getStateInfo();

    protected double updateStateOnCurve(double distance) {
        while (curveParameter < 1.0 && distance > EPS) {
            double curveParameterDelta = 0.01;
            double nextCurveParameter = Math.min(curveParameter + curveParameterDelta, 1.0);
            MapPoint currentPosition = curve.getPoint(curveParameter);
            MapPoint nextPosition = curve.getPoint(nextCurveParameter);
            double distanceToNextPosition = currentPosition.distanceTo(nextPosition);
            if (distance < distanceToNextPosition) {
                curveParameter = Math.min(curveParameter + distance / distanceToNextPosition * curveParameterDelta,
                        1.0);
                distance = 0;
            }
            else {
                curveParameter = nextCurveParameter;
                distance -= distanceToNextPosition;
            }
        }
        return distance;
    }
}
