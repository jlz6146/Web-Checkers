package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.logging.Logger;

/**
 * UI controller to POST INFO or ERROR message for resigning from a game.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class PostResignGameRoute implements Route {
    //The log for this object
    private static final Logger LOG = Logger.getLogger(PostResignGameRoute.class.getName());

    //Variables used to hold the objects used by this route
    private final GameCenter gameCenter;
    private final PlayerLobby lobby;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /resignGame} HTTP requests.
     *
     * @param gameCenter the game center used to coordinate the state of the WebCheckers Application.
     */
    public PostResignGameRoute(GameCenter gameCenter, PlayerLobby lobby) {
        LOG.finer("PostBackupMoveRoute is initialized.");
        this.gameCenter = gameCenter;
        this.lobby = lobby;
    }

    /**
     * Resigns from the current game.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return json message
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String name = request.session().attribute("name");

        Gson gson = new Gson();
        return gson.toJson(gameCenter.resign(name));
    }
}
