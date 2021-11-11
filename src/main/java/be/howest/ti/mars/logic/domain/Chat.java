package be.howest.ti.mars.logic.domain;

public class Chat {
    private final int chatid;
    private final String username;

    public Chat(int chatid, String username){
        this.chatid = chatid;
        this.username = username;
    }

    public int getChatid() {
        return chatid;
    }

    public String getUsername() {
        return username;
    }
}
