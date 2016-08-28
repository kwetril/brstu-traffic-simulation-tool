package by.brstu.tst.core.config;

/**
 * Created by a.klimovich on 28.08.2016.
 */
public class Configuration {

    /**
     * @return default road width in meters
     */
    public float roadWidth() {
        return 3.5f;
    }

    /**
     * @return default road markup width in meters
     */
    public float roadMarkupWidth() {
        return 0.1f;
    }

    /**
     * @return dash pattern for road markup placed between two lanes in one direction
     */
    public float[] laneSeparationMarkupDashPattern() {
        return new float[] {1f, 2f};
    }
}
