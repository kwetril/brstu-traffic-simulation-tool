package by.brstu.tst.core.simulation.distribution;

import java.util.Random;

/**
 * Created by a.klimovich on 09.09.2016.
 */
public class UniformDistribution implements IRandomDistribution {
    private float minValue;
    private float maxValue;
    private Random generator;

    public UniformDistribution(float minValue, float maxValue, long seed) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        generator = new Random(seed);
    }

    @Override
    public float generateNextValue() {
        return (float) generator.nextDouble() * (maxValue - minValue) + minValue;
    }
}
