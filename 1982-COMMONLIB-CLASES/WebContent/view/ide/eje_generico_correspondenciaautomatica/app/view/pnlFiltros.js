/*
 * File: app/view/pnlFiltros.js
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

Ext.define('MyApp.view.pnlFiltros', {
    extend: 'Ext.window.Window',
    alias: 'widget.pnlFiltros',

    height: 438,
    id: 'pnlFiltros',
    scrollable: true,
    width: 344,
    closeAction: 'hide',
    title: 'Filtros Adicionales',
    maximizable: true,
    defaultListenerScope: true,

    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    listeners: {
        boxready: 'onPnlFiltrosBoxReady'
    },

    onPnlFiltrosBoxReady: function(component, width, height, eOpts) {
        Ext.define(this.getId(), {
            statics: {
                extraParams: component.extraParams,
                ejemplo:1
            }
        });

        var params = eval(this.getId()+".extraParams");

        Ext.define('enmComponente', {
            singleton: true,
            combo: 1,
            treepanel: 2
        });

        var enmFiltros = [];
        var x=0;
        var windowID = this.getId();

        /*CREA MODELO*/
        Ext.define('cAccion_window_categoria_model', {
            extend: 'Ext.data.Model',
            fields: [
                {name: 'id'				, type: 'string'},
                {name: 'nombre' 		, type: 'string'},
                {name: 'column_name' 	, type: 'string'},
                {name: 'icondo' 		, type: 'string'},
                {name: 'idcliente' 		, type: 'string'},
            ]
        });


        /*CREA STORE*/
        var url = '../../../EjeCoreI?claseweb=cl.imasd.view.sencha.anywhere.Conf';
        url += '&modulo=anywhere_webc_filtradoadicional';
        url += '&accion=get';
        url += '&thing=Categorias';

        var storeCategorias = Ext.create('Ext.data.Store',{
            model:"cAccion_window_categoria_model",
            id:"cAccion_window_categoria_store",
            proxy: {
                type: 'ajax',
                url: url,
                reader: {
                    type: 'json',
                    root: 'data'
                }
            }
        });


        storeCategorias.load(function(){
            this.each(function(record){
                enmFiltros.push({
                    posicion: x,
                    componente: enmComponente.treepanel,
                    id_filtro: record.data.id,
                    titulo: record.data.nombre,
                    tooltip: 'Filtrar por '+record.data.nombre,
                    icono: record.data.icono,
                    core: 'EjeCoreI',
                    claseweb: 'cl.imasd.view.sencha.anywhere.Conf',
                    modulo: 'anywhere_webc_filtradoadicional',
                    thing: 'Item',
                    extraParams: {idcategoria: record.data.column_name},
                    accion: 'getTree',
                    filas: [{name: 'key', type: 'int'}, {name: 'text'}]
                });

            });


            Ext.getCmp(windowID).fnConstruir(enmFiltros);

        });





    },

    fnConstruir: function(arrTipo) {

        var windowID = this.getId();

        var tlbFiltros = new Ext.Toolbar({
            xtype: 'toolbar',
            dock: 'top',
            id: 'tbrFiltros',
            items: [
                {
                    xtype: 'tbseparator',
                    id: 'sprFiltros_btns'
                },
                {
                    xtype: 'container',
                    flex: 1
                },
                {
                    xtype: 'tbseparator'
                },
                {
                    xtype: 'button',
                    id: 'btnFiltroAplicar',
                    icon: '../../images/btns/table_refresh.png',
                    text: 'Aplicar Filtros',
                    tooltip: 'Presione aquí para aplicar filtros.',
                    handler: function() {
                        Ext.getCmp(windowID).onSuccess(arrTipo);
                    }
                }
            ]
        });

        /*
        var cntSimple = Ext.create('Ext.container.Container', {
            id: 'pnlFiltro_Simle',
            layout: 'vbox',
            padding: 5
        });
        */

        var cntFiltros = Ext.create('Ext.panel.Panel',{
            id: 'pnlFiltrosAccordion',
            layout: 'accordion',
            flex: 1,
            header: false
        });

        //Ext.getCmp(this.getId()).add(cntSimple);
        Ext.getCmp(this.getId()).add(cntFiltros);
        Ext.getCmp(this.getId()).addDocked(tlbFiltros);

        var bolPanelSimpleCreado = false;
        var windowID = this.getId();

        Ext.each(arrTipo, function(tipo){
            if (tipo.componente===enmComponente.treepanel){
                Ext.getCmp('pnlFiltrosAccordion').add(Ext.getCmp(windowID).fnCrearTreePanel(arrTipo, tipo));
            }else{
                Ext.getCmp('pnlFiltro_Simle').add(Ext.getCmp(windowID).fnCrearCombo(tipo));
            }
        });

        Ext.getCmp('sprFiltros_btns').hide();

    },

    fnCrearTreePanel: function(arrTipo, tipo) {

                var windowID = this.getId();

                var store = Ext.create('Ext.data.TreeStore',{
                    id: 'Filtro.ts'+tipo.id_filtro,
                    pageSize: 50000,
                    timeout: 1200000,
                    fields : tipo.filas,
                    proxy: {
                        type: 'ajax',
                        url: '../../../'+tipo.core+'?claseweb='+tipo.claseweb+'&modulo='+tipo.modulo+'&accion='+tipo.accion+'&thing='+tipo.thing,
                        extraParams: tipo.extraParams,
                        reader: {
                            type: 'json',
                            rootProperty: 'children'
                        }
                    }
                });

                store.load();

                var treePanel = Ext.create('Ext.tree.Panel', {
                    id: 'tpnFiltro_'+tipo.id_filtro,
                    icon: tipo.icono,
                    title: tipo.titulo,
                    store: store,
                    displayField: 'text',
                    rootVisible: false,
                    hidden: true,
                    viewConfig: {
                        listeners: {
                            itemclick: function(dataview, record, item, index, e, eOpts) {
                                Ext.getCmp(windowID).fnItemClick(dataview, record, item, index, e, eOpts);
                            }
                        }
                    }
                });


                Ext.getCmp('tbrFiltros').add(tipo.posicion, Ext.create('Ext.Button',  {
                    tooltip: tipo.tooltip,
                    icon: tipo.icono,
                    enableToggle: true,
                    listeners: {
                        click: function(button, e, eOpts) {
                            Ext.getCmp(windowID).fnMostrarFiltro(arrTipo, tipo);
                        }
                    }
                }));

                return treePanel;


    },

    fnCrearCombo: function() {
                var store = Ext.create('Ext.data.Store',{
                    id: 'Filtro.jsGet'+tipo.id_filtro,
                    pageSize: 50000,
                    timeout: 1200000,
                    fields : tipo.filas,
                    proxy: {
                        type: 'ajax',
                        url: '../../../'+tipo.core+'?claseweb='+tipo.claseweb+'&modulo='+tipo.modulo+'&accion='+tipo.accion+'&thing='+tipo.thing,
                        extraParams: tipo.extraParams,
                        reader: {
                            type: 'json',
                            rootProperty: 'data'
                        }
                    }
                });

                store.load();

                var cbxCombo = Ext.create('Ext.form.ComboBox', {
                    id: 'cmbFiltro_'+tipo.id_filtro,
                    fieldLabel: tipo.titulo,
                    store: store,
                    labelWidth: tipo.labelWidth,
                    displayField: tipo.displayField,
                    valueField: tipo.valueField,
                    renderTo: Ext.getBody()
                });

                return cbxCombo;


    },

    fnMostrarFiltro: function(arrTipo, tipo) {

        if (tipo.componente===enmComponente.treepanel){
            if (Ext.getCmp('tpnFiltro_'+tipo.id_filtro).isVisible()){
                Ext.getCmp('tpnFiltro_'+tipo.id_filtro).hide();
            }else{
                Ext.getCmp('tpnFiltro_'+tipo.id_filtro).show();
            }

            var x=0;
            var bolVisible = false;

            Ext.each(arrTipo, function(tipo){
                if (tipo.componente===enmComponente.treepanel){
                    if (Ext.getCmp('tpnFiltro_'+tipo.id_filtro).isVisible()){
                        bolVisible=true;
                    }

                    x++;

                    if (x===arrTipo.length){
                        if (bolVisible){
                            Ext.getCmp('sprFiltros_btns').show();
                           // Ext.getCmp('btnFiltroMarcaTodo').show();
                        }else{
                            Ext.getCmp('sprFiltros_btns').hide();
                          //  Ext.getCmp('btnFiltroMarcaTodo').hide();
                        }
                    }
                }
            });
        }


    },

    fnItemClick: function(dataview, record, item, index, e, eOpts) {
        var strId = record.get('id');
        var bolAtivo = false;
        if (record.get('icon')==='../../images/btns/checkbox_null.png'){
            bolAtivo = true;
        }

        treeNode = dataview.ownerGrid.getRootNode();
        Ext.getCmp(this.getId()).fnSetAllNodes(treeNode.findChild('id',strId,true), bolAtivo);

        Ext.getCmp('btnFiltroAplicar').setDisabled(false);

    },

    fnSetAllNodes: function(node, bolActivo) {
                var strIcon = '../../images/btns/checkbox_null.png';
                if (bolActivo){
                    strIcon = '../../images/btns/checkbox_add.png';
                }

                var windowID = this.getId();
                node.set('icon', strIcon);
                node.set('activo', bolActivo);

                if(!node.isLeaf()){
                    node.eachChild(
                        function(ndHijo){
                            Ext.getCmp(windowID).fnSetAllNodes(ndHijo, bolActivo);
                        }
                    );
                }


    },

    onSuccess: function(arrTipo) {
        var params = eval(this.getId()+".extraParams");
        var windowID = this.getId();

        if(params != null) {
            if(params.success != null) {

                var x=0;

                var arrFiltros = [];
                var arrReponse = [];
                var response = {};

                Ext.each(arrTipo, function(tipo){
                    if (Ext.getCmp('tpnFiltro_'+tipo.id_filtro).isVisible()){
                        if (tipo.componente===enmComponente.treepanel){
                            arrReponse.push(tipo.id_filtro);
                            response["campos"] = arrReponse;
                            response[tipo.id_filtro] = Ext.getCmp(windowID).fnArrayFromTree('tpnFiltro_'+tipo.id_filtro, "key");

                        }else if (tipo.componente===enmComponente.combo){
                            arrFiltros.push({
                                componente: tipo.componente,
                                id_filtro: tipo.id_filtro,
                                record: Ext.getStore('Filtro.jsGet'+tipo.id_filtro)
                                .findRecord(tipo.valueField, Ext.getCmp('cmbFiltro_'+tipo.id_filtro).getValue())
                            });

                        }
                    }
                    x++;

                    if (x===arrTipo.length){
                        var fn = eval(params.success);
                        fn(response);
                    }
                });

            }
        }
    },

    fnStrFromTree: function(nodo, dataName) {
        var strLocales = "";

        try {
            var windowID = this.getId();

            if(nodo != null) {
                nodo.eachChild(function(nodeHijo) {

                    var unid_ID = nodeHijo.get(dataName);

                    if (nodeHijo.get('activo')==true && unid_ID != null){
                        if (strLocales!='' && strLocales!=null){
                            strLocales+=',';
                        }
                        strLocales=strLocales+"\""+unid_ID+"\"";
                    }

                    var casiResponse = Ext.getCmp(windowID).fnStrFromTree(nodeHijo, dataName);

                    if(casiResponse != null && casiResponse != ""){
                        if(strLocales != "" && strLocales !=null){
                            strLocales +=',';
                        }
                        strLocales += casiResponse;
                    }
                });
            }

        }
        catch(e) {
            console.log(e);
        }

        return strLocales;
    },

    fnArrayFromTree: function(treeKey, dataName) {
              var treeNode = Ext.getCmp(treeKey).getRootNode();
              var str = this.fnStrFromTree(treeNode,dataName);
              return JSON.parse("["+str+"]");

    }

});