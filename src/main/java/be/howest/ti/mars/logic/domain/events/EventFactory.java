package be.howest.ti.mars.logic.domain.events;

import io.vertx.core.json.JsonObject;

public class EventFactory {
    private static final EventFactory instance = new EventFactory();

    public static EventFactory getInstance(){
        return instance;
    }


    public IncomingEvent createIncomingEvent(JsonObject json){
        EventType eventType = EventType.fromString(json.getString("type"));
        String marsid = json.getString("marsid");
        IncomingEvent event = new DiscardEvent(marsid);
        switch(eventType){
            case MESSAGE:
                event = new MessageEvent(marsid, json.getString("message"));
                break;
            case PRIVATEMESSAGE:
                event = new PrivateMessageEvent(marsid,json.getString("message"),json.getString("chatid"));
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


}
