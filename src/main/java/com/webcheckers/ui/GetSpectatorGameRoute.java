package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI Controller to GET the spectator game page.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class GetSpectatorGameRoute implements Route {
    //The log for this object
    private static final Logger LOG = Logger.getLogger(GetSpectatorGameRoute.class.getName());

    //Variables used to hold the objects used by this route
    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final PlayerLobby lobby;

    //Attributes in the view used when displaying the game page
    static final String CURRENT_USER_ATTR = "currentUser";
    static final String VIEW_MODE_ATTR = "viewMode";
    static final String RED_PLAYER_ATTR = "redPlayer";
    static final String WHITE_PLAYER_ATTR = "whitePlayer";
    static final String ACTIVE_COLOR_ATTR = "activeColor";
    static final String BOARD_ATTR = "board";
    static final String MESSAGE_ATTR = "message";
    static final String GAME_ID_ATTR = "gameID";

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /spectator/game} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     * @param gameCenter the game center used to coordinate the state of the WebCheckers Application.
     * @param lobby the lobby used to hold all players that are currently logged in
     */
    public GetSpectatorGameRoute(final TemplateEngine templateEngine, GameCenter gameCenter, PlayerLobby lobby) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gameCenter = gameCenter;
        this.lobby = lobby;
        //
        LOG.config("GetSpectatorGameRoute is initialized.");
    }

    /**
     * Render the WebCheckers spectator Game page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the spectator Game page
     */
    @Override

    public Object handle(Request request, Response response){
        LOG.finer("GetSpectatorGameRoute is invoked.");
        String name = request.session().attribute("name");
        if (name == null) {
            response.redirect(WebServer.HOME_URL);
            return null;
        } else if(request.queryParams().isEmpty()) {
            response.redirect(WebServer.HOME_URL);
            return null;
        }

        Map<String, Object> vm = new HashMap<>();
        int gameID = Integer.parseInt(request.queryParams(GAME_ID_ATTR));
        CheckersGame game = gameCenter.getGameByID(gameID);
        Player current = lobby.getPlayer(name);
        vm.put(CURRENT_USER_ATTR, current);
        gameCenter.addSpectator(gameID, current);
        vm.put(VIEW_MODE_ATTR, CheckersGame.Mode.SPECTATOR);
        Player red = game.redPlayer();
        vm.put(RED_PLAYER_ATTR, red);
        Player white = game.whitePlayer();
        vm.put(WHITE_PLAYER_ATTR, white);
        Piece.Color activeColor = game.getCurrentColor();
        vm.put(ACTIVE_COLOR_ATTR, activeColor);
        vm.put(BOARD_ATTR, game.getBoard(activeColor != Piece.Color.RED));
        if(game.isGameOver()) {
            vm.put(MESSAGE_ATTR, Message.info(game.gameOverMessage()));
        }
        vm.put("title", red.getName() + " VS " + white.getName());

        // render the View
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));
    }
}
