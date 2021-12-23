package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.domain.Chat;
import be.howest.ti.mars.logic.domain.ChatMessage;
import be.howest.ti.mars.logic.domain.User;
import be.howest.ti.mars.logic.exceptions.MarsResourceNotFoundException;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * In the MarsOpenApiBridge class you will find one handler-method per API operation.
 * The job of the "bridge" is to bridge between JSON (request and response) and Java (the controller).
 * <p>
 * For each API operation you should get the required data from the `Request` class,
 * pass it to the controller and use its result to generate a response in the `Response` class.
 */
public class MarsOpenApiBridge {
    private static final Logger LOGGER = Logger.getLogger(MarsOpenApiBridge.class.getName());
    private final MarsController controller;

    public MarsOpenApiBridge() {
        this.controller = new DefaultMarsController();
    }

    public MarsOpenApiBridge(MarsController controller) {
        this.controller = controller;
    }

    public void createUser(RoutingContext ctx){
        try{
            User user = controller.createUser(Request.from(ctx).getMarsId());
            Response.sendUser(ctx, user);
        }catch(RepositoryException ex){
            Response.sendFailure(ctx, 400,"Could not create user");
        }
    }

    public void getUser(RoutingContext ctx){
        try{
            User user = controller.getUser(Request.from(ctx).getMarsId());
            Response.sendUser(ctx, user);
        }catch(RepositoryException ex){
            Response.sendFailure(ctx, 400, "Error retrieving user");
        }
    }

    public void getContacts(RoutingContext ctx){
        List<User> contacts = controller.getContacts(Request.from(ctx).getMarsId());
        Response.sendContacts(ctx, contacts);
    }

    public void addContact(RoutingContext ctx){
        try{
            boolean res = controller.addContact(Request.from(ctx).getMarsId(),Request.from(ctx).getContactId());
            if(res){
                Response.sendUserAddResponse(ctx,201,"Added contact");
            }
            else{
                Response.sendUserAddResponse(ctx, 400,"Could not add contact");
            }
        }catch(RepositoryException ex){
            Response.sendUserAddResponse(ctx, 400,"Could not add contact");
        }
    }

    public void deleteContact(RoutingContext ctx){
        try{
            controller.deleteContact(Request.from(ctx).getMarsId(),Request.from(ctx).getContactId());
            Response.sendEmptyResponse(ctx, 201);
        }catch(RepositoryException ex) {
            Response.sendFailure(ctx, 400, "Could not remove contact");
        }
    }

    public void getChatids(RoutingContext ctx){
        List<Chat> chats = controller.getChatids(Request.from(ctx).getMarsId());
        Response.sendChatids(ctx, chats);
    }

    public void getMessages(RoutingContext ctx){
        try{
            List<ChatMessage> messages = controller.getMessages(Request.from(ctx).getMarsId(),Request.from(ctx).getChatid());
            Response.sendMessages(ctx, messages);
        }catch(RepositoryException ex){
            Response.sendFailure(ctx, 400, "Could not retrieve messages");
        }
    }

    public void getInformation(RoutingContext ctx){
        Random rand = new SecureRandom();
        int totalfriends = controller.getContacts(Request.from(ctx).getMarsId()).size();
        int closestfriend = 0;
        if(totalfriends  != 0){
            closestfriend = rand.nextInt(500);
        }
        Response.sendGeneralInformation(ctx,totalfriends,rand.nextInt(totalfriends+1),closestfriend);
    }

    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Installing cors handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.INFO, "Installing failure handlers for all operations");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.INFO, "Installing handler for: createUser");
        routerBuilder.operation("createUser").handler(this::createUser);

        LOGGER.log(Level.INFO, "Installing handler for: getUser");
        routerBuilder.operation("getUser").handler(this::getUser);

        LOGGER.log(Level.INFO, "Installing handler for: getContacts");
        routerBuilder.operation("getContacts").handler(this::getContacts);

        LOGGER.log(Level.INFO, "Installing handler for: addContact");
        routerBuilder.operation("addContact").handler(this::addContact);

        LOGGER.log(Level.INFO, "Installing handler for: deleteContact");
        routerBuilder.operation("deleteContact").handler(this::deleteContact);

        LOGGER.log(Level.INFO, "Installing handler for: getChatids");
        routerBuilder.operation("getChatids").handler(this::getChatids);

        LOGGER.log(Level.INFO, "Installing handler for: getMessages");
        routerBuilder.operation("getMessages").handler(this::getMessages);

        LOGGER.log(Level.INFO, "Installing handler for: getInformation");
        routerBuilder.operation("getInformation").handler(this::getInformation);

        LOGGER.log(Level.INFO, "All handlers are installed, creating router.");
        return routerBuilder.createRouter();
    }

    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        if (cause instanceof MarsResourceNotFoundException) {
            code = 404;
        } else if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, quote);
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }
}
