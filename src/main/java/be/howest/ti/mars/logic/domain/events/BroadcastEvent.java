package be.howest.ti.mars.logic.domain.events;

public class BroadcastEvent extends OutgoingEvent{


    public BroadcastEvent(String message) {
        super(EventType.BROADCAST, message);
    }
}
