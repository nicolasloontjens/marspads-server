package be.howest.ti.mars.logic.domain.events;


//A normal MessageEvent is used for broadcast messages for the people in your vicinity.
public class MessageEvent extends IncomingEvent{

    private final String message;

    public MessageEvent(int marsid, String message) {
        super(EventType.MESSAGE, marsid);
        this.message = message;
    }

    public MessageEvent(EventType t, int marsid, String message){
        super(t, marsid);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
