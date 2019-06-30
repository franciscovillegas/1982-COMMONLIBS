/*
 * File: app/view/modal_typeParam.js
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

Ext.define('MyApp.view.modal_typeParam', {
    extend: 'Ext.window.Window',
    alias: 'widget.modal_typeParam',

    requires: [
        'MyApp.view.modal_typeParamViewModel',
        'Ext.toolbar.Toolbar',
        'Ext.form.field.Text',
        'Ext.button.Button',
        'Ext.form.Panel',
        'Ext.grid.Panel',
        'Ext.view.Table',
        'Ext.selection.CheckboxModel',
        'Ext.grid.column.Action'
    ],

    viewModel: {
        type: 'modal_typeparam'
    },
    height: 270,
    id: 'modal_typeParam',
    resizable: false,
    width: 237,
    layout: 'fit',
    title: 'Tipos de Parámetro',
    modal: true,
    defaultListenerScope: true,

    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'bottom',
            layout: {
                type: 'hbox',
                pack: 'end'
            },
            items: [
                {
                    xtype: 'textfield',
                    hidden: true,
                    id: 'id_type',
                    fieldLabel: 'Label',
                    name: 'id_type'
                },
                {
                    xtype: 'button',
                    handler: function(button, e) {
                        var id_corr_value = Ext.getCmp("id_corr_value").getValue();
                        var id_type = Ext.getCmp("id_type").getValue();

                        if(id_type==null || id_type == "") {
                            Ext.Msg.alert("Error","Debe seleccionar el nuevo tipo.");
                        }
                        else {
                            var f = Ext.getCmp("modal_typeParam_form").getForm();

                            f.submit({
                                url : "../../../EjeI?modulo=eje_generico_administradorparametros&thing=ParamTypeAsignacion&accion=upd",
                                params: {
                                    id_corr_value: id_corr_value,
                                    id_type : id_type
                                },
                                waitMsg: "Esperando respuesta del servidor...",
                                success: function(form, action) {
                                    var resp = action.result;

                                    MyApp.app.getController("ctrTool").jsonManejaError(resp, function() {
                                        Ext.getStore("values").load();
                                        Ext.getCmp("modal_typeParam").destroy();
                                    });
                                },
                                failure: function(form, action) {
                                    var resp = action.result;
                                    MyApp.app.getController("ctrTool").jsonManejaError(resp);
                                }
                            });
                        }


                    },
                    icon: '../../images/btns/tick.ico',
                    text: 'Aceptar'
                },
                {
                    xtype: 'button',
                    handler: function(button, e) {
                        Ext.getCmp("modal_typeParam").destroy();

                    },
                    icon: '../../images/btns/cross.ico',
                    text: 'Cancelar'
                }
            ]
        }
    ],
    listeners: {
        show: 'onModal_typeParamShow'
    },
    items: [
        {
            xtype: 'form',
            id: 'modal_typeParam_form',
            layout: 'fit',
            header: false,
            title: 'My Form',
            items: [
                {
                    xtype: 'gridpanel',
                    header: false,
                    title: 'My Grid Panel',
                    store: 'ParamType',
                    columns: [
                        {
                            xtype: 'gridcolumn',
                            hidden: true,
                            dataIndex: 'id_type',
                            text: 'Id Type'
                        },
                        {
                            xtype: 'gridcolumn',
                            hidden: true,
                            dataIndex: 'icon',
                            text: 'Icon'
                        },
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'nemo',
                            menuDisabled: true,
                            text: 'Nemo',
                            flex: 1
                        },
                        {
                            xtype: 'gridcolumn',
                            hidden: true,
                            dataIndex: 'selected',
                            text: 'Selected'
                        },
                        {
                            xtype: 'actioncolumn',
                            renderer: function(value, metaData, record, rowIndex, colIndex, store, view) {

                                this.items[0].icon = record.get("icon");


                            },
                            width: 25,
                            menuDisabled: true,
                            items: [
                                {

                                }
                            ]
                        }
                    ],
                    selModel: {
                        selType: 'checkboxmodel',
                        mode: 'SINGLE'
                    },
                    listeners: {
                        cellclick: 'onGridpanelCellClick'
                    }
                },
                {
                    xtype: 'textfield',
                    hidden: true,
                    id: 'id_corr_value',
                    fieldLabel: 'Label',
                    name: 'id_corr_value'
                }
            ]
        }
    ],

    onModal_typeParamShow: function(component, eOpts) {

                var e = Ext.getStore("ParamType");
                e.getProxy().setExtraParam("id_corr_value",component.extraParams.id_corr_value);

                e.load();

                Ext.getCmp("id_corr_value").setValue(component.extraParams.id_corr_value);

    },

    onGridpanelCellClick: function(tableview, td, cellIndex, record, tr, rowIndex, e, eOpts) {
                console.log(record.data);
                Ext.getCmp("id_type").setValue(record.get("id_type"));

    }

});