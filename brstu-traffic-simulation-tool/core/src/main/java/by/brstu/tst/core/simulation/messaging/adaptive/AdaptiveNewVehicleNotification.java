package by.brstu.tst.core.simulation.messaging.adaptive;

import by.brstu.tst.core.simulation.messaging.ControlMessage;
import by.brstu.tst.core.simulation.messaging.ControlMessageType;

import static by.brstu.tst.core.simulation.messaging.ControlMessageType.ADAPTIVE_NEW_VEHICLE_NOTIFICATION;

/**
 * Created by a.klimovich on 22.10.2016.
 */
public class AdaptiveNewVehicleNotification extends ControlMessage {
    private String road;

    public AdaptiveNewVehicleNotification(String vehicle, String road, String intersection) {
        super(ADAPTIVE_NEW_VEHICLE_NOTIFICATION, vehicle, intersection);
        this.road = road;
    }

    public String getRoad() {
        return road;
    }
}
