package be.howest.ti.mars.logic.domain.events;

public class PrivateMessageEvent extends MessageEvent{

    private String chatid;

    public PrivateMessageEvent(String marsid, String message, String chatid) {
        super(EventType.PRIVATEMESSAGE, marsid, message);
        this.chatid = chatid;
    }

    public String getChatid() {
        return chatid;
    }
}
