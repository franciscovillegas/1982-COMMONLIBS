/*
 * File: app/controller/ctrTool.js
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

Ext.define('MyApp.controller.ctrTool', {
    extend: 'Ext.app.Controller',

    consolelog: function(msg) {
        try {
            console.log(msg);
        }
        catch(e) {
        }
    },

    fnErrorDetectado: function() {
        var mensg='<p>Ha ocurido un error al generar su solicitud.</p> ';
        mensg=mensg+'<p>Por favor inténtelo nuevamente.</p> ';

        Ext.Msg.show({
            title: 'Error inesperado',
            msg: mensg,
            buttons: Ext.Msg.OK,
            icon: Ext.Msg.ERROR
        });
    },

    fnAbrirFormulario: function(pnlPanel, mwWindow) {
        var view = 'MyApp.view.'+mwWindow;
        var idPanel = pnlPanel.replace('.','');
        var idWindwow = mwWindow.replace('.','');

        if(Ext.get(idWindwow) === null) {
            var win = Ext.create(view);
            Ext.getCmp(idPanel).add(win);
            win.show();
        }
        else {
            Ext.WindowMgr.bringToFront(idWindwow);
        }
    },

    grid_delSelected: function(idGrilla) {
        if(idGrilla == null) {
            console.log("[ERROR!! grid_getSelected] alguno de los parámetros vienen en blanco");
            return;
        }


        var seleccionados=Ext.getCmp(idGrilla).getSelectionModel().getSelection();

        var y = seleccionados.length;
        if (y>0){
            Ext.each(seleccionados, function(record, index, allRecords) {
                 Ext.getCmp(idGrilla).getStore().remove(record);

            });
        }


        /*

        var grid = Ext.getCmp(idGrilla);
        var store = grid.getStore();

        var selectedRecord = grid.getSelectionModel().getSelection()[0];
        var row = grid.store.indexOf(selectedRecord);

        store.removeAt(row);

        if (store.getCount() > row) {
            grid.getSelectionModel().select(row);
        }else if (store.getCount() > 0) {
            grid.getSelectionModel().select(store.getCount()-1);
        }
        */
    },

    grid_getSelected: function(gridID, keyRecord) {
        if(gridID == null || keyRecord == null) {
            console.log("[ERROR!! grid_getSelected] alguno de los parámetros vienen en blanco");
            return;
        }

        var strPersonas = '';

        var seleccionados=Ext.getCmp(gridID).getSelectionModel().getSelection();

        var y = seleccionados.length;
        if (y>0){
            Ext.each(seleccionados, function(record, index, allRecords) {
                if (strPersonas!==''){
                    strPersonas=strPersonas+',';
                }
                strPersonas=strPersonas+record.get(keyRecord);
            });
        }

        return strPersonas;

    },

    grid_getSelectedArray: function(gridKey, keyRecord) {
          return JSON.parse("["+MyApp.app.getController("ctrTool")
                                          .grid_getSelected
                                          (gridKey,keyRecord)+"]");


    },

    grid_moveSelected: function(gridID_origen, gridID_destino) {
                if(gridID_origen == null || gridID_destino == null) {
                    console.log("[ERROR!! grid_moveSelected] alguno de los parámetros vienen en blanco o null");
                    return;
                }

                var strPersonas = '';

                var seleccionados=Ext.getCmp(gridID_origen).getSelectionModel().getSelection();
                var store = Ext.getCmp(gridID_destino).getStore();



                var y = seleccionados.length;
                if (y>0){
                    Ext.each(seleccionados, function(record, index, allRecords) {
                        store.add(record);
                        //Ext.getCmp(gridID_origen).getStore().remove(record);
                    });
                }

                MyApp.app.getController("ctrTool").grid_delSelected(gridID_origen);


                return strPersonas;


    },

    store_copy: function(storeOrigenID, storeDestID) {

                if(storeOrigenID == null || storeDestID == null) {
                    console.log("[ERROR!! store_moveAllRecord] alguno de los parámetros vienen en blanco o null");
                    return;
                }

                try {
                    console.log("ANTES [ORIGEN:"+Ext.getStore(storeOrigenID).data.length + " DESTINO:"+Ext.getStore(storeDestID).data.length+"]");
                }
                catch(e) {

                }

                Ext.getStore(storeOrigenID).data.each(function(record) {
                    Ext.getStore(storeDestID).add(record);
                });

                try {
                    console.log("AHORA [ORIGEN:"+Ext.getStore(storeOrigenID).data.length + " DESTINO:"+Ext.getStore(storeDestID).data.length+"]");
                }
                catch(e) {

                }

    },

    store_clear: function(storeID) {

                if(storeID == null || storeID == null) {
                    console.log("[ERROR!! store_moveAllRecord] alguno de los parámetros vienen en blanco o null");
                    return;
                }

                Ext.getStore(storeID).removeAll(true);

    },

    store_getArray: function(idStore) {
        var array = new Array();

            Ext.getStore(idStore).each(function(record){

               array.push(record.data);

            });

        return array;

    },

    store_getEncode: function(idStore) {
                return Ext.encode(this.store_getArray(idStore));
    },

    tree_getSelected: function(nodo, dataName) {
                var strLocales = "";

                try {
                    if(nodo != null) {
                        nodo.eachChild(function(nodeHijo) {
                           // console.log(nodeHijo);
                            var unid_ID = nodeHijo.get(dataName);

                            if (nodeHijo.get('activo')==true){
                                if (strLocales!='' && strLocales!=null){
                                    strLocales+=',';
                                }
                                strLocales=strLocales+unid_ID;
                            }

                            var casiResponse = MyApp.app.getController("ctrTool").tree_getSelected(nodeHijo, dataName);

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

    tree_setSelected: function(nodo, functionNode) {
                try {
                    if(nodo != null) {
                        nodo.eachChild(function(nodeHijo) {
                            var f = eval(functionNode);
                            f(nodeHijo);


                            MyApp.app.getController("ctrTool").tree_setSelected(nodeHijo, functionNode);


                        });
                    }

                }
                catch(e) {
                    console.log(e);
                }



    },

    url_getParam: function(sParam) {
        /*
        var sPageURL = window.location.search.substring(1);
        var sURLVariables = sPageURL.split('&');

        return sURLVariables[0];

        for (var i = 0; i < sURLVariables.length; i++) {
            var sParameterName = sURLVariables[i].split('=');
            if (sParameterName[0] == sParam) {
                return sParameterName[1];
            }
        }
        */

        var url = document.location.href;
        var inicio = document.location.href.indexOf("?");
        var queryString = url.substring(inicio + 1, url.length);
        var params = Ext.Object.fromQueryString(queryString);
        return params;
    },

    fnShowWindow: function(windowID, parentWindowID, params) {
        try {
            var id = "MyApp.view."+windowID;

            if(Ext.get(windowID) == null) {
                var win = Ext.create(id);
                if(parentWindowID != null) {
                    Ext.getCmp(parentWindowID).add(win);

                    try {
                        console.log(parentWindowID + ' ->>>- ' + id);
                        Ext.getCmp(parentWindowID).setConstrainContainer(win);
                    }
                    catch(e) {

                    }
                }

                win.extraParams = params;
                win.show();

            }
            else {
                Ext.get(windowID).show();
                Ext.get(windowID).focus();
            }


        }
        catch(e) {
            console.log(e);
        }


    },

    fnGetCheckItemsTree: function(nodo, dataName) {
        var strLocales = "";

        try {
            if(nodo != null) {
                nodo.eachChild(function(nodeHijo) {
                    var unid_ID = nodeHijo.get(dataName);

                    if (nodeHijo.get('activo')==true){
                        if (strLocales!='' && strLocales!=null){
                            strLocales+=',';
                        }
                        strLocales=strLocales+unid_ID;
                    }

                    var casiResponse = MyApp.app.getController("ctrTool").fnGetCheckItemsTree(nodeHijo, dataName);

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

    fnSetCheckItemsTree: function(nodo, functionNode) {
                try {
                    if(nodo != null) {
                        nodo.eachChild(function(nodeHijo) {
                            var f = eval(functionNode);
                            f(nodeHijo);


                            MyApp.app.getController("ctrTool").fnSetCheckItemsTree(nodeHijo, functionNode);


                        });
                    }

                }
                catch(e) {
                    console.log(e);
                }


    },

    fnShowWindowOnMain: function(windowID) {
        this.fnShowWindow(windowID, "pnlISM_Descanso" );
    },

    fnLoadGrafico: function(modulo, thing, idDiv, minPersonas, limitePersonas) {
                if(limitePersonas == null) {
                    limitePersonas = 5000;
                }
                var cantPersonas = 0;
                var strPersonas='';

                var idcliente = Ext.getCmp('cmbISM_Cliente').getValue();
                var seleccionados=Ext.getCmp('gpnISM_PorLocal_Personas').getSelectionModel().getSelection();
                var y = seleccionados.length;
                if (y>0){
                    Ext.each(seleccionados, function(record, index, allRecords) {
                        if (strPersonas!==''){
                            strPersonas=strPersonas+',';

                        }
                        strPersonas=strPersonas+record.get('idusuario');
                        cantPersonas += 1;
                    });
                }

                if(cantPersonas == 0) {
                    var msg = "Error, debes elegir al menos una personas. ";
                    Ext.Msg.alert("Anywhere",msg);

                }else if(cantPersonas > limitePersonas) {
                    var msg = "Error, demasiadas personas seleccionadas, debes elegir un máximo de ";
                    msg+= limitePersonas + " persona(s)";
                    Ext.Msg.alert("Anywhere",msg);
                }
                else {
                    Ext.Ajax.request({
                    url: "../../../EjeCore",
                    method: 'POST',
                    timeout: 60000000,
                    params: {
                        claseweb : "cl.eje.view.sencha.ConfSegura",
                        modulo	 : modulo,
                        accion   : "get",
                        thing	 : thing,
                        personas :strPersonas,
                        idproceso:idcliente
                    },
                    success: function(response){
                        var text = response.responseText;
                        var json = JSON.parse(text);
                        console.log(json);
                        console.log(json);
                        if( $("#"+idDiv).length > 0) {
                            $("#"+idDiv).highcharts(json);
                        }

                    },
                    error: function() {

                    }
                });
                }


    },

    getArrayFromGridTree: function(treeKey) {
                var treeNode = Ext.getCmp(treeKey).getRootNode();
                var str = MyApp.app.getController("ctrTool")
                                   .fnGetCheckItemsTree(treeNode,"unid_id");
               // console.log(str);
                return JSON.parse("["+str+"]");


    },

    fnSetComboFromStore: function(storeID, fieldIDWithBooleanValue, comboID) {
          Ext.getStore(storeID).each(function(record,idx){
                    if(record.get(fieldIDWithBooleanValue) == "1"    ||
                       record.get(fieldIDWithBooleanValue) ==  1     ||
                       record.get(fieldIDWithBooleanValue) == "true" ||
                       record.get(fieldIDWithBooleanValue) == true) {

                        Ext.getCmp(comboID).setValue(record);
                    }
                });


    }

});