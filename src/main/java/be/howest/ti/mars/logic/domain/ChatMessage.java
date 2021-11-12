package be.howest.ti.mars.logic.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatMessage {
    private final int chatid;
    private final String name;
    private final String content;
    private final String timestamp;
    private static final Logger LOGGER = Logger.getLogger(ChatMessage.class.getName());

    public ChatMessage(int chatid, String name, String content, String timestamp){
        this.chatid = chatid;
        this.name = name;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getChatid() {
        return chatid;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
