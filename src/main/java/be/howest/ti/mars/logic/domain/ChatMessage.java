package be.howest.ti.mars.logic.domain;



public class ChatMessage {
    private final int chatid;
    private final String name;
    private final String content;
    private final String timestamp;

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
