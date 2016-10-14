package by.brstu.tst.core.statistics;

import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by a.klimovich on 13.10.2016.
 */
public class VehicleDynmicsStatCollector implements IStatsCollector {
    private List<INewPointListener> listeners;
    private HashSet<String> existingVehicles;
    private double updatePeriod;
    private double nextUpdate;
    private int addedVehicles;
    private int deletedVehicles;

    public VehicleDynmicsStatCollector(double updatePeriodInSeconds) {
        listeners = new ArrayList<>();
        existingVehicles = new HashSet<>();
        updatePeriod = updatePeriodInSeconds;
        nextUpdate = 0;
        addedVehicles = 0;
        deletedVehicles = 0;
    }

    @Override
    public void updateStats(SimulationState state) {
        HashSet<String> nextExistingVehicles = new HashSet<>();
        int stepAddedVehicles = 0;
        for (MovingVehicle vehicle : state.getVehicles()) {
            nextExistingVehicles.add(vehicle.getVehicleInfo().getIdentifier());
            if (!existingVehicles.contains(vehicle.getVehicleInfo().getIdentifier())) {
                stepAddedVehicles++;
            }
        }
        int stepDeletedVehicles = existingVehicles.size() - nextExistingVehicles.size() + stepAddedVehicles;
        addedVehicles += stepAddedVehicles;
        deletedVehicles += stepDeletedVehicles;
        existingVehicles = nextExistingVehicles;

        double currentTime = state.getSimulationTime();
        if (currentTime + 0.001 > nextUpdate) {
            for (INewPointListener listener : listeners) {
                listener.addPoint(currentTime, addedVehicles, "added");
                listener.addPoint(currentTime, deletedVehicles, "deleted");
            }
            addedVehicles = 0;
            deletedVehicles = 0;
            nextUpdate = currentTime + updatePeriod;
        }
    }

    @Override
    public void addNewPointListener(INewPointListener listener) {
        listeners.add(listener);
    }
}
