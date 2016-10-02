package by.brstu.tst.core.simulation.messaging;

import java.util.HashMap;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class ControlMessage {
    private IMessageProcessor sender;
    private IMessageProcessor receiver;
    private ControlMessageType messageType;
    private HashMap<String, String> messageData;

    public ControlMessage(ControlMessageType messageType) {
        this.messageType = messageType;
        messageData = new HashMap<>();
    }
}
