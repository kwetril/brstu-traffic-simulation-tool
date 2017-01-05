package by.brstu.tst.core.statistics;

import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;

import java.util.*;

/**
 * Created by a.klimovich on 14.10.2016.
 */
public class TimeInSystemStatsCollector implements IStatsCollector {
    private List<INewPointListener> listeners;
    private HashMap<String, Double> timeInSystem;
    private double updatePeriod;
    private double nextUpdate;

    private double avgTimeSum = 0.0;
    private int avgNumRecords = 0;

    public TimeInSystemStatsCollector(double updatePeriodInSeconds) {
        listeners = new ArrayList<>();
        timeInSystem = new HashMap<>();
        updatePeriod = updatePeriodInSeconds;
        nextUpdate = 0;
    }

    @Override
    public void updateStats(SimulationState state) {
        //System.out.println("UpdateStats: " + Thread.currentThread().getId());
        double currentTime = state.getSimulationTime();
        HashSet<String> currentVehicles = new HashSet<>();
        for (MovingVehicle vehicle : state.getVehicles()) {
            currentVehicles.add(vehicle.getVehicleInfo().getIdentifier());
            if (!timeInSystem.containsKey(vehicle.getVehicleInfo().getIdentifier())) {
                timeInSystem.put(vehicle.getVehicleInfo().getIdentifier(), currentTime);
            }
        }
        Iterator<Map.Entry<String, Double>> iterator = timeInSystem.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> entry = iterator.next();
            if (!currentVehicles.contains(entry.getKey())) {
                avgTimeSum += currentTime - entry.getValue();
                avgNumRecords++;
                iterator.remove();
            }
        }

        if (currentTime + 0.001 > nextUpdate) {
            ArrayList<Double> times = new ArrayList<>(timeInSystem.values());
            int size = times.size();
            if (size > 0) {
                times.sort((x1, x2) -> -Double.compare(x1, x2));
                for (int i = 0; i < times.size(); i++) {
                    times.set(i, currentTime - times.get(i));
                }
                for (INewPointListener listener : listeners) {
                    listener.addPoint(currentTime, times.get(0), "min");
                    listener.addPoint(currentTime, times.get((int) (0.25 * size)), "25%");
                    listener.addPoint(currentTime, times.get((int) (0.5 * size)), "50%");
                    listener.addPoint(currentTime, times.get((int) (0.75 * size)), "75%");
                    listener.addPoint(currentTime, times.get(size - 1), "max");
                    listener.addPoint(currentTime, avgTimeSum / Math.max(1, avgNumRecords), "avg");
                }
            }
            nextUpdate = currentTime + updatePeriod;
        }
        //System.out.println("End UpdateStats: " + Thread.currentThread().getId());
    }

    @Override
    public void addNewPointListener(INewPointListener listener) {
        listeners.add(listener);
    }
}
