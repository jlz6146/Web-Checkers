/**
 * This module exports the CheckingMyTurnState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the state in which
 * the view makes the Ajax call to the server to check whether
 * it's the current player's turn.
 */
define(function (require) {
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
  function CheckingMyTurnState(controller) {
    // private attributes
    this._controller = controller;
  }

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  CheckingMyTurnState.prototype.getName = function getName() {
    return PlayModeConstants.CHECKING_MY_TURN;
  };

  /**
   * Method when entering this state.
   */
  CheckingMyTurnState.prototype.onEntry = function onEntry() {
    this._controller.disableButton(PlayModeConstants.RESIGN_BUTTON_ID);
    // query the server if it's my turn
    AjaxUtils.callServer('/checkTurn',
        // the handler method should be run in the context of 'this' State object
        handleResponse, this);
  };

  //
  // Private methods
  //

  function handleResponse(message) {
    if (message.type === 'INFO') {
      if (message.text === 'true') {
        // end the State machine by refreshing the Game View (via a browser page request)
        this._controller.refresh();
      } else {
        this._controller.setState(PlayModeConstants.WAITING_TO_CHECK_MY_TURN);
      }
    }
    // handle error message
    else {
      this._controller.displayMessage(message);
      this._controller.setState(PlayModeConstants.WAITING_TO_CHECK_MY_TURN);
    }
  }

  // export class constructor
  return CheckingMyTurnState;
  
});
