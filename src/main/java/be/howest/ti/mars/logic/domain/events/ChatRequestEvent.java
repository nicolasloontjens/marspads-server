package be.howest.ti.mars.logic.domain.events;

public class ChatRequestEvent extends IncomingEvent{

    private final int receivercontactid;
    private final int value;

    public ChatRequestEvent(int sendermarsid, int receivermid, int value) {
        super(EventType.REQUEST, sendermarsid);
        this.receivercontactid = receivermid;
        this.value = value;
    }

    public int getReceivercontactid() {
        return receivercontactid;
    }

    public int getValue() {
        return value;
    }
}
