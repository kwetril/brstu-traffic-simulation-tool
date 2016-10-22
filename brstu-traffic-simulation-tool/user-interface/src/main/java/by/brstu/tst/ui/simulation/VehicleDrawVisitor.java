package by.brstu.tst.ui.simulation;

import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.IVehicleVisitor;
import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.vehicle.Vehicle;
import by.brstu.tst.core.vehicle.VehicleTechnicalParameters;
import by.brstu.tst.ui.simulation.selection.VehicleSelector;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by a.klimovich on 03.09.2016.
 */
public class VehicleDrawVisitor implements IVehicleVisitor {
    private Graphics2D graphics;
    private Vector baseVector;
    private VehicleSelector vehicleSelector;

    public VehicleDrawVisitor(Graphics2D graphics, VehicleSelector vehicleSelector) {
        this.graphics = graphics;
        baseVector = new Vector(0.0, 1.0);
        this.vehicleSelector = vehicleSelector;
    }

    @Override
    public void visit(MovingVehicle vehicle) {
        graphics.setColor(Color.RED);
        Path2D.Double vehicleShape = vehicleToPath(vehicle);
        graphics.fill(vehicleShape);
        drawSelectionMarker(vehicle);
    }

    private void drawSelectionMarker(MovingVehicle vehicle) {
        Vehicle vehicleInfo = vehicle.getVehicleInfo();
        if (vehicleSelector.getSelectedVehicle() != null
                && vehicleSelector.getSelectedVehicle().getVehicleInfo().getIdentifier().equals(
                vehicleInfo.getIdentifier())) {
            double width = vehicleInfo.getTechnicalParameters().getWidth();
            MapPoint position = vehicle.getRouteStateInfo().getPosition();
            graphics.setColor(Color.ORANGE);
            graphics.fill(new Ellipse2D.Double(position.getX() - width / 2,
                    position.getY() - width / 2, width, width));
        }

    }

    private Path2D.Double vehicleToPath(MovingVehicle vehicle) {
        VehicleTechnicalParameters techParams = vehicle.getVehicleInfo().getTechnicalParameters();
        float width = techParams.getWidth();
        float length = techParams.getLength();
        double rotation = baseVector.angleClockwise(vehicle.getRouteStateInfo().getDirection());
        Rectangle2D.Double vehicleRect = new Rectangle2D.Double(-width/2, -length/2, width, length);
        AffineTransform transform = new AffineTransform();
        MapPoint position = vehicle.getRouteStateInfo().getPosition();
        transform.translate(position.getX(), position.getY());
        transform.rotate(Math.PI * 2 - rotation);
        return new Path2D.Double(vehicleRect, transform);
    }
}
