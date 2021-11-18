package be.howest.ti.mars.logic.domain;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.domain.events.*;

public class Chatroom {

    private static final MarsController controller = new DefaultMarsController();

    public static OutgoingEvent handleEvent(IncomingEvent e){
        OutgoingEvent outgoingEvent = null;
        switch(e.getType()){
            case MESSAGE:
                outgoingEvent = handlePublicMessageEvent((MessageEvent) e);
                break;
            case PRIVATEMESSAGE:
                outgoingEvent = handlePrivateMessageEvent((PrivateMessageEvent) e);
                break;
        }
        return outgoingEvent;
    }

    private static OutgoingEvent handlePublicMessageEvent(MessageEvent e){
        String outgoingMessage = String.format("%s: %s",controller.getUser(Integer.parseInt( e.getMarsid())).getName(), e.getMessage());
        return EventFactory.getInstance().createBroadcastEvent(outgoingMessage);
    }

    private static OutgoingEvent handlePrivateMessageEvent(PrivateMessageEvent e){
        String outgoingMessage = String.format("%s: %s", controller.getUser(Integer.parseInt( e.getMarsid())).getName(), e.getMessage());
        storeMessageInDatabase(e);
        return EventFactory.getInstance().createMulticastEvent(outgoingMessage, Integer.parseInt(e.getChatid()));
    }

    private static void storeMessageInDatabase(PrivateMessageEvent e){
        int chatid = Integer.parseInt(e.getChatid());
        int marsid = Integer.parseInt(e.getMarsid());
        String messageContents = e.getMessage();
    }
}
