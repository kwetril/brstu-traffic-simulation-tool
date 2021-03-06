package by.brstu.tst.core.simulation.flows;

import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.simulation.distribution.IRandomDistribution;
import by.brstu.tst.core.simulation.driving.IDriverFactory;
import by.brstu.tst.core.simulation.routing.Route;
import by.brstu.tst.core.vehicle.Vehicle;
import by.brstu.tst.core.vehicle.VehicleFactory;
import by.brstu.tst.core.vehicle.VehicleType;

import java.util.List;
import java.util.Random;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class StaticVehicleFlow implements IVehicleFlow {
    private VehicleType vehicleType;
    private IDriverFactory driverFactory;
    private Route route;
    private IRandomDistribution flowDistribution;
    private ActivationPeriod activationPeriod;
    private float nextVehicleGenerationTime;
    int lanes;
    Random laneGenerator;

    public StaticVehicleFlow(VehicleType vehicleType, IDriverFactory driverFactory,
                       Route route, IRandomDistribution flowDistribution,
                       ActivationPeriod activationPeriod) {
        this.vehicleType = vehicleType;
        this.driverFactory = driverFactory;
        this.route = route;
        this.flowDistribution = flowDistribution;
        this.activationPeriod = activationPeriod;
        this.nextVehicleGenerationTime = activationPeriod.getActivationTime()
                + flowDistribution.generateNextValue();
        lanes = route.getSource().getNumCoonnectedLanes(route.getNextRoad(route.getSource()));
        laneGenerator = new Random();
    }

    @Override
    public void appendNewVehicles(float time, List<MovingVehicle> vehicles) {
        if (!activationPeriod.isActive(time) || time < nextVehicleGenerationTime) {
            return;
        }
        while (nextVehicleGenerationTime <= time) {
            Vehicle vehicle = VehicleFactory.createVehicle(vehicleType);
            vehicles.add(new MovingVehicle(vehicle, driverFactory, route, 20, laneGenerator.nextInt(lanes)));
            nextVehicleGenerationTime += flowDistribution.generateNextValue();
            System.out.printf("Time: %s; next time: %s\n", time, nextVehicleGenerationTime);
        }
    }
}
