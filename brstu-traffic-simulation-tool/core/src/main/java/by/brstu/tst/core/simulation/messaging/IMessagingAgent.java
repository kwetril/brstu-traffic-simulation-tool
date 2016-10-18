package by.brstu.tst.core.simulation.messaging;

import by.brstu.tst.core.simulation.SimulationState;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public interface IMessagingAgent {
    Iterable<ControlMessage> updateInnerState(SimulationState simulationState,
                          Iterable<ControlMessage> inputMessages);
}
