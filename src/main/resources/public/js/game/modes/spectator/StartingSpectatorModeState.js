/**
 * This module exports the StartingSpectatorModeState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the starting state
 * of the Spectator mode.
 * 
 * This is an exercise for the student.
 */
define(function(require){
  'use strict';
  
  // imports
  var SpectatorModeConstants = require('./SpectatorModeConstants');

  /**
   * Constructor function.
   * 
   * @param {SpectatorController} controller
   *    The Spectator mode controller object.
   * @param {GameView} view
   *    The Game view object.
   * @param {GameState} gameState
   *    The state of the game.
   */
  function StartingSpectatorModeState(controller, view, gameState) {
    // private attributes
    this._controller = controller;
    this._view = view;
    this._gameState = gameState;
  }

  //
  // Public (external) methods
  //

  /**
   * Get the symbolic name this state.
   */
  StartingSpectatorModeState.prototype.getName = function getName() {
    return SpectatorModeConstants.STARTING_SPECTATOR_MODE;
  };
  
  /**
   * Method when entering this state.
   * 
   * Build and begin the Spectator view mode.
   */
  StartingSpectatorModeState.prototype.onEntry = function onEntry() {
    // initialize the main View content
    this._initializeView();
    //
    this._controller.setState(SpectatorModeConstants.WAITING_FOR_NEXT_TURN);
  };

  //
  // Private (external) methods
  //

  /**
   * Initializes the content in the game-info fieldset.
   */
  StartingSpectatorModeState.prototype._initializeView = function _initializeView() {
    // Create helper text
    let helperText = `${this._gameState.getRedPlayer()}, Red, is playing ${this._gameState.getWhitePlayer()}. <br/><br/>`;
    if (this._gameState.isGameOver()) {
      helperText += `<b> ${this._gameState.getGameOverMessage()} </b>`;
    } else {
      helperText += `It's ${this._gameState.getActivePlayer()} turn.  The page will refresh periodically.`;
    }
    this._view.setHelperText(helperText);
  };

  // export class constructor
  return StartingSpectatorModeState;
  
});
