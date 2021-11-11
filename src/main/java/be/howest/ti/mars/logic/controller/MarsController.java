package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Chat;
import be.howest.ti.mars.logic.domain.ChatMessage;
import be.howest.ti.mars.logic.domain.Quote;
import be.howest.ti.mars.logic.domain.User;
import io.vertx.core.Future;

import java.util.List;

public interface MarsController {
    User createUser(int marsid);

    User getUser(int marsid);

    List<User> getContacts(int marsid);

    boolean addContact(int marsid, int contactid);

    boolean deleteContact(int marsid, int contactid);

    List<Chat> getChatids(int marsid);

    List<ChatMessage> getMessages(int marsid, int chatid);

    Quote getQuote(int quoteId);

    List<Quote> allQuotes();

    Quote createQuote(String quote);

    Quote updateQuote(int quoteId, String quote);

    void deleteQuote(int quoteId);

    Future<Quote> getRandomQuote();
}
