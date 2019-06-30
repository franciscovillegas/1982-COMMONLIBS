/*
 * File: app/view/window_zonasadministracion.js
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

Ext.define('MyApp.view.window_zonasadministracion', {
    extend: 'Ext.window.Window',
    alias: 'widget.window_zonasadministracion',

    requires: [
        'MyApp.view.window_zonasadministracionViewModel',
        'Ext.toolbar.Toolbar',
        'Ext.button.Button',
        'Ext.grid.Panel',
        'Ext.view.Table',
        'Ext.grid.column.Check'
    ],

    viewModel: {
        type: 'window_zonasadministracion'
    },
    constrain: true,
    height: 408,
    id: 'window_zonasadministracion',
    width: 395,
    layout: 'fit',
    title: 'Administración',
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
                    xtype: 'button',
                    handler: function(button, e) {
                        var obj = MyApp.app.getController("ctrTool").store_getArray("js_zonasadministracion");


                        var f = eval(MyApp.retorno_user_app.func);
                        f(obj);

                        Ext.getCmp("window_zonasadministracion").destroy();

                    },
                    icon: '../../images/btns/tick.ico',
                    text: 'Cerrar'
                }
            ]
        }
    ],
    items: [
        {
            xtype: 'gridpanel',
            header: false,
            title: 'My Grid Panel',
            store: 'js_zonasadministracion',
            columns: [
                {
                    xtype: 'gridcolumn',
                    hidden: true,
                    dataIndex: 'app_id',
                    text: 'App Id'
                },
                {
                    xtype: 'gridcolumn',
                    dataIndex: 'descripcion',
                    text: 'Descripcion',
                    flex: 1
                },
                {
                    xtype: 'gridcolumn',
                    hidden: true,
                    dataIndex: 'orden',
                    text: 'Orden'
                },
                {
                    xtype: 'gridcolumn',
                    hidden: true,
                    dataIndex: 'is_administrable',
                    text: 'Is Administrable'
                },
                {
                    xtype: 'checkcolumn',
                    width: 38,
                    dataIndex: 'activo',
                    menuDisabled: true,
                    text: ''
                }
            ]
        }
    ],
    listeners: {
        render: 'onWindow_zonasadministracionRender',
        show: 'onWindow_zonasadministracionShow'
    },

    onWindow_zonasadministracionRender: function(component, eOpts) {
        
    },

    onWindow_zonasadministracionShow: function(component, eOpts) {
        console.log(component.extraParams);

        var str = null;

        try {
            str = Ext.decode(component.extraParams.strJson);
        }
        catch(e) {
            console.log(e);
        }
        Ext.getStore("js_zonasadministracion").removeAll();

        Ext.iterate(str, function(key, value) {

            console.log();

            console.log("RECORD ADDED");

            Ext.getStore("js_zonasadministracion").add(key);

        });


        var f = component.extraParams.func;
        Ext.define('MyApp.retorno_user_app', {
            statics: {
                func: f
            }
        });

    }

});