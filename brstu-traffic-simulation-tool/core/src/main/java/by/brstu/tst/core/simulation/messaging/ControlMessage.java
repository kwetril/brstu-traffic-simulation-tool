package by.brstu.tst.core.simulation.messaging;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public class ControlMessage {
    private ControlMessageType messageType;
    private String sender;
    private String receiver;

    public ControlMessage(ControlMessageType messageType, String sender, String receiver) {
        this.messageType = messageType;
        this.sender = sender;
        this.receiver = receiver;
    }

    public ControlMessageType getType() {
        return messageType;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public boolean isBroadcast() {
        return receiver == null;
    }
}
