package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;
import static org.mockito.Mockito.*;

/**
 * The unit test suite for the {@link PostSignOutRoute} component.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
@Tag("UI-tier")
public class PostSignOutRouteTest {
    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link PlayerLobby} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private PostSignOutRoute CuT;

    // friendly objects
    private PlayerLobby lobby;

    // mock objects
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;
    private GameCenter gameCenter;

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
        gameCenter = mock(GameCenter.class);

        // create a unique CuT for each test
        lobby = new PlayerLobby();
        CuT = new PostSignOutRoute(engine, gameCenter);
    }

    /**
     * Test that CuT redirects to the Home view when the session is brand new.
     */
    @Test
    public void test_newSession() {
        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * redirect to the Home view
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Test that CuT signs the player out and redirects to the Home view when the player is logged in.
     */
    @Test
    public void test_oldSession() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        lobby.addPlayer("player");

        // Arrange the test scenario: There is an existing session with a player who is logged in
        when(session.attribute("name")).thenReturn("player");
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        doNothing().when(gameCenter).removePlayer("player");

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * redirect to the Game view
        verify(response).redirect(WebServer.HOME_URL);
        // make sure gameCenter.removePlayer() was called
        verify(gameCenter).removePlayer("player");
        verify(gameCenter, times(1)).removePlayer("player");
    }

    /**
     * Test that CuT signs the player out and redirects to the Home view when the player is logged in.
     */
    @Test
    public void test_inGameError() {
        when(session.attribute("name")).thenReturn("player");
        when(gameCenter.inGame("player")).thenReturn(true);
        CheckersGame mockGame = mock(CheckersGame.class);
        when(gameCenter.getGame("player")).thenReturn(mockGame);
        doNothing().when(mockGame).clearTurnMoves();

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * redirect to the Game view
        verify(response).redirect(WebServer.GAME_URL + "?error=IN_GAME_ERROR_MESSAGE");
        verify(mockGame, times(1)).clearTurnMoves();
    }
}
