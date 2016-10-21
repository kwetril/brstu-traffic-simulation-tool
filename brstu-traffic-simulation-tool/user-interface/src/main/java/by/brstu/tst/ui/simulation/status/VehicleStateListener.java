package by.brstu.tst.ui.simulation.status;

import by.brstu.tst.core.simulation.MovingVehicle;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public interface VehicleStateListener {
    void selectedVehicleStatusChanged(MovingVehicle vehicle);
}
