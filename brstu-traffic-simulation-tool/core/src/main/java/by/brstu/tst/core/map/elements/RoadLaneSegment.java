package by.brstu.tst.core.map.elements;

import by.brstu.tst.core.map.primitives.BezierCurve;

/**
 * Created by kwetril on 7/7/16.
 */
public class RoadLaneSegment {
    private RoadSegment segment;
    private BezierCurve curve;
    private int number;

    public RoadLaneSegment(RoadSegment segment, int number, BezierCurve curve) {
        this.segment = segment;
        this.number = number;
        this.curve = curve;
    }

    public RoadSegment getSegment() {
        return segment;
    }

    public BezierCurve getCurve() {
        return curve;
    }

    public int getNumber() {
        return number;
    }
}
