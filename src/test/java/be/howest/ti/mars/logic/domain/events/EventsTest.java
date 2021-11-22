package be.howest.ti.mars.logic.domain.events;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class EventsTest {

    private EventFactory getEventFactory(){
        return new EventFactory();
    }

    private JsonObject createbasicjsonobject(){
        JsonObject obj = new JsonObject();
        obj.put("marsid", 1);
        obj.put("message","test");
        return obj;
    }


    @Test
    void testCreateMessageEvent(){

        JsonObject obj = createbasicjsonobject();
        obj.put("type","message");
        IncomingEvent event = getEventFactory().createIncomingEvent(obj);
        assertEquals( MessageEvent.class,event.getClass());
        assertEquals(EventType.MESSAGE,event.getType());
        assertEquals(1,event.getMarsid());
    }

    @Test
    void testDiscardEvent(){
        JsonObject obj = createbasicjsonobject();
        obj.put("type", "");
        IncomingEvent event = getEventFactory().createIncomingEvent(obj);
        assertEquals(DiscardEvent.class, event.getClass());
        assertEquals(EventType.DISCARD,event.getType());
        assertEquals(1,event.getMarsid());
    }

    @Test
    void testChatRequestEvent(){
        JsonObject obj = createbasicjsonobject();
        obj.put("type","chatrequest");
        obj.put("sendermid", 1);
        obj.put("receivercontactid", 2);
        obj.put("answer", 0);
        ChatRequestEvent event = (ChatRequestEvent) getEventFactory().createIncomingEvent(obj);
        assertEquals(ChatRequestEvent.class,event.getClass());
        assertEquals(EventType.REQUEST,event.getType());
        assertEquals(1,event.getMarsid());
        assertEquals(2,event.getReceivercontactid());
        assertEquals(0,event.getValue());
    }

    @Test
    void testPrivateMessageEvent(){
        JsonObject obj = createbasicjsonobject();
        obj.put("type", "privatemessage");
        obj.put("chatid", 1);
        PrivateMessageEvent event = (PrivateMessageEvent) getEventFactory().createIncomingEvent(obj);
        assertEquals(PrivateMessageEvent.class,event.getClass());
        assertEquals(EventType.PRIVATEMESSAGE,event.getType());
        assertEquals(1,event.getMarsid());
        assertEquals("test",event.getMessage());
        assertEquals("1",event.getChatid());
    }

    @Test
    void testOutgoingEvent(){
        BroadcastEvent event = getEventFactory().createBroadcastEvent("hello");
        assertEquals(BroadcastEvent.class, event.getClass());
        assertEquals(EventType.BROADCAST,event.getType());
        assertEquals("hello",event.getMessage());
    }


}
