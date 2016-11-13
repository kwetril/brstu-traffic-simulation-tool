package by.brstu.tst.core.simulation.control.autonomous.algorithm;

import java.util.HashMap;

/**
 * Created by a.klimovich on 13.11.2016.
 */
public class VehiclePerDirectionStats {
    private HashMap<String, HashMap<Integer, Integer> > vehicleCounts;
    private HashMap<String, String> vehicleToDirection;

    public VehiclePerDirectionStats() {
        vehicleCounts = new HashMap<>();
        vehicleToDirection = new HashMap<>();
    }

    public void addVehicle(String vehicleId, String roadFrom, Integer direction) {
        if (vehicleRegistered(vehicleId)) {
            removeVehicle(vehicleId);
        }
        vehicleToDirection.put(vehicleId, roadFrom + "#" + direction);
        HashMap<Integer, Integer> subMap;
        if (!vehicleCounts.containsKey(roadFrom)) {
            subMap = new HashMap<>();
            subMap.put(direction, 1);
            vehicleCounts.put(roadFrom, subMap);
        }
        else {
            subMap = vehicleCounts.get(roadFrom);
            if (!subMap.containsKey(direction)) {
                subMap.put(direction, 1);
            } else {
                subMap.put(direction, subMap.get(direction) + 1);
            }
        }
    }

    public void removeVehicle(String vehicleId) {
        if (!vehicleRegistered(vehicleId)) {
            return;
        }
        String[] fromTo = vehicleToDirection.get(vehicleId).split("#");
        vehicleToDirection.remove(vehicleId);
        HashMap<Integer, Integer> subMap = vehicleCounts.get(fromTo[0]);
        Integer direction = Integer.parseInt(fromTo[1]);
        subMap.put(direction, subMap.get(direction) - 1);
    }

    boolean vehicleRegistered(String vehicleId) {
        return vehicleToDirection.containsKey(vehicleId);
    }

    public int getCountByDirection(String from, Integer direction) {
        if (!vehicleCounts.containsKey(from)) {
            return 0;
        }
        HashMap<Integer, Integer> subMap = vehicleCounts.get(from);
        if (!subMap.containsKey(direction)) {
            return 0;
        }
        return subMap.get(direction);
    }

    public int getCountByRoad(String from) {
        if (!vehicleCounts.containsKey(from)) {
            return 0;
        }
        int result = 0;
        for (Integer value : vehicleCounts.get(from).values()) {
            result += value;
        }
        return result;
    }
}
