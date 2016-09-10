package by.brstu.tst.core.vehicle;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class VehicleTechnicalParameters {
    private float width;
    private float length;
    private float maxAccelerationRate;
    private float maxDecelerationRate;

    public VehicleTechnicalParameters(float width, float length,
                                      float maxAccelerationRate, float maxDecelerationRate) {
        this.width = width;
        this.length = length;
        this.maxAccelerationRate = maxAccelerationRate;
        this.maxDecelerationRate = maxDecelerationRate;
    }

    public float getWidth() {
        return width;
    }

    public float getLength() {
        return length;
    }

    public float getMaxAccelerationRate() {
        return maxAccelerationRate;
    }

    public float getMaxDecelerationRate() {
        return maxDecelerationRate;
    }
}
