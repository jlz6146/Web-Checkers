package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The unit test suite for the {@link PostSpectatorCheckTurnRoute} component.
 * 
 * @author esl7511@rit.edu
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
@Tag("UI-Tier")
public class PostSpectatorCheckTurnRouteTest {
    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link PlayerLobby} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private PostSpectatorCheckTurnRoute CuT;

    // friendly objects
    private PlayerLobby playerLobby;
    private Gson gson;

    // mock objects
    private GameCenter gameCenter;
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

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
        gson = new Gson();

        // create a unique CuT for each test
        playerLobby = new PlayerLobby();
        CuT = new PostSpectatorCheckTurnRoute(gameCenter, playerLobby);
    }

    /**
     * Test that when the player's name is null, CuT redirects to home page.
     */
    @Test
    public void test_nullName() throws Exception {
        //setup
        when(session.attribute("name")).thenReturn(null);
        final TemplateEngineTester tester = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(tester.makeAnswer());
        //invoke
        CuT.handle(request, response);
        //analyze the results
        // * verify it redirects to homepage when the player's name is null
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Test that when the spectated game is null, CuT redirects to home page.
     */
    @Test
    public void test_nullSpectatedGame() throws Exception {
        //setup
        when(session.attribute("name")).thenReturn("not null");
        final TemplateEngineTester tester = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(tester.makeAnswer());
        when(gameCenter.getGameBySpectator(any())).thenReturn(null);
        //invoke
        CuT.handle(request, response);
        //analyze the results
        // * verify it redirects to homepage when the spectated game is null
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Test that when a new turn exists, the correct json response is received.
     */
    @Test
    public void test_newTurnExists() throws Exception {
        //setup
        when(session.attribute("name")).thenReturn("not null");
        final TemplateEngineTester tester = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(tester.makeAnswer());
        CheckersGame mockGame = mock(CheckersGame.class);
        when(gameCenter.getGameBySpectator(any())).thenReturn(mockGame);
        when(mockGame.isNewTurn()).thenReturn(true);
        //invoke
        String json = (String) CuT.handle(request, response);
        //analyze the results
        assertEquals(gson.toJson(Message.info("true")), json);
        verify(mockGame, times(1)).setNewTurn(false);
    }

    /**
     * Test that when no new turn exists, the correct json response is received.
     */
    @Test
    public void test_noNewTurnExists() throws Exception {
        //setup
        when(session.attribute("name")).thenReturn("not null");
        final TemplateEngineTester tester = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(tester.makeAnswer());
        CheckersGame mockGame = mock(CheckersGame.class);
        when(gameCenter.getGameBySpectator(any())).thenReturn(mockGame);
        when(mockGame.isNewTurn()).thenReturn(false);
        //invoke
        String json = (String) CuT.handle(request, response);
        //analyze the results
        assertEquals(gson.toJson(Message.info("false")), json);
    }
}
