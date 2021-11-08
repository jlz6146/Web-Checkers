/**
 * This module exports the GameState class constructor.
 * 
 * This Value Object holds a snapshot of the state of the checkers game.
 * Information about who the players are, the current player and whose
 * turn is it.
 */
define(function(require){
  'use strict';

  // imports
  const BrowserUtils = require('../util/BrowserUtils');

  /**
   * Constructor function.
   */
  function GameState(gameData) {

    // public (internal) methods

    /**
     * Get the unique ID of this game.
     */
    this.getGameID = function getGameID() {
      return BrowserUtils.getParameterByName('gameID') || gameData.gameID;
    };

    /**
     * Get the View mode for the Game View.
     * One of PLAY, SPECTATOR or REPLAY
     */
    this.getViewMode = function getViewMode() {
      return gameData.viewMode;
    };

    /**
     * Get an object representing a set of mode-specific options to the Game View.
     */
    this.getModeOptions = function getModeOptions() {
      return gameData.modeOptions;
    };

    /**
     * Get a single mode-specific option value from the name.
     */
    this.getModeOption = function getModeOption(optionName) {
      return gameData.modeOptions[optionName];
    };

    /**
     * Get the name of the Player currently viewing the Game View.
     * This is the HTTP session's user.
     */
    this.getCurrentUser = function getCurrentUser() {
      return gameData.currentUser;
    };

    /**
     * Get the name of the RED Player in this game.
     */
    this.getRedPlayer = function getRedPlayer() {
      return gameData.redPlayer;
    };

    /**
     * Get the name of the WHITE Player in this game.
     */
    this.getWhitePlayer = function getWhitePlayer() {
      return gameData.whitePlayer;
    };

    /**
     * Get the name of the Player whose turn is active.
     */
    this.getActivePlayer = function getActivePlayer() {
      return this.isRedsTurn() ? gameData.redPlayer : gameData.whitePlayer;
    };

    /**
     * Get the name of the opponent of the current Player.
     * This method is only applicable in the PLAY mode.
     */
    this.getOpponentPlayer = function getOpponentPlayer() {
      return this.isPlayerRed() ? gameData.whitePlayer : gameData.redPlayer;
    };

    /**
     * Query whether RED is the active player.
     *
     * @return {boolean}  true if the active player is the RED player
     */
    this.isRedsTurn = function isRedsTurn() {
      return gameData.activeColor === 'RED';
    };

    /**
     * Query whether current user is the RED player.
     *
     * @return {boolean}  true if the current user is the RED player
     */
    this.isPlayerRed = function isPlayerRed() {
      return gameData.redPlayer === gameData.currentUser;
    };

    /**
     * Query whether current user is the WHITE player.
     *
     * @return {boolean}  true if the current user is the WHITE player
     */
    this.isPlayerWhite = function isPlayerWhite() {
      return gameData.whitePlayer === gameData.currentUser;
    };

  }

  //
  // Public (external) methods
  //

  /**
   * The name of the modeOption that states when the game is over.
   */
  GameState.IS_END_OPTION = "isGameOver";

  /**
   * The name of the modeOption that states when the game is over.
   */
  GameState.END_MESSAGE_OPTION = "gameOverMessage";

  /**
   * Queries whether this state is valid for the Play mode.
   */
  GameState.prototype.isValidInPlayMode = function isValidInPlayMode() {
      return (this.getCurrentUser() === this.getRedPlayer()
          || this.getCurrentUser() === this.getWhitePlayer());
  };

  /**
   * Queries whether the game is over; IOWs someone won or resgined.
   */
  GameState.prototype.isGameOver = function isGameOver() {
      return this.getModeOption(GameState.IS_END_OPTION) || false;
  };

  /**
   * Supplies the 'end of game' message.
   */
  GameState.prototype.getGameOverMessage = function getGameOverMessage() {
    if (!this.isGameOver()) {
      throw new Error("Game isn't over yet.")
    }
    return this.getModeOption(GameState.END_MESSAGE_OPTION) || "Game over, man!";
  };

    /**
   * Queries whether it's the current player's turn.
   */
  GameState.prototype.isMyTurn = function isMyTurn() {
    return (this.isPlayerRed() && this.isRedsTurn())
    || (this.isPlayerWhite() && !this.isRedsTurn());
  };
  
  // export class constructor
  return GameState;
  
});
