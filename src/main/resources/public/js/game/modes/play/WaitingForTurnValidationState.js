/**
 * This module exports the WaitingForTurnValidationState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the view state
 * in which the player has submitted their turn and is waiting
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
  function WaitingForTurnValidationState(controller) {
    // private attributes
    this._controller = controller;
  }

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  WaitingForTurnValidationState.prototype.getName = function getName() {
    return PlayModeConstants.WAITING_FOR_TURN_VALIDATION;
  };
  
  /**
   * Hook when entering this state.
   */
  WaitingForTurnValidationState.prototype.onEntry = function onEntry() {
    // clear the turn temporarily (to put back if the SubmitTurn action fails)
    this._controller.clearTurnDuringSubmit();
    // send the action to the server
    AjaxUtils.callServer('/submitTurn',
        // the handler method should be run in the context of 'this' State object
        handleResponse, this);
  };

  //
  // Private methods
  //

  /**
   * Handle the Ajax reponse.
   *
   * If successfully, this action refreshes Game view.
   *
   * @param message  the Message from the server
   */
  function handleResponse(message) {
    this._controller.displayMessage(message);
    if (message.type === 'INFO') {
      // end the State machine by refreshing the Game View (via a browser page request)
      this._controller.refresh();
    }
    // handle error message
    // there are valid error conditions, such as not completing a jump sequence.
    else {
      this._controller.displayMessage(message);
      // put the turn state back
      this._controller.putTurnBackAfterFailedSubmit();
      this._controller.setState(PlayModeConstants.STABLE_TURN);
    }
  }

  // export class constructor
  return WaitingForTurnValidationState;
  
});
