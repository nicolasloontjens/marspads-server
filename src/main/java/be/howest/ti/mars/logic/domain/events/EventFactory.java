package be.howest.ti.mars.logic.domain.events;

import io.vertx.core.json.JsonObject;

public class EventFactory {
    private static final EventFactory instance = new EventFactory();

    public static EventFactory getInstance(){
        return instance;
    }

    public IncomingEvent createIncomingEvent(JsonObject json){

    }


}
