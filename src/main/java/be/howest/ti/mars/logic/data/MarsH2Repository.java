package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Chat;
import be.howest.ti.mars.logic.domain.ChatMessage;
import be.howest.ti.mars.logic.domain.NotificationData;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import org.h2.tools.Server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
This is only a starter class to use an H2 database.
In this start-project there was no need for an Interface MarsRepository.
Please always use interfaces when needed.
To make this class useful, please complete it with the topics seen in the module OOA & SD
- Make sure the conf/config.json (local & production) properties are correct.
- The h2 web console is available at http://localhost:9000
- The h2 database file is located at ~/mars-db
- Don't create tables manually but create sql files in the folder resources.
  - With each deploy create the db structure from scratch (it's just a poc).
  - Two starter sql script are already given.
- Hint:
  - Mocking this repository for testing purposes is not needed.
    Create database creating and population scripts in plain SQL (resources folder).
    Use the @Before or @BeforeEach (depending on the type of test) to quickly create a populated database.
 */
public class MarsH2Repository {
    private static final Logger LOGGER = Logger.getLogger(MarsH2Repository.class.getName());
    private static final String SQL_INSERT_USER = "insert into user(marsid, name) values (?,?)";
    private static final String SQL_GET_USER = "select * from user where marsid = ?";
    private static final String SQL_GET_USER_WITH_CONTACTID = "select * from marsidcontactid where contactid = ?";
    private static final String SQL_SET_CONTACTID = "insert into marsidcontactid (marsid) values(?)";
    private static final String SQL_GET_CONTACTID = "select contactid from marsidcontactid where marsid = ?";
    private static final String SQL_GET_CONTACTS_MARSID = "select marsid, contactid from marsidcontactid m where contactid in (select us.contactid from user u left join usercontacts us on u.marsid = us.marsid where u.marsid = ?)";
    private static final String SQL_INSERT_CONTACT = "insert into usercontacts (marsid, contactid) values(?,?)";
    private static final String SQL_DELETE_CONTACT = "delete from usercontacts where marsid = ? and contactid = ?";
    private static final String SQL_GET_CHATIDS1 = "select chatid, marsid1 from chats where marsid2 = ?";
    private static final String SQL_GET_CHATIDS2 = "select chatid, marsid2 from chats where marsid1 = ?";
    private static final String SQL_GET_MESSAGES = "select * from chatmessages where chatid = ?";
    private static final String SQL_GET_PARTICIPATING_CHATTERS = "select marsid1, marsid2 from chats where chatid = ?";
    private static final String SQL_INSERT_CHAT = "insert into chats (marsid1, marsid2) values(?,?)";
    private static final String SQL_INSERT_CHAT_MESSAGE = "insert into chatmessages(chatid, marsid, content) values(?,?,?)";
    private static final String SQL_INSERT_SUBSCRIPTION= "update user set endpoint = ?, userkey = ?, auth = ? where marsid = ?";
    private static final String SQL_RETRIEVE_SUBSCRIPTION= "select endpoint, userkey, auth from user where marsid = ?";


    private final Server dbWebConsole;
    private final String username;
    private final String password;
    private final String url;

    public MarsH2Repository(String url, String username, String password, int console) {
        try {
            this.username = username;
            this.password = password;
            this.url = url;
            this.dbWebConsole = Server.createWebServer(
                    "-ifNotExists",
                    "-webPort", String.valueOf(console)).start();
            LOGGER.log(Level.INFO, "Database webconsole started on port: {0}", console);
            this.generateData();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "DB configuration failed", ex);
            throw new RepositoryException("Could not configure MarsH2repository");
        }
    }

    public User createUser(User user){
        User currentuser = user;
        try(
                Connection con = getConnection();
                PreparedStatement stmnt = con.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
        ){
            stmnt.setInt(1,currentuser.getMarsid());
            stmnt.setString(2,currentuser.getName());

            //check if an update occurred
            int affectedRows = stmnt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try(ResultSet generatedKeys = stmnt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    currentuser.setContactid(setContactid(currentuser.getMarsid()));
                    return user;
                }else{
                    throw new SQLException("Creating user failed, no row affected");
                }
            }

        }catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create user.", ex);
            throw new RepositoryException("Could not get user");
        }
    }

    public int setContactid(int marsid){
        try(
                Connection con = getConnection();
                PreparedStatement stmnt = con.prepareStatement(SQL_SET_CONTACTID, Statement.RETURN_GENERATED_KEYS)
        ){
            stmnt.setInt(1,marsid);
            stmnt.executeUpdate();
            try(ResultSet generatedKeys = stmnt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    return generatedKeys.getInt("contactid");
                }
            }
            throw new RepositoryException("Could not add new users contactid");
        }catch(SQLException ex){
            LOGGER.log(Level.SEVERE,"Could not add new users contactid",ex);
            throw new RepositoryException("Could not add user");
        }
    }

    public int getContactid(int marsid){
        //get contactid from specific user
        try(
                Connection con = getConnection();
                PreparedStatement stmnt = con.prepareStatement(SQL_GET_CONTACTID);
        ){
            stmnt.setInt(1,marsid);
            try(ResultSet rs = stmnt.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
                else{
                    throw new SQLException("Could not get contactid");
                }
            }
        }catch(SQLException ex){
            LOGGER.log(Level.SEVERE,"Failed to get contactid", ex);
            throw new RepositoryException("Could not get contactid.");
        }
    }

    public User getUserByContactid(int contactid){
        try(
                Connection con = getConnection();
                PreparedStatement stmnt = con.prepareStatement(SQL_GET_USER_WITH_CONTACTID)
        ){
            stmnt.setInt(1,contactid);
            try(ResultSet rs = stmnt.executeQuery()){
                if(rs.next()){
                    int mid = rs.getInt("marsid");
                    return new User(mid,"",contactid);
                }
                else{
                    throw new RepositoryException("Failed to get user with their contactid");
                }
            }
        }catch(SQLException ex){
            LOGGER.log(Level.SEVERE,"Failed to get user by their contactid.", ex);
            throw new RepositoryException("Could not get user using their contactid.");
        }
    }

    public User getUser(int marsid){
        try(
            Connection con = getConnection();
            PreparedStatement stmnt = con.prepareStatement(SQL_GET_USER)
        ){
            stmnt.setInt(1,marsid);
            try(ResultSet rs = stmnt.executeQuery()){
                if(rs.next()){
                    int mid = rs.getInt("marsid");
                    String name = rs.getString("name");
                    int contactid = getContactid(mid);
                    return new User(mid,name,contactid);
                }
                else{
                    throw new RepositoryException("Failed to get user");
                }
            }
        }catch(SQLException ex){
            LOGGER.log(Level.SEVERE,"Failed to get user.", ex);
            throw new RepositoryException("Could not get user.");
        }
    }

    public List<User> getContacts(int marsid){
        try(
                Connection con = getConnection();
                PreparedStatement stmnt = con.prepareStatement(SQL_GET_CONTACTS_MARSID);
        ){
            stmnt.setInt(1,marsid);
            ResultSet rs = stmnt.executeQuery();
            List<User> contacts = new ArrayList<>();
            while(rs.next()){
                //we retrieve the contacts their marsid and contactid, then add it to the array and call the getuser function with their current marsid to show their name
                contacts.add(new User(getUser(rs.getInt(1)).getName(),rs.getInt(2)));
            }
            return contacts;
        }catch(SQLException ex){
            LOGGER.log(Level.SEVERE,"Failed to get contacts");
            throw new RepositoryException("Could not get contacts");
        }
    }

    public boolean addContact(int marsid, int contactid){
        try(
             Connection con = getConnection();
             PreparedStatement stmnt = con.prepareStatement(SQL_INSERT_CONTACT);
        ){
            stmnt.setInt(1, marsid);
            stmnt.setInt(2,contactid);
            int affectedrows = stmnt.executeUpdate();
            if(affectedrows == 0){
                throw new SQLException("Adding contact failed, no rows affected.");
            }
            return true;
        }catch(SQLException ex){
            LOGGER.log(Level.SEVERE,"Failed to add contact to contacts");
            throw new RepositoryException("Could not add contact to contacts");
        }
    }

    public boolean deleteContact(int marsid, int contactid){
        try(
            Connection con = getConnection();
            PreparedStatement stmnt = con.prepareStatement(SQL_DELETE_CONTACT);
        ){
            stmnt.setInt(1, marsid);
            stmnt.setInt(2,contactid);
            stmnt.execute();
        }catch(SQLException ex){
            LOGGER.log(Level.INFO, "Could not remove contact from contacts", ex);
            throw new RepositoryException("Could not remove the contact from contacts");
        }
        return true;
    }

    public List<Chat> getChatids(int marsid){
        try(
                Connection con = getConnection();
                PreparedStatement stmnt1 = con.prepareStatement(SQL_GET_CHATIDS1);
                PreparedStatement stmnt2 = con.prepareStatement(SQL_GET_CHATIDS2);
        ){
            stmnt1.setInt(1,marsid);
            stmnt2.setInt(1,marsid);
            ResultSet rs1 = stmnt1.executeQuery();
            ResultSet rs2 = stmnt2.executeQuery();
            return handleGetChatids(rs1,rs2);
        }catch(SQLException ex){
            LOGGER.log(Level.SEVERE,"Failed to get chatids", ex);
            throw new RepositoryException("Could not retrieve chatids");
        }
    }

    private List<Chat> handleGetChatids(ResultSet rs1, ResultSet rs2){
        List<Chat> chats = new ArrayList<>();
        try{
            while(rs1.next()){
                int chatid = rs1.getInt(1);
                int currmarsid = rs1.getInt(2);
                chats.add(new Chat(chatid,getUser(currmarsid).getName()));
            }
            while(rs2.next()){
                int chatid = rs2.getInt(1);
                int currmarsid = rs2.getInt(2);
                String name = getUser(currmarsid).getName();
                if(chats.stream().noneMatch(chat -> chat.getChatid() == chatid)){
                    chats.add(new Chat(chatid, name));
                }
            }
            return chats;
        }catch(SQLException ex){
            LOGGER.log(Level.SEVERE,"Failed to get chatids");
            throw new RepositoryException("Could not retrieve chatids");
        }
    }

    public List<ChatMessage> getMessages(int marsid, int chatid){
        List<ChatMessage> chats = new ArrayList<>();
        try(
            Connection con = getConnection();
            PreparedStatement stmnt = con.prepareStatement(SQL_GET_PARTICIPATING_CHATTERS);
            PreparedStatement stmnt2 = con.prepareStatement(SQL_GET_MESSAGES);
        ){
            stmnt.setInt(1,chatid);
            stmnt2.setInt(1,chatid);
            ResultSet rs = stmnt.executeQuery();
            if(rs.next() && rs.getInt(1) == marsid || rs.getInt(2) == marsid){
                ResultSet rs2 = stmnt2.executeQuery();
                while(rs2.next()){
                    int currusermid = rs2.getInt(2);
                    chats.add(new ChatMessage(chatid, getUser(currusermid).getName(),rs2.getString(3),rs2.getString(4)));
                }
                return chats;
            }
            throw new RepositoryException("This marsid is not in this chat");
        }catch(SQLException ex){
            LOGGER.log(Level.SEVERE,"Failed to get messages", ex);
            throw new RepositoryException("Could not retrieve messages");
        }
    }

    public int createChat(int marsiduser1, int marsiduser2){
        try(
                Connection con = getConnection();
                PreparedStatement stmnt = con.prepareStatement(SQL_INSERT_CHAT, Statement.RETURN_GENERATED_KEYS);
        ){
            stmnt.setInt(1,marsiduser1);
            stmnt.setInt(2,marsiduser2);
            int affectedrows = stmnt.executeUpdate();

            if(affectedrows == 0){
                throw new SQLException("Creating chat failed");
            }

            try(ResultSet generatedKeys = stmnt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }else{
                    throw new SQLException("Creating user failed, no row affected");
                }
            }
        }catch(SQLException ex){
            LOGGER.log(Level.SEVERE, "Failed to add chat to database", ex);
            throw new RepositoryException("Failed to add chat");
        }
    }

    public boolean insertChatMessage(int chatid, int marsid, String content){
        try(
                Connection con = getConnection();
                PreparedStatement stmnt = con.prepareStatement(SQL_INSERT_CHAT_MESSAGE);
        ){
            stmnt.setInt(1,chatid);
            stmnt.setInt(2,marsid);
            stmnt.setString(3,content);
            int affectedrows = stmnt.executeUpdate();
            if(affectedrows == 0){
                throw new SQLException("Adding message failed");
            }
            return true;
        }catch(SQLException ex){
            LOGGER.log(Level.SEVERE,"Failed to add message to the chat", ex);
            throw new RepositoryException("Failed to add message");
        }
    }

    public void insertUserPushSubscription(int marsid, NotificationData subscription){
        try(
                Connection con = getConnection();
                PreparedStatement stmnt = con.prepareStatement(SQL_INSERT_SUBSCRIPTION);
        ){
            stmnt.setString(1,subscription.getEndpoint());
            stmnt.setString(2,subscription.getUserkey());
            stmnt.setString(3,subscription.getAuth());
            stmnt.setInt(4,marsid);
            int affectedrows = stmnt.executeUpdate();
            if(affectedrows == 0){
                throw new SQLException("Failed to add subscription");
            }
        }catch(SQLException ex) {
            LOGGER.log(Level.SEVERE,"Failed to add push subscription to user", ex);
        }
    }

    public NotificationData retrieveSubscriptionDataWithMarsID(int marsid){
        try(
                Connection con = getConnection();
                PreparedStatement stmnt = con.prepareStatement(SQL_RETRIEVE_SUBSCRIPTION);
        ){
            stmnt.setInt(1,marsid);
            try(ResultSet rs = stmnt.executeQuery()){
                if(rs.next()){
                    String endpoint = rs.getString(1);
                    String userkey = rs.getString(2);
                    String auth = rs.getString(3);
                    return new NotificationData(endpoint, userkey, auth);
                }
                else{
                    throw new SQLException("Failed to retrieve data");
                }
            }
        }catch (SQLException ex) {
            LOGGER.log(Level.SEVERE,"Failed to retrieve push subscription data", ex);
            throw new RepositoryException("Could not retrieve data.");
        }
    }

    public void cleanUp() {
        if (dbWebConsole != null && dbWebConsole.isRunning(false))
            dbWebConsole.stop();
    }

    public void generateData() {
        try {
            executeScript("db-create.sql");
            executeScript("db-populate.sql");
        } catch (IOException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Execution of database scripts failed.", ex);
        }
    }

    private void executeScript(String fileName) throws IOException, SQLException {
        String createDbSql = readFile(fileName);
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(createDbSql);
        ) {
            stmt.executeUpdate();
        }
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null)
            throw new RepositoryException("Could not read file: " + fileName);

        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
