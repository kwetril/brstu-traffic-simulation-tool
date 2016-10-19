package by.brstu.tst.core.simulation.driving;

import by.brstu.tst.core.simulation.MovingVehicle;

/**
 * Created by a.klimovich on 20.10.2016.
 */
public interface IDriverFactory {
    BaseVehicleDriver createDriver(MovingVehicle vehicle);
}
