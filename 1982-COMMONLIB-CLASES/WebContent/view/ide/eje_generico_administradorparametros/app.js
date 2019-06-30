/*
 * File: app.js
 *
 * This file was generated by Sencha Architect version 3.2.0.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 5.1.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 5.1.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

// @require @packageOverrides
Ext.Loader.setConfig({

});


Ext.application({
    models: [
        'modulos',
        'params',
        'values',
        'Cliente',
        'ParamType',
        'CoreParam',
        'mdl_checkbd'
    ],
    stores: [
        'Modulos',
        'params',
        'values',
        'Cliente',
        'ParamType',
        'CoreParam',
        'store_checkbd'
    ],
    views: [
        'MyViewport',
        'modal_cliente',
        'modal_modulo',
        'modal_typeParam',
        'modal_imgUpload',
        'modal_confLocal',
        'wind_checkbd'
    ],
    controllers: [
        'tool',
        'ctrTool'
    ],
    name: 'MyApp',

    launch: function() {
        Ext.create('MyApp.view.MyViewport');
        Ext.Ajax.setDefaultHeaders({
            'Accept':'application/json'
        });


        Ext.data.StoreManager.each(function(k,v) {

            Ext.apply(k.on("load", function(store, operation, success, response) {
                console.log("loading StoreID:"+k.getStoreId());

                try {
                    var  json = Ext.JSON.decode(response._response.responseText);

                    if(!json.success) {
                        if(!json.usuario_is_session_valid) {
                            Ext.Msg.alert("Error", json.message, function() {
                                window.location.href = json.context_path;
                            });
                        }
                    }
                }
                catch(e) {
                    console.log(e);
                }

            }));

        });



    }

});