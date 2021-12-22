package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.domain.Chat;
import be.howest.ti.mars.logic.domain.ChatMessage;
import be.howest.ti.mars.logic.domain.User;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * The Response class is responsible for translating the result of the controller into
 * JSON responses with an appropriate HTTP code.
 */
public class Response {

    private Response() { }

    public static void sendUser(RoutingContext ctx, User user){
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(user));
    }

    public static void sendContacts(RoutingContext ctx, List<User> contacts){
        sendJsonResponse(ctx, 200,new JsonArray(contacts));
    }

    public static void sendChatids(RoutingContext ctx, List<Chat> chats){
        sendJsonResponse(ctx, 200, new JsonArray(chats));
    }

    public static void sendMessages(RoutingContext ctx, List<ChatMessage> messages){
        sendJsonResponse(ctx, 200, new JsonArray(messages));
    }

    public static void sendEmptyResponse(RoutingContext ctx, int statusCode) {
        ctx.response()
                .setStatusCode(statusCode)
                .end();
    }

    public static void sendGeneralInformation(RoutingContext ctx, int totalfriends, int friendsnearyou, int closestfriend){
        JsonObject obj = new JsonObject();
        obj.put("total friends", totalfriends);
        obj.put("friends near you", friendsnearyou);
        obj.put("closest friend in meters", closestfriend);
        sendJsonResponse(ctx, 200, obj);
    }

    public static void sendUserAddResponse(RoutingContext ctx, int statusCode, String message){
        sendJsonResponse(ctx, statusCode, new JsonObject().put("response",message));
    }

    private static void sendJsonResponse(RoutingContext ctx, int statusCode, Object response) {
        ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(statusCode)
                .end(Json.encodePrettily(response));
    }

    public static void sendFailure(RoutingContext ctx, int code, String quote) {
        sendJsonResponse(ctx, code, new JsonObject()
                .put("failure", code)
                .put("cause", quote));
    }
}
