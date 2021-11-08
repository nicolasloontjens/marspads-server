package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.Quote;
import io.vertx.core.Future;

import java.util.List;

public interface MarsController {
    Quote getQuote(int quoteId);

    List<Quote> allQuotes();

    Quote createQuote(String quote);

    Quote updateQuote(int quoteId, String quote);

    void deleteQuote(int quoteId);

    Future<Quote> getRandomQuote();
}
