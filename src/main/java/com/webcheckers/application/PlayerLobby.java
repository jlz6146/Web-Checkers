package com.webcheckers.application;

import com.webcheckers.model.Player;

import java.util.HashMap;
import java.util.Set;

/**
 * Holds all currently logged in players and is used to add more players as they log in.
 *
 * @author Jesse Burdick-Pless jb4411@g.rit.edu
 */
public class PlayerLobby {
    private final HashMap<String, Player> players;

    /**
     * An enum used when returning info about the status of adding a player to the lobby.
     */
    public enum NameStatus {INVALID, VALID, DUPLICATE}

    //The string format used for PlayerLobby.toString()
    static final String LOBBY_STRING_FORMAT = "{Lobby, Number of Players: %d, Active Players: [%s]}";

    /**
     * Create a new PlayerLobby to hold active players.
     */
    public PlayerLobby() {
        this.players = new HashMap<>();
    }

    /**
     * Try to add a new player, returns a message to the user if the selected name is already in use.
     *
     * @param name the name of the player to try to add
     * @return the status of the player creation
     */
    public synchronized NameStatus addPlayer(String name) {
        if(name == null) {
            return NameStatus.INVALID;
        }
        if(!name.matches("[a-zA-Z0-9]+[a-zA-Z0-9\\s]*$")) {
            return NameStatus.INVALID;
        } else if(players.containsKey(name)) {
            return NameStatus.DUPLICATE;
        }

        Player newPlayer = new Player(name);
        players.put(name, newPlayer);
        return NameStatus.VALID;
    }

    /**
     * Return the number of currently active players.
     *
     * @return the number of active players
     */
    public int getNumPlayers() {
        return this.players.size();
    }

    /**
     * Return the player with the name given, if a player with that name exists.
     *
     * @param name the name of the player to get
     * @return the player with the given name, if a player with that name exists
     */
    public Player getPlayer(String name){
        return players.get(name);
    }

    /**
     * Return a set of the names of all currently active players.
     *
     * @return a set of the names of all currently active players
     */
    public Set<String> getAllPlayers() {
        return players.keySet();
    }

    /**
     * Remove a player from the lobby of active players.
     *
     * @param name the name of the player to be removed
     */
    public void removePlayer(String name) {
        if(name != null) {
            players.remove(name);
        }
    }

    /**
     * Return the string representation of the PlayerLobby.
     *
     * @return the string representation of the PlayerLobby
     */
    @Override
    public synchronized String toString() {
        StringBuilder active_players = new StringBuilder("");
        String player;
        Object[] playerList = players.keySet().toArray();
        for (int i = 0; i < players.size(); i++) {
            player = (String) playerList[i];
            if (i == players.size()-1) {
                active_players.append(player);
            } else {
                active_players.append(player).append(", ");
            }
        }

        return String.format(LOBBY_STRING_FORMAT, players.size(), active_players);
    }
}
