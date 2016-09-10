package by.brstu.tst.core.vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class VehicleFactory {
    private static AtomicInteger nextIdentifier = new AtomicInteger(0);

    public static Vehicle createVehicle(VehicleType type) {
        return createCustomVehicle(getVehicleTypeParams().get(type));
    }

    public static Vehicle createCustomVehicle(VehicleTechnicalParameters technicalParameters) {
        int currentVehicleId = nextIdentifier.getAndAdd(1);
        return new Vehicle(String.valueOf(currentVehicleId), technicalParameters);
    }

    private static Map<VehicleType, VehicleTechnicalParameters> getVehicleTypeParams() {
        Map<VehicleType, VehicleTechnicalParameters> typeToParametersMap = new HashMap<>();
        typeToParametersMap.put(VehicleType.CAR, new VehicleTechnicalParameters(2,4,1,1));
        return typeToParametersMap;
    }
}
