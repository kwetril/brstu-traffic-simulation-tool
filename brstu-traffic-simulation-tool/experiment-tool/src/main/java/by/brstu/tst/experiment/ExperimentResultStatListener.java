package by.brstu.tst.experiment;

import by.brstu.tst.core.statistics.INewPointListener;

/**
 * Created by a.klimovich on 05.01.2017.
 */
public class ExperimentResultStatListener implements INewPointListener {
    private double resultX;
    private double resultY;

    @Override
    public void addPoint(double x, double y, String seriesName) {
        if (seriesName.equals("avg")) {
            resultX = x;
            resultY = y;
        }
    }

    public double getResultX() {
        return resultX;
    }

    public double getResultY() {
        return resultY;
    }
}
