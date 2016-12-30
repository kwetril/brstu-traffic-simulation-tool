package by.brstu.tst.core.simulation.driving;

import by.brstu.tst.core.map.primitives.Vector;

/**
 * Created by a.klimovich on 30.12.2016.
 */
public class IntelligentDriverModel {
    private double a;
    private double b;
    private double T;
    private double s0;
    private double delta;

    public IntelligentDriverModel(double a, double b, double T, double s0, double delta) {
        this.a = a;
        this.b = b;
        this.T = T;
        this.s0 = s0;
        this.delta = delta;
    }

    public double getAcceleration(double v, double deltaV, double s, double v0) {
        double sStar = s0 + Math.max(0, v * T + v * deltaV / 2 / Math.sqrt(a * b));
        return a * (1 - Math.pow(v / v0, delta) - Math.pow(sStar / s, 2));
    }
}
