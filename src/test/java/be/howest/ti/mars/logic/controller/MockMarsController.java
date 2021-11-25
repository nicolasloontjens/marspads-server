package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Chat;
import be.howest.ti.mars.logic.domain.ChatMessage;
import be.howest.ti.mars.logic.domain.User;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockMarsController implements MarsController {
    private static final String SOME_QUOTE = "quote";
    private List<User> users = new ArrayList<>();
    private Map<Integer, Integer> marsidcontacts = new HashMap<>();

    @Override
    public User createUser(int marsid){
        User user = new User(marsid,"bob",234);
        users.add(user);
        return user;
    }

    @Override public User getUser(int marsid){
        users.add(new User(marsid,"Joe"));
        return users.get(0);
    }

    @Override
    public User getUserByContactid(int contactid) {
        return null;
    }

    @Override
    public List<User> getContacts(int marsid) {
        List<User> contacts = new ArrayList<>();

        for(Integer i : marsidcontacts.values()){

            if(marsidcontacts.get(marsid) == i){
                contacts.add(new User("john",i));
            }
        }
        return contacts;
    }

    @Override
    public boolean addContact(int marsid, int contactid) {
        if(marsidcontacts.containsKey(marsid)){
            if(marsidcontacts.get(marsid) == contactid){
                return false;
            }
        }
        marsidcontacts.put(marsid, contactid);
        return true;
    }

    @Override
    public boolean deleteContact(int marsid, int contactid) {
        return false;
    }

    @Override
    public List<Chat> getChatids(int marsid){
        return null;
    }

    @Override
    public List<ChatMessage> getMessages(int marsid, int chatid) {
        return null;
    }

    @Override
    public boolean addChatid(int marsid1, int marsid2) {
        return false;
    }

    @Override
    public boolean addChatMessage(int chatid, int marsid, String content) {
        return false;
    }

    @Override
    public void insertUserPushSubscription(int marsid, JsonObject subscription) {

    }
}
