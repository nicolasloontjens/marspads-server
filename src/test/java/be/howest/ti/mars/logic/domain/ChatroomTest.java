package be.howest.ti.mars.logic.domain;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.events.*;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatroomTest {

    private static final String URL = "jdbc:h2:~/mars-db";

    @BeforeAll
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", "jdbc:h2:~/mars-db",
                "username", "",
                "password", "",
                "webconsole.port", 9000));
        Repositories.configure(dbProperties);
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
        MarsController marsController = new DefaultMarsController();
        marsController.createUser(1);
        marsController.createUser(2);
    }

    @Test
    void testHandleMessage(){
        MessageEvent event = new MessageEvent(1,"hello mr fors");
        Chatroom chatroom = Chatroom.getInstance();
        OutgoingEvent result = chatroom.handleEvent(event);
        assertEquals(BroadcastEvent.class,result.getClass());
        assertNotEquals("",result.getMessage());
        assertEquals(EventType.BROADCAST, result.getType());
    }

    @Test
    void testHandlePrivateMessage(){
        PrivateMessageEvent event = new PrivateMessageEvent(1,"","2");
        Chatroom chatroom = Chatroom.getInstance();
        OutgoingEvent result = chatroom.handleEvent(event);
        assertEquals(MulticastEvent.class,result.getClass());
        assertEquals(EventType.MULTICAST,result.getType());
        assertNotEquals("",result.getMessage());
    }

    @Test
    void testHandleChatRequest(){
        ChatRequestEvent event = new ChatRequestEvent(1,2,1);
        Chatroom chatroom = Chatroom.getInstance();
        UnicastEvent result = (UnicastEvent) chatroom.handleEvent(event);
        assertEquals(UnicastEvent.class,result.getClass());
        assertEquals(EventType.UNICAST,result.getType());
        assertEquals(1,result.getValue());
        assertEquals(1,result.getSendermid());
        assertNotEquals("",result.getSendername());
    }

    @Test
    void testsendChatRequestNotification(){
        ChatRequestEvent event = new ChatRequestEvent(1,2,1);
        Chatroom chatroom = Chatroom.getInstance();
        assertThrows(NullPointerException.class, () -> {
            chatroom.sendChatRequestNotification(event);
        });
    }

    @Test
    void storeUserSubscriptionInDB(){
        JsonObject obj = new JsonObject();
        JsonObject keys = new JsonObject();
        keys.put("p256dh","a");
        keys.put("auth","b");
        obj.put("endpoint","test");
        obj.put("keys",keys);
        JsonObject finalObj = new JsonObject();
        finalObj.put("marsid", 1);
        finalObj.put("type","subscription");
        finalObj.put("subscription",obj);
        SubscriptionEvent event = (SubscriptionEvent) EventFactory.getInstance().createIncomingEvent(finalObj);
        Chatroom chatroom = Chatroom.getInstance();
        chatroom.storeUserSubscriptionInDatabase(event);
    }
}