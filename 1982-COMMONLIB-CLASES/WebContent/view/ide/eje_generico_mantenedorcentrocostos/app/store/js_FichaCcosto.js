/*
 * File: app/store/js_FichaCcosto.js
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

Ext.define('MyApp.store.js_FichaCcosto', {
    extend: 'Ext.data.Store',

    requires: [
        'MyApp.model.mdl_Fichaccosto',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            pageSize: 5000,
            storeId: 'js_FichaCcosto',
            model: 'MyApp.model.mdl_Fichaccosto',
            proxy: {
                type: 'ajax',
                url: '../../../servlet/EjeCoreI?claseweb=cl.eje.view.sencha.Conf&modulo=eje_generico_mantenedorcentrocostos&accion=get&thing=Ficha',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            },
            listeners: {
                load: {
                    fn: me.onJsonstoreLoad,
                    scope: me
                }
            }
        }, cfg)]);
    },

    onJsonstoreLoad: function(store, records, successful, eOpts) {
        var form = Ext.getCmp('frm_Mantenedorccosto');
        form.loadRecord(store.data.first());
    }

});