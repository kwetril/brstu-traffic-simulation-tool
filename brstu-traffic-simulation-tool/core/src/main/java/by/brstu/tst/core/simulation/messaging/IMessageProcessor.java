package by.brstu.tst.core.simulation.messaging;

import java.util.List;

/**
 * Created by a.klimovich on 02.10.2016.
 */
public interface IMessageProcessor {
    void receive(List<ControlMessage> messages);
    List<ControlMessage> send();
    String getName();
}
