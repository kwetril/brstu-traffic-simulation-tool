package by.brstu.tst.core.simulation;

import by.brstu.tst.core.simulation.messaging.ControlMessage;
import com.google.common.collect.Iterables;

import java.util.Collections;

/**
 * Created by a.klimovich on 03.09.2016.
 */
public class UpdateVehicleStateVisitor implements IVehicleVisitor {
    private SimulationState state;
    private Iterable<ControlMessage> inputMessages;
    private Iterable<ControlMessage> outputMessages;
    private float timeDelta;

    public UpdateVehicleStateVisitor(SimulationState state, Iterable<ControlMessage> messages, float timeDelta) {
        this.state = state;
        this.timeDelta = timeDelta;
        this.inputMessages = messages;
        this.outputMessages = Collections.emptyList();
    }

    @Override
    public void visit(MovingVehicle vehicle) {
        outputMessages = Iterables.concat(outputMessages, vehicle.getDriver().updateInnerState(state, inputMessages));
        vehicle.updatePosition(timeDelta);
    }

    public Iterable<ControlMessage> getOutputMessages() {
        return outputMessages;
    }
}
