package by.brstu.tst.core.simulation.flows;

import by.brstu.tst.core.simulation.MovingVehicle;

import java.util.List;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public interface IVehicleFlow {
    void appendNewVehicles(float time, List<MovingVehicle> vehicles);
}
