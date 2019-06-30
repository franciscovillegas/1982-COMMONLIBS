/*
 * File: app/store/jsGetColaborador.js
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

Ext.define('MyApp.store.jsGetColaborador', {
    extend: 'Ext.data.Store',

    requires: [
        'MyApp.model.mdlColaborador',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            pageSize: 10000,
            storeId: 'jsGetColaborador',
            autoLoad: true,
            model: 'MyApp.model.mdlColaborador',
            proxy: {
                type: 'ajax',
                url: '../../../servlet/EjeCoreI?claseweb=cl.eje.view.sencha.Conf&modulo=eje_generico_trackingintranet&accion=get&thing=Colaborador',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            }
        }, cfg)]);
    }
});