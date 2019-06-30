/*
 * File: app/store/ISM/PorPersona/tsLocales.js
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

Ext.define('MyApp.store.ISM.PorPersona.tsLocales', {
    extend: 'Ext.data.TreeStore',

    requires: [
        'MyApp.model.ISM.mdlTreeLocal',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            pageSize: 5000,
            storeId: 'ISM.PorPersona.tsLocales',
            model: 'MyApp.model.ISM.mdlTreeLocal',
            proxy: {
                type: 'ajax',
                url: '../../../EjeCoreI?claseweb=cl.imasd.view.sencha.anywhere.Conf&modulo=anywhere_ipos_administraralertas&accion=get&thing=Local',
                reader: {
                    type: 'json',
                    rootProperty: ''
                }
            }
        }, cfg)]);
    }
});