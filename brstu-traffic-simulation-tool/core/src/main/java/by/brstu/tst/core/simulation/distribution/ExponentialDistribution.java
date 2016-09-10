package by.brstu.tst.core.simulation.distribution;

import java.util.Random;

/**
 * Created by a.klimovich on 10.09.2016.
 */
public class ExponentialDistribution implements IRandomDistribution {
    private float lambda;
    private Random generator;

    public ExponentialDistribution(float lambda, long seed) {
        this.lambda = lambda;
        generator = new Random(seed);
    }

    @Override
    public float generateNextValue() {
        return (float) Math.log(1 - generator.nextDouble())/(-lambda);
    }
}
