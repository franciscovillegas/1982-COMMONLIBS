/*
 * File: app/store/jsonGroupUnidades.js
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

Ext.define('MyApp.store.jsonGroupUnidades', {
    extend: 'Ext.data.TreeStore',

    requires: [
        'MyApp.model.groupOrganica_model',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'jsonGroupUnidades',
            model: 'MyApp.model.groupOrganica_model',
            proxy: {
                type: 'ajax',
                timeout: 3000000,
                url: '../../../EjeCore?claseweb=cl.eje.view.sencha.ConfSegura&modulo=eje_generico_correspondenciaautomatica&thing=Organica&accion=get&treeType=check',
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
                    console.log("Yep, is loading. So, im going to stop this.");
                    return false;
                }
    }

});