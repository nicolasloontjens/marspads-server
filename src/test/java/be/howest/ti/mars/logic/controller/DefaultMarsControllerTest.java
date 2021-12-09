package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Chat;
import be.howest.ti.mars.logic.domain.ChatMessage;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultMarsControllerTest {

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
    }

    private MarsController createUserAndContacts(){
        MarsController marsController = new DefaultMarsController();
        marsController.createUser(1);
        marsController.createUser(2);
        marsController.addContact(1,2);
        return marsController;
    }

    @Test
    void createUser(){
        MarsController marsController = new DefaultMarsController();

        User user = marsController.createUser(1);
        assertEquals(1, user.getContactid());
    }

    @Test
    void createUserError(){
        MarsController marsController = new DefaultMarsController();

        marsController.createUser(1);
        assertThrows(RepositoryException.class,() -> {
            marsController.createUser(1);
        });
    }

    @Test
    void getUserError(){
        MarsController marsController = new DefaultMarsController();
        assertThrows(RepositoryException.class, () -> {
            marsController.getUser(1);
        });
    }

    @Test
    void getUser(){
        MarsController marsController = createUserAndContacts();
        User user = marsController.getUser(1);
        assertEquals(1, user.getMarsid());
    }

    @Test
    void getContactidError(){
        assertThrows(RepositoryException.class,() -> {
            Repositories.getH2Repo().getContactid(1);
        });
    }

    @Test
    void getContacts(){
        MarsController marsController = new DefaultMarsController();
        marsController.createUser(1);
        marsController.createUser(2);
        assertEquals(0,marsController.getContacts(1).size());
    }

    @Test
    void addContact(){
        MarsController marsController = createUserAndContacts();
        assertEquals(1,marsController.getContacts(1).size());
    }

    @Test
    void addContactErrorTest(){
        MarsController marsController = createUserAndContacts();
        assertThrows(RepositoryException.class,() -> {
            marsController.addContact(1,2);
        });

    }

    @Test
    void deleteContact(){
        MarsController marsController = createUserAndContacts();
        assertEquals(1,marsController.getContacts(1).size());
        marsController.deleteContact(1,2);
        assertEquals(0,marsController.getContacts(1).size());
    }

    @Test
    void createChatid(){
        MarsController marsController = createUserAndContacts();
        marsController.addChatid(1,2);
        Chat chat = marsController.getChatids(1).get(0);
        Chat chat1 = new Chat(1,"Test",1);
        assertNotEquals("",chat1.getUsername());
        assertEquals(chat1.getChatid(), chat.getChatid());
        assertEquals(1,chat1.getContactid());
    }

    @Test
    void getChatids(){
        MarsController marsController = createUserAndContacts();
        assertEquals(0,marsController.getChatids(1).size());
    }

    @Test
    void createMessages(){
        MarsController marsController = createUserAndContacts();
        marsController.createUser(3);
        marsController.addChatid(1,2);
        marsController.addChatMessage(1,1,"Test content");
        ChatMessage chatMessage = new ChatMessage(1,"Test","Test content", "2021-11-11 08:55:01");
        List<ChatMessage> messagesfromcontroller = marsController.getMessages(1,1);
        assertEquals(1,messagesfromcontroller.size());
        assertEquals(chatMessage.getChatid(),messagesfromcontroller.get(0).getChatid());
        assertEquals(chatMessage.getContent(),messagesfromcontroller.get(0).getContent());
        assertNotEquals("",chatMessage.getName());
        assertNotEquals(chatMessage.getTimestamp(),messagesfromcontroller.get(0).getTimestamp());
        assertThrows(RepositoryException.class,() -> {
            List<ChatMessage> chatMessage1 = marsController.getMessages(3,1);
        });

    }

    @Test
    void getMessages(){
        MarsController marsController = new DefaultMarsController();
        marsController.createUser(1);
        marsController.createUser(2);
        marsController.addContact(1,2);
    }

}
