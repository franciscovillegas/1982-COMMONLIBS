/*
 * File: app/view/panelUpload_2.js
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

Ext.define('MyApp.view.panelUpload_2', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.panelUpload_2',

    requires: [
        'MyApp.view.panelUpload_2ViewModel',
        'Ext.form.RadioGroup',
        'Ext.form.field.Radio',
        'Ext.form.field.File'
    ],

    viewModel: {
        type: 'panelupload_2'
    },
    height: 153,
    id: 'panelUpload_2',
    width: 471,
    header: false,
    title: 'My Panel',

    layout: {
        type: 'vbox',
        align: 'stretch',
        padding: 20
    },
    items: [
        {
            xtype: 'radiogroup',
            width: 471,
            fieldLabel: 'Label',
            hideLabel: true,
            allowBlank: false,
            items: [
                {
                    xtype: 'radiofield',
                    name: 'tipo',
                    boxLabel: 'Trabajadores',
                    inputValue: '1'
                },
                {
                    xtype: 'radiofield',
                    name: 'tipo',
                    boxLabel: 'Haberes, Descuentos y otros',
                    inputValue: '2'
                }
            ]
        },
        {
            xtype: 'filefield',
            id: 'fileToUpload',
            margin: '10 20 10 20',
            fieldLabel: 'Archivo Excel',
            name: 'fileToUpload',
            allowBlank: false
        }
    ]

});