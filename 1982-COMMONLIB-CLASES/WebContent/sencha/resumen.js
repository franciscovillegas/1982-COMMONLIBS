Ext.Loader.setConfig({
	enabled: true,
	paths	: {
		Ext	: "../sencha/extjs"
	},
});

Ext.Loader.setPath('Ext.ux', '../sencha/extjs/examples/ux');

Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.util.*',
    'Ext.Img',
    'Ext.tip.QuickTipManager',
    'Ext.ux.LiveSearchGridPanel',
    'Ext.toolbar.Paging',
    'Ext.ux.PreviewPlugin',
    'Ext.ModelManager',
    'Ext.form.*'
]);


Ext.onReady(function() {

Ext.tip.QuickTipManager.init();	

function showUsers(n,c,e) {
	
	
	//console.log("nombre: " + n, "cargo: " + c,"empresa: " + e);
	Ext.QuickTips.init();
     
    function change(val){
        return '<span style="color:green;">' + val + '</span>';
    }

    function pctChange(val){
       return '<span style="color:green;">' + val + '</span>';
    }     
    

    Ext.define('Usuario',{
    	extend: 'Ext.data.Model',
    	
    	fields: [
    		{name: 'rut', type: 'int'},
    		{name: 'digito_ver',      type: 'varchar'},
    		{name: 'password_usuario',     type: 'varchar'},
    		{name: 'nombre',  type: 'varchar'},
    		{name: 'empresa', type: 'varchar'},
    		{name: 'cargo', type: 'varchar'}
    	],
    	
    	idProperty: 'rut'
    		
    });
    
    var itemsporpagina = 200;
    
    var store = Ext.create('Ext.data.Store', {
    	id:'UsuarioStore',
    	model: 'Usuario',
    	pageSize: itemsporpagina,	
    	proxy: {
    		type: 'ajax',
    		url: '../servlet/EjeCore?claseweb=portal.com.eje.serhumano.tracking.TrackingListas&pageLoad=users&nombre=' + n + '&cargo=' + c + '&empresa=' + e, //servlet Tracking
    		reader: {
    			type: 'json',
				root: 'root',
				totalProperty: 'totalCount'
    			},
    	},
    	listeners: {
    		load : function (store, records, success, operation, options) {
    		//console.log ("1: " + store + " 2: " + records + " 3: " + success + " 4: " + operation +  " 5: " + options)
    			if(records==''){
		    		Ext.MessageBox.show({
			            title: 'Lo sentimos...',
			            msg: 'La Búsqueda no obtuvo ninguna coincidencia, favor intentelo denuevo',
			            icon: Ext.MessageBox.ERROR,
			            buttons: Ext.Msg.OK
			        });
		    	}
		  	}, 
    	},
    	autoLoad: false,

    });
   
	store.load({
	    params: {
	        start: 0,          
	        limit: itemsporpagina,
	    }
	});
	//console.log(store);
    
    Ext.create('Ext.ux.LiveSearchGridPanel', {
    	id: 'miPanel',
	    bodyPadding: 10,
	    bodyBorder: true,
        store: store,
        columnLines: true,
        forceFit : false ,
       	maximized : true,
        columns: [
            {
                text     : 'RUT',
                width    : 100, 
                sortable : false,
                dataIndex: 'rut',
	            editor: {
	                // defaults to textfield if no xtype is supplied
	                allowBlank: false
	            }
            },
            {
                text     : 'DV', 
                width    : 50, 
                sortable : true, 
                dataIndex: 'digito_ver',
	            editor: {
	                // defaults to textfield if no xtype is supplied
	                allowBlank: false
	            }
            },
            {
                text     : 'Contraseña', 
                width    : 100, 
                sortable : true, 
                dataIndex: 'password_usuario',
                renderer: change
            },
            {
                text     : 'Nombre', 
                width    : 300, 
                sortable : true, 
                dataIndex: 'nombre',
                renderer: pctChange
            },
            {
                text     : 'Empresa', 
                width    : 150, 
                sortable : true, 
                dataIndex: 'empresa'
            },
            {
                text     : 'Cargos', 
                width    : 170, 
                sortable : true, 
                dataIndex: 'cargo'
            }
        ],

        dockedItems: [{
            xtype: 'pagingtoolbar',
            store: store,   // un simple pagingToolBar. Se trata por el lado del servidor
            dock: 'bottom',
            pageSize: itemsporpagina,    
            displayInfo: true
        }],
        height: 560,
        width: 890,
        title: 'Lista de Usuarios',
        renderTo: 'userList', // a que?
        viewConfig: {
            stripeRows: true
        }
    
    });
 }
 	var empresas = Ext.create('Ext.data.Store', {
	    id:'factoryStore',
	    fields: ['id', 'empresa'],
		proxy: {
			type: 'ajax',
			url: '../servlet/EjeCore?claseweb=portal.com.eje.serhumano.tracking.TrackingListas&pageLoad=empresa', //servlet Tracking
			reader: {
				type: 'json',
			},
		},
		autoLoad: true,
	});

 	var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
        clicksToMoveEditor: 1,
        autoCancel: false
    })
    
	var formPanel = Ext.create('Ext.form.Panel', {
	    frame: true,
	    title: 'Seleccione el filtro de personas que quiere realizar',
	    width: 850,
	    height: 550,
	    bodyPadding: 10,
	    fieldDefaults: {
	        labelAlign: 'center',
	        labelWidth: 150,
	        anchor: '100%',
			msgTarget: 'none',
		    invalidCls: ''
	    },
	    
	    	
	    /*
	     * VALIDACIONES FUNCIONANDO / NO ELIMINAR
		listeners: {
		    fieldvaliditychange: function() {
		        this.updateErrorState();
		    },
		    fielderrorchange: function() {
		        this.updateErrorState();
		    }
		},
		
		updateErrorState: function() {
		    var yo = this,
		        errorCmp, fields, errors;
		
		    if (yo.hasBeenDirty || yo.getForm().isDirty()) { //prevents showing global error when form first loads
		        errorCmp = yo.down('#formErrorState');
		        fields = yo.getForm().getFields();
		        errors = [];
		        fields.each(function(field) {
		            Ext.Array.forEach(field.getErrors(), function(error) {
		                errors.push({name: field.getFieldLabel(), error: error});
		            });
		        });
		        errorCmp.setErrors(errors);
		        yo.hasBeenDirty = true;
		    }
		},*/
		buttons: [{
			scale: 'large',
			text: 'Limpiar',
	        handler: function() {
	            this.up('form').getForm().reset();
	        }
		}, {
			cls: 'contactBtn',
	        scale: 'large',
	        text: 'Filtrar Búsqueda',
	        handler: function(){
				var form = this.up('form').getForm();
		    	nombre = form.findField('nombre');
		    	cargo = form.findField('cargo');
		    	empresa = form.findField('empresa');
				if (form.isValid()) {
					console.log("valido");
				}
		    	if(typeof nombre.value === 'undefined' || typeof nombre.value === 'undefined'){
		    		showUsers("", "",  empresa.getRawValue());
		    	}
		    	else{
			        showUsers(nombre.value, cargo.value,  empresa.getRawValue());
		        }
        	},
		},
		{
		    cls: 'contactBtn',
		    scale: 'large',
		    text: 'Mostrar Lista Completa',
		    handler: function(){
				showUsers("","","");
		    },
		}],
		/*
		 * VALIDACIONES FUNCIONADO *
		 * NO ELIMINAR *
		 * LABEL EN EL FONDO CON LA VALIDACION Y HBOX CON MENSAJES PERZONALIZADOS *
		 * 
		 * 
		dockedItems: [{
            xtype: 'container',
            dock: 'bottom',
            layout: {
                type: 'hbox',
                align: 'top',
            },
            padding: '10, 10, 5',

 			items: [{
				xtype: 'component',
				id: 'formErrorState',
				baseCls: 'form-error-state',
				flex: 1,
				validText: '',
				invalidText: 'Favor revisar datos ingresados',
				tipTpl: Ext.create('Ext.XTemplate', '<ul><tpl for="."><li><span class="field-name">{name}</span>: <span class="error">{error}</span></li></tpl></ul>'),
				
				getTip: function() {
				    var tip = this.tip;
				    if (!tip) {
				        tip = this.tip = Ext.widget('tooltip', {
				            target: this.el,
				            title: 'Detalles :',
				            autoHide: false,
				            anchor: 'top',
				            mouseOffset: [-11, -2],
				            closable: true,
				            constrainPosition: false,
				            cls: 'errors-tip'
				        });
				        tip.show();
				    }
				    return tip;
				},
				
				setErrors: function(errors) {
				    var yo = this,
				        baseCls = yo.baseCls,
				        tip = yo.getTip();
				
				    errors = Ext.Array.from(errors);
				
				    // Update CSS class and tooltip content
				    if (errors.length) {
				        yo.addCls(baseCls + '-invalid');
				        yo.removeCls(baseCls + '-valid');
				        yo.update(yo.invalidText);
				        tip.setDisabled(false);
				        tip.update(yo.tipTpl.apply(errors));
				    } else {
				        yo.addCls(baseCls + '-valid');
				        yo.removeCls(baseCls + '-invalid');
				        yo.update(yo.validText);
				        tip.setDisabled(true);
				        tip.hide();
				    }
				},
			}],

        }],
		*/
	    items: [
	    
	    {
	    	xtype: 'combobox',
	    	fieldLabel: 'Seleccione la Empresa',
	        store: empresas,
	        name: 'empresa',
	        queryMode: 'local',
	        valueField: 'id',
	        //renderTo: formPanel,
	        tpl: Ext.create('Ext.XTemplate', '<tpl for=".">', '<div class="x-boundlist-item">{empresa}</div>', '</tpl>'),
	        displayTpl: Ext.create('Ext.XTemplate', '<tpl for=".">', '{empresa}', '</tpl>')
	    },
	    {
	    	xtype: 'textfield',
	    	name: 'nombre',
	    	fieldLabel: 'Nombre de la persona',
	    	allowBlank: false
	    },
	    {
	    	xtype: 'textfield',
	    	name: 'cargo',
	    	fieldLabel: 'Cargo',
	    	allowBlank: false
	    },
	    
	    ],
	});  
	
	
	var form = formPanel.getForm();
	
	formPanel.render('formUsers');
    
});