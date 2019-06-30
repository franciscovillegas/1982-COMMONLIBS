/*
 * File: app/store/js_Tree.js
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

Ext.define('MyApp.store.js_Tree', {
    extend: 'Ext.data.TreeStore',

    requires: [
        'MyApp.model.mdl_Tree',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'js_Tree',
            autoLoad: true,
            model: 'MyApp.model.mdl_Tree',
            proxy: {
                type: 'ajax',
                timeout: 300000,
                url: '../../../servlet/EjeCoreI?claseweb=cl.eje.view.sencha.Conf&modulo=eje_generico_gestionperfiles&accion=get&thing=TreePerfiles',
                reader: {
                    type: 'json',
                    rootProperty: 'children'
                }
            }
        }, cfg)]);
    }
});