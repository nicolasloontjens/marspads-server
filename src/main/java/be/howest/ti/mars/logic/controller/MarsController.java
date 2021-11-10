package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Quote;
import be.howest.ti.mars.logic.domain.User;
import io.vertx.core.Future;

import java.util.List;

public interface MarsController {
    User createUser(int marsid);

    User getUser(int marsid);

    List<User> getContacts(int marsid);

    Quote getQuote(int quoteId);

    List<Quote> allQuotes();

    Quote createQuote(String quote);

    Quote updateQuote(int quoteId, String quote);

    void deleteQuote(int quoteId);

    Future<Quote> getRandomQuote();
}
