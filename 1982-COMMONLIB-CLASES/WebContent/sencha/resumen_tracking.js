Ext.Loader.setConfig({
	enabled: true,
	paths	: {
		Ext	: "/ist/portalrrhh/mapping/sencha"
	},
});

Ext.Loader.setPath('Ext.ux', '/ist/portalrrhh/mapping/sencha/examples/ux');

Ext.require([
'Ext.chart.*',
'Ext.Window', 
'Ext.fx.target.Sprite', 
'Ext.layout.container.Fit', 
'Ext.window.MessageBox'
]);

 
Ext.onReady(function () {

	function showChart(a,b) {
		//console.log(a);
		Ext.define('ModelPortalTracking',{
			extend: 'Ext.data.Model',
			
			fields: [
				{name: 'name'	, type: 'varchar'},
				{name: 'data1'	, type: 'int'}
			],
			
			idProperty: 'name'
				
		});
		
	 
		Ext.define('StorePortalTracking', {
			extend: 'Ext.data.Store',
			model: 'ModelPortalTracking',
			proxy: {
				type: 'ajax',
				url: 'http://localhost:8080/ist/portalrrhh/servlet/EjeCoreI?claseweb=portal.com.eje.serhumano.tracking.CWebSencha&fecha_inicio=' + a + '&fecha_termino=' + b, //servlet Tracking
				
				reader: {
					type: 'json', //salida JSON
				}
			},
			
			autoLoad: true,
		});
	
	
		var StorePortalTracking = Ext.create('StorePortalTracking');
		
	
		//LABELS
	    Ext.define('Ext.chart.theme.CustomBlue', {
	        extend: 'Ext.chart.theme.Base',
	        
	        constructor: function(config) {
	            var titleLabel = {
	                font: 'bold 18px Arial'
	            }, axisLabel = {
	                fill: 'rgb(8,69,148)',
	                font: '12px Arial',
	                spacing: 2,
	                padding: 5
	            };
	            
	            this.callParent([Ext.apply({
	               axis: {
	                   stroke: '#084594'
	               },
	               axisLabelLeft: axisLabel,
	               axisLabelBottom: axisLabel,
	               axisTitleLeft: titleLabel,
	               axisTitleBottom: titleLabel
	           }, config)]);
	        }
	    });
	    
	    var chart = Ext.create('Ext.chart.Chart', {
	        animate: true,
	        shadow: true,
	        store: StorePortalTracking,
	        axes: [{
	            type: 'Numeric',
	            position: 'bottom',
	            fields: ['data1'],
	            label: {
	                renderer: Ext.util.Format.numberRenderer('0,0')
	            },
	            title: 'Numero de Entradas',
	            grid: true,
	            minimum: 0
	        }, {
	            type: 'Category',
	            position: 'left',
	            fields: ['name'],
	            title: 'Sección del Portal'
	        }],
	        theme: 'CustomBlue',
	        background: {
	            gradient: {
	                id: 'backgroundGradient',
	                angle: 45,
	                stops: {
	                    0: {
	                        color: '#ffffff'
	                    },
	                    100: {
	                        color: '#eaf1f8'
	                    }
	                }
	            }
	        },
	        series: [{
	            type: 'bar',
	            axis: 'bottom',
	            highlight: true,
	            tips: {
	                trackMouse: true,
	                renderer: function(storeItem, item) {
	                    this.setTitle(storeItem.get('name') + ': ' + storeItem.get('data1') + ' visitas');
	                }
	            },
	            label: {
	              display: 'insideEnd',
	                  field: 'data1',
	                  renderer: Ext.util.Format.numberRenderer('0'),
	                  orientation: 'horizontal',
	                  color: '#333',
	                'text-anchor': 'middle'
	            },
	            xField: 'name',
	            yField: ['data1']
	        }]
	    });
	 
    
    // CREAR UNA VENTANA
    var win = Ext.create('Ext.window.Window', {
        width: 900,
        height: 750,
        minHeight: 400,
        minWidth: 550,
        hidden: false,
        maximized: true,
        title: 'Totalizador de la búsqueda',
        autoShow: true,
        layout: 'fit',
        tbar: [{
            text: 'Guardar Gráfico',
            handler: function() {
                Ext.MessageBox.confirm('Confirmar Descarga', 'Le gustaría descargar el gráfico como una imagen?', function(choice){
                    if(choice == 'yes'){
                    	//Ext.draw.engine.SvgExporter.self.generate(null, chart.surface)
                        chart.save({
                            type: 'image/png'
                        });
                    }
                });
            }
        }],
        items: chart
    });
   }
	
	var formPanel = Ext.create('Ext.form.Panel', {
	    frame: true,
	    title: 'Seleccione las Fechas a buscar',
	    width: 340,
	    bodyPadding: 5,
	    
	    fieldDefaults: {
	        labelAlign: 'center',
	        labelWidth: 90,
	        anchor: '100%'
	    },
	
	    items: [{
	        xtype: 'datefield',
	        name: 'fecha_inicio',
	        fieldLabel: 'Desde: ',
	        dateFormat: 'm-d-Y',
	        allowBlank: false,
	    allowEdit: false
	    },
	    {
	        xtype: 'datefield',
	        name: 'fecha_termino',
	        fieldLabel: 'Hasta: ',
	        dateFormat: 'm-d-Y',
	        allowBlank: false,
	    allowEdit: false
	    },
	    {
	        xtype: 'button',
	        cls: 'contactBtn',
	        scale: 'large',
	        text: 'Buscar',
	        handler: function(){
	    		fi = form.findField('fecha_inicio');
	    		ft = form.findField('fecha_termino');
	            showChart(Ext.Date.format(fi.value, 'ymd'),Ext.Date.format(ft.value, 'ymd'));
	                },
	    }
	    ],
	});  
	
	var form = formPanel.getForm();

	formPanel.render('formFecha');
});
