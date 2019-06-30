/*
 * File: app/store/values.js
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

Ext.define('MyApp.store.values', {
    extend: 'Ext.data.Store',

    requires: [
        'MyApp.model.values',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            pageSize: 50000,
            remoteSort: true,
            storeId: 'values',
            model: 'MyApp.model.values',
            proxy: {
                type: 'ajax',
                url: '../../../EjeI?modulo=eje_generico_administradorparametros&thing=Valores&accion=get',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            }
        }, cfg)]);
    }
});