package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI Controller to POST the Signin page.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class PostSigninRoute implements Route {
    //The log for this object
    private static final Logger LOG = Logger.getLogger(PostSigninRoute.class.getName());

    //Variables used to hold the objects used by this route
    private final TemplateEngine templateEngine;
    private final PlayerLobby lobby;

    //Attributes in the view used when displaying the sigin page
    static final String SIGNIN_MESSAGE_ATTR = "message";

    //The view name for the sigin page
    static final String VIEW_NAME = "signin.ftl";

    //Error messages displayed to the user
    static final String INVALID_NAME_MESSAGE = "The name you entered is invalid. Please enter a name " +
            "containing only letters, numbers, and spaces.";
    static final String DUPLICATE_NAME_MESSAGE = "The name you entered is already in use. " +
            "Please enter a different name.";

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signin} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     * @param lobby the lobby of active players
     */
    public PostSigninRoute(TemplateEngine templateEngine, PlayerLobby lobby) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("PostSigninRoute is initialized.");
        this.lobby = lobby;
    }


    /**
     * Render the WebCheckers Signin page.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response){
        LOG.finer("GetSigninRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();
        ModelAndView mv;

        if(request.session().attribute(GetSigninRoute.PLAYER_NAME_ATTR) != null) {
            response.redirect(WebServer.HOME_URL);
            return null;
        }

        String name = request.queryParams(GetSigninRoute.PLAYER_NAME_ATTR);
        PlayerLobby.NameStatus result = this.lobby.addPlayer(name);
        if(result != null) {
            switch (result) {
                case INVALID:
                    vm.put(SIGNIN_MESSAGE_ATTR, Message.error(INVALID_NAME_MESSAGE));
                    mv = new ModelAndView(vm, VIEW_NAME);
                    return templateEngine.render(mv);

                case DUPLICATE:
                    vm.put(SIGNIN_MESSAGE_ATTR, Message.error(DUPLICATE_NAME_MESSAGE));
                    mv = new ModelAndView(vm, VIEW_NAME);
                    return templateEngine.render(mv);

                case VALID:
                    request.session().attribute(GetSigninRoute.PLAYER_NAME_ATTR, name);
                    LOG.info(name + " has signed in.");
                    response.redirect(WebServer.HOME_URL);
                    return null;
            }
        }
        return null;
    }
}
