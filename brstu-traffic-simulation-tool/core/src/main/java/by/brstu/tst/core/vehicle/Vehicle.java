package by.brstu.tst.core.vehicle;

import by.brstu.tst.core.map.primitives.MapPoint;
import by.brstu.tst.core.map.primitives.Vector;

/**
 * Created by a.klimovich on 03.09.2016.
 */
public class Vehicle {
    private MapPoint position;
    private Vector velocity;

    public Vehicle(MapPoint position) {
        this(position, new Vector(0, 0));
    }

    public Vehicle(MapPoint position, Vector velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public MapPoint getPosition() {
        return position;
    }

    public MapPoint updatePosition(float timeDelta) {
        position = velocity.clone().multiply(timeDelta).addToPoint(position);
        return position;
    }

    public Vector updateVelocity(Vector velocityDelta) {
        return velocity.add(velocityDelta);
    }

    public void accept(IVehicleVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("[Vehicle: (%s, %s); %s mps]", position.getX(), position.getY(), velocity.getLength());
    }
}
