package by.brstu.tst.core.simulation;

import by.brstu.tst.core.vehicle.IVehicleVisitor;
import by.brstu.tst.core.vehicle.Vehicle;

/**
 * Created by a.klimovich on 03.09.2016.
 */
public class UpdateVehicleStateVisitor implements IVehicleVisitor {
    private float timeDelta;

    public UpdateVehicleStateVisitor(float timeDelta) {
        this.timeDelta = timeDelta;
    }

    @Override
    public void visit(Vehicle vehicle) {
        vehicle.updatePosition(timeDelta);
        //System.out.println(vehicle.toString());
    }
}
