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

    /*
    todo: fix test
    @Test
    void testHandleChatRequest(){
        ChatRequestEvent event = new ChatRequestEvent(1,2,0);
        Chatroom chatroom = Chatroom.getInstance();
        UnicastEvent result = (UnicastEvent) chatroom.handleEvent(event);
        assertEquals(UnicastEvent.class,result.getClass());
        assertEquals(EventType.UNICAST,result.getType());
        assertEquals(0,result.getValue());
        assertEquals(1,result.getSendermid());
        assertNotEquals("",result.getSendername());
    }
    */
}