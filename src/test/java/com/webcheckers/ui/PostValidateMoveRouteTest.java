package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import spark.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * The unit test suite for the {@link PostValidateMoveRoute} component.
 *
 * @author esl7511@rit.edu
 */
@Tag("UI-tier")
public class PostValidateMoveRouteTest {
    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link GameCenter}, and {@link PlayerLobby}, components are thoroughly tested so
     * we can use them safely as "friendly" dependencies.
     */
    private PostValidateMoveRoute CuT;

    // friendly objects
    private GameCenter gameCenter;
    private PlayerLobby lobby;
    private Gson gson;

    // mock objects
    private TemplateEngine engine;
    private Session session;
    private Request request;
    private Response response;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        
        
        // create a unique CuT for each test
        gson = new Gson();
        lobby = new PlayerLobby();
        gameCenter = new GameCenter(lobby);
        CuT = new PostValidateMoveRoute(engine, gameCenter);
    }

    /**
     * Test that handle() works correctly.
     */
    @Test
    public void test_handle() throws Exception{
        //setup
        lobby.addPlayer("player2");
        lobby.addPlayer("player");
        Player player = lobby.getPlayer("player");
        Player player2 = lobby.getPlayer("player2");
        gameCenter.addPlayer(player.getName());
        gameCenter.addPlayer(player2.getName());
        gameCenter.createGame(player.getName(), player2.getName());
        CheckersGame game = gameCenter.getGame(player.getName());
        Move move = new Move(new Position(5, 0), new Position(4, 1));

        when(request.session().attribute("name")).thenReturn(player.getName());
        when(request.queryParams("actionData")).thenReturn(gson.toJson(move));

        //invoke
        CuT.handle(request, response);

        //analyze the results
        // * calling handle equals calling the CheckersGame testMove method
        assertEquals(CuT.handle(request, response), gson.toJson(game.testMove(move)));
        
    }
}
