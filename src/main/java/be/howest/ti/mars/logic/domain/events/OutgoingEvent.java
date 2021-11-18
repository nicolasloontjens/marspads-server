package be.howest.ti.mars.logic.domain.events;

public class OutgoingEvent extends Event{

    private final String message;

    public OutgoingEvent(EventType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
