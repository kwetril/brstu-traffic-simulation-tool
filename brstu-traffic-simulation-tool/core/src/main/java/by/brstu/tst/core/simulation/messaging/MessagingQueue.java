package by.brstu.tst.core.simulation.messaging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.klimovich on 20.10.2016.
 */
public class MessagingQueue {
    List<ControlMessage> currentMessages;
    List<ControlMessage> newMessages;

    public MessagingQueue() {
        currentMessages = new ArrayList<>(50);
        newMessages = new ArrayList<>(50);
    }

    public List<ControlMessage> getCurrentMessages() {
        return currentMessages;
    }

    public void addMessage(ControlMessage message) {
        newMessages.add(message);
    }

    public void endSimulationStep() {
        List<ControlMessage> tmp = currentMessages;
        currentMessages = newMessages;
        newMessages = tmp;
        newMessages.clear();
    }
}
