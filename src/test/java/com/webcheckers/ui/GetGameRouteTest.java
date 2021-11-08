package com.webcheckers.ui;
import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Piece;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;
import java.util.HashSet;
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The unit test suite for the {@link GetGameRoute} component.
 *
 * @author dsk1354@rit.edu
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
@Tag("UI-tier")
public class GetGameRouteTest {
    /**
     * The component-under-test (CuT).
     *
     * This is a stateless component so we only need one.
     * The {@link PlayerLobby} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private GetGameRoute CuT;

    // friendly objects
    private PlayerLobby playerLobby;

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

        // create a unique CuT for each test
        playerLobby = new PlayerLobby();
        CuT = new GetGameRoute(engine, gameCenter);

    }

    /**
     * Test that when given a null player, redirects to home page.
     */
    @Test
    public void test_nullPlayer_error(){
        // Arrange the test scenario: null player
        when(gameCenter.createGame(null, null)).thenReturn(GameCenter.GameStatus.NULL_PLAYER);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test (ignore the output)
        CuT.handle(request, response);

        // Analyze the results
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Test that when given a player who is not already in a game, CuT redirects to home page
     */
    @Test
    public void test_playerNotInGame(){
        // Arrange the test scenario: player not in game
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(request.queryParams()).thenReturn(new HashSet<>());
        when(gameCenter.inGame("player")).thenReturn(false);
        when(session.attribute("name")).thenReturn("player");

        // Invoke the test (ignore the output)
        CuT.handle(request, response);

        // Analyze the results
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Test that when given a player who is already in a game, CuT redirects to home page
     */
    @Test
    public void test_playerInGame_error(){
        // Arrange the test scenario: the player challenged is already in a game.
        when(gameCenter.createGame(null, null)).thenReturn(GameCenter.GameStatus.IN_GAME);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("name")).thenReturn("player");
        when(gameCenter.inGame("player")).thenReturn(false);
        HashSet<String> set = new HashSet<>();
        set.add("not empty");
        when(request.queryParams()).thenReturn(set);

        // Invoke the test (ignore the output)
        CuT.handle(request, response);

        // Analyze the results
        verify(response).redirect(WebServer.HOME_URL+ "?error=" + GameCenter.GameStatus.IN_GAME + "&user=null");
    }


    /**
     * Test that when given the same player, redirects to home page
     */
    @Test
    public void test_samePlayer_error(){
        // Arrange the test scenario: the playerLobby has two players willing to play.
        when(gameCenter.createGame(null, null)).thenReturn(GameCenter.GameStatus.SAME_PLAYER);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("name")).thenReturn("player");
        when(gameCenter.inGame("player")).thenReturn(false);
        HashSet<String> set = new HashSet<>();
        set.add("not empty");
        when(request.queryParams()).thenReturn(set);

        // Invoke the test (ignore the output)
        CuT.handle(request, response);

        // Analyze the results
        verify(response).redirect(WebServer.HOME_URL + "?error=" + GameCenter.GameStatus.SAME_PLAYER);
    }

    /**
     * Test that when given a null player, redirects to home page
     */
    @Test
    public void test_nullOpponent(){
        // Arrange the test scenario: null player
        when(gameCenter.getOpponent("name")).thenReturn(null);
        when(gameCenter.createGame(null, null)).thenReturn(GameCenter.GameStatus.NULL_PLAYER);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("name")).thenReturn("player");
        when(gameCenter.inGame("player")).thenReturn(false);
        HashSet<String> set = new HashSet<>();
        set.add("not empty");
        when(request.queryParams()).thenReturn(set);

        // Invoke the test (ignore the output)
        CuT.handle(request, response);

        // Analyze the results
        verify(response).redirect(WebServer.HOME_URL + "?error=" + GameCenter.GameStatus.NULL_PLAYER);
    }

    /**
     * Test that when given a player who is spectating a game, CuT redirects to home page
     */
    @Test
    public void test_spectating_error(){
        // Arrange the test scenario: the player challenged is already in a game.
        when(gameCenter.createGame(null, null)).thenReturn(GameCenter.GameStatus.SPECTATING);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(session.attribute("name")).thenReturn("player");
        when(gameCenter.inGame("player")).thenReturn(false);
        HashSet<String> set = new HashSet<>();
        set.add("not empty");
        when(request.queryParams()).thenReturn(set);

        // Invoke the test (ignore the output)
        CuT.handle(request, response);

        // Analyze the results
        verify(response).redirect(WebServer.HOME_URL+ "?error=" + GameCenter.GameStatus.SPECTATING + "&user=null");
    }

    /**
     * Test that the Game view will create a new game if all conditions are met.
     */
    @Test
    public void test_newGame(){
        // Arrange the test scenario: the playerLobby has two players willing to play.
        playerLobby.addPlayer("player1");
        playerLobby.addPlayer("player2");
        Player player1 = playerLobby.getPlayer("player1");
        player1.setColor(Piece.Color.RED);
        Player player2 = playerLobby.getPlayer("player2");

        when(session.attribute("name")).thenReturn("player1");
        when(request.session().attribute(GetGameRoute.RED_PLAYER_NAME_ATTR)).thenReturn("player1");
        when(request.session().attribute(GetGameRoute.RED_PLAYER_NAME_ATTR)).thenReturn("player2");
        Set<String> mockSet = new HashSet<>();
        mockSet.add("Not empty.");
        when(request.queryParams()).thenReturn(mockSet);
        when(gameCenter.createGame(any(), any())).thenReturn(GameCenter.GameStatus.CREATED);
        when(gameCenter.getOpponent(any())).thenReturn(player2);
        when(gameCenter.getLobby()).thenReturn(playerLobby);
        CheckersGame game = mock(CheckersGame.class);
        when(gameCenter.getGame(any())).thenReturn(game);
        when(game.getMode()).thenReturn(CheckersGame.Mode.PLAY);
        when(game.redPlayer()).thenReturn(player1);
        when(game.whitePlayer()).thenReturn(player2);
        when(game.getCurrentColor()).thenReturn(Piece.Color.RED);


        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test (ignore the output)
        CuT.handle(request, response);

        // Analyze the content passed into the render method
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetGameRoute.CURRENT_USER_ATTR, player1);
        testHelper.assertViewModelAttribute(GetGameRoute.VIEW_MODE_ATTR, CheckersGame.Mode.PLAY);
        testHelper.assertViewModelAttribute(GetGameRoute.RED_PLAYER_ATTR, player1);
        testHelper.assertViewModelAttribute(GetGameRoute.WHITE_PLAYER_ATTR, player2);
        testHelper.assertViewModelAttribute(GetGameRoute.ACTIVE_COLOR_ATTR, "RED");
        verify(response).redirect(WebServer.GAME_URL);

        //   * test view name
        testHelper.assertViewName("game.ftl");
    }

    /**
     * Test that CuT redirects to the Home page when the player is in a game with a null opponent.
     */
    @Test
    public void test_oldSessionNullOpponent(){
        // Arrange the test scenario: null player
        when(gameCenter.getOpponent("name")).thenReturn(null);
        Set<String> mockSet = new HashSet<>();
        mockSet.add("Not empty.");
        when(request.queryParams()).thenReturn(mockSet);
        when(session.attribute("name")).thenReturn("player");
        when(gameCenter.inGame("player")).thenReturn(true);

        // Invoke the test (ignore the output)
        CuT.handle(request, response);

        // Analyze the results
        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Test that the the correct information is displayed given a session already
     * exists and the player is the white player.
     */
    @Test
    public void test_oldSession(){
        when(gameCenter.getOpponent("player")).thenReturn(new Player("opponent"));
        Set<String> mockSet = new HashSet<>();
        mockSet.add("Not empty.");
        when(request.queryParams()).thenReturn(mockSet);
        when(session.attribute("name")).thenReturn("player");
        when(gameCenter.inGame("player")).thenReturn(true);
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(gameCenter.getLobby()).thenReturn(playerLobby);
        playerLobby.addPlayer("player");
        playerLobby.addPlayer("opponent");
        CheckersGame mockGame = mock(CheckersGame.class);
        when(gameCenter.getGame(anyString())).thenReturn(mockGame);
        when(mockGame.isGameOver()).thenReturn(false);
        when(mockGame.gameOverMessage()).thenReturn(null);

        // Invoke the test (ignore the output)
        CuT.handle(request, response);

        // Analyze the content passed into the render method
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetGameRoute.ACTIVE_COLOR_ATTR, "WHITE");

        //   * test view name
        testHelper.assertViewName("game.ftl");
    }
}
