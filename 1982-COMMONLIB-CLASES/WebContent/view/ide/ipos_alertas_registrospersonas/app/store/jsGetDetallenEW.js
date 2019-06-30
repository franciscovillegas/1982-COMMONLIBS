/*
 * File: app/store/jsGetDetallenEW.js
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

Ext.define('MyApp.store.jsGetDetallenEW', {
    extend: 'Ext.data.Store',

    singleton: true,
    requires: [
        'MyApp.model.modeloDetalle',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            groupField: 'titulo',
            pageSize: 1000,
            storeId: 'jsGetDetallenEW',
            model: 'MyApp.model.modeloDetalle',
            proxy: {
                type: 'ajax',
                url: '../../../EjeCoreI?claseweb=cl.imasd.view.sencha.anywhere.Conf&modulo=anywhere_ipos_administraralertas&accion=get&thing=DetalleRegistros',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            }
        }, cfg)]);
    }
});