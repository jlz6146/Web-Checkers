package com.webcheckers.application;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test suite for the {@link GameCenter} component.
 *
 * @author Dominic Kavanagh dsk1354@rit.edu
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
@Tag("Application-tier")
public class GameCenterTest {
    /**
     * The component-under-test (CuT).
     * The {@link PlayerLobby}, component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private GameCenter CuT;

    // friendly objects
    private PlayerLobby playerLobby;

    /**
     * Setup new objects for each test.
     */
    @BeforeEach
    public void setup() {
        // Setup CuT and create the game
        playerLobby = new PlayerLobby();
        CuT = new GameCenter(playerLobby);
    }

    /**
     * Test that you can construct a new GameCenter.
     */
    @Test
    public void test_createCenter() {
        new GameCenter(playerLobby);
    }

    /**
     * Test that you can create a game.
     */
    @Test
    public void test_makeGame() {
        final GameCenter CuT = new GameCenter(playerLobby);
        CuT.addPlayer("Player1");
        CuT.addPlayer("Player2");
        CuT.createGame("Player1","Player2");

        final CheckersGame game = CuT.getGame("Player1");

        // Analyze the results
        // 1) the returned game is real
        assertNotNull(game);
        // 2) the game is in mode PLAY
        assertEquals(CheckersGame.Mode.PLAY, game.getMode());

        // 3) The players have the correct colors.
        assertEquals("Player1",game.redPlayer().getName());
        assertEquals("Player2",game.whitePlayer().getName());
    }

    /**
     * Test that you can get a players opponent.
     */
    @Test
    public void test_findOpponent() {
        CuT.addPlayer("Player1");
        CuT.addPlayer("Player2");
        CuT.createGame("Player1","Player2");

        // Opponent has correct name
        assertEquals(playerLobby.getPlayer("Player2"), CuT.getOpponent("Player1"));
        assertEquals(playerLobby.getPlayer("Player1"), CuT.getOpponent("Player2"));
    }

    /**
     * Test that getting an opponent when the player is not in a game does not cause an error.
     */
    @Test
    public void test_findOpponentNullGame() {
        CuT.addPlayer("Player1");

        // Opponent does not exist
        assertNull(CuT.getOpponent("Player1"));
    }

    /**
     * Test getting the player lobby from CuT.
     */
    @Test
    public void test_getLobby() {
        assertEquals(playerLobby, CuT.getLobby());
    }

    /**
     * Test getting a game by its ID.
     */
    @Test
    public void test_getGameByID() {
        CuT.addPlayer("Player1");
        CuT.addPlayer("Player2");
        CuT.createGame("Player1","Player2");

        assertEquals(CuT.getGame("Player1"), CuT.getGameByID(Objects.hash("Player1","Player2")));
    }

    /**
     * Test checking if a player is already in a game.
     */
    @Test
    public void test_inGame() {
        CuT.addPlayer("Player1");
        CuT.addPlayer("Player2");
        CuT.addPlayer("not in game");
        CuT.createGame("Player1","Player2");

        assertTrue(CuT.inGame("Player1"));
        assertFalse(CuT.inGame("not in game"));
    }

    /**
     * Test the "IN_GAME" error when creating a game.
     */
    @Test
    public void test_createGame_IN_GAME_error() {
        CuT.addPlayer("Player1");
        CuT.addPlayer("Player2");
        CuT.addPlayer("Player3");
        CuT.createGame("Player1","Player2");

        assertEquals(GameCenter.GameStatus.IN_GAME, CuT.createGame("Player3","Player1"));
    }

    /**
     * Test the "NULL_PLAYER" error when creating a game.
     */
    @Test
    public void test_createGame_NULL_PLAYER_error() {
        assertEquals(GameCenter.GameStatus.NULL_PLAYER, CuT.createGame(null,null));
    }

    /**
     * Test the "SAME_PLAYER" error when creating a game.
     */
    @Test
    public void test_createGame_SAME_PLAYER_error() {
        CuT.addPlayer("same");
        assertEquals(GameCenter.GameStatus.SAME_PLAYER, CuT.createGame("same","same"));
    }

    /**
     * Test the "SPECTATING" error when creating a game.
     */
    @Test
    public void test_createGame_SPECTATING_error() {
        CuT.addPlayer("Player1");
        CuT.addPlayer("Player2");
        CuT.addPlayer("Player3");
        CuT.createGame("Player1","Player2");
        CuT.addPlayer("spectating");
        CuT.addSpectator(Objects.hash("Player1", "Player2"), playerLobby.getPlayer("spectating"));
        assertEquals(GameCenter.GameStatus.SPECTATING, CuT.createGame("player3","spectating"));
    }

    /**
     * Test ending an active game.
     */
    @Test
    public void test_endgame() {
        CuT.addPlayer("best");
        CuT.addPlayer("movie");
        CuT.createGame("best","movie");
        CuT.endGame("best");

        assertTrue(CuT.inGame("best"));
        assertTrue(CuT.inGame("movie"));
        assertTrue(CuT.inEndGame("best"));
        assertTrue(CuT.inEndGame("movie"));
    }

    /**
     * Test exiting a game.
     */
    @Test
    public void test_exitGame() {
        CuT.addPlayer("player1");
        CuT.addPlayer("player2");
        CuT.createGame("player1","player2");
        CuT.exitGame("player1");

        assertFalse(CuT.inGame("player1"));
        assertTrue(CuT.inGame("player2"));
        assertFalse(CuT.inEndGame("player1"));
    }

    /**
     * Test removing a player from the lobby.
     */
    @Test
    public void test_removePlayer() {
        CuT.addPlayer("Player1");
        CuT.addPlayer("Player2");
        CuT.addPlayer("Player3");
        CuT.createGame("Player1","Player2");

        CuT.removePlayer("Player3");
        //Assert "Player3" is no longer in the lobby
        assertNull(playerLobby.getPlayer("Player3"));

        CuT.removePlayer("Player1");
        //Assert "Player1" is no longer in the lobby
        assertNull(playerLobby.getPlayer("Player1"));
        //Assert the game "Player1" was in has ended.
        assertFalse(CuT.inGame("Player1"));
        assertTrue(CuT.inGame("Player2"));
        //Assert "Player1" is still in the lobby
        assertNotNull(playerLobby.getPlayer("Player2"));
    }

    /**
     * Test getting all active players.
     */
    @Test
    public void test_getAllActiveGames() {
        // Case: initial state, active games should be empty
        assertTrue(CuT.getAllActiveGames().isEmpty());

        CuT.addPlayer("Player1");
        CuT.addPlayer("Player2");
        CuT.createGame("Player1","Player2");

        // Case: an active game exists
        Map<String, String> activeGames = CuT.getAllActiveGames();
        assertFalse(activeGames.isEmpty());

        String gameStr = "Player1 VS Player2";
        assertTrue(activeGames.containsKey(gameStr));

        int gameID = Objects.hash("Player1","Player2");
        assertEquals(Integer.toString(gameID), activeGames.get(gameStr));
    }

    /**
     * Test getting the map of currently spectated games.
     */
    @Test
    public void test_getSpectatedGames() {
        // Case: initial state, no games are being spectated
        Map<Integer, Set<Player>> spectatedGames = CuT.getSpectatedGames();
        assertTrue(spectatedGames.isEmpty());
        Map<Integer, Set<Player>> expectedMap = new HashMap<>();
        assertEquals(expectedMap, spectatedGames);
    }

    /**
     * Test getting the map of spectators.
     */
    @Test
    public void test_getSpectators() {
        // Case: initial state, there are no spectators
        Map<Player, CheckersGame> spectators = CuT.getSpectators();
        assertTrue(spectators.isEmpty());
        Map<Player, CheckersGame> expectedMap = new HashMap<>();
        assertEquals(expectedMap, spectators);
    }

    /**
     * Test adding a spectator.
     */
    @Test
    public void test_addSpectator() {
        CuT.addPlayer("Player1");
        CuT.addPlayer("Player2");
        CuT.addPlayer("spectator1");
        CuT.addPlayer("spectator2");
        CuT.createGame("Player1","Player2");
        int gameID = Objects.hash("Player1","Player2");
        CheckersGame game = CuT.getGameByID(gameID);

        // Case: game not already in spectatedGames
        Player spectator1 = playerLobby.getPlayer("spectator1");
        CuT.addSpectator(gameID, spectator1);
        Map<Integer, Set<Player>> spectatedGames = CuT.getSpectatedGames();
        Map<Player, CheckersGame> spectators = CuT.getSpectators();
        // Assert that the game was added to spectatedGames
        assertTrue(spectatedGames.containsKey(gameID));
        assertTrue(spectatedGames.get(gameID).contains(spectator1));
        // Assert that the spectator was added to spectators
        assertTrue(spectators.containsKey(spectator1));
        assertEquals(game, spectators.get(spectator1));

        // Case: game already in spectatedGames
        Player spectator2 = playerLobby.getPlayer("spectator2");
        CuT.addSpectator(gameID, spectator2);
        spectatedGames = CuT.getSpectatedGames();
        spectators = CuT.getSpectators();
        //Assert that the spectator was added to the set of players spectating the game
        assertTrue(spectatedGames.get(gameID).contains(spectator2));
        // Assert that the spectator was added to spectators
        assertTrue(spectators.containsKey(spectator2));
        assertEquals(game, spectators.get(spectator2));
    }

    /**
     * Test removing a spectator.
     */
    @Test
    public void test_removeSpectator() {
        CuT.addPlayer("Player1");
        CuT.addPlayer("Player2");
        CuT.addPlayer("spectator1");
        CuT.createGame("Player1","Player2");
        int gameID = Objects.hash("Player1","Player2");
        CheckersGame game = CuT.getGameByID(gameID);
        Player spectator1 = playerLobby.getPlayer("spectator1");
        CuT.addSpectator(gameID, spectator1);

        // Invoke the test
        CuT.removeSpectator(gameID, spectator1);
        Map<Integer, Set<Player>> spectatedGames = CuT.getSpectatedGames();
        Map<Player, CheckersGame> spectators = CuT.getSpectators();
        //Assert that the spectator was removed from the set of players spectating the game
        assertFalse(spectatedGames.get(gameID).contains(spectator1));
        // Assert that the spectator was removed from  spectators
        assertFalse(spectators.containsKey(spectator1));
    }

    /**
     * Test getting a game by a player spectating it.
     */
    @Test
    public void test_getGameBySpectator() {
        CuT.addPlayer("Player1");
        CuT.addPlayer("Player2");
        CuT.addPlayer("spectator");
        CuT.createGame("Player1","Player2");
        int gameID = Objects.hash("Player1","Player2");
        CheckersGame game = CuT.getGameByID(gameID);
        Player spectator = playerLobby.getPlayer("spectator");

        // Case: the spectator is not spectating any games
        assertNull(CuT.getGameBySpectator(spectator));

        // Case: the spectator is spectating an active game
        CuT.addSpectator(gameID, spectator);
        assertEquals(game, CuT.getGameBySpectator(spectator));
    }

    /**
     * Test resigning from a game.
     */
    @Test
    public void test_resign() {
        // Case: null player
        assertEquals(GameCenter.COULD_NOT_RESIGN_MESSAGE, CuT.resign(null));

        // Case: null game
        CuT.addPlayer("Player1");
        assertEquals(GameCenter.COULD_NOT_RESIGN_MESSAGE, CuT.resign("Player1"));

        // Case: resign successfully
        CuT.addPlayer("Player2");
        CuT.createGame("Player1","Player2");
        assertEquals(GameCenter.RESIGNED_MESSAGE, CuT.resign("Player1"));
    }
}
