package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Chat;
import be.howest.ti.mars.logic.domain.ChatMessage;
import be.howest.ti.mars.logic.domain.Quote;
import be.howest.ti.mars.logic.domain.User;
import com.sun.tools.jconsole.JConsoleContext;
import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<User> getContacts(int marsid) {
        List<User> contacts = new ArrayList<>();
        System.out.println(marsidcontacts.values());
        for(Integer i : marsidcontacts.values()){
            System.out.println(i);
            if(marsidcontacts.get(marsid) == i){
                contacts.add(new User("john",i));
            }
        }
        return contacts;
    }

    @Override
    public boolean addContact(int marsid, int contactid) {
        System.out.println(marsid + contactid);
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
    public Quote getQuote(int quoteId) {
        return new Quote(quoteId, SOME_QUOTE);
    }

    @Override
    public List<Quote> allQuotes() {
        List<Quote> quotes = new ArrayList<>();
        quotes.add(new Quote(1,"an interesting quote"));
        quotes.add(new Quote(2,"an even more interesting quote"));
        return quotes;
    }

    @Override
    public Quote createQuote(String quote) {
        return new Quote(1, quote);
    }

    @Override
    public Quote updateQuote(int quoteId, String quote) {
        return new Quote(quoteId, quote);
    }

    @Override
    public void deleteQuote(int quoteId) {
    }

    @Override
    public Future<Quote> getRandomQuote() {
        Promise<Quote> promise = Promise.promise();
        promise.complete(new Quote(1, SOME_QUOTE));
        return promise.future();
    }
}
