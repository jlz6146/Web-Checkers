/**
 * This module exports a map of functions used in processing Ajax responses.
 */
define(function (require) {
  'use strict';

  // imports
  const LangUtils = require('./LangUtils');

  /**
   * Utility object with methods for invoking Ajax calls to the server.
   */
  const AjaxUtils = {

    /**
     * The state of the Game View.
     */
    _gameState: null,

    /**
     * Set the state of the Game View.  This should only be called once after
     * the GameView DOM has been fully loaded.  See main.js
     *
     * @param gameState  the state of the Game View supplied by main.js
     */
    setGameState: function(gameState) {
      AjaxUtils._gameState = gameState;
    },

    /**
     * Get the ID of the game being viewed.
     *
     * @return {string} the ID of the game
     */
    getGameID: function() {
      return AjaxUtils._gameState.getGameID();
    },

    /**
     * Make an Ajax call to the server.
     *
     * <p>
     *   This always uses an HTTP POST action and I would prefer to use the
     *   jQuery post function that provides a very nice Promise API, but in
     *   order to detect when student's code incorrectly sends an HTTP 302 (redirect)
     *   response instead of a JSON response I had to use the raw 'ajax' function
     *   and pass-in a "success callback" that captures the raw XHR object.
     *
     * @param actionURL  the URL for a server Ajax action
     * @param callback  the developer's handler for the successful response (expecting JSON)
     * @param callbackContext  the object context within which the callback is executed (the 'this' object)
     */
    callServer: function (actionURL, callback, callbackContext) {
      // send the HTTP request and immediately return; the callback will be invoked asynchronously
      jQuery.ajax(makeAjaxOptionsWithNoData(actionURL, callback, callbackContext));
    },

    /**
     * Make an Ajax call to the server that supplies an action data object.
     *
     * <p>
     *   This always uses an HTTP POST action and I would prefer to use the
     *   jQuery post function that provides a very nice Promise API, but in
     *   order to detect when student's code incorrectly sends an HTTP 302 (redirect)
     *   response instead of a JSON response I had to use the raw 'ajax' function
     *   and pass-in a "success callback" that captures the raw XHR object.
     *
     * @param actionURL  the URL for a server Ajax action
     * @param actionData  the data (as an object or a raw string) to send to the server in the body of the POST request
     * @param callback  the developer's handler for the successful response (expecting JSON)
     * @param callbackContext  the object context within which the callback is executed (the 'this' object)
     */
    callServerWithData: function (actionURL, actionData, callback, callbackContext) {
      // send the HTTP request and immediately return; the callback will be invoked asynchronously
      jQuery.ajax(makeAjaxOptionsWithActionData(actionURL, actionData, callback, callbackContext));
    }
  };

  //
  // Private functions
  //

  /**
   * Parse the HTTP response text into a JSON object.
   *
   * @param {string} data  the HTTP response text
   * @return {object}  the JavaScript object represented in the response text
   * @throws {Error}  when the response text is XML/HTML
   */
  function myDataFilter(data) {
    if (data[0] === '<') {
      throw new Error('HTML content detected in Ajax reponse.'
          + '  Did you accidentally perform a redirect in your Spark Route?'
          + '  Sorry, that\'s a no-no.');
    } else {
      return JSON.parse(data);
    }
  }

  /**
   * This function is a generic Ajax error handler.
   * It logs an error to the console and pops-up an alert.
   */
  function handleErrorResponse(xhr, textStatus, error) {
    // error handling
    if (xhr.status === 404) {
      throw new Error('This Ajax call has not been implemented.');
    } else if (xhr.status === 500) {
      throw new Error('This Ajax call threw an exception: ' + error);
    } else if (xhr.status === 0) {
      throw new Error('The WebCheckers server is down.');
    } else {
      throw new Error(`Unknown error (status=${xhr.status}) error: '${error}'`);
    }
  }

  /**
   * Make the Ajax options for an action that requires <em>no</em> additional data.
   *
   * @param actionURL  the URL of the Ajax action
   * @param callback  the developer's handler for the successful response (expecting JSON)
   * @param callbackContext  the object context within which the callback is executed (the 'this' object)
   *
   * @return the options object for an Ajax call in jQuery
   */
  function makeAjaxOptionsWithNoData(actionURL, callback, callbackContext) {
    return _makeAjaxOptions(actionURL, ajaxParams(), callback, callbackContext);
  }

  /**
   * Make the Ajax options for an action that requires additional data.
   *
   * @param actionURL  the URL of the Ajax action
   * @param actionData  the data to be passed in the Ajax call
   * @param callback  the developer's handler for the successful response (expecting JSON)
   * @param callbackContext  the object context within which the callback is executed (the 'this' object)
   *
   * @return the options object for an Ajax call in jQuery
   */
  function makeAjaxOptionsWithActionData(actionURL, actionData, callback, callbackContext) {
    return _makeAjaxOptions(actionURL, ajaxParams(actionData), callback, callbackContext);
  }

  /**
   * Build the parameters for an Ajax call.  It may contain a gameID if available
   * and an actionData JSON object if available.
   *
   * @param actionData  an option data object to be passed on the Ajax call
   *
   * @return  an HTTP request parameters object
   */
  function ajaxParams(actionData) {
    const params = { };
    // add gameID if available
    const gameID = AjaxUtils.getGameID();
    if (LangUtils.exists(gameID)) {
      params.gameID = gameID;
    }
    // add action data if available
    if (LangUtils.exists(actionData)) {
      params.actionData = convertActionData(actionData);
    }
    // combine the gameID param with an option actionData param
    return params;
  }

  /**
   * Converts an object into JSON format; or just a raw string.
   *
   * @param actionData {object|string}  an object or a string
   *
   * @return {string}  the JSON representation of the object or the string
   */
  function convertActionData(actionData) {
    return (typeof actionData === 'object') ? JSON.stringify(actionData) : actionData;
  }

  /**
   * Make an Ajax call to the server.
   *
   * @param actionURL  the URL for a server Ajax action
   * @param parameters  the query parameters sent to the server in the body of the POST request
   * @param callback  the developer's handler for the successful response (expecting JSON)
   * @param callbackContext  the object context within which the callback is executed (the 'this' object)
   *
   * @return the options object for an Ajax call in jQuery
   */
  function _makeAjaxOptions(actionURL, parameters, callback, callbackContext) {
    return {

      // HTTP action
      method: 'POST',
      url: actionURL,

      // action data (as JSON)
      contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
      data: parameters,

      // HTTP callback handlers
      beforeSend: function() {
        console.debug(`POST ${actionURL} being sent.`);
      },
      dataFilter: myDataFilter,
      success: callback.bind(callbackContext),
      error: handleErrorResponse,
      complete: function (xhr, textStatus) {
        // log Ajax call
        console.debug(`POST ${actionURL} response complete with '${textStatus}' status.`);
      }
    };
  }

  return AjaxUtils;
});
