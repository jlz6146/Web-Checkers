/**
 * This module exports the CheckingForNextTurnState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the state in which
 * the view makes the Ajax call to the server to check whether
 * the next turn has been made in the game being spectated.
 */
define(function(require){
  'use strict';

  // imports
  const SpectatorModeConstants = require('./SpectatorModeConstants');
  const AjaxUtils = require('../../util/AjaxUtils');

  /**
   * Constructor function.
   * 
   * @param {SpectatorController} controller
   *    The Spectator mode controller object.
   */
  function CheckingForNextTurnState(controller) {
    // private attributes
    this._controller = controller;
  }

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  CheckingForNextTurnState.prototype.getName = function getName() {
    return SpectatorModeConstants.CHECKING_FOR_NEXT_TURN;
  };

  /**
   * Method when entering this state.
   */
  CheckingForNextTurnState.prototype.onEntry = function onEntry() {
    // query the server if the next turn has been played
    AjaxUtils.callServer('/spectator/checkTurn',
        // the handler method should be run in the context of 'this' State object
        handleResponse, this);
  };

  //
  // Private methods
  //

  function handleResponse(message) {
    // is it successful?
    if (message.type === 'INFO') {
      // check for special case messages
      if (message.text === 'true') {
        // tell the browser to redisplay the Game View to get the updated board
        window.location.reload(true);
      }
      // otherwise, check to see if there is a message to display
      else {
        if (message.text !== 'false') {
          this._controller.displayMessage(message);
        }
        // and go back to the wait state
        this._controller.setState(SpectatorModeConstants.WAITING_FOR_NEXT_TURN);
      }
    }
    // handle error message
    else {
      this._controller.displayMessage(message);
      this._controller.setState(SpectatorModeConstants.WAITING_FOR_NEXT_TURN);
    }
  }

  // export class constructor
  return CheckingForNextTurnState;
  
});
