package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Quote;
import be.howest.ti.mars.logic.domain.User;
import io.netty.util.internal.StringUtil;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MarsH2RepositoryTest {
    private static final String URL = "jdbc:h2:~/mars-db";

    @BeforeAll
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url","jdbc:h2:~/mars-db",
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories.configure(dbProperties, WebClient.create(Vertx.vertx()));
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
    }

    @Test
    void createUser(){
        User newuser = new User(1,"tester", -1);
        Assertions.assertTrue(newuser.getMarsid() == 1 && newuser.getName().equals("tester") && newuser.getContactid() == -1);
        User user = Repositories.getH2Repo().createUser(newuser);
        Assertions.assertTrue(user.getMarsid() == 1 && user.getName().equals("tester"));
    }

    @Test
    void getUser(){
        User newuser = new User(1,"tester",-1);
        Repositories.getH2Repo().createUser(newuser);
        Assertions.assertEquals(Repositories.getH2Repo().getUser(1).getName(), newuser.getName());
    }

    @Test
    void addContact(){

    }

    @Test
    void getContacts(){

    }

    @Test
    void getQuote() {
        // Arrange
        int id = 1;

        // Act
        Quote quote = Repositories.getH2Repo().getQuote(id);

        // Assert
        Assertions.assertTrue(quote != null && !StringUtil.isNullOrEmpty(quote.getValue()));
    }

    @Test
    void allQuotes(){
        List<Quote> quotes = Repositories.getH2Repo().allQuotes();

        Assertions.assertTrue(quotes.size() > 0 && !quotes.get(1).getValue().equals(""));
    }

    @Test
    void updateQuote() {
        // Arrange
        int id = 1;
        String quoteValue = "some value";

        // Act
        Quote quote = Repositories.getH2Repo().updateQuote(id, quoteValue);

        // Assert
        Assertions.assertNotNull(quote);
        Assertions.assertEquals(quoteValue, quote.getValue());
    }

    @Test
    void insertQuote() {
        // Arrange
        String quoteValue = "some value";

        // Act
        Quote quote = Repositories.getH2Repo().insertQuote(quoteValue);

        // Assert
        Assertions.assertNotNull(quote);
        Assertions.assertEquals(quoteValue, quote.getValue());
    }

    @Test
    void deleteQuote() {
        // Arrange
        int id = 1;

        // Act
        Repositories.getH2Repo().deleteQuote(id);

        // Assert
        Assertions.assertNull(Repositories.getH2Repo().getQuote(id));
    }

}
