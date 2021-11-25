package be.howest.ti.mars.logic.domain.events;

import be.howest.ti.mars.logic.domain.NotificationData;
import io.vertx.core.json.JsonObject;

public class SubscriptionEvent extends IncomingEvent{

    private final NotificationData data;
    public SubscriptionEvent(int marsid, JsonObject data) {
        super(EventType.SUBSCRIPTION, marsid);
        data = new NotificationData(data.getString("endpoint"),data.getJsonObject("keys").getString("p256dh"),data.getJsonObject("keys").getString("auth"));
    }

    public NotificationData getData() {
        return data;
    }
}
