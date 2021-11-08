/**
 * This module exports the StartingReplayModeState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the starting state
 * of the Replay mode.
 * 
 * This is an exercise for the student.
 */
define(function(require){
  'use strict';
  
  // imports
  var ReplayModeConstants = require('./ReplayModeConstants');

  /**
   * Constructor function.
   * 
   * @param {ReplayController} controller
   *    The Replay mode controller object.
   * @param {GameView} view
   *    The Game view object.
   * @param {GameState} gameState
   *    The state of the game.
   */
  function StartingReplayModeState(controller, view, gameState) {
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
  StartingReplayModeState.prototype.getName = function getName() {
    return ReplayModeConstants.STARTING_REPLAY_MODE;
  };
  
  /**
   * Method when entering this state.
   * 
   * Build and begin the Replay view mode.
   */
  StartingReplayModeState.prototype.onEntry = function onEntry() {
    // initialize the main View content
    this._initializeView();
    //
    this._controller.setState(ReplayModeConstants.WAITING_FOR_USER_ACTION);
  };

  //
  // Private (external) methods
  //

  /**
   * Initializes the content in the game-info fieldset.
   */
  StartingReplayModeState.prototype._initializeView = function _initializeView() {
    // Create helper text
    let helperText = `${this._gameState.getRedPlayer()}, Red, is playing ${this._gameState.getWhitePlayer()}. <br/><br/>`;
    if (this._gameState.isGameOver()) {
      helperText += `<b> ${this._gameState.getGameOverMessage()} </b>`;
    } else {
      helperText += 'Use the Next and Previous buttons to move forward and backward between turns. <br/><br/>';
    }
    this._view.setHelperText(helperText);
  };

  // export class constructor
  return StartingReplayModeState;
  
});
