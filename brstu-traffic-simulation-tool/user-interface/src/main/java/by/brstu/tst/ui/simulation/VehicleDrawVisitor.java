package by.brstu.tst.ui.simulation;

import by.brstu.tst.core.map.primitives.Vector;
import by.brstu.tst.core.simulation.IVehicleVisitor;
import by.brstu.tst.core.simulation.MovingVehicle;
import by.brstu.tst.core.vehicle.VehicleTechnicalParameters;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by a.klimovich on 03.09.2016.
 */
public class VehicleDrawVisitor implements IVehicleVisitor {
    private Graphics2D graphics;
    private Vector baseVector;

    public VehicleDrawVisitor(Graphics2D graphics) {
        this.graphics = graphics;
        baseVector = new Vector(0.0, 1.0);
    }

    @Override
    public void visit(MovingVehicle vehicle) {
        graphics.setColor(Color.RED);
        Path2D.Double vehicleShape = vehicleToPath(vehicle);
        graphics.fill(vehicleShape);
    }

    private Path2D.Double vehicleToPath(MovingVehicle vehicle) {
        VehicleTechnicalParameters techParams = vehicle.getVehicleInfo().getTechnicalParameters();
        float width = techParams.getWidth();
        float length = techParams.getLength();
        double rotation = baseVector.angleClockwise(vehicle.getVelocity());
        Rectangle2D.Double vehicleRect = new Rectangle2D.Double(-width/2, -length/2, width, length);
        AffineTransform transform = new AffineTransform();
        transform.translate(vehicle.getPosition().getX(), vehicle.getPosition().getY());
        transform.rotate(Math.PI * 2 - rotation);
        return new Path2D.Double(vehicleRect, transform);
    }
}
