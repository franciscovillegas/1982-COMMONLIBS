/*
 * File: app/controller/ctrMain.js
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

Ext.define('MyApp.controller.ctrMain', {
    extend: 'Ext.app.Controller',

    control: {
        "#cmb_Periodo": {
            change: 'onComboboxChange_Cmb_Periodo'
        },
        "#btn_Exportar": {
            click: 'onButtonClick_Bnt_Exportar'
        },
        "#btn_Refresh": {
            click: 'onButtonClick_Btn_Refresh'
        },
        "#btn_Filtro": {
            click: 'onButtonClick_Btn_Filtro'
        }
    },

    onComboboxChange_Cmb_Periodo: function(field, newValue, oldValue, eOpts) {
        var periodo = newValue;
        Ext.getStore("jsGetColaborador").load( { params: { "periodo":periodo } } );
    },

    onButtonClick_Bnt_Exportar: function(button, e, eOpts) {
        var periodo = Ext.getCmp("cmb_Periodo").getValue();
        window.location.href="../../../servlet/EjeCoreI?claseweb=cl.eje.view.sencha.Conf&modulo=eje_generico_reporteingresos&accion=export&thing=Colaborador&periodo="+periodo;
    },

    onButtonClick_Btn_Refresh: function(button, e, eOpts) {
        Ext.getStore("jsGetColaborador").load();
        Ext.getCmp("txt_Filtro").setValue(null);
    },

    onButtonClick_Btn_Filtro: function(button, e, eOpts) {
        var filtro = Ext.getCmp("txt_Filtro").getValue();
        Ext.getStore("jsGetColaborador").load( { params: {"filtro":filtro} } );
    }

});