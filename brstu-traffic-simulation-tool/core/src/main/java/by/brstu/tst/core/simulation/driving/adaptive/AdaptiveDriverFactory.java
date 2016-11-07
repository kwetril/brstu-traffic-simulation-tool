package by.brstu.tst.core.simulation.driving.adaptive;

import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.driving.BaseVehicleDriver;
import by.brstu.tst.core.simulation.driving.IDriverFactory;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AdaptiveDriverFactory implements IDriverFactory {
    @Override
    public BaseVehicleDriver createDriver(MovingVehicle vehicle) {
        return new AdaptiveVehicleDriver(vehicle);
    }
}
