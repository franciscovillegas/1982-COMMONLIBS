/*
 * File: app/store/jsonHaberesClasificacion.js
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

Ext.define('MyApp.store.jsonHaberesClasificacion', {
    extend: 'Ext.data.Store',

    requires: [
        'MyApp.model.modelHaberesClasificacion',
        'Ext.data.proxy.Ajax',
        'Ext.data.reader.Json'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            pageSize: 500,
            storeId: 'jsonHaberesClasificacion',
            model: 'MyApp.model.modelHaberesClasificacion',
            proxy: {
                type: 'ajax',
                timeout: 300000,
                url: '../../../servlet/EjeCore?claseweb=cl.eje.view.sencha.ConfSegura&modulo=eje_generico_cargadormaestros&thing=RelacionHaberesMotor&accion=get',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            }
        }, cfg)]);
    }
});