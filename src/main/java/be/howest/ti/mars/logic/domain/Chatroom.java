package be.howest.ti.mars.logic.domain;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.domain.events.EventFactory;
import be.howest.ti.mars.logic.domain.events.IncomingEvent;
import be.howest.ti.mars.logic.domain.events.MessageEvent;
import be.howest.ti.mars.logic.domain.events.OutgoingEvent;

public class Chatroom {

    private static final MarsController controller = new DefaultMarsController();

    public static OutgoingEvent handleEvent(IncomingEvent e){
        OutgoingEvent outgoingEvent = null;
        switch(e.getType()){
            case MESSAGE:
                outgoingEvent = handlePublicMessageEvent((MessageEvent) e);
            case PRIVATEMESSAGE:
                break;
        }
        return outgoingEvent;
    }

    private static OutgoingEvent handlePublicMessageEvent(MessageEvent e){
        String outgoingMessage = String.format("<p>%s: %s",controller.getUser(Integer.parseInt( e.getMarsid())).getName(), e.getMessage());
        return EventFactory.getInstance().createBroadcastEvent(outgoingMessage);
    }
}
