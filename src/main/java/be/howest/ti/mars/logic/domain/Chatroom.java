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
            case REQUEST:
                outgoingEvent = handleChatRequest((ChatRequestEvent) e);
                break;
        }
        return outgoingEvent;
    }

    private static OutgoingEvent handlePublicMessageEvent(MessageEvent e){
        String outgoingMessage = String.format("%s: %s",controller.getUser(e.getMarsid()).getName(), e.getMessage());
        return EventFactory.getInstance().createBroadcastEvent(outgoingMessage);
    }

    private static OutgoingEvent handlePrivateMessageEvent(PrivateMessageEvent e){
        String outgoingMessage = String.format("%s: %s", controller.getUser(e.getMarsid()).getName(), e.getMessage());
        storeMessageInDatabase(e);
        return EventFactory.getInstance().createMulticastEvent(outgoingMessage, Integer.parseInt(e.getChatid()));
    }

    private static OutgoingEvent handleChatRequest(ChatRequestEvent e){
        int sendermid = e.getMarsid();
        int receivercontactid = e.getReceivercontactid();
        int receivermid = controller.getUserByContactid(receivercontactid).getMarsid();
        int value = e.getValue();
        UnicastEvent response = null;
        //first check if they already have a chat, if so just discard it
        for(Chat chat : controller.getChatids(sendermid)){
            for(Chat chat2 : controller.getChatids(receivermid)){
                if(chat.getChatid() == chat2.getChatid()){
                    return null;
                }
            }
        }
        switch(value){
            case 0:
            case -1:
                //receiver says no => send no response to sender
                //receiver hasn't seen it yet
                response = EventFactory.getInstance().createUnicastEvent(controller.getUser(sendermid), receivermid, value);
                break;
            case 1:
                //receiver says yes => send yes response to sender and create chat in db
                response = EventFactory.getInstance().createUnicastEvent(controller.getUser(sendermid),sendermid,value);
                controller.addChatid(sendermid,receivermid);
                break;
        }
        return response;
    }

    private static void storeMessageInDatabase(PrivateMessageEvent e){
        int chatid = Integer.parseInt(e.getChatid());
        int marsid = e.getMarsid();
        String messageContents = e.getMessage();
        controller.addChatMessage(chatid, marsid, messageContents);
    }
}
