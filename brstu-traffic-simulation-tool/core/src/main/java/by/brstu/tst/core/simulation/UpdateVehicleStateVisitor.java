package by.brstu.tst.core.simulation;

import by.brstu.tst.core.simulation.messaging.MessagingQueue;

/**
 * Created by a.klimovich on 03.09.2016.
 */
public class UpdateVehicleStateVisitor implements IVehicleVisitor {
    private SimulationState state;
    private MessagingQueue messagingQueue;
    private float timeDelta;

    public UpdateVehicleStateVisitor(SimulationState state, MessagingQueue messagingQueue, float timeDelta) {
        this.state = state;
        this.timeDelta = timeDelta;
        this.messagingQueue = messagingQueue;
    }

    @Override
    public void visit(MovingVehicle vehicle) {
        vehicle.getDriver().updateInnerState(state, messagingQueue);
        vehicle.updatePosition(timeDelta);
    }
}
