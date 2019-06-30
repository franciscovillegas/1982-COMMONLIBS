/*
 * File: app/store/jsonInstancia.js
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

Ext.define('MyApp.store.jsonInstancia', {
    extend: 'Ext.data.Store',

    requires: [
        'MyApp.model.modelInstancia',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            pageSize: 1000,
            storeId: 'jsonInstancia',
            model: 'MyApp.model.modelInstancia',
            proxy: {
                type: 'ajax',
                url: '../../../servlet/EjeCore?claseweb=cl.eje.view.sencha.ConfSegura&modulo=eje_generico_cargadormaestros&thing=Upload&accion=get',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            }
        }, cfg)]);
    }
});