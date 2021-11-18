package be.howest.ti.mars.logic.domain.events;

public class MulticastEvent extends OutgoingEvent{

    private final int chatid;

    public MulticastEvent(String message, int chatid) {
        super(EventType.MULTICAST, message);
        this.chatid = chatid;
    }

    public int getChatid() {
        return chatid;
    }
}
