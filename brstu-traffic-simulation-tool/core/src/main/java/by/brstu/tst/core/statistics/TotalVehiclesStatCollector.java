package by.brstu.tst.core.statistics;

import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.klimovich on 13.10.2016.
 */
public class TotalVehiclesStatCollector implements IStatsCollector {
    private List<INewPointListener> listeners;
    private double updatePeriod;
    private double nextUpdate;

    public TotalVehiclesStatCollector(double updatePeriodInSeconds) {
        listeners = new ArrayList<>();
        updatePeriod = updatePeriodInSeconds;
        nextUpdate = 0;
    }

    @Override
    public void updateStats(SimulationState state) {
        double currentTime = state.getSimulationTime();
        if (currentTime + 0.001 > nextUpdate) {
            for (INewPointListener listener : listeners) {
                listener.addPoint(currentTime, state.getVehicles().size(), "total");
            }
            nextUpdate = currentTime + updatePeriod;
        }
    }

    @Override
    public void addNewPointListener(INewPointListener listener) {
        listeners.add(listener);
    }
}
