package by.brstu.tst.ui.simulation.selection;

import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.simulation.IVehicleVisitor;
import by.brstu.tst.core.simulation.MovingVehicle;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class SelectedVehicleSearcher implements IVehicleVisitor {
    private MapPoint clickPoint;
    private double distance = Double.MAX_VALUE;
    private MovingVehicle selectedVehicle;

    public SelectedVehicleSearcher(MapPoint clickPoint) {
        this.clickPoint = clickPoint;
    }

    @Override
    public void visit(MovingVehicle vehicle) {
        double currentDistance = clickPoint.distanceTo(vehicle.getRouteStateInfo().getPosition());
        if (currentDistance < vehicle.getVehicleInfo().getTechnicalParameters().getLength() * 2
                && currentDistance < distance) {
            distance = currentDistance;
            selectedVehicle = vehicle;
        }
    }

    public MovingVehicle getSelectedVehicle() {
        return selectedVehicle;
    }
}
