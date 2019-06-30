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
        var isArray = (keyRecord.constructor == Array);

        if(gridID == null || keyRecord == null) {
            MyApp.app.getController("ctrTool").consolelog("[ERROR!! grid_getSelected] alguno de los parámetros vienen en blanco");
            return;
        }

        var strPersonas = '';

        var seleccionados=Ext.getCmp(gridID).getSelectionModel().getSelection();

        var y = seleccionados.length;
        if (y>0){
            Ext.each(seleccionados, function(record, index, allRecords) {
                if(isArray) {
                    strPersonas+="{";

                    Ext.each(keyRecord, function(k,v) {
                        if(!record.data[k]) {
                            MyApp.app.getController("ctrTool").consolelog("@@@@ No existe la columna \""+k+"\"");
                        }

                        if (strPersonas!=='{'){
                            strPersonas=strPersonas+',';
                        }

                        strPersonas+= ("\""+k+"\":\""+record.get(k)+"\"");
                    });

                    strPersonas+="}";
                }
                else {
                    if(!record.data[keyRecord]) {
                        MyApp.app.getController("ctrTool").consolelog("@@@@ No existe la columna \""+keyRecord+"\"");
                    }
                    else if (strPersonas!==''){
                        strPersonas=strPersonas+',';
                    }

                    strPersonas=strPersonas+record.get(keyRecord);
                }


            });
        }

        return strPersonas;

    },

    grid_getSelectedArray: function(gridKey, keyRecord) {
        var str = "["+MyApp.app.getController("ctrTool").grid_getSelected(gridKey,keyRecord)+"]";

        try {
            return JSON.parse(str);
        }
        catch(e) {
            MyApp.app.getController("ctrTool").consolelog("@@@@ Valor json no válido, no fue posible obtener el Array.");
        }

        return null;

    },

    grid_getSelectedRecords: function(gridID) {
                var seleccionados=Ext.getCmp(gridID).getSelectionModel().getSelection();
                return seleccionados;

    },

    grid_getSelectedData: function(gridID) {
                var seleccionados=Ext.getCmp(gridID).getSelectionModel().getSelection();
                var array = [];

                Ext.each(seleccionados || [], function(record) {
                    if(record != null) {
                        Ext.each(record.data || [], function(key, index) {
                            array.push(key);
                        }, this);
                    }
                }, this);

                return array;

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

    grid_setSelected: function(jsonAsObject, modelAsObject, tableToAddData) {
        var store = new Ext.data.Store({
            model: modelAsObject,
            proxy: {
                type: 'ajax',
                reader: {
                    type: 'json'
                }
            },
            autoLoad: false
        });

        if(!(jsonAsObject instanceof Array)) {
            jsonAsObject = Ext.JSON.decode(jsonAsObject);
        }

        Ext.each(jsonAsObject, function(row) {
            store.add(row);
        });


        tableToAddData.setStore(store);
        tableToAddData.getSelectionModel().selectAll();


    },

    grid_exportexcel: function(gridSelector) {
        var grid = Ext.getCmp(gridSelector);

        if (grid.store.getCount()===0){
            Ext.Msg.show({
                title: 'Exportar',
                msg: 'Sin informaci\u00f3n para exportar.',
                buttons: Ext.Msg.OK,
                icon: Ext.Msg.INFO
            });
        }else{
            var strParams = '';
            var ArrColumnas = [];

            Ext.iterate(grid.store.getProxy().extraParams, function(key, value) {
                strParams=strParams+'&'+key+'='+value;
            });

            var strURL = grid.store.getProxy().url+'&return_type=application/excel'+strParams;
            Ext.each(grid.columns, function(col, index) {
                if (grid.columns[index].isVisible() && grid.columns[index].xtype!=='actioncolumn'){
                    if (col.text==='&#160;'){
                        ArrColumnas.push({columna: col.dataIndex, texto: col.container.component.text});
                    }else{
                        ArrColumnas.push({columna: col.dataIndex, texto: col.text});
                    }
                }
            });

            var url  =  strURL + '&columnas='+Ext.encode(ArrColumnas);

            console.log(url);
            window.document.location = url;

        }

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

        Ext.getStore(storeDestID).data.clear();

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

    tree_getSelectedRecords: function(nodo, acceptKeys) {
        var retornoArray = [];

        try {
            if(nodo != null) {
                nodo.eachChild(function(nodeHijo) {
                    var casiResponse = MyApp.app.getController("ctrTool").tree_getSelectedRecords(nodeHijo, acceptKeys);

                    if(casiResponse != null) {
                        if(casiResponse.length > 0) {

                            var pos = 0;
                            for(var o in casiResponse) {
                                retornoArray.push(casiResponse[pos++]);
                            }
                        }
                    }


                    if(nodeHijo.get("activo") == true) {
                        var jsonCloned = {};
                        var json = nodeHijo.data;

                        if(acceptKeys == null) {
                            acceptKeys = [];

                            for(var k in json){
                                if("children" != k) {
                                    acceptKeys.push(k);
                                }
                            }
                        }

                        for(var k in json){
                            if( acceptKeys.indexOf(k) != -1){
                                jsonCloned[k] = nodeHijo.get(k);
                            }
                        }


                        retornoArray.push(jsonCloned);

                    }




                });
            }

        }
        catch(e) {
            console.log(e);
        }


        return retornoArray;
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

    tree_setAllNodes: function(node, record, allBooleanRecursiveValue) {

                if(allBooleanRecursiveValue == null) {
                    allBooleanRecursiveValue = !(record.get("activo") != null && record.get("activo") != false);
                }

                var strIcon = '../../images/btns/checkbox_null.png';
                if (allBooleanRecursiveValue){
                    strIcon = '../../images/btns/checkbox_add.png';
                }


                node.set('icon', strIcon);
                node.set('activo', allBooleanRecursiveValue);

                if(!node.isLeaf()){
                    node.eachChild(
                        function(ndHijo){
                            MyApp.app.getController("ctrTool").tree_setAllNodes(ndHijo, record, allBooleanRecursiveValue);
                        }
                    );
                }

    },

    url_getParam: function() {
        var url = document.location.href;
        var inicio = document.location.href.indexOf("?");
        var queryString = url.substring(inicio + 1, url.length);
        var params = Ext.Object.fromQueryString(queryString);
        return params;
    },

    fnShowWindowOnMain: function(windowID) {
        this.fnShowWindow(windowID, "pnlISM_Descanso" );
    },

    fnShowWindow: function(windowID, parentWindowID, params, functionJavascript) {
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

                console.log("showing..");
                win.extraParams = params;

                console.log("showing.. 2");
                win.show();

                console.log("showing.. 3");
                win.focus();

            }
            else {
                Ext.get(windowID).show();
                Ext.get(windowID).focus();
            }


        }
        catch(e) {
            console.log(e);
        }
        finally {
            if(functionJavascript != null) {
                if( functionJavascript instanceof Function ) {
                    var f = eval(functionJavascript);
                    f();
                }
            }
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


    },

    jsonManejaError: function(resp, onSuccess, onFailure) {
                if(resp == null) {
                    Ext.Msg.alert("Error", "Ha ocurrido un error desconocido.", onFailure);
                }
                else {
                    if(resp.success == true) {
                        if(resp.message != null) {
                            Ext.Msg.alert("Servidor", resp.message, onSuccess);
                        }
                        else {
                            var func = eval(onSuccess);
                            func();
                        }
                    }
                    else {
                        if(resp.message == null) {
                            Ext.Msg.alert("Error", "Ha ocurrido un error desconocido.", onFailure);
                        }
                        else {
                            Ext.Msg.alert("Error", resp.message, onFailure);
                        }
                    }
                }

    },

    util_onLoadApp: function(defWindow) {
            console.log("[utilOnLoadApp]");
            var params = MyApp.app.getController("ctrTool").url_getParam();

            if( params.w != null) {
                defWindow = params.w;
            }

            MyApp.app.getController("ctrTool").fnShowWindow(defWindow, null, null, function() {
                console.log(Ext.getCmp(defWindow));

                if(params.w != null) {

                    if( params.maximized != null) {
                        if( params.maximized == "true") {
                            Ext.getCmp(defWindow).maximize();
                        }
                    }

                    if( params.maximizable != null) {
                        //Ext.getCmp(defWindow).setMaximizable(params.maximizable); NO FUNCIONA
                    }

                    if( params.minimizable != null) {
                        //Ext.getCmp(defWindow).setMinimizable(params.minimizable); NO FUNCIONA
                    }

                    if( params.closable != null) {
                        //Ext.getCmp(defWindow).setClosable(params.setClosable); NO FUNCIONA
                    }

                    if( params.hidden != null ) {
                        var p = eval(params.hidden);

                        Ext.each(p, function(key) {
                            var keyObj = Ext.getCmp(key);

                            if(keyObj != null) {
                                try {
                                    keyObj.setHidden(true);
                                }
                                catch(e) {
                                    console.log(e);
                                }
                            }
                        });
                    }
                }
            });




    },

    buildGridFromGetMethod: function(url, params, panelParent) {
               Ext.Ajax.request({
                        url: url,
                        params: params,
                        success: function(response){
                            var data = JSON.parse(response.responseText);

                            if(data.success==true) {
                                MyApp.app.getController("ctrTool").buildGridFromGetMethod2(data, panelParent);
                            }
                            else {
                                Ext.Msg.alert("Error",
                                              "No ha podiso ser procesada la petici&oacute;n");
                            }
                        },
                        failure : function(response) {
                                 Ext.Msg.alert("Error",
                                              "No ha podiso ser procesada la petici&oacute;n");
                        }
                    });


    },

    buildGridFromGetMethod2: function(data, panelParent, url, params) {
                if(Ext.getCmp("grillaDetalleConsultaData") != null) {
                    Ext.getCmp("grillaDetalleConsultaData").destroy();
                }

                /*MODEL*/
                //create dynamic columns
                var modelFields = [];

                Ext.Array.each(data.columns, function (name, index, resultfields) {
                    var this_modelField = {};
                    this_modelField['mapping'] = name.name;
                    this_modelField['name'] = name.name;
                    modelFields.push(this_modelField);
                });

                Ext.define("modelDetalleConsultaData", {
                    extend: 'Ext.data.Model',
                    fields: modelFields
                });

                /*STORE*/
                var store = new Ext.data.Store({
                    storeId: "storeDetalleConsultaData",
                    model : "modelDetalleConsultaData",
                    sortOnLoad: true,
                    proxy: {
                        type: 'ajax',
                        reader: {
                            type: 'json',
                            rootProperty: "data"
                        }
                    },
                    autoLoad: false
                });

                /*GRID*/
                var newGrid = Ext.create('Ext.grid.Panel', {
                    id:"grillaDetalleConsultaData",
                    title: 'Detalle',
                    header : false,
                    store : "storeDetalleConsultaData"
                });

                Ext.getCmp(panelParent).add(newGrid);


                /*CREA COLUMNAS DINAMICAMENTES*/
                var basePos = 1;
                var columnsAno = new Array();
                for(var i=0; i<data.columns.length ; i++) {


                    var array = new Array();

                    var dataIndexStr = (data.columns[i].name).toLowerCase();
                    var localRenderer = null;
                    var width = 100;
                    var text =  data.columns[i].name;

                    var columnMes =  { 	  xtype: 'gridcolumn',
                                      menuDisabled: true,
                                      dataIndex: data.columns[i].name,
                                      text: text ,
                                      width: width
                                     };


                    columnsAno.push(columnMes);
                    basePos++;
                }

                Ext.getCmp("grillaDetalleConsultaData").headerCt.insert(basePos, columnsAno);

                /*RELOAD DATA*/
                for(var i=0; i<data.data.length ; i++) {
                    console.log(data.data[i]);

                    store.add(data.data[i]);
                }

    },

    replaceAll: function(str, find, toReplace) {
                if(str != null) {
                    while(str.indexOf(find) != -1) {
                        str.replaceAll(find, toReplace);
                    }
                }

                return str;
    },

    msgError: function(data) {
        var mensg='<p>Ha ocurido un error al generar su solicitud.</p> ';
        mensg=mensg+'<p>Por favor int\u00e9ntelo nuevamente.</p> ';

        if(data != null) {
            if(data.message != null) {
                mensg=mensg+'<p>'+data.message+'</p> ';
            }
        }


        Ext.Msg.show({
            title: 'Error inesperado',
            msg: mensg,
            buttons: Ext.Msg.OK,
            icon: Ext.Msg.ERROR
        });
    }

});