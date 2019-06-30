/*
 * File: app/store/ISM/PorPersona/dsPersonas.js
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

Ext.define('MyApp.store.ISM.PorPersona.dsPersonas', {
    extend: 'Ext.data.Store',

    requires: [
        'MyApp.model.ISM.mdlPersona',
        'Ext.data.proxy.Direct',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            pageSize: 50000,
            storeId: 'ISM.PorPersona.dsPersonas',
            model: 'MyApp.model.ISM.mdlPersona',
            proxy: {
                type: 'direct',
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});