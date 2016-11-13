package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import by.brstu.tst.core.map.elements.DirectedRoad;
import by.brstu.tst.core.map.elements.Intersection;
import by.brstu.tst.core.map.primitives.BezierCurve;
import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.map.utils.RoadConnectorDescription;

/**
 * Created by a.klimovich on 13.11.2016.
 */
public class LaneChoosingAlgorithm {
    private Intersection intersection;

    //statistics, key is road-road pair, value - number of vehicles seen
    //it is used to find best lanes to move for this road-road pair
    private VehiclePerDirectionStats vehiclePerDirectionStats;

    public LaneChoosingAlgorithm(Intersection intersection) {
        this.intersection = intersection;
        vehiclePerDirectionStats = new VehiclePerDirectionStats();
    }

    public void addVehicle(String vehicleId, DirectedRoad roadFrom, DirectedRoad roadTo) {
        int direction = getTurnDirection(roadFrom, roadTo);
        vehiclePerDirectionStats.addVehicle(vehicleId, roadFrom.getName(), direction);
    }

    public void removeVehicle(String vehicleId) {
        vehiclePerDirectionStats.removeVehicle(vehicleId);
    }

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
                    priorities[i] *= Math.exp(-2*(i + 1));
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
                    priorities[i] *= Math.exp(-2*(Math.abs(i - priorities.length / 2.0) + 1));
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
                    priorities[i] *= Math.exp(-2*(priorities.length - i));
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

    private int getTurnDirection(DirectedRoad roadFrom, DirectedRoad roadTo) {
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
