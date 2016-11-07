package by.brstu.tst.core.simulation.control.adaptive;

import java.util.HashMap;

/**
 * Created by a.klimovich on 06.11.2016.
 */
public class RoadMovingCounter {
    private int numMinutes;
    HashMap<Integer, Integer> minuteToCount;
    private int lastExpiredMinute;
    private int count;

    public RoadMovingCounter(int numMinutes) {
        this.numMinutes = numMinutes;
        minuteToCount = new HashMap<>();
        lastExpiredMinute = -1;
        count = 0;
    }

    /**
     * It is assumed that time passed to this method can only increase during execution
     */
    public void incrementCounter(double time) {
        int minute = ((int) time) % 60;
        for (int i = lastExpiredMinute + 1; i <= minute - numMinutes; i++) {
            if (minuteToCount.containsKey(i)) {
                count -= minuteToCount.get(i);
                minuteToCount.remove(i);
            }
        }
        lastExpiredMinute = minute - numMinutes;
        if (minuteToCount.containsKey(minute)) {
            minuteToCount.put(minute, minuteToCount.get(minute) + 1);
        } else {
            minuteToCount.put(minute, 1);
        }
        count++;
    }

    public int getCount() {
        return count;
    }
}
