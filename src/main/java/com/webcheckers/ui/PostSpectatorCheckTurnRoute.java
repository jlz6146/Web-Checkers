package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

/**
 * UI controller to POST checking if there is a new turn the spectator has not yet seen.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class PostSpectatorCheckTurnRoute implements Route {
    //The log for this object
    private static final Logger LOG = Logger.getLogger(PostSpectatorCheckTurnRoute.class.getName());

    //Variables used to hold the objects used by this route
    private final GameCenter gameCenter;
    private final PlayerLobby lobby;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /spectator/stopWatching} HTTP requests.
     *
     * @param gameCenter the game center used to coordinate the state of the WebCheckers Application
     * @param lobby the lobby used to hold all players that are currently logged in
     */
    public PostSpectatorCheckTurnRoute(GameCenter gameCenter, PlayerLobby lobby) {
        this.gameCenter = gameCenter;
        this.lobby = lobby;
        //
        LOG.config("PostSpectatorCheckTurnRoute is initialized.");
    }

    /**
     * Check if there is a new turn the spectator has not yet seen.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   a message with the text false if there is not a new turn, and true otherwise
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostSpectatorCheckTurnRoute is invoked.");
        Gson gson = new Gson();

        String name = request.session().attribute("name");
        if(name == null) {
            response.redirect(WebServer.HOME_URL);
            return null;
        }
        Player current = lobby.getPlayer(name);
        CheckersGame spectatedGame = gameCenter.getGameBySpectator(current);
        if(spectatedGame == null) {
            response.redirect(WebServer.HOME_URL);
            return null;
        }

        String json;
        if(spectatedGame.isNewTurn()){
            spectatedGame.setNewTurn(false);
            json = gson.toJson(Message.info("true"));
        } else {
            json = gson.toJson(Message.info("false"));
        }
        return json;
    }
}
