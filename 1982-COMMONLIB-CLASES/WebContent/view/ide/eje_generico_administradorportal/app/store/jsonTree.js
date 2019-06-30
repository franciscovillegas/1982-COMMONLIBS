/*
 * File: app/store/jsonTree.js
 *
 * This file was generated by Sencha Architect
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

Ext.define('MyApp.store.jsonTree', {
    extend: 'Ext.data.TreeStore',

    requires: [
        'MyApp.model.jsonPrestaciones',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'jsonTree',
            model: 'MyApp.model.jsonPrestaciones',
            proxy: {
                type: 'ajax',
                url: '../../../EjeCore?claseweb=cl.eje.view.sencha.ConfSegura&modulo=eje_generico_administradorportal&thing=TreePrestaciones&accion=get',
                reader: {
                    type: 'json'
                }
            },
            listeners: {
                beforeload: {
                    fn: me.onTreeStoreBeforeLoad,
                    scope: me
                }
            }
        }, cfg)]);
    },

    onTreeStoreBeforeLoad: function(store, operation, eOpts) {
        if(store.isLoading()) {
            return false;
        }

    }

});