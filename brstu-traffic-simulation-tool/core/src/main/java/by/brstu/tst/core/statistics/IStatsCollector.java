package by.brstu.tst.core.statistics;

import by.brstu.tst.core.simulation.SimulationState;

/**
 * Created by a.klimovich on 13.10.2016.
 */
public interface IStatsCollector {
    void updateStats(SimulationState state);
    void addNewPointListener(INewPointListener listener);
}
