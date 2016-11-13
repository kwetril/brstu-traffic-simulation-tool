package by.brstu.tst.core.simulation.control.autonomous.algorithm.laning;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.control.autonomous.algorithm.VehiclePerDirectionStats;

/**
 * Created by a.klimovich on 13.11.2016.
 */
public class AutonomousLaneChoosingAlgorithm extends BaseLaneChoosingAlgorithm {

    //statistics, key is road-road pair, value - number of vehicles seen
    //it is used to find best lanes to move for this road-road pair
    private VehiclePerDirectionStats vehiclePerDirectionStats;

    public AutonomousLaneChoosingAlgorithm(Intersection intersection) {
        super(intersection);
        vehiclePerDirectionStats = new VehiclePerDirectionStats();
    }

    @Override
    public void addVehicle(String vehicleId, DirectedRoad roadFrom, DirectedRoad roadTo) {
        int direction = getTurnDirection(roadFrom, roadTo);
        vehiclePerDirectionStats.addVehicle(vehicleId, roadFrom.getName(), direction);
    }

    @Override
    public void removeVehicle(String vehicleId) {
        vehiclePerDirectionStats.removeVehicle(vehicleId);
    }

    @Override
    public double[] getLanePriorities(DirectedRoad roadFrom, DirectedRoad roadTo) {
        double[] priorities = new double[roadFrom.getNumLanes()];
        if (priorities.length == 1) {
            priorities[0] = 1.0;
            return priorities;
        }
        int direction = getTurnDirection(roadFrom, roadTo);
        double count = Math.max(1, vehiclePerDirectionStats.getCountByRoad(roadFrom.getName()));
        double[] fractions = new double[]{
                1.0 * vehiclePerDirectionStats.getCountByDirection(roadFrom.getName(), -1) / count,
                1.0 * vehiclePerDirectionStats.getCountByDirection(roadFrom.getName(), 0) / count,
                1.0 * vehiclePerDirectionStats.getCountByDirection(roadFrom.getName(), 1) / count
        };
        fillPriorities(priorities, fractions, direction);
        return priorities;
    }

    private void fillPriorities(double[] priorities, double[] fractions, int direction) {
        double fraction;
        double fractionPerLane = 1. / priorities.length;
        switch (direction) {
            case -1:
                fraction = fractions[0];
                for (int i = 0; i < priorities.length; i++) {
                    if (fraction > fractionPerLane) {
                        priorities[i] = fractionPerLane / fractions[0];
                        fraction -= fractionPerLane;
                    } else {
                        priorities[i] = fraction / fractions[0];
                        break;
                    }
                }
                for (int i = 0; i < priorities.length; i++) {
                    priorities[i] *= Math.exp(-2 * (i + 1));
                }
                break;
            case 0:
                fraction = fractions[0];
                double[] tmp = new double[priorities.length];
                for (int i = 0; i < tmp.length; i++) {
                    if (fraction > fractionPerLane) {
                        tmp[i] = fractionPerLane;
                        fraction -= fractionPerLane;
                    } else {
                        tmp[i] = fraction;
                        break;
                    }
                }
                fraction = fractions[2];
                for (int i = tmp.length - 1; i >= 0; i--) {
                    if (fraction > fractionPerLane) {
                        tmp[i] = fractionPerLane;
                        fraction -= fractionPerLane;
                    } else {
                        tmp[i] = fraction;
                        break;
                    }
                }
                for (int i = 0; i < priorities.length; i++) {
                    priorities[i] = (fractionPerLane - tmp[i]) / fractionPerLane;
                }
                for (int i = 0; i < priorities.length; i++) {
                    priorities[i] *= Math.exp(-2 * (Math.abs(i - priorities.length / 2.0) + 1));
                }
                break;
            case 1:
                fraction = fractions[2];
                for (int i = priorities.length - 1; i >= 0; i--) {
                    if (fraction > fractionPerLane) {
                        priorities[i] = fractionPerLane / fractions[2];
                        fraction -= fractionPerLane;
                    } else {
                        priorities[i] = fraction / fractions[2];
                        break;
                    }
                }
                for (int i = priorities.length - 1; i >= 0; i--) {
                    priorities[i] *= Math.exp(-2 * (priorities.length - i));
                }
                break;
        }
        double sum = 0;
        for (int i = 0; i < priorities.length; i++) {
            sum += priorities[i];
        }
        for (int i = 0; i < priorities.length; i++) {
            priorities[i] /= sum;
        }
    }
}
