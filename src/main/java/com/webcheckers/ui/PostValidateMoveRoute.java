package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.logging.Logger;

/**
 * The UI Controller to POST validating a move.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class PostValidateMoveRoute implements Route  {
    //The log for this object
    private static final Logger LOG = Logger.getLogger(PostSigninRoute.class.getName());

    //Variables used to hold the objects used by this route
    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /validateMove} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     * @param gameCenter the game center used to coordinate the state of the WebCheckers Application.
     */
    public PostValidateMoveRoute(TemplateEngine templateEngine, GameCenter gameCenter) {
        LOG.finer("PostValidateMoveRoute is initialized.");
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
    }

    /**
     * Check if the move made is valid.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the game page
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String name = request.session().attribute("name");
        CheckersGame game = gameCenter.getGame(name);

        Gson gson = new Gson();
        Move move = gson.fromJson(request.queryParams("actionData"), Move.class);

        return gson.toJson(game.testMove(move));
    }
}
