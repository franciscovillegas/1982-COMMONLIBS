/*
 * File: ctrMessage.js
 *
 * This file was generated by PeopleManager
 * 
 */

Ext.define('MyApp.controller.ctrMessage', {
    extend: 'Ext.app.Controller',

    msg: function(message) {
        /*
            var ex = {functionName:'Funcion_a_Ejecutar', params:[1,2,3,4..]}
            parent.ctrMessage.msg(ex);
        */
        if (message !== null) {
            if (message.functionName !== null) {
                if (message.params !== null) {
                    var array = message.params;
                    var f = eval(message.functionName);
                    f(array);
                }else {
                    var f = eval(message.functionName);
                    f();
                }
            }
        }
    }

});