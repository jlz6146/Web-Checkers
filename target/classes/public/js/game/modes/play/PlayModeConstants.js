/**
 * This module exports a map of constants used in the PLAY mode.
 */
define(function () {
  'use strict';

  /**
   * This module is a map of constant symbols to their names.
   * Used in methods to change GameView Play mode states.
   */
  return {

    //
    // States
    //

    STARTING_PLAY_MODE: 'Starting Play Mode'
    // "Playing My Turn" composite states
    , EMPTY_TURN: 'Empty Turn'
    , WAITING_FOR_MOVE_VALIDATION: 'Waiting for Move Validation'
    , STABLE_TURN: 'Stable Turn'
    , WAITING_FOR_TURN_VALIDATION: 'Waiting for Turn Validation'
    , WAITING_FOR_BACKUP_VALIDATION: 'Waiting for Backup Validation'
    // "Waiting for My Turn" composite states
    , WAITING_TO_CHECK_MY_TURN: 'Waiting for My Turn'
    , CHECKING_MY_TURN: 'Checking for My Turn on the Server'
    // The "Game Over" singular state
    , GAME_OVER: 'Showing Game Over'

    //
    // Buttons
    //

    , BACKUP_BUTTON_ID: 'backupBtn'
    , BACKUP_BUTTON_TOOLTIP: 'Remove the last move with your current turn.'
    , SUBMIT_BUTTON_ID: 'submitBtn'
    , SUBMIT_BUTTON_TOOLTIP: 'Commit your current turn to the server.'
    , RESIGN_BUTTON_ID: 'resignBtn'
    , RESIGN_BUTTON_TOOLTIP: 'Resign from the game.'
    , EXIT_BUTTON_ID: 'exitBtn'
    , EXIT_BUTTON_TOOLTIP: 'Click here to exit the game and go to the Home page.'

  };
});
