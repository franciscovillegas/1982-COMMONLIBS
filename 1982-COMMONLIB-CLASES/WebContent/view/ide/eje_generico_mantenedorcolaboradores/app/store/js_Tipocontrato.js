/*
 * File: app/store/js_Tipocontrato.js
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

Ext.define('MyApp.store.js_Tipocontrato', {
    extend: 'Ext.data.Store',

    requires: [
        'MyApp.model.mdl_Tipocontrato',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            pageSize: 5000,
            storeId: 'js_Tipocontrato',
            autoLoad: true,
            model: 'MyApp.model.mdl_Tipocontrato',
            proxy: {
                type: 'ajax',
                url: '../../../servlet/EjeCoreI?claseweb=cl.eje.view.sencha.Conf&modulo=eje_generico_mantenedorcolaboradores&accion=get&thing=Tipocontrato',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            }
        }, cfg)]);
    }
});