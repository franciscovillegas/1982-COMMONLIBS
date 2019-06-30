/*
 * File: app/view/pnl_mantenedorccostos.js
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

Ext.define('MyApp.view.pnl_mantenedorccostos', {
    extend: 'Ext.panel.Panel',

    requires: [
        'MyApp.view.pnl_MantenedorcargosViewModel3',
        'Ext.grid.Panel',
        'Ext.grid.column.Column',
        'Ext.grid.View',
        'Ext.toolbar.Toolbar',
        'Ext.form.field.Text',
        'Ext.button.Button',
        'Ext.form.Panel',
        'Ext.Img',
        'Ext.form.field.Checkbox',
        'Ext.form.field.Hidden'
    ],

    viewModel: {
        type: 'pnl_mantenedorccostos'
    },
    constrain: true,
    autoShow: true,
    height: 350,
    id: 'pnl_Mantenedorccosto',
    width: 800,
    title: 'Mantenedor de Centros de Costo',

    layout: {
        type: 'hbox',
        align: 'stretch'
    },
    items: [
        {
            xtype: 'gridpanel',
            flex: 3,
            id: 'grid_Mantenedor_ccosto_lista',
            title: 'Listado',
            store: 'js_CcostoAll',
            columns: [
                {
                    xtype: 'gridcolumn',
                    width: 90,
                    dataIndex: 'centro_costo',
                    text: 'Codigo'
                },
                {
                    xtype: 'gridcolumn',
                    width: 350,
                    dataIndex: 'descrip',
                    text: 'Centro Costo'
                }
            ],
            viewConfig: {
                id: 'grid_Mantenedor_ccosto_lista_view'
            },
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'bottom',
                    items: [
                        {
                            xtype: 'textfield',
                            id: 'txt_Mantenedor_ccosto_buscar'
                        },
                        {
                            xtype: 'button',
                            id: 'btn_Mantenedor_ccosto_buscar',
                            icon: '../../images/find.png'
                        },
                        {
                            xtype: 'button',
                            id: 'btn_Mantenedor_ccosto_refresh',
                            margin: '0 0 0 10',
                            icon: '../../images/refresh.png',
                            text: 'Refrescar'
                        }
                    ]
                }
            ]
        },
        {
            xtype: 'panel',
            flex: 4,
            autoScroll: true,
            width: 100,
            title: 'Detalle Centro Costo',
            dockedItems: [
                {
                    xtype: 'toolbar',
                    dock: 'bottom',
                    items: [
                        {
                            xtype: 'button',
                            id: 'btn_Mantenedor_ccosto_nuevo',
                            icon: '../../images/add.png',
                            text: 'Nuevo Centro Costo'
                        },
                        {
                            xtype: 'button',
                            id: 'btn_Mantenedor_ccosto_grabar',
                            margin: '0 0 0 10',
                            icon: '../../images/save.png',
                            text: 'Grabar'
                        }
                    ]
                }
            ],
            items: [
                {
                    xtype: 'form',
                    id: 'frm_Mantenedorccosto',
                    margin: '15 5 5 5',
                    bodyPadding: 10,
                    layout: {
                        type: 'table',
                        columns: 2
                    },
                    items: [
                        {
                            xtype: 'textfield',
                            id: 'txt_Codigo_ccosto',
                            maxWidth: 200,
                            fieldLabel: 'C&oacute;digo',
                            labelWidth: 50,
                            name: 'centro_costo'
                        },
                        {
                            xtype: 'image',
                            height: 20,
                            id: 'img_Ccosto',
                            margin: '0 0 0 -135',
                            width: 20,
                            src: '../../images/transparent.png'
                        },
                        {
                            xtype: 'textfield',
                            colspan: 2,
                            id: 'txt_Nombre_ccosto',
                            maxWidth: 350,
                            width: 350,
                            fieldLabel: 'Nombre',
                            labelWidth: 50,
                            name: 'descrip'
                        },
                        {
                            xtype: 'checkboxfield',
                            colspan: 2,
                            id: 'chb_Vigente_ccosto',
                            margin: '0 0 0 50',
                            name: 'vigente',
                            boxLabel: 'Centro Costo Vigente',
                            inputValue: 'S',
                            uncheckedValue: 'N'
                        }
                    ]
                },
                {
                    xtype: 'hiddenfield',
                    id: 'hidden_ccosto_tipo',
                    fieldLabel: 'Label'
                }
            ]
        },
        {
            xtype: 'form',
            flex: 1,
            hidden: true,
            id: 'frm_Mantenedor_ccosto_existencia',
            bodyPadding: 10,
            title: 'My Form'
        }
    ]

});