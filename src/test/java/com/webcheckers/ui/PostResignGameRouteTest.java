package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The unit test suite for the {@link PostResignGameRoute} component.
 *
 * @author jb4411@g.rit.edu
 */
@Tag("UI-tier")
public class PostResignGameRouteTest {
    /**
     * The component-under-test (CuT).
     *
     * <p>
     * The {@link PlayerLobby} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private PostResignGameRoute CuT;

    // friendly objects
    private PlayerLobby playerLobby;
    private Gson gson;

    //mock objects
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
        CuT = new PostResignGameRoute(gameCenter, playerLobby);
    }

    /**
     * Test that the handle() method works correctly.
     */
    @Test
    public void test_handle() throws Exception {
        String str = (String) CuT.handle(request, response);
    }
}
