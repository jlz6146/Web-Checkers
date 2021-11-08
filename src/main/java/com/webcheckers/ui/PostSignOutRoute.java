package com.webcheckers.ui;
import com.webcheckers.application.GameCenter;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI Controller to POST the SignOut page.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class PostSignOutRoute implements Route {
    //The log for this object
    private static final Logger LOG = Logger.getLogger(GetSigninRoute.class.getName());

    //Variables used to hold the objects used by this route
    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signout} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     * @param gameCenter the game center used to coordinate the state of the WebCheckers Application.
     */
    public PostSignOutRoute(final TemplateEngine templateEngine, GameCenter gameCenter){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gameCenter = gameCenter;
        LOG.config("GetSignOutRoute is initialized");
    }

    /**
     * Signs the player out
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return null (redirect to the Home Page)
     */
    @Override
    public Object handle(Request request, Response response){
        LOG.finer("GetSignOutRoute is invoked.");
        String name = request.session().attribute("name");

        if(gameCenter.inGame(name)) {
            response.redirect(WebServer.GAME_URL + "?error=IN_GAME_ERROR_MESSAGE");
            gameCenter.getGame(name).clearTurnMoves();
            return null;
        }

        if(name != null){
            gameCenter.removePlayer(name);
            request.session().attribute(GetSigninRoute.PLAYER_NAME_ATTR, null);
            LOG.info(name + " has signed out.");
        }
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
