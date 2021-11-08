package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.logging.Logger;

import static com.webcheckers.ui.GetSpectatorGameRoute.GAME_ID_ATTR;

/**
 * The UI Controller to GET when a spectator wants to exit spectating.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class GetSpectatorStopWatchingRoute implements Route {
    //The log for this object
    private static final Logger LOG = Logger.getLogger(GetSpectatorStopWatchingRoute.class.getName());

    //Variables used to hold the objects used by this route
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /spectator/stopWatching} HTTP requests.
     *
     * @param gameCenter the game center used to coordinate the state of the WebCheckers Application.
     */
    public GetSpectatorStopWatchingRoute(GameCenter gameCenter) {
        this.gameCenter = gameCenter;
        //
        LOG.config("GetSpectatorStopWatchingRoute is initialized.");
    }

    /**
     * Stop spectating the current game and return the spectator to the Home page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   null (redirect to the home page)
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("GetSpectatorStopWatchingRoute is invoked.");
        String name = request.session().attribute("name");
        if(name == null || request.queryParams().isEmpty()) {
            response.redirect(WebServer.HOME_URL);
            return null;
        }
        Player current = gameCenter.getLobby().getPlayer(name);
        int gameID = Integer.parseInt(request.queryParams(GAME_ID_ATTR));
        gameCenter.removeSpectator(gameID, current);
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
