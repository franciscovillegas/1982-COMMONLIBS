/*
 * File: app/view/winUpload.js
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

Ext.define('MyApp.view.winUpload', {
    extend: 'Ext.window.Window',
    alias: 'widget.winUpload',

    requires: [
        'MyApp.view.winUploadViewModel',
        'Ext.form.Panel',
        'Ext.form.field.ComboBox',
        'Ext.button.Button',
        'Ext.toolbar.Toolbar'
    ],

    viewModel: {
        type: 'winupload'
    },
    constrain: true,
    height: 309,
    id: 'winUpload',
    width: 438,
    layout: 'fit',
    title: 'Cargar',
    defaultListenerScope: true,

    items: [
        {
            xtype: 'form',
            id: 'panel_submit_upload',
            layout: 'absolute',
            header: false,
            title: 'My Panel',
            items: [
                {
                    xtype: 'combobox',
                    margin: '10 20 10 20',
                    width: 348,
                    fieldLabel: 'Periodo',
                    name: 'periodo',
                    allowBlank: false,
                    displayField: 'peri_id',
                    store: 'jsonPeriodo',
                    valueField: 'peri_id'
                },
                {
                    xtype: 'combobox',
                    x: 0,
                    y: 30,
                    margin: '10 20 10 20',
                    width: 350,
                    fieldLabel: 'Motor',
                    name: 'motor',
                    allowBlank: false,
                    displayField: 'motor',
                    store: 'jsonMotor',
                    valueField: 'id_motor',
                    listeners: {
                        change: 'onComboboxChange'
                    }
                },
                {
                    xtype: 'button',
                    handler: function(button, e) {
                        MyApp.app.getController("ctrTool").fnShowWindow("winPeriodo");

                    },
                    x: 370,
                    y: 10,
                    id: 'btnCreateNewPeriodo',
                    icon: '../../images/btns/outlook_new_items.ico',
                    text: ''
                },
                {
                    xtype: 'container',
                    x: -5,
                    y: 70,
                    height: 140,
                    id: 'filesContainer',
                    layout: 'fit'
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    x: 110,
                    y: 254,
                    dock: 'bottom',
                    layout: {
                        type: 'hbox',
                        pack: 'end'
                    },
                    items: [
                        {
                            xtype: 'button',
                            handler: function(button, e) {


                                var form = Ext.getCmp("panel_submit_upload").getForm();

                                if (form.isValid()) {
                                    form.submit({
                                        timeout: 3600,
                                        waitMsg: 'Cargando...',
                                        url: '../../../EjeCore?claseweb=cl.eje.view.sencha.ConfSegura&modulo=eje_generico_cargadormaestros&thing=Upload&accion=add' ,
                                        success: function (form, action) {
                                            var json = action.result;

                                            Ext.getStore("jsonInstancia").load();
                                            Ext.Msg.alert('Success', "Archivo cargado con exito.<br/>"+json.data[0].msg);
                                        },
                                        failure: function (form, action) {
                                            var json = action.result;

                                            Ext.Msg.alert('Error', json.message, function() {
                                                Ext.getStore("jsonInstancia").load();
                                            });
                                        }
                                    });
                                }
                                else {
                                    Ext.Msg.alert('Error', 'Debe llenar todos los campos marcados en rojo.');
                                }

                            },
                            icon: '../../images/btns/upgrade_downgrade_account.ico',
                            text: 'Cargar'
                        },
                        {
                            xtype: 'button',
                            handler: function(button, e) {
                                try {
                                    Ext.getCmp("winUpload").destroy();
                                }
                                catch(e) {

                                }

                            },
                            icon: '../../images/btns/cross.png',
                            text: 'Cancelar'
                        }
                    ]
                }
            ]
        }
    ],

    onComboboxChange: function(field, newValue, oldValue, eOpts) {
                if("1" == newValue) {
                    MyApp.app.getController("ctrTool").fnShowWindow("panelUpload_2","filesContainer");

                }


    }

});