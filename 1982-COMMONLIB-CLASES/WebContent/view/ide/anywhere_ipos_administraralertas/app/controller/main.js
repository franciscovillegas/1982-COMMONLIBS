/*
 * File: app/controller/main.js
 *
 * This file was generated by Sencha Architect version 3.2.0.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 5.0.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 5.0.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('MyApp.controller.main', {
    extend: 'Ext.app.Controller',

    id: '#viewport_main',

    control: {
        "#viewport_main": {
            show: 'onMainShow'
        }
    },

    onMainShow: function(target) {
                console.log("[controllerInit onWindowShow]");

                var params = MyApp.app.getController("ctrTool").getParams();
                var defWindow = "login_window";

                if( params.w != null) {
                    defWindow = params.w;
                }

                console.log("toWindow:"+defWindow);
                MyApp.app.getController("ctrTool").showWindow(defWindow);

                Ext.getCmp("viewport_main").destroy();
    },

    loginNormal: function() {
                    console.log("LOGIN NORMAL");


                    var windowID = "MyViewport";
                    var id = "MyApp.view."+windowID;

                    if(Ext.get(windowID) == null) {
                        var win = Ext.create(id);

                        win.show();
                    }
                    else {
                        Ext.get(windowID).show();
                        Ext.get(windowID).focus();
                    }

                    MyApp.app.getController("ISM.Filtros").initViewport();

                    Ext.getCmp("login.loginfalso").setVisible(false);

    },

    loginAdministrador: function() {
                    console.log("LOGIN ADMINISTRADOR");

                    Ext.getCmp("login.loginfalso").setVisible(false);
                    var windowID = "MyViewport";
                    var id = "MyApp.view."+windowID;

                    if(Ext.get(windowID) == null) {
                        var win = Ext.create(id);

                        win.show();
                    }
                    else {
                        Ext.get(windowID).show();
                        Ext.get(windowID).focus();
                    }

                    MyApp.app.getController("ISM.Filtros").initViewport();
    },

    isInvitado: function() {
        var params = MyApp.app.getController("ctrTool").getParams();
        if("invitado" == params.user) {
            return true;
        }

        return false;
    }

});
