package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.User;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.*;

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
        Repositories.configure(dbProperties);
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
}
