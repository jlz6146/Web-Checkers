/**
 * This module exports a map of functions for simple language Utilities.
 */
define(function(){
    'use strict';

    return {

      /**
       * Tests whether the 'thing' passed in exists.
       *
       * @param thing  the thing to be tested
       *
       * @return true, if the thing exists
       */
      exists: function (thing) {
        return thing !== undefined && thing !== null;
      }
    };
});
