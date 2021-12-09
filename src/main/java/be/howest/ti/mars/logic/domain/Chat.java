package be.howest.ti.mars.logic.domain;

public class Chat {
    private final int chatid;
    private final String username;
    private final int contactid;

    public Chat(int chatid, String username, int contactid){
        this.chatid = chatid;
        this.username = username;
        this.contactid = contactid;
    }

    public int getChatid() {
        return chatid;
    }

    public String getUsername() {
        return username;
    }

    public int getContactid() {
        return contactid;
    }
}
