/**
 * This module exports the PlayController class constructor.
 * 
 * This component manages the Play mode behavior of the GameView.
 * 
 * It uses the State pattern to handle the changes in UI behavior
 * as the view changes from one state to another.
 */
define(function(require){
  'use strict';
  
  // imports
  const StatePatternMixin = require('../../util/StatePatternMixin');
  const ControlsToolbarMixin = require('../../util/ControlsToolbarMixin');
  const AjaxUtils = require('../../util/AjaxUtils');
  const LangUtils = require('../../util/LangUtils');
  const PlayModeConstants = require('./PlayModeConstants');

  // import PLAY mode states
  const StartingPlayModeState = require('./StartingPlayModeState');
  // "Playing My Turn" composite states
  const EmptyTurnState = require('./EmptyTurnState');
  const WaitingForMoveValidationState = require('./WaitingForMoveValidationState');
  const StableTurnState = require('./StableTurnState');
  const WaitingForTurnValidationState = require('./WaitingForTurnValidationState');
  const WaitingForBackupValidationState = require('./WaitingForBackupValidationState');
  // "Waiting for My Turn" composite states
  const WaitingForMyTurnState = require('./WaitingForMyTurnState');
  const CheckingMyTurnState = require('./CheckingMyTurnState');
  // The "Game Over" singular state
  const GameOverState = require('./GameOverState');

  //
  // Constructor
  //

  /**
   * Constructor function.
   * 
   * @param {GameView} view
   *    The Game view object.
   * @param {BoardController} boardController
   *    The game board controller object.
   * @param {GameState} gameState
   *    The state of the game.
   */
  function PlayController(view, boardController, gameState) {
    // private data
    this._turn = [];
    this._turnTemp = null;
    this._pendingMove = null;
    this.$activePiece = null;
    this._boardController = boardController;
    this._gameState = gameState;
    
    // Add the State Pattern mixin
    StatePatternMixin.call(this);
    // create states and a lookup map
    this.addStateDefinition(PlayModeConstants.STARTING_PLAY_MODE,
        new StartingPlayModeState(this, view, gameState));
    // "Playing My Turn" composite states
    this.addStateDefinition(PlayModeConstants.EMPTY_TURN,
        new EmptyTurnState(this));
    this.addStateDefinition(PlayModeConstants.WAITING_FOR_MOVE_VALIDATION,
        new WaitingForMoveValidationState(this));
    this.addStateDefinition(PlayModeConstants.STABLE_TURN,
        new StableTurnState(this));
    this.addStateDefinition(PlayModeConstants.WAITING_FOR_TURN_VALIDATION,
        new WaitingForTurnValidationState(this));
    this.addStateDefinition(PlayModeConstants.WAITING_FOR_BACKUP_VALIDATION,
        new WaitingForBackupValidationState(this));
    // "Waiting for My Turn" composite states
    this.addStateDefinition(PlayModeConstants.WAITING_TO_CHECK_MY_TURN,
        new WaitingForMyTurnState(this));
    this.addStateDefinition(PlayModeConstants.CHECKING_MY_TURN,
        new CheckingMyTurnState(this));
    // The "Game Over" singular state
    this.addStateDefinition(PlayModeConstants.GAME_OVER,
        new GameOverState(this, gameState));
    
    // Add the Controls toolbar mixin
    ControlsToolbarMixin.call(this);
    // create mode control buttons
    this.addButton(PlayModeConstants.BACKUP_BUTTON_ID, 'Backup', false,
        PlayModeConstants.BACKUP_BUTTON_TOOLTIP, this.backupMove);
    this.addButton(PlayModeConstants.SUBMIT_BUTTON_ID, 'Submit turn', false,
        PlayModeConstants.SUBMIT_BUTTON_TOOLTIP, this.submitTurn);
    this.addButton(PlayModeConstants.RESIGN_BUTTON_ID, 'Resign', true,
        PlayModeConstants.RESIGN_BUTTON_TOOLTIP, this.resignGame);
    this.addButton(PlayModeConstants.EXIT_BUTTON_ID, 'Exit', true,
        PlayModeConstants.EXIT_BUTTON_TOOLTIP, this.exitGame);

    // Public (internal) methods

    /**
     * Start Play mode.
     */
    this.startup = function startup() {
      // start Play mode
      this.setState(PlayModeConstants.STARTING_PLAY_MODE);
    };

    /**
     * Gets a jQuery element for the Piece as a specific position.
     * Returns null if there is no Piece at that Space.
     */
    this.initializePlayMode = function initializePlayMode() {
      // establish Piece move listener for Play mode
      boardController.addPieceMoveListener((event) => this.requestMove(event.move));
      // initialize the Board for game play
      boardController.initializeDragAndDrop(gameState);
    };
    
    /**
     * Display a message to the Game View.
     * Delegate to the GameView component.
     */
    this.displayMessage = function displayMessage(message) {
      view.displayMessage(message);
    }

  }

  //
  // Public (external) methods
  //

  /**
   * Request a move; could be a single move or a jump.
   * This message has state-specific behavior.
   */
  PlayController.prototype.requestMove = function requestMove() {
    this._delegateStateMessage('requestMove', arguments);
  };

  /**
   * Backup a single move.  This message has state-specific behavior.
   */
  PlayController.prototype.backupMove = function backupMove() {
    this._delegateStateMessage('backupMove', arguments);
  };

  /**
   * This user action submits a turn to the server.
   */
  PlayController.prototype.submitTurn = function submitTurn() {
    this._delegateStateMessage('submitTurn', arguments);
  };

  /**
   * Queries whether the Game View can be deactivated; usually from
   * navigating way from the page.
   *
   * @return {boolean} true, if there is no active turn
   */
  PlayController.prototype.canDeactivate = function canDeactivate() {
    return ! this.isTurnActive();
  };

  /**
   * Resign from the game.
   * 
   * This action leaves the current Game view and retrieves an
   * updated Game view from the server.
   */
  PlayController.prototype.resignGame = function resignGame() {
    // confirm that the player really wants to resign
    const yes = window.confirm('Are you sure you want to resign?');
    if (!yes) {
      // if not, the return
      this.displayMessage( {type: 'INFO', text: 'Resignation cancelled.'} );
      return;
    }
    
    // if confirmed, then send the resignation command to the server
    AjaxUtils.callServer('/resignGame',
        // the handler method should be run in the context of 'this' Controller object
        handleResponse, this);

    // 
    function handleResponse(message) {
      if (message.type === 'INFO') {
        // tell the browser to route the player to the Home page
        window.location = '/';
      }
      // handle error message
      else {
        this.displayMessage(message);
      }
    }
  };

  /**
   * Exit the game by navigating to the Home page.
   */
  PlayController.prototype.exitGame = function exitGame() {
    window.location = '/';
  };

  /**
   * Exit the game by navigating to the Home page.
   */
  PlayController.prototype.refresh = function refresh() {
    let gameViewURL = '/game';
    const gameID = this._gameState.getGameID();
    if (LangUtils.exists(gameID)) {
      gameViewURL = `/game?gameID=${gameID}`;
    }
    window.location = gameViewURL;
  };

  PlayController.prototype.isTurnActive = function isTurnActive() {
    return this._turn.length > 0;
  };

  PlayController.prototype.clearTurnDuringSubmit = function clearTurnDuringSubmit() {
    this._turnTemp = this._turn;
    this._turn = [];
    return this._turnTemp;
  };

  PlayController.prototype.putTurnBackAfterFailedSubmit = function putTurnBackAfterFailedSubmit(turn) {
    this._turn = this._turnTemp;
    this._turnTemp = null;
  };

  PlayController.prototype.getPendingMove = function getPendingMove() {
    return this._pendingMove;
  };

  PlayController.prototype.enableAllMyPieces = function enableAllMyPieces() {
    return this._boardController.enableAllMyPieces();
  };

  PlayController.prototype.disableAllMyPieces = function disableAllMyPieces() {
    return this._boardController.disableAllMyPieces();
  };

  /**
   * Sets the move that was just requested by the player.
   * It is 'pending' until the server validates it.
   */
  PlayController.prototype.setPendingMove = function setPendingMove(pendingMove) {
    if (pendingMove === undefined || pendingMove === null) {
      throw new Error('pendingMove must not be null');
    }
    //
    this._pendingMove = pendingMove;
    // move the Piece
    var $piece = this._boardController.getPiece$(this._pendingMove.start);
    if ($piece === null) throw new Error('No Piece found at: ' + this._pendingMove.start);
    // start the 'pending' style animations
    this._boardController.setSpacePending(this._pendingMove.start);
    this._boardController.setSpacePending(this._pendingMove.end);
    // move the Piece on the board
    this._boardController.movePiece($piece, this._pendingMove);
  };
  
  /**
   * Remove pending move from consideration.
   * Thus putting the Piece back to the starting point.
   */
  PlayController.prototype.resetPendingMove = function resetPendingMove() {
    // clear the 'pending' styles
    this._boardController.resetSpacePending(this._pendingMove.start);
    this._boardController.resetSpacePending(this._pendingMove.end);
    // move the Piece back
    this.undoMove(this._pendingMove);
    // clear the state variable
    this._pendingMove = null;
  };
  
  /**
   * Add the server-validated pending move to the turn.
   */
  PlayController.prototype.addPendingMove = function addPendingMove() {
    // is this the first move?  if so then store the $activePiece
    if (this.$activePiece === null) {
      this.$activePiece = this._boardController.getPiece$(this._pendingMove.end);
      console.info('$activePiece', this.$activePiece);
    }
    // change the 'pending' to 'valid'
    this._boardController.setSpaceValidated(this._pendingMove.start);
    this._boardController.setSpaceValidated(this._pendingMove.end);
    // store the move on the turn list
    this._turn.push(this._pendingMove);
    // clear the state variable
    this._pendingMove = null;
  };

  /**
   * Remove the most recent move from the turn.
   * 
   * @return
   *    true if there are more moves remaining; otherwise, false
   */
  PlayController.prototype.popMove = function popMove() {
    if (!this.isTurnActive()) return false;
    //
    var move = this._turn.pop();
    this.undoMove(move);
    // clear 'valid' styles
    this._boardController.resetSpaceValidated(move.end);
    if (!this.isTurnActive()) {
      this._boardController.resetSpaceValidated(move.start);
      this.$activePiece = null;
    }
    //
    return !this.isTurnActive();
  };

  PlayController.prototype.enableActivePiece = function enableActivePiece() {
    // pre-conditions
    if (this.$activePiece === null) {
      throw new Error('No active Piece.');
    }
    //
    this._boardController.enablePiece(this.$activePiece);
  };

  PlayController.prototype.undoMove = function undoMove(move) {
    var $piece = this._boardController.getPiece$(move.end);
    if ($piece === null) {
      throw new Error('No Piece found at: ' + move.end);
    }
    this._boardController.movePiece($piece, move.reverse());
  };

  // export class constructor
  return PlayController;
  
});
