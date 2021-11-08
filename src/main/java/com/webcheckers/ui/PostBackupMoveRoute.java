package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import spark.*;
import java.util.logging.Logger;


/**
 * UI controller to POST INFO or ERROR message for removing last valid move.
 *
 * @author Eric Landers esl7511@rit.edu
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class PostBackupMoveRoute implements Route{
    //The log for this object
    private static final Logger LOG = Logger.getLogger(PostBackupMoveRoute.class.getName());

    //Variables used to hold the objects used by this route
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /validateMove} HTTP requests.
     *
     * @param gameCenter the game center used to coordinate the state of the WebCheckers Application.
     */
    public PostBackupMoveRoute(GameCenter gameCenter) {
        LOG.finer("PostBackupMoveRoute is initialized.");
        this.gameCenter = gameCenter;
    }

    /**
     * Removes last valid move.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return json message
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String name = request.session().attribute("name");
        CheckersGame game = gameCenter.getGame(name);

        Gson gson = new Gson();
        return gson.toJson(game.backupMove());
    }
}