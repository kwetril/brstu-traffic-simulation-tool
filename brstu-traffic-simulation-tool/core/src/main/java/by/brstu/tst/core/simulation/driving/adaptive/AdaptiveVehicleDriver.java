package by.brstu.tst.core.simulation.driving.adaptive;

import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.SimulationState;
import by.brstu.tst.core.simulation.driving.BaseVehicleDriver;
import by.brstu.tst.core.simulation.messaging.MessagingQueue;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AdaptiveVehicleDriver extends BaseVehicleDriver {
    public AdaptiveVehicleDriver(MovingVehicle vehicle) {
        super(vehicle);
    }

    @Override
    public boolean seeCarsInFront() {
        return false;
    }

    @Override
    public void updateInnerState(SimulationState simulationState, MessagingQueue messagingQueue) {

    }
}
