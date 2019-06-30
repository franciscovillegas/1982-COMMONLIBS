/*
 * File: app/controller/mcButton.js
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

Ext.define('MyApp.controller.mcButton', {
    extend: 'Ext.app.Controller',

    control: {
        "button": {
            click: 'onButtonClick'
        }
    },

    onButtonClick: function(button, e, eOpts) {
        var id=button.id;
        switch(id) {
            case 'btn_Mantenedor_ccosto_refresh':
                Ext.getStore("js_CcostoAll").load();
                Ext.getCmp("txt_Mantenedor_ccosto_buscar").reset();
                Ext.getCmp("frm_Mantenedorccosto").reset();
                break;
            case 'btn_Mantenedor_ccosto_buscar':
                var filtro = Ext.getCmp("txt_Mantenedor_ccosto_buscar").getValue();
                Ext.getStore("js_CcostoAll").load({params : {"filtro": filtro} });
                break;
            case 'btn_Mantenedor_ccosto_nuevo':
                Ext.getCmp("frm_Mantenedorccosto").reset();
                Ext.getCmp("frm_Mantenedorccosto").getForm().findField('centro_costo').setReadOnly(false);
                Ext.getCmp('hidden_ccosto_tipo').setValue(0);
                break;
            case 'btn_Mantenedor_ccosto_grabar':
                this.grabarCentrocosto();
                break;
        }
    },

    grabarCentrocosto: function() {
        var error=false;
        var form = Ext.getCmp("frm_Mantenedorccosto").getForm();
        var tipo = Ext.getCmp("hidden_ccosto_tipo").getValue();

        form.submit({
            url:"../../../servlet/EjeCoreI?claseweb=cl.eje.view.sencha.Conf&modulo=eje_generico_mantenedorcentrocostos&accion=save&thing=Centrocosto",
            method: "POST",
            params:	{ "tipo" : tipo },
            waitMsg	: 'Grabando los datos del centro de costo...',
            success	: function(form,result){
                try {
                    var data = Ext.JSON.decode(result.response.responseText);
                    if (data.success==="true" || data.success===true){
                        Ext.Msg.show({
                            title: 'Actualización',
                            msg: 'Los datos del Centro de Costo fueron guardados correctamente.',
                            buttons: Ext.Msg.OK,
                            icon: Ext.Msg.INFO
                        });
                        Ext.getCmp("frm_Mantenedorccosto").reset();
                        Ext.getStore("js_CcostoAll").load();
                    }
                    else{
                        error=true;
                    }
                }
                catch(e){
                    error=true;
                }
                if (error===true){
                    var mensg='<p>Ha ocurido un error al tratar de grabar la informaci\u00f3n.</p> ';
                    mensg=mensg+'<p>Por favor int\u00e9ntelo nuevamente.</p> ';
                    Ext.Msg.show({
                        title: 'Error inesperado',
                        msg: mensg,
                        buttons: Ext.Msg.OK,
                        icon: Ext.Msg.ERROR
                    });
                }
            },
            complete: function() {

            }
        });
    }

});
