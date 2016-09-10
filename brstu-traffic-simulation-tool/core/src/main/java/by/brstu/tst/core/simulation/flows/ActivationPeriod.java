package by.brstu.tst.core.simulation.flows;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class ActivationPeriod {
    private float activationTime;
    private float deactivationTime;

    public ActivationPeriod(float activationTime, float deactivationTime) {
        this.activationTime = activationTime;
        this.deactivationTime = deactivationTime;
    }

    public ActivationPeriod(float activationTime) {
        this(activationTime, Float.MAX_VALUE);
    }

    public ActivationPeriod() {
        this(0, Float.MAX_VALUE);
    }

    public float getActivationTime() {
        return activationTime;
    }

    public float getDeactivationTime() {
        return deactivationTime;
    }

    public boolean isActive(float time) {
        return (activationTime <= time && time <= deactivationTime);
    }
}
