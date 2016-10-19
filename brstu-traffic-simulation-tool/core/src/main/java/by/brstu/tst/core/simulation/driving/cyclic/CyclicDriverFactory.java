package by.brstu.tst.core.simulation.driving.cyclic;

import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.driving.BaseVehicleDriver;
import by.brstu.tst.core.simulation.driving.IDriverFactory;

/**
 * Created by a.klimovich on 20.10.2016.
 */
public class CyclicDriverFactory implements IDriverFactory {
    @Override
    public BaseVehicleDriver createDriver(MovingVehicle vehicle) {
        return new CyclicVehicleDriver(vehicle);
    }
}
