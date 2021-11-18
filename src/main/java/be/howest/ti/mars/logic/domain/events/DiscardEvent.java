package be.howest.ti.mars.logic.domain.events;

public class DiscardEvent extends IncomingEvent{

    public DiscardEvent(String clientId) {
        super(EventType.DISCARD, clientId);
    }

}
