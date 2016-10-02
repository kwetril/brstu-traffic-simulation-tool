package by.brstu.tst.core.simulation;

/**
 * Created by a.klimovich on 03.09.2016.
 */
public class UpdateVehicleStateVisitor implements IVehicleVisitor {
    private SimulationState state;
    private float timeDelta;

    public UpdateVehicleStateVisitor(SimulationState state, float timeDelta) {
        this.state = state;
        this.timeDelta = timeDelta;
    }

    @Override
    public void visit(MovingVehicle vehicle) {
        vehicle.updateState(state);
        vehicle.updatePosition(timeDelta);
    }
}
