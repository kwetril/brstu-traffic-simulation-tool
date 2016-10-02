package by.brstu.tst.core.simulation.messaging;

import java.util.HashMap;
import java.util.List;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class MessageQueue {
    private HashMap<String, List<ControlMessage>> messages;
    private List<ControlMessage> broadcastMessages;


    public void putMessages(List<ControlMessage> messages) {
        for (ControlMessage message : messages) {
        }

    }

    public List<ControlMessage> getMessages(IMessageProcessor messenger) {
        return null;
    }
}
