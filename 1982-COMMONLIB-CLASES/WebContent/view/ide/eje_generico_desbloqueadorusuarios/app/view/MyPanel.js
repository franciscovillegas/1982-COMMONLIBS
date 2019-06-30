/*
 * File: app/view/MyPanel.js
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

Ext.define('MyApp.view.MyPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.mypanel',

    requires: [
        'MyApp.view.MyPanelViewModel',
        'Ext.grid.Panel',
        'Ext.grid.column.Column',
        'Ext.grid.View',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button',
        'Ext.selection.CheckboxModel'
    ],

    viewModel: {
        type: 'mypanel'
    },
    height: 250,

    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    items: [
        {
            xtype: 'gridpanel',
            flex: 1,
            title: 'Bloqueos : Intentos Fallidos',
            forceFit: true,
            columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'rutdv',
                    text: 'Rut'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'nombre',
                    text: 'Nombre'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'cargo',
                    text: 'Cargo'
                },
                {
                    xtype: 'gridcolumn',
                    hidden: true,
                    dataIndex: 'rut',
                    text: 'rut'
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'bottom',
                    items: [
                        {
                            xtype: 'button',
                            id: 'btn_desbloquear_intentos',
                            icon: '../../images/btns/unlock.png',
                            text: 'Desbloquear Usuarios'
                        }
                    ]
                }
            ],
            selModel: {
                selType: 'checkboxmodel'
            }
        },
        {
            xtype: 'gridpanel',
            flex: 1,
            title: 'Bloqueos: No Uso',
            forceFit: true,
            columns: [
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'rutdv',
                    text: 'Rut'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'nombre',
                    text: 'Nombre'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'cargo',
                    text: 'Cargo'
                },
                {
                    xtype: 'gridcolumn',
                    hidden: true,
                    dataIndex: 'rut',
                    text: 'rut'
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'bottom',
                    items: [
                        {
                            xtype: 'button',
                            id: 'btn_desbloquear_nouso',
                            icon: '../../images/btns/unlock.png',
                            text: 'Desbloquear Usuarios'
                        }
                    ]
                }
            ],
            selModel: {
                selType: 'checkboxmodel'
            }
        }
    ]

});