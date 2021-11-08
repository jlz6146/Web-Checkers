/**
 * This module exports the GameOverState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the Play mode starting state.
 * Its job is to initialize the Play game controls and then decide the
 * next state based upon whether the it's the current player's turn or not.
 */
define(function(require){
  'use strict';
  
  // imports
  const PlayModeConstants = require('./PlayModeConstants');

  /**
   * Constructor function.
   * 
   * @param {PlayController} controller
   *    The Play mode controller object.
   */
  function GameOverState(controller, gameState) {
    // private attributes
    this._controller = controller;
    this._gameState = gameState;
  }

  //
  // Public (external) methods
  //

  /**
   * Get the symbolic name this state.
   */
  GameOverState.prototype.getName = function getName() {
    return PlayModeConstants.GAME_OVER;
  };
  
  /**
   * Method when entering this state.
   */
  GameOverState.prototype.onEntry = function onEntry() {
    // setup the View for Game Over state
    this._controller.hideButton(PlayModeConstants.BACKUP_BUTTON_ID);
    this._controller.hideButton(PlayModeConstants.SUBMIT_BUTTON_ID);
    this._controller.hideButton(PlayModeConstants.RESIGN_BUTTON_ID);
  };

  // export class constructor
  return GameOverState;
  
});
