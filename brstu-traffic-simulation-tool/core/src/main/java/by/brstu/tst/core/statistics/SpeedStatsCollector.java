package by.brstu.tst.core.statistics;

import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.klimovich on 13.10.2016.
 */
public class SpeedStatsCollector implements IStatsCollector {
    private ArrayList<Double> speedArray;
    private List<INewPointListener> listeners;
    private double updatePeriod;
    private double nextUpdate;

    public SpeedStatsCollector(double updatePeriodInSeconds) {
        speedArray = new ArrayList<>();
        this.updatePeriod = updatePeriodInSeconds;
        this.nextUpdate = 0.0;
        listeners = new ArrayList<>();
    }

    @Override
    public void updateStats(SimulationState state) {
        for (MovingVehicle vehicle : state.getVehicles()) {
            speedArray.add(vehicle.getSpeed());
        }

        double currentTime = state.getSimulationTime();
        if (currentTime + 0.001 > nextUpdate) {
            if (speedArray.size() > 0) {
                speedArray.sort((x1, x2) -> Double.compare(x1, x2));
                int size = speedArray.size();
                for (INewPointListener listener : listeners) {
                    listener.addPoint(currentTime, speedArray.get(0), "min");
                    listener.addPoint(currentTime, speedArray.get((int) (size * 0.25)), "25%");
                    listener.addPoint(currentTime, speedArray.get((int) (size * 0.5)), "50%");
                    listener.addPoint(currentTime, speedArray.get((int) (size * 0.75)), "75%");
                    listener.addPoint(currentTime, speedArray.get(size - 1), "max");
                }
                speedArray.clear();
            }
            nextUpdate = currentTime + updatePeriod;
        }
    }


    @Override
    public void addNewPointListener(INewPointListener listener) {
        listeners.add(listener);
    }
}
