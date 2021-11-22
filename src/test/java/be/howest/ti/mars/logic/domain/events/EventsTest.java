package be.howest.ti.mars.logic.domain.events;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class EventsTest {

    private JsonObject createbasicjsonobject(){
        JsonObject obj = new JsonObject();
        obj.put("marsid", 1);
        obj.put("message","test");
        return obj;
    }

    @Test
    void testCreateMessageEvent(){
        EventFactory eventFactory = new EventFactory();
        JsonObject obj = createbasicjsonobject();
        obj.put("type","message");
        IncomingEvent event = eventFactory.createIncomingEvent(obj);
        assertEquals(event.getType(),EventType.MESSAGE);
        assertEquals(event.getMarsid(), 1);
    }

    @Test
    void testDiscardEvent(){
        JsonObject obj = createbasicjsonobject();
    }
}
