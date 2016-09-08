package by.brstu.tst.core.map.elements;

/**
 * Created by kwetril on 9/8/16.
 */
public class RoadLane {
    private DirectedRoad parentRoad;
    private int number;

    public RoadLane(DirectedRoad parentRoad, int number) {
        this.parentRoad = parentRoad;
        this.number = number;
    }
}
