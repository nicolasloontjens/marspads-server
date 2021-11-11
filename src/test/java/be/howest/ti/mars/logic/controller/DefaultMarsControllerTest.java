package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Quote;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.exceptions.MarsResourceNotFoundException;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;
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
        Repositories.configure(dbProperties, WebClient.create(Vertx.vertx()));
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
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

        assertThrows(MarsResourceNotFoundException.class, () -> {
            marsController.getUser(1);
        });
    }

    @Test
    void getUser(){
        MarsController marsController = new DefaultMarsController();
        marsController.createUser(1);
        User user = marsController.getUser(1);
        assertEquals(1, user.getMarsid());
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
        MarsController marsController = new DefaultMarsController();
        marsController.createUser(1);
        marsController.createUser(2);
        marsController.addContact(1,2);
        assertEquals(1,marsController.getContacts(1).size());
    }

    @Test
    void addContactErrorTest(){
        MarsController marsController = new DefaultMarsController();
        marsController.createUser(1);
        marsController.createUser(2);
        marsController.addContact(1,2);
        assertThrows(RepositoryException.class,() -> {
            marsController.addContact(1,2);
        });

    }

    @Test
    void deleteContact(){
        MarsController marsController = new DefaultMarsController();
        marsController.createUser(1);
        marsController.createUser(2);
        marsController.addContact(1,2);
        assertEquals(1,marsController.getContacts(1).size());
        marsController.deleteContact(1,2);
        assertEquals(0,marsController.getContacts(1).size());
    }


    @Test
    void getQuote() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        Quote quote = sut.getQuote(0);

        //Assert
        assertTrue(quote != null && StringUtils.isNoneBlank(quote.getValue()));
    }

    @Test
    void allQuotes(){
        MarsController marsController = new DefaultMarsController();

        List<Quote> quotes = marsController.allQuotes();

        assertTrue(quotes.size() > 0 && !quotes.get(1).getValue().equals(""));
    }

    @Test
    void deleteQuote() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        sut.deleteQuote(0);

        //Assert
        assertThrows(MarsResourceNotFoundException.class, () -> sut.getQuote(0));
    }

    @Test
    void updateQuote() {
        // Arrange
        MarsController sut = new DefaultMarsController();
        Quote quote = sut.createQuote("some value");

        // Act
        Quote updatedQuoted = sut.updateQuote(quote.getId(), "new value");

        //Assert
        assertEquals("new value", updatedQuoted.getValue());
    }

    @Test
    void createQuote() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        Quote quote = sut.createQuote("new value");

        //Assert
        assertEquals("new value", quote.getValue());
    }

    @Test
    void getQuoteWithUnknownIdThrowsNotFound() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(MarsResourceNotFoundException.class, () -> sut.getQuote(-1));
    }

    @Test
    void createQuoteWithEmptyQuoteThrowsIllegalArgument() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.createQuote(""));
    }

    @Test
    void updateQuoteWithWrongIdThrowsIllegalArgument() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.updateQuote(-1, "some quote"));
    }

    @Test
    void updateQuoteWithUnknownIdThrowsMarsResourceNotFoundException() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(MarsResourceNotFoundException.class, () -> sut.updateQuote(1000, "some quote"));
    }

    @Test
    void updateQuoteWithEmptyQuoteThrowsIllegalArgument() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.updateQuote(1, ""));
    }

    @Test
    void deleteQuoteWithUnknownIdThrowsNotFound() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(MarsResourceNotFoundException.class, () -> sut.deleteQuote(-1));
    }

}
