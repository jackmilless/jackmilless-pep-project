package Controller;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    SocialMediaService socialMediaService;
    
    public SocialMediaController() {
        socialMediaService = new SocialMediaService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIDHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account registeredAccount = socialMediaService.registerAccount(account);

        if(registeredAccount == null) {
            context.status(400);
        } else {
            context.status(200);
            context.json(mapper.writeValueAsString(registeredAccount));
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account openedAccount = socialMediaService.loginAccount(account);

        if(openedAccount == null) {
            context.status(401);
        } else {
            context.status(200);
            context.json(mapper.writeValueAsString(openedAccount));
        }
    }

    private void createMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message createdMessage = socialMediaService.createMessage(message);

        if(createdMessage == null) {
            context.status(400);
        } else {
            context.status(200);
            context.json(mapper.writeValueAsString(createdMessage));
        }
    }

    private void getAllMessagesHandler(Context context) {
        context.status(200);
        context.json(socialMediaService.getAllMessages());
    }

    private void getMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message obtainedMessage = socialMediaService.getMessageByID(message_id);

        context.status(200);
        if(obtainedMessage == null) {
            context.json("");
        } else {
            context.json(mapper.writeValueAsString(obtainedMessage));
        }
    }

    private void deleteMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = socialMediaService.deleteMessageByID(message_id);
        
        if(deletedMessage == null) {
            context.json("");
        } else {
            context.status(200);
            context.json(mapper.writeValueAsString(deletedMessage));
        }
    }

    private void updateMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = mapper.readValue(context.body(), Message.class);
        Message updatedMessage = socialMediaService.updateMessageByID(message.message_text, message_id);

        if(updatedMessage == null) {
            context.status(400);
        } else {
            context.status(200);
            context.json(mapper.writeValueAsString(updatedMessage));
        }
    }

    private void getMessagesByAccountIDHandler(Context context) throws JsonProcessingException {
        context.status(200);
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(socialMediaService.getMessagesByAccountID(account_id));
    }
}