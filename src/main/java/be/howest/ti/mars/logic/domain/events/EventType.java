package be.howest.ti.mars.logic.domain.events;

public enum EventType {

    UNICAST("unicast"),
    BROADCAST("broadcast"),
    MESSAGE("message"),
    DISCARD("discard"),
    PRIVATEMESSAGE("privateMessage");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public static EventType fromString(String type) {
        for(EventType eventType: EventType.values()){
            if (eventType.type.equals(type)) {
                return eventType;
            }
        }
        return EventType.DISCARD;
    }
}
