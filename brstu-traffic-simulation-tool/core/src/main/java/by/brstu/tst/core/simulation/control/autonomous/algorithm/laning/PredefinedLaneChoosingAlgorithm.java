package by.brstu.tst.core.simulation.control.autonomous.algorithm.laning;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;

/**
 * Created by a.klimovich on 13.11.2016.
 */
public class PredefinedLaneChoosingAlgorithm extends BaseLaneChoosingAlgorithm {
    public PredefinedLaneChoosingAlgorithm(Intersection intersection) {
        super(intersection);
    }

    @Override
    public void addVehicle(String vehicleId, DirectedRoad roadFrom, DirectedRoad roadTo) {
    }

    @Override
    public void removeVehicle(String vehicleId) {
    }

    @Override
    public double[] getLanePriorities(DirectedRoad roadFrom, DirectedRoad roadTo) {
        double[] priorities = new double[roadFrom.getNumLanes()];
        if (priorities.length == 1) {
            priorities[0] = 1;
            return priorities;
        }
        int direction = getTurnDirection(roadFrom, roadTo);
        switch (direction) {
            case -1:
                priorities[0] = 1;
                break;
            case 0:
                if (priorities.length == 2) {
                    priorities[0] = priorities[1] = 0.5;
                } else {
                    for (int i = 1; i < priorities.length; i++) {
                        priorities[i] = 1.0 / (priorities.length - 2);
                    }
                }
                break;
            case 1:
                priorities[priorities.length - 1] = 1;
                break;
        }
        return priorities;
    }
}
