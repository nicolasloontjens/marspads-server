package be.howest.ti.mars.logic.domain.events;

public class DiscardEvent extends IncomingEvent{

    public DiscardEvent(int clientId) {
        super(EventType.DISCARD, clientId);
    }

}
