	function loadJSONforGraficos(url, paramType, params, func) {

		$.ajax({
			  url: url,
			  data: params,
			  type: paramType,
			  success: eval (func),
			  dataType: 'html'
			});
	}
	/*
		gtype='Serie'
	    gtype='NegativeBars'
	    gtype='StackedBar'
	    gtype='DualAxesLineAndColumn'
	*/
	function showGrafico(gtype,path,idContainer,params) {
		
		loadJSONforGraficos("../servlet/EjeCoreI?claseweb=highchart.SGraphs&gtype="+gtype+"&path="+path,
				"get",
				params,	
					function(dataCall) {
						data = JSON.parse(dataCall);
						var hChart = new HighChart(idContainer);
						
						if("Serie" == gtype) {
							hChart.loadSerieGraph(data.titulo,data.subtitulo,data.xLabel,data.yLabel,data.series);
						}
						else if("NegativeBars"  == gtype) {
							hChart.loadDistributionChar(data.titulo,data.subtitulo,data.categorias,data.series,data.absoluteRange);
						}
						else if("StackedBar"  == gtype) {
							hChart.loadStackedBarChar(data.titulo, data.subtitulo, data.categorias, data.xLabel, data.series);
						}
						else if ("DualAxesLineAndColumn" == gtype ){
							hChart.loadDualAxesLineAndColumn(data.titulo, data.subtitulo, data.categorias, data.yLabel, data.yLabelSecundario,data.xLabel, data.series);
						}
						else if ("LineTime" == gtype ){
							hChart.loadLineTimeGraph( data.titulo ,  data.yLabel , data.series ); 
						}
						else if ("SpiderWeb" == gtype ){
							hChart.loadSpiderWeb( data.titulo , data.subtitulo, data.categorias, data.series ); 
						}
					});	
	}


	function HighChart(idContainer) {
		this.idContainer = idContainer;
		this.chart = null;

		HighChart.prototype.loadLineTimeGraph = function (paramtitle , paramyLabel , paramseries  ) {
	        chart = new Highcharts.Chart({

	            chart: {
	            	renderTo: this.idContainer,
	                zoomType: 'x',
	                spacingRight: 20
	            },
	            title: {
	                text: paramtitle
	            },
	            subtitle: {
	                text: document.ontouchstart === undefined ?
	                    'Click and drag in the plot area to zoom in' :
	                    'Drag your finger over the plot to zoom in'
	            },
	            xAxis: {
	                type: 'datetime',
	                maxZoom: 14 * 24 * 3600000, // fourteen days
	                title: {
	                    text: paramyLabel
	                }
	            },
	            yAxis: {
	                title: {
	                    text: "Fechas"
	                }
	            },
	            tooltip: {
	                shared: true
	            },
	            legend: {
	                enabled: false
	            },
	            plotOptions: {
	                area: {
	                    fillColor: {
	                        linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
	                        stops: [
	                            [0, Highcharts.getOptions().colors[0]],
	                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
	                        ]
	                    },
	                    lineWidth: 1,
	                    marker: {
	                        enabled: false
	                    },
	                    shadow: false,
	                    states: {
	                        hover: {
	                            lineWidth: 1
	                        }
	                    },
	                    threshold: null
	                }
	            },
	            series: paramseries
	        });
		}
		
		HighChart.prototype.loadSerieGraph = function (paramtitle , paramsubtitle, paramxAxis, paramyLabel , paramseries ) {
			        chart = new Highcharts.Chart({
			            chart: {
			                renderTo: this.idContainer,
			                type: 'spline',
			                marginRight: 130
			            },
			            title: {
			                text: paramtitle ,
			                x: -20 //center
			            },
			            subtitle: {
			                text: paramsubtitle ,
			                x: -20,
			                marginTop: 20
			            },
			            xAxis: {
			                categories: paramxAxis
			            },
			            yAxis: {
			                title: {
			                    text: paramyLabel
			                },
			                plotLines: [{
			                    value: 0,
			                    width: 1,
			                    color: '#808080'
			                }]
			            },
			            tooltip: {
			                formatter: function() {
			                        return '<b>'+ this.series.name +'</b><br/>'+
			                        this.x +': '+ this.y ;
			                }
			            },
			            legend: {
			                layout: 'vertical',
			                align: 'right',
			                verticalAlign: 'top',
			                x: -10,
			                y: 100,
			                borderWidth: 0
			            },
			            series: paramseries
			        });
		}
	
	
		HighChart.prototype.loadDistributionChar = function (paramtitle , paramsubtitle, paramcategories, paramseries, absoluteRange) {
			 chart = new Highcharts.Chart({
		            chart: {
		                renderTo: this.idContainer,
		                type: 'bar'
		            },
		            title: {
		                text: paramtitle
		            },
		            subtitle: {
		                text: paramsubtitle
		            },
		            xAxis: [{
		                categories: paramcategories,
		                reversed: false
		            }, { // mirror axis on right side
		                opposite: true,
		                reversed: false,
		                categories: paramcategories,
		                linkedTo: 0
		            }],
		            yAxis: {
		                title: {
		                    text: null
		                },
		                labels: {
		                    formatter: function(){
		                        return  this.value;
		                    }
		                },
		                min: -1 * absoluteRange,
		                max: absoluteRange
		            },
		    
		            plotOptions: {
		                series: {
		                    stacking: 'normal'
		                }
		            },
		    
		            tooltip: {
		                formatter: function(){
		                    return '<b>'+ this.series.name +', Dimensión: '+ this.point.category +'</b><br/>'+
		                        'Puntaje: '+ Highcharts.numberFormat(Math.abs(this.point.y), 0);
		                }
		            },
		    
		            series: paramseries
		        });
		}
		
		HighChart.prototype.loadStackedBarChar = function (paramtitle , paramsubtitle, paramcategories, paramyAxis, paramseries) {
				chart = new Highcharts.Chart({
		            chart: {
		                renderTo: this.idContainer,
		                type: 'bar'
		            },
		            title: {
		                text: paramtitle
		            },
		            subtitle: {
		                text: paramsubtitle
		            },
		            xAxis: {
		                categories: paramcategories
		            },
		            yAxis: {
		                min: 0,
		                title: {
		                    text: paramyAxis
		                }
		            },
		            legend: {
		                backgroundColor: '#FFFFFF',
		            },
		            tooltip: {
		                formatter: function() {
		                    return ''+
		                        this.series.name +': '+ this.y +'';
		                }
		            },
		            plotOptions: {
		                series: {
		                    stacking: 'normal'
		                }
		            },
		                series: paramseries
		        });
		}
		
		
		HighChart.prototype.loadDualAxesLineAndColumn = function (paramtitle , paramsubtitle, paramcategories, paramyLabel, paramyLabelOpuesto, paramxLabel, paramseries) {
			chart = new Highcharts.Chart({
	            chart: {
	                renderTo: this.idContainer,
	                zoomType: 'xy'
	            },
	            title: {
	                text: paramtitle
	            },
	            subtitle: {
	                text: paramsubtitle
	            },
	            xAxis: [{
	                categories: paramcategories
	            }],
	            yAxis:[{ // Primary yAxis
	                labels: {
	                    formatter: function() {
	                        return this.value +'&deg;C';
	                    },
	                    style: {
	                        color: '#89A54E'
	                    }
	                },
	                title: {
	                    text: 'Temperature',
	                    style: {
	                        color: '#89A54E'
	                    }
	                }
	            }, { // Secondary yAxis
	                title: {
	                    text: 'Rainfall',
	                    style: {
	                        color: '#4572A7'
	                    }
	                },
	                labels: {
	                    formatter: function() {
	                        return this.value +' mm';
	                    },
	                    style: {
	                        color: '#4572A7'
	                    }
	                },
	                opposite: true
	            }],
	            tooltip: {
	                formatter: function() {
	                        return '<b>'+ this.series.name +'</b><br/>'+
	                        this.x +': '+ this.y + "%";
	                }
	            },
	            legend: {
	                backgroundColor: '#FFFFFF'
	            },
	            series:paramseries
	        });
		}
		
		
		HighChart.prototype.loadSpiderWeb = function (paramtitle , paramsubtitle, paramcategories, paramseries) {
			chart = new Highcharts.Chart({
				 
			    chart: {
			    	renderTo: this.idContainer,
			        polar: true,
			        type: 'line'
			    },
			    
			    title: {
			        text: paramtitle,
			        x: -80
			    },
			    
	            subtitle: {
	                text: paramsubtitle
	            },
	            
			    pane: {
			    	size: '80%'
			    },
			    
			    xAxis: {
			        categories: paramcategories,
			        tickmarkPlacement: 'on',
			        lineWidth: 0
			    },
			        
			    yAxis: {
			        gridLineInterpolation: 'polygon',
			        lineWidth: 0,
			        min: 0
			    },
			    
			    tooltip: {
			    	shared: true,
			        pointFormat: '<span style="color:{series.color}">{series.name}: <b>{point.y:,.0f}</b><br/>'
			    },
			    
			    legend: {
			        align: 'right',
			        verticalAlign: 'middle',
			        y: 70,
			        layout: 'vertical'
			    },
			    
			    series:paramseries
	        });
		}
	
}