package be.howest.ti.mars.logic.domain.events;

import be.howest.ti.mars.logic.domain.User;

public class UnicastEvent extends OutgoingEvent{

    private final User sender;
    private final int receivermid;
    private final int chatid;
    private final int value;

    public UnicastEvent(User sender, int receivermid, int value) {
        super(EventType.UNICAST, "");
        this.chatid = -1;
        this.sender = sender;
        this.receivermid = receivermid;
        this.value = value;
    }

    public UnicastEvent(User sender, int receivermid, int value, int chatid) {
        super(EventType.UNICAST, "");
        this.chatid = chatid;
        this.sender = sender;
        this.receivermid = receivermid;
        this.value = value;
    }

    public int getSendermid(){
        return sender.getMarsid();
    }

    public String getSendername(){
        return sender.getName();
    }

    public int getReceivermid() {
        return receivermid;
    }

    public int getValue() {
        return value;
    }

    public int getChatid() {
        return chatid;
    }
}
