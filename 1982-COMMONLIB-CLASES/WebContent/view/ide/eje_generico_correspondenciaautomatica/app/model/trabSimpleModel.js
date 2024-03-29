/*
 * File: app/model/trabSimpleModel.js
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

Ext.define('MyApp.model.trabSimpleModel', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.field.String'
    ],

    fields: [
        {
            type: 'string',
            name: 'rut'
        },
        {
            name: 'digito_ver'
        },
        {
            name: 'nombres'
        },
        {
            name: 'ape_paterno'
        },
        {
            name: 'ape_materno'
        },
        {
            name: 'mail'
        },
        {
            name: 'id_grupo'
        },
        {
            name: 'id_correspondencia'
        },
        {
            name: 'id_timer'
        }
    ]
});