package by.brstu.tst.visualization;

/**
 * Created by kwetril on 7/6/16.
 */
public class RoadSegment {
    private int startPointId;
    private int endPointId;

    public RoadSegment(int startPointId, int endPointId) {
        this.startPointId = startPointId;
        this.endPointId = endPointId;
    }

    public int getStartPointId() {
        return startPointId;
    }

    public int getEndPointId() {
        return endPointId;
    }
}
