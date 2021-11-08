package com.webcheckers.application;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import java.util.*;
import java.util.logging.Logger;

/**
 * Used to coordinate the state of the WebCheckers Application.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class GameCenter {
    //Values used for holding active games and players
    private static final Logger LOG = Logger.getLogger(GameCenter.class.getName());
    private final PlayerLobby lobby;
    private final HashMap<Player, CheckersGame> inGame;
    private final HashMap<Integer, CheckersGame> games;
    private final HashMap<Integer, Set<Player>> spectatedGames;
    private final HashMap<Player, CheckersGame> spectators;
    private final HashMap<Player, CheckersGame> inEndGame;

    // Messages to alert to an unmade move/invalid single move when jump move is available
    static final Message COULD_NOT_RESIGN_MESSAGE = Message.error("Could not resign!");

    // Message to alert the player the the game has ended
    static final Message RESIGNED_MESSAGE = Message.info("Resigned successfully!");


    /**
     * An enum used when returning info about the status of game creation.
     */
    public enum GameStatus {
        IN_GAME,
        NULL_PLAYER,
        SAME_PLAYER,
        SPECTATING,
        CREATED
    }

    /**
     * Get the opponent of the player whose name is passed in, if an opponent exists.
     *
     * @param name the name of the current player
     * @return the current player's opponent, if an opponent exists
     */
    public Player getOpponent(String name) {
        CheckersGame game = getGame(name);
        if(game != null) {
            Player red = game.redPlayer();
            if(red.getName().equals(name)) {
                return game.whitePlayer();
            } else {
                return red;
            }
        }
        return null;
    }

    /**
     * Get the active player lobby.
     *
     * @return the player lobby
     */
    public PlayerLobby getLobby() {
        return lobby;
    }

    /**
     * Get the game the player whose name is passed in is currently playing, if they are in a game.
     *
     * @param name the name of the current player
     * @return the game the current player is in, if one exists
     */
    public CheckersGame getGame(String name) {
        return inGame.get(lobby.getPlayer(name));
    }

    /**
     * Get a game by its unique identifier.
     *
     * @param gameID the ID of the game
     * @return the game if it exists
     */
    public CheckersGame getGameByID(int gameID) {
        return games.get(gameID);
    }

    /**
     * Create a new GameCenter.
     *
     * @param playerLobby the lobby of active players
     */
    public GameCenter(PlayerLobby playerLobby) {
        this.inGame = new HashMap<>();
        this.lobby = playerLobby;
        this.games = new HashMap<>();
        this.spectatedGames = new HashMap<>();
        this.spectators = new HashMap<>();
        this.inEndGame = new HashMap<>();
    }

    /**
     * Add a player to the player lobby.
     *
     * @param name the name of the player
     */
    public void addPlayer(String name){
        this.lobby.addPlayer(name);
    }

    /**
     * Attempts to create a new game of WebCheckers.
     *
     * @param redPlayerName the name of the player using red checkers
     * @param whitePlayerName the name of the player using white checkers
     * @return whether or not a game was successfully created
     */
    public synchronized GameStatus createGame(String redPlayerName, String whitePlayerName) {
        Player red = lobby.getPlayer(redPlayerName);
        Player white = lobby.getPlayer(whitePlayerName);

        if(inGame.containsKey(red) || inGame.containsKey(white)) {
            return GameStatus.IN_GAME;
        } else if(spectators.containsKey(red) || spectators.containsKey(white)) {
            return GameStatus.SPECTATING;
        } else if(red == null || white == null) {
            return GameStatus.NULL_PLAYER;
        } else if(red.equals(white)) {
            return GameStatus.SAME_PLAYER;
        }

        CheckersGame game = new CheckersGame(red, white, CheckersGame.Mode.PLAY, new BoardView(red, white));
        inGame.put(red, game);
        inGame.put(white, game);
        games.put(Objects.hash(redPlayerName, whitePlayerName), game);

        LOG.info("New checkers game created for " + red.getName() + " and " + white.getName());

        return GameStatus.CREATED;
    }

    /**
     * End the game the player is in, and remove both players from the game.
     *
     * @param name the name of the player to be removed
     */
    public synchronized void endGame(String name) {
        Player player = lobby.getPlayer(name);
        Player opponent = getOpponent(name);
        if(player != null && inGame(name)) {
            inEndGame.put(player, inGame.get(player));
            if(opponent != null && inGame(opponent.getName())) {
                inEndGame.put(opponent, inGame.get(opponent));
            }
        }
    }

    /**
     * Remove the player with the name passed in from the game they are in, if any.
     *
     * @param name the name of the player trying to exit a game
     */
    public synchronized void exitGame(String name) {
        Player player = lobby.getPlayer(name);
        if(player != null) {
            CheckersGame game = getGame(name);
            inEndGame.remove(player);
            inGame.remove(player);
        }
    }

    /**
     * Check if a player is already in a game.
     *
     * @param name the name of the player being checked
     * @return whether or not the player is already in a game
     */
    public synchronized boolean inGame(String name){
        return inGame.containsKey(lobby.getPlayer(name));
    }

    /**
     * End an active game if the player is in one, then remove them from the player lobby.
     *
     * @param name the name of the player to be removed
     */
    public synchronized void removePlayer(String name){
        if(inGame(name)) {
            endGame(name);
        }
        lobby.removePlayer(name);
    }

    /**
     * Get all active games and their game id's.
     *
     * @return A map of all active games and their game id's
     */
    public synchronized Map<String, String> getAllActiveGames() {
        HashMap<String, String> activeGames = new HashMap<>();
        for(int id : games.keySet()) {
            if(!games.get(id).isGameOver()) {
                String gameStr = games.get(id).toString();
                activeGames.put(gameStr, Integer.toString(id));
            }
        }
        return activeGames;
    }


    /**
     * Add a player to the set of players spectating the game with the gameID passed in.
     *
     * @param gameID the ID of the game to spectate
     * @param spectator the player to spectate said game
     */
    public void addSpectator(int gameID, Player spectator) {
        this.spectators.put(spectator, this.getGameByID(gameID));
        if(!this.spectatedGames.containsKey(gameID)) {
            this.spectatedGames.put(gameID, new HashSet<>());
        }
        this.spectatedGames.get(gameID).add(spectator);
    }

    /**
     * Remove the player passed in from the set of players spectating the game with the gameID passed in.
     *
     * @param gameID the ID of the game to remove the spectator from
     * @param spectator the player that will no longer be spectating said game
     */
    public void removeSpectator(int gameID, Player spectator) {
        this.spectatedGames.get(gameID).remove(spectator);
        this.spectators.remove(spectator);
    }

    /**
     * Get the game the player whose name is passed in is currently spectating, if they are spectating a game.
     *
     * @param spectator the name of the current player
     * @return the game the spectator is spectating, if one exists
     */
    public CheckersGame getGameBySpectator(Player spectator) {
        return this.spectators.get(spectator);
    }

    /**
     * Get the map of currently spectated games.
     *
     * @return all currently spectated games
     */
    public Map<Integer, Set<Player>> getSpectatedGames() {
        return spectatedGames;
    }

    /**
     * Get the map of spectators.
     *
     * @return all active spectators
     */
    public Map<Player, CheckersGame> getSpectators() {
        return spectators;
    }


    /**
     * Check if a player is in a game that has ended.
     *
     * @param name the name of the player being checked
     * @return whether or not the player is already in a game
     */
    public synchronized boolean inEndGame(String name) {
        return inEndGame.containsKey(lobby.getPlayer(name));
    }

    /**
     * Tries to end the game the player is in by them resigning.
     *
     * @param name the name of the player trying to resign
     * @return a message determining if a they successfully resigned
     */
    public Message resign(String name) {
        Player player = lobby.getPlayer(name);
        if(player == null) {
            return COULD_NOT_RESIGN_MESSAGE;
        }
        CheckersGame game = getGame(name);
        if(game == null) {
            return COULD_NOT_RESIGN_MESSAGE;
        }
        game.endGame(CheckersGame.EndReason.RESIGNED, player.getColor());
        endGame(name);
        return RESIGNED_MESSAGE;
    }
}
