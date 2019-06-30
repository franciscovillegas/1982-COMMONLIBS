/*
 * File: app/view/window_personasperfil.js
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

Ext.define('MyApp.view.window_personasperfil', {
    extend: 'Ext.window.Window',
    alias: 'widget.window_personasperfil',

    requires: [
        'MyApp.view.window_personasperfilViewModel',
        'Ext.grid.Panel',
        'Ext.view.Table',
        'Ext.grid.column.Column',
        'Ext.button.Button',
        'Ext.toolbar.Paging'
    ],

    viewModel: {
        type: 'window_personasperfil'
    },
    constrain: true,
    height: 427,
    id: 'window_personasperfil',
    width: 459,
    layout: 'fit',
    title: 'Personas en Perfil',
    modal: true,
    defaultListenerScope: true,

    items: [
        {
            xtype: 'gridpanel',
            header: false,
            title: 'My Grid Panel',
            store: 'js_colsInPerfil',
            columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'rut',
                    text: 'Rut'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'nombre',
                    text: 'Nombre',
                    flex: 1
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
                        Ext.getCmp("window_personasperfil").destroy();

                    },
                    icon: '../../images/btns/tick.ico',
                    text: 'Cerrar'
                }
            ]
        },
        {
            xtype: 'pagingtoolbar',
            dock: 'bottom',
            width: 360,
            displayInfo: true,
            store: 'js_colsInPerfil'
        }
    ],
    listeners: {
        show: 'onWindow_personasperfilShow'
    },

    onWindow_personasperfilShow: function(component, eOpts) {
                console.log("Show");
                var proxy = Ext.getStore("js_colsInPerfil").getProxy();
                proxy.setExtraParam("id_perfil", component.extraParams.id_perfil);

                Ext.getStore("js_colsInPerfil").load();

    }

});