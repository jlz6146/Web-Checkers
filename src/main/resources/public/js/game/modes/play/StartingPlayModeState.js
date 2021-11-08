/**
 * This module exports the StartingPlayModeState class constructor.
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
   * @param {GameView} view
   *    The Game view object.
   * @param {GameState} gameState
   *    The state of the game.
   */
  function StartingPlayModeState(controller, view, gameState) {
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
  StartingPlayModeState.prototype.getName = function getName() {
    return PlayModeConstants.STARTING_PLAY_MODE;
  };
  
  /**
   * Method when entering this state.
   */
  StartingPlayModeState.prototype.onEntry = function onEntry() {
    // validate game state for Play mode
    if (!this._gameState.isValidInPlayMode()) {
      alert('StartingPlayModeState invalid state variables.');
      return;
    }
    // 1) initialize the main View content
    this._initializeView();
    // 2) decide which state to transition to next
    if (this._gameState.isGameOver()) {
      console.debug(this._gameState.getGameOverMessage());
      this._controller.setState(PlayModeConstants.GAME_OVER);
    } else {
      // hide the Exit button
      this._controller.hideButton(PlayModeConstants.EXIT_BUTTON_ID);
      // switch to an active Play state
      if (this._gameState.isMyTurn()) {
        console.debug("It's your turn.");
        this._controller.setState(PlayModeConstants.EMPTY_TURN);
      } else {
        console.debug("It's not your turn.");
        this._controller.setState(PlayModeConstants.WAITING_TO_CHECK_MY_TURN);
      }
    }
  };
  
  //
  // Private (external) methods
  //

  /**
   * Initializes the content in the game-info fieldset.
   */
  StartingPlayModeState.prototype._initializeView = function _initializeView() {
    // Create helper text
    var opponentPlayer = this._gameState.getOpponentPlayer();
    var helperText = 'You are playing a game of checkers with ' + opponentPlayer + '. <br/><br/>';
    if (this._gameState.isGameOver()) {
      helperText += '<b>' + this._gameState.getGameOverMessage() + '</b>';
    } else if (this._gameState.isMyTurn()) {
      helperText += "It's your turn.  Drag-and-drop one of your pieces to make moves.\n"
          + "Use the Backup button to remove the most recent move. \n"
          + "Use the Submit button when you are ready to commit your complete turn.";
    } else {
      helperText += "It's " + opponentPlayer + " turn.  The page will refresh periodically\n";
      helperText += "and you will be informed when it is your turn.";
    }
    this._view.setHelperText(helperText);
    
    // Adjust current player's indicator
    if (this._gameState.isPlayerRed()) {
      this._view.setRedPlayersName('You');
    }
    if (this._gameState.isPlayerWhite()) {
      this._view.setWhitePlayersName('You');
    }
  };
  
  // export class constructor
  return StartingPlayModeState;
  
});
