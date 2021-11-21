package be.howest.ti.mars.logic.domain.events;

import be.howest.ti.mars.logic.domain.User;
import io.vertx.core.json.JsonObject;

public class EventFactory {
    private static final EventFactory instance = new EventFactory();

    public static EventFactory getInstance(){
        return instance;
    }


    public IncomingEvent createIncomingEvent(JsonObject json){
        EventType eventType = EventType.fromString(json.getString("type"));
        IncomingEvent event;
        if(json.containsKey("marsid")){
            event = new DiscardEvent(json.getInteger("marsid"));
        }
        else{
            event = new DiscardEvent(json.getInteger("sendermid"));
        }
        switch(eventType){
            case MESSAGE:
                event = new MessageEvent(json.getInteger("marsid"), json.getString("message"));
                break;
            case PRIVATEMESSAGE:
                event = new PrivateMessageEvent(json.getInteger("marsid"),json.getString("message"),json.getString("chatid"));
                break;
            case REQUEST:
                event = new ChatRequestEvent(json.getInteger("sendermid"), json.getInteger("receivercontactid"), json.getInteger("answer"));
                break;
        }
        return event;
    }

    public BroadcastEvent createBroadcastEvent(String msg) {
        return new BroadcastEvent(msg);
    }

    public MulticastEvent createMulticastEvent(String msg, int chatid){
        return new MulticastEvent(msg, chatid);
    }

    public UnicastEvent createUnicastEvent(User sender, int receivermid, int value){
        return new UnicastEvent(sender, receivermid,value);
    }
}
