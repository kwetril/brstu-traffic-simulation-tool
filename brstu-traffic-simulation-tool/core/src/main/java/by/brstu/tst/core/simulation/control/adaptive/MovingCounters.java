package by.brstu.tst.core.simulation.control.adaptive;

import java.util.HashMap;

/**
 * Created by a.klimovich on 06.11.2016.
 */
public class MovingCounters {
    private HashMap<String, RoadMovingCounter> counters;
    private int numMinutes;

    public MovingCounters(int numMinutes) {
        this.numMinutes = numMinutes;
        counters = new HashMap<>();
    }

    public void incrementCounter(String road, double time) {
        if (!counters.containsKey(road)) {
            counters.put(road, new RoadMovingCounter(numMinutes));
        }
        counters.get(road).incrementCounter(time);
    }

    public int getTotalCount() {
        int result = 0;
        for (RoadMovingCounter counter : counters.values()) {
            result += counter.getCount();
        }
        return result;
    }

    public int getCountForRoad(String road) {
        if (counters.containsKey(road)) {
            return counters.get(road).getCount();
        }
        return 0;
    }
}
