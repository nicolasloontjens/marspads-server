package be.howest.ti.mars.logic.domain;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatMessage {
    private final int chatid;
    private final String name;
    private final String content;
    private Date timestamp;
    private static final Logger LOGGER = Logger.getLogger(ChatMessage.class.getName());

    public ChatMessage(int chatid, String name, String content, String stringtimestamp){
        this.chatid = chatid;
        this.name = name;
        this.content = content;
        try{
            timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(stringtimestamp);
        }catch(ParseException ex){
            LOGGER.log(Level.SEVERE,"Could not parse timestamp, assigned current timestamp as value");
            timestamp = new Date();
        }
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

    public Date getTimestamp() {
        return timestamp;
    }
}
