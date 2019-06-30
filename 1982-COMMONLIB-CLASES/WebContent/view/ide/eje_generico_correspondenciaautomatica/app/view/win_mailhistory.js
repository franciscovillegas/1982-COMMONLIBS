/*
 * File: app/view/win_mailhistory.js
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

Ext.define('MyApp.view.win_mailhistory', {
    extend: 'Ext.window.Window',
    alias: 'widget.win_mailhistory',

    requires: [
        'MyApp.view.win_mailhistoryViewModel',
        'Ext.form.Panel',
        'Ext.grid.Panel',
        'Ext.grid.column.Action',
        'Ext.view.Table',
        'Ext.toolbar.Paging',
        'Ext.button.Button'
    ],

    viewModel: {
        type: 'win_mailhistory'
    },
    height: 450,
    id: 'win_mailhistory',
    resizable: false,
    width: 614,
    layout: 'fit',
    title: 'Correos generados',
    modal: true,

    items: [
        {
            xtype: 'form',
            id: 'form_mailhistory',
            layout: 'fit',
            header: false,
            title: 'My Form',
            items: [
                {
                    xtype: 'gridpanel',
                    header: false,
                    title: 'My Grid Panel',
                    store: 'jsonMailGenerated',
                    columns: [
                        {
                            xtype: 'actioncolumn',
                            maxWidth: 25,
                            minWidth: 25,
                            width: 25,
                            menuDisabled: true,
                            items: [
                                {
                                    handler: function(view, rowIndex, colIndex, item, e, record, row) {
                                        var idCorrespondencia = record.get("id_correspondencia");
                                        console.log(idCorrespondencia);

                                        Ext.getCmp("dest_subject").setValue(record.get("asunto"));
                                        CKEDITOR.instances["dest_body"].setData(record.get("body"));

                                        Ext.getStore("destBCC").getProxy().setExtraParam("id_correspondencia", idCorrespondencia);
                                        Ext.getStore("destCC").getProxy().setExtraParam("id_correspondencia", idCorrespondencia);
                                        Ext.getStore("destTO").getProxy().setExtraParam("id_correspondencia", idCorrespondencia);


                                        /*UNA VEZ QUE CARGUE DEBE HACER LOS SIGUIENTE*/
                                        Ext.getStore("destBCC").on("load", function() {
                                            var strMail = "";
                                            Ext.getStore("destBCC").data.each(function(record) {
                                                if(strMail != "") {
                                                    strMail+=",";
                                                }
                                                strMail+=record.get("nombres");//+"<"+record.get("mail")+">";
                                            });
                                            Ext.getCmp("dest_bcc").setValue(strMail);
                                        }, this, {
                                            single : true
                                        });


                                        Ext.getStore("destCC").on("load", function() {
                                            var strMail = "";
                                            Ext.getStore("destCC").data.each(function(record) {
                                                if(strMail != "") {
                                                    strMail+=",";
                                                }
                                                strMail+=record.get("nombres");//+"<"+record.get("mail")+">";
                                            });
                                            Ext.getCmp("dest_cc").setValue(strMail);
                                        }, this, {
                                            single : true
                                        });

                                        Ext.getStore("destTO").on("load", function() {
                                            var strMail = "";
                                            Ext.getStore("destTO").data.each(function(record) {
                                                if(strMail != "") {
                                                    strMail+=",";
                                                }
                                                strMail+=record.get("nombres");//+"<"+record.get("mail")+">";
                                            });
                                            Ext.getCmp("dest_to").setValue(strMail);
                                        }, this, {
                                            single : true
                                        });


                                        /*CARGA*/
                                        Ext.getStore("destBCC").load();
                                        Ext.getStore("destCC").load();
                                        Ext.getStore("destTO").load();


                                        /*LOAD PROGRAMACIONES*/
                                        Ext.getStore("programaciones").getProxy().setExtraParam("id_correspondencia",idCorrespondencia);
                                        Ext.getStore("programaciones").load();

                                        /*ESTABLECE EL ID_CORRESPONDENCIA*/
                                        Ext.getCmp("hidden_idcorrespondencia").setValue(idCorrespondencia);




                                    },
                                    icon: '../../images/btns/eye.ico'
                                }
                            ]
                        },
                        {
                            xtype: 'gridcolumn',
                            hidden: true,
                            maxWidth: 100,
                            minWidth: 100,
                            width: 100,
                            dataIndex: 'fec_create',
                            text: 'Creación'
                        },
                        {
                            xtype: 'gridcolumn',
                            maxWidth: 70,
                            minWidth: 70,
                            width: 70,
                            dataIndex: 'id_correspondencia',
                            text: 'ID'
                        },
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'asunto',
                            text: 'Asunto',
                            flex: 1
                        },
                        {
                            xtype: 'gridcolumn',
                            hidden: true,
                            dataIndex: 'body',
                            text: 'Body'
                        },
                        {
                            xtype: 'gridcolumn',
                            hidden: true,
                            dataIndex: 'dest_to',
                            text: 'Dest To'
                        },
                        {
                            xtype: 'gridcolumn',
                            hidden: true,
                            dataIndex: 'dest_cc',
                            text: 'Dest Cc'
                        },
                        {
                            xtype: 'gridcolumn',
                            hidden: true,
                            dataIndex: 'dest_bcc',
                            text: 'Dest Bcc'
                        },
                        {
                            xtype: 'gridcolumn',
                            hidden: true,
                            dataIndex: 'fec_sended',
                            text: 'Fec Sended'
                        },
                        {
                            xtype: 'gridcolumn',
                            hidden: true,
                            dataIndex: 'fec_execute',
                            text: 'Fec Execute'
                        },
                        {
                            xtype: 'gridcolumn',
                            hidden: true,
                            dataIndex: 'id_timer',
                            text: 'Id Timer'
                        },
                        {
                            xtype: 'actioncolumn',
                            maxWidth: 25,
                            minWidth: 25,
                            width: 25,
                            menuDisabled: true,
                            items: [
                                {
                                    handler: function(view, rowIndex, colIndex, item, e, record, row) {
                                        Ext.MessageBox.confirm('Eliminar', '¿Está seguro que desea eliminar esta Plantilla? <br/> Esto eliminará todas las programaciones de dicha plantilla.', function(btn){
                                            if(btn === 'yes'){
                                                var form = Ext.getCmp("form_mailhistory").getForm();
                                                form.submit({
                                                    url: '../../../EjeS?modulo=eje_generico_correspondenciaautomatica&thing=MailGenerated&accion=del',
                                                    params : {
                                                        id_correspondencia : record.get("id_correspondencia")
                                                    },
                                                    waitMsg: 'Eliminando...',
                                                    method: 'POST',
                                                    success: function (form, action) {
                                                        console.log(action.result);

                                                        Ext.getStore("jsonMailGenerated").remove(record);

                                                        //Ext.getStore("jsonMailGenerated").load();
                                                    },
                                                    failure: function(form, action) {
                                                        Ext.Msg.alert('Error', "Ha ocurrido un error desconocido, por favor inténtalo nuevamente.");
                                                    }

                                                });
                                            }
                                        });
                                    },
                                    icon: '../../images/btns/cross.ico'
                                }
                            ]
                        }
                    ],
                    dockedItems: [
                        {
                            xtype: 'pagingtoolbar',
                            dock: 'bottom',
                            width: 360,
                            displayInfo: true,
                            store: 'jsonMailGenerated'
                        }
                    ]
                }
            ]
        }
    ],
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
                    xtype: 'button',
                    handler: function(button, e) {
                        Ext.getCmp("win_mailhistory").destroy();

                    },
                    icon: '../../images/btns/tick.ico',
                    text: 'Cerrar'
                }
            ]
        },
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    xtype: 'button',
                    handler: function(button, e) {
                        Ext.getStore("jsonMailGenerated").load();

                    },
                    icon: '../../images/btns/arrow_refresh.ico',
                    text: ''
                }
            ]
        }
    ]

});