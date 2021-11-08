/**
 * This module exports the WaitingForMoveValidationState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the view state
 * in which the player has requested a move and is waiting
 * for server validation.
 */
define(function(require){
  'use strict';
  
  // imports
  const PlayModeConstants = require('./PlayModeConstants');
  const AjaxUtils = require('../../util/AjaxUtils');

  /**
   * Constructor function.
   * 
   * @param {PlayController} controller
   *    The Play mode controller object.
   */
  function WaitingForMoveValidationState(controller) {
    // private attributes
    this._controller = controller;
  }

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  WaitingForMoveValidationState.prototype.getName = function getName() {
    return PlayModeConstants.WAITING_FOR_MOVE_VALIDATION;
  };
  
  /**
   * Hook when entering this state.
   */
  WaitingForMoveValidationState.prototype.onEntry = function onEntry() {
    const move = this._controller.getPendingMove();

    // 1) disable UI controls
    this._controller.disableButton(PlayModeConstants.BACKUP_BUTTON_ID);
    this._controller.disableButton(PlayModeConstants.SUBMIT_BUTTON_ID);
    this._controller.disableButton(PlayModeConstants.RESIGN_BUTTON_ID);
    
    // 2) disable all Pieces
    this._controller.disableAllMyPieces();
    
    // 3) ask the server to validate the pending move
    AjaxUtils.callServerWithData(
        // the action takes a single move
        '/validateMove', move,
        // the handler method should be run in the context of 'this' State object
        handleResponse, this);
  };

  //
  // Private methods
  //

  function handleResponse(message) {
    this._controller.displayMessage(message);
    if (message.type === 'ERROR') {
      this._controller.resetPendingMove();
      const hasMoves = this._controller.isTurnActive();
      this._controller.setState(hasMoves ? PlayModeConstants.STABLE_TURN : PlayModeConstants.EMPTY_TURN);
    }
    else {
      this._controller.addPendingMove();
      this._controller.setState(PlayModeConstants.STABLE_TURN);
    }
  }

  // export class constructor
  return WaitingForMoveValidationState;
  
});
