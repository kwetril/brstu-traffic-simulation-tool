package by.brstu.tst.core.simulation.driving.autonomous;

import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.driving.BaseVehicleDriver;
import by.brstu.tst.core.simulation.driving.IDriverFactory;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AutonomousDriverFactory implements IDriverFactory {
    @Override
    public BaseVehicleDriver createDriver(MovingVehicle vehicle) {
        return new AutonomousVehicleDriver(vehicle);
    }
}
