package be.howest.ti.mars.logic.domain;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return chatid == chat.chatid && username.equals(chat.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatid, username);
    }
}
