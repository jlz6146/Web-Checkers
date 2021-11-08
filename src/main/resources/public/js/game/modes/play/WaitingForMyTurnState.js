/**
 * This module exports the WaitingForMyTurnState class constructor.
 *
 * This component is an concrete implementation of a state
 * for the Game view; this state represents the state in which
 * the view is waiting between calls to the server to check whether
 * it's the current player's turn.
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
    function WaitingForMyTurnState(controller) {
        // private attributes
        this._controller = controller;
    };

    //
    // Public (external) methods
    //

    /**
     * Get the name of this state.
     */
    WaitingForMyTurnState.prototype.getName = function getName() {
        return PlayModeConstants.WAITING_TO_CHECK_MY_TURN;
    }

    /**
     * Method when entering this state.
     */
    WaitingForMyTurnState.prototype.onEntry = function onEntry() {
        this._controller.enableButton(PlayModeConstants.RESIGN_BUTTON_ID);
        // wait five seconds then check my turn
        setTimeout(() => { this._controller.setState(PlayModeConstants.CHECKING_MY_TURN); }, 5000);
    }

    // export class constructor
    return WaitingForMyTurnState;

});
