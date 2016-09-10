package by.brstu.tst.core.vehicle;

/**
 * Created by a.klimovich on 03.09.2016.
 *
 * Objects of this class represent singe vehicle
 * and store information about its technical characteristics.
 */
public class Vehicle {
    private String identifier;
    private VehicleTechnicalParameters technicalParameters;

    public Vehicle(String identifier, VehicleTechnicalParameters technicalParameters) {
        this.identifier = identifier;
        this.technicalParameters = technicalParameters;
    }

    public String getIdentifier() {
        return identifier;
    }

    public VehicleTechnicalParameters getTechnicalParameters() {
        return technicalParameters;
    }
}
