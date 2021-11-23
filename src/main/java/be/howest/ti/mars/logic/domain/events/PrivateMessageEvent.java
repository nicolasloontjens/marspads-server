package be.howest.ti.mars.logic.domain.events;

public class PrivateMessageEvent extends MessageEvent{

    private final String chatid;

    public PrivateMessageEvent(int marsid, String message, String chatid) {
        super(EventType.PRIVATEMESSAGE, marsid, message);
        this.chatid = chatid;
    }

    public String getChatid() {
        return chatid;
    }
}
