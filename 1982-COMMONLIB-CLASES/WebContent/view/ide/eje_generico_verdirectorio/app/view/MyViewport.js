/*
 * File: app/view/MyViewport.js
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

Ext.define('MyApp.view.MyViewport', {
    extend: 'Ext.container.Viewport',
    alias: 'widget.myviewport',

    requires: [
        'MyApp.view.MyViewportViewModel',
        'Ext.grid.Panel',
        'Ext.grid.column.Column',
        'Ext.grid.View',
        'Ext.form.field.Text',
        'Ext.button.Button',
        'Ext.toolbar.Paging'
    ],

    viewModel: {
        type: 'myviewport'
    },
    layout: 'fit',

    items: [
        {
            xtype: 'gridpanel',
            autoScroll: true,
            id: 'grid_Ingresos',
            forceFit: true,
            store: 'jsGetColaborador',
            columns: [
                {
                    xtype: 'gridcolumn',
                    width: 130,
                    dataIndex: 'rut',
                    text: 'Rut'
                },
                {
                    xtype: 'gridcolumn',
                    width: 190,
                    dataIndex: 'nombre',
                    text: 'Nombre'
                },
                {
                    xtype: 'gridcolumn',
                    width: 180,
                    dataIndex: 'ape_paterno',
                    text: 'Ape.Paterno'
                },
                {
                    xtype: 'gridcolumn',
                    width: 180,
                    dataIndex: 'ape_materno',
                    text: 'Ape.Materno'
                },
                {
                    xtype: 'gridcolumn',
                    width: 115,
                    dataIndex: 'cargo',
                    text: 'Cod.Cargo'
                },
                {
                    xtype: 'gridcolumn',
                    width: 250,
                    dataIndex: 'desc_cargo',
                    text: 'Nombre Cargo'
                },
                {
                    xtype: 'gridcolumn',
                    width: 115,
                    dataIndex: 'unidad',
                    text: 'Cod.Unidad'
                },
                {
                    xtype: 'gridcolumn',
                    width: 200,
                    dataIndex: 'desc_unidad',
                    text: 'Nombre Unidad'
                },
                {
                    xtype: 'gridcolumn',
                    width: 200,
                    dataIndex: 'empresa',
                    text: 'Empresa'
                },
                {
                    xtype: 'gridcolumn',
                    width: 130,
                    dataIndex: 'telefono',
                    text: 'Teléfono'
                },
                {
                    xtype: 'gridcolumn',
                    width: 130,
                    dataIndex: 'celular',
                    text: 'Celular'
                },
                {
                    xtype: 'gridcolumn',
                    width: 100,
                    align: 'center',
                    dataIndex: 'anexo',
                    text: 'Anexo'
                },
                {
                    xtype: 'gridcolumn',
                    width: 250,
                    dataIndex: 'mail',
                    text: 'E-mail'
                }
            ],
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'top',
                    items: [
                        {
                            xtype: 'textfield',
                            id: 'txt_Filtro',
                            width: 350,
                            fieldLabel: 'Buscar Colaborador',
                            labelWidth: 120
                        },
                        {
                            xtype: 'button',
                            id: 'btn_Filtro',
                            icon: '../../images/find.png',
                            tooltip: 'Filtrar'
                        },
                        {
                            xtype: 'button',
                            id: 'btn_Refresh',
                            icon: '../../images/refresh.png',
                            tooltip: 'Refresca'
                        }
                    ]
                },
                {
                    xtype: 'toolbar',
                    dock: 'bottom',
                    hidden: true,
                    items: [
                        {
                            xtype: 'button',
                            id: 'btn_Exportar',
                            icon: '../../images/excel.png',
                            text: 'Exportar'
                        }
                    ]
                },
                {
                    xtype: 'pagingtoolbar',
                    dock: 'bottom',
                    width: 360,
                    displayInfo: true,
                    store: 'jsGetColaborador'
                }
            ]
        }
    ]

});