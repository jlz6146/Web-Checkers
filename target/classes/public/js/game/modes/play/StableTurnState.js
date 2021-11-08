/**
 * This module exports the StableTurnState class constructor.
 * 
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the view state
 * in which the player has created a non-empty Turn.  From
 * this state the user may request another move or to submit
 * the current set of moves as a single turn.
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
  function StableTurnState(controller) {
    // private attributes
    this._controller = controller;
  }

  //
  // Public (external) methods
  //

  /**
   * Get the name of this state.
   */
  StableTurnState.prototype.getName = function getName() {
    return PlayModeConstants.STABLE_TURN;
  };

  /**
   * Hook when entering this state.
   */
  StableTurnState.prototype.onEntry = function onEntry() {
    // enable all UI controls
    this._controller.enableButton(PlayModeConstants.BACKUP_BUTTON_ID);
    this._controller.enableButton(PlayModeConstants.SUBMIT_BUTTON_ID);
    // re-enable active Piece
    this._controller.enableActivePiece();
  };

  /**
   * The player may request an additional move for a given turn.
   */
  StableTurnState.prototype.requestMove = function requestMove(pendingMove) {
    // register the requested move
    this._controller.setPendingMove(pendingMove);
    // and change the data to Pending
    this._controller.setState(PlayModeConstants.WAITING_FOR_MOVE_VALIDATION);
  };

  /**
   * Backup a single move.
   */
  StableTurnState.prototype.backupMove = function backupMove() {
    this._controller.setState(PlayModeConstants.WAITING_FOR_BACKUP_VALIDATION);
  };

  /**
   * Submit the Turn to the server.
   */
  StableTurnState.prototype.submitTurn = function submitTurn() {
    this._controller.setState(PlayModeConstants.WAITING_FOR_TURN_VALIDATION);
  };

  // export class constructor
  return StableTurnState;
  
});
