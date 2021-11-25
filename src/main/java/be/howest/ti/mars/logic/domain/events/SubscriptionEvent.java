package be.howest.ti.mars.logic.domain.events;

import io.vertx.core.json.JsonObject;

public class SubscriptionEvent extends IncomingEvent{

    private final JsonObject data;
    public SubscriptionEvent(int marsid, JsonObject data) {
        super(EventType.SUBSCRIPTION, marsid);
        this.data = data;
    }

    public JsonObject getData() {
        return data;
    }
}
