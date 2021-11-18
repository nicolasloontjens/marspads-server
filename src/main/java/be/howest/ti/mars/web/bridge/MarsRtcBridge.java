package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.domain.Chatroom;
import be.howest.ti.mars.logic.domain.events.BroadcastEvent;
import be.howest.ti.mars.logic.domain.events.EventFactory;
import be.howest.ti.mars.logic.domain.events.IncomingEvent;
import be.howest.ti.mars.logic.domain.events.OutgoingEvent;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;


/**
 * In the MarsRtcBridge class you will find one example function which sends a message on the message bus to the client.
 * The RTC bridge is one of the class taught topics.
 * If you do not choose the RTC topic you don't have to do anything with this class.
 * Otherwise, you will need to expand this bridge with the websockets topics shown in the other modules.
 *
 * Compared to the other classes only the bare minimum is given.
 * The client-side starter project does not contain any teacher code about the RTC topic.
 * The rtc bridge is already initialized and configured in the WebServer.java.
 * No need to change the WebServer.java
 *
 * As a proof of concept (poc) one message to the client is sent every 30 seconds.
 *
 * The job of the "bridge" is to bridge between websockets events and Java (the controller).
 * Just like in the openapi bridge, keep business logic isolated in the package logic.
 * <p>
 */
public class MarsRtcBridge {
    private EventBus eb;
    private static final String CHNL_TO_CLIENTS = "events.to.clients";

    public SockJSHandler createSockJSHandler(Vertx vertx) {
        final SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        final PermittedOptions inbound = new PermittedOptions().setAddressRegex("events\\..+");
        final PermittedOptions outbound = inbound;

        final SockJSBridgeOptions options = new SockJSBridgeOptions().addInboundPermitted(inbound).addOutboundPermitted(outbound);

        sockJSHandler.bridge(options);
        return sockJSHandler;
    }

    public void handleIncomingMessage(Message<JsonObject> msg){
        System.out.println(msg.body());
        IncomingEvent incomingEvent = EventFactory.getInstance().createIncomingEvent(msg.body());
        OutgoingEvent outgoingEvent = Chatroom.handleEvent(incomingEvent);
        handleOutgoingEvent(outgoingEvent);
    }

    public void setEb(Vertx vertx){
        eb = vertx.eventBus();
    }

    private void handleOutgoingEvent(OutgoingEvent outgoingEvent){
        switch(outgoingEvent.getType()){
            case BROADCAST:
                broadcastMessage((BroadcastEvent) outgoingEvent);
                break;
            case DISCARD:
                break;
        }
    }

    private void broadcastMessage(BroadcastEvent event){
        eb.publish(CHNL_TO_CLIENTS, event.getMessage());
    }
}
