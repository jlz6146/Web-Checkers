package com.webcheckers.ui;

import static com.webcheckers.ui.GetSpectatorGameRoute.GAME_ID_ATTR;
import static org.junit.jupiter.api.Assertions.*;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * The unit test suite for the {@link GetSpectatorStopWatchingRoute} component.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 * @author Jesse Zhao jlz6146@g.rit.edu
 */

@Tag("UI-tier")
public class GetSpectatorStopWatchingRouteTest {
    /**
    * The component-under-test (CuT).
    */
    private GetSpectatorStopWatchingRoute CuT;

    // mock objects
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;
    private GameCenter gameCenter;
    private PlayerLobby lobby;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        gameCenter = mock(GameCenter.class);
        lobby = mock(PlayerLobby.class);

        // create a unique CuT for each test
        CuT = new GetSpectatorStopWatchingRoute(gameCenter);
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
     * Test that when the query parameters of the request is null, CuT redirects to home page
     */
    @Test
    public void test_Nullparam() throws Exception {
        when(request.queryParams()).thenReturn(null);
        final TemplateEngineTester tester = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(tester.makeAnswer());
        //invoke
        CuT.handle(request, response);
        //analyze the results
        // * verify it redirects to homepage when the player's name is null
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Test that the handle() method works correctly when the player's name is not null.
     */
    @Test
    public void test_handle() throws Exception {
        when(session.attribute("name")).thenReturn("spectator");
        int gameID = 12345;
        String gameIDStr = Integer.toString(12345);
        Set<String> mockSet = new HashSet<>();
        mockSet.add(gameIDStr);
        when(request.queryParams()).thenReturn(mockSet);
        when(request.queryParams(GAME_ID_ATTR)).thenReturn(gameIDStr);
        when(gameCenter.getLobby()).thenReturn(lobby);
        Player spectator = new Player("spectator");
        when(lobby.getPlayer("spectator")).thenReturn(spectator);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * redirect to the Game view
        verify(response).redirect(WebServer.HOME_URL);
        // make sure gameCenter.removeSpectator() was called
        verify(gameCenter, times(1)).removeSpectator(gameID, spectator);
    }
}
