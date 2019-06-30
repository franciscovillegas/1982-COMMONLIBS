	/* DEPRECADO */

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
	function showGrafico(gtype,path,idContainer) {
		
		
		loadJSONforGraficos("../../../servlet/EjeCoreI?claseweb=highchart.SGraphs&gtype="+gtype+"&path="+path,
				"get",
				{},	
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
					});	
	}

	
	function showGraficoPostSeguro(gtype,path,idContainer, params) {
		if(params == null) {
			params={};
		}
		
		params["claseweb"] = "highchart.SGraphsSeguro";
		params["gtype"] = gtype;
		params["path"] = path;
 
		$("#"+idContainer).html("<img src=\"../../images/progress_bar.gif\"  />");
		
		$.ajax({
	        url: '../../../servlet/EjeCore',
	        data: params ,
			type: 'POST',
			dataType: "html",
	        success: function (dataCall) {
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
	        },
	        error: function (e) {
	             console.log(e);
	        }
	    }); 
		
	}

	function HighChart(idContainer) {
		this.idContainer = idContainer;
		this.chart = null;

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
		                    return '<b>'+ this.series.name +', Dimensi�n: '+ this.point.category +'</b><br/>'+
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
	
}