package by.brstu.tst.core.simulation.driving;

import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.messaging.IMessagingAgent;
import by.brstu.tst.core.simulation.routing.info.RouteStateInfo;

/**
 * Created by a.klimovich on 17.10.2016.
 */
public abstract class BaseVehicleDriver implements IMessagingAgent {
    protected MovingVehicle vehicle;

    public BaseVehicleDriver(MovingVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public abstract boolean seeCarsInFront();
}
