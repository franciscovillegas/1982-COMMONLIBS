
function loadTreeZone(tipoOrganica) {
		loadJSONforMain('../servlet/Tool?htm=CarpetaElectronica/contenedorTreeHtml.html',
				'GET',{},'html',
				function(data){
					$("#contenido").html(data);
					showTree(tipoOrganica);
				});
	}
	
	
	function showTree(tipoOrganica) {
		$("#tree").html("Cargando arbol...");
		loadJSONforMain('../servlet/EjeCoreI?claseweb=portal.com.eje.tools.jqtree.JqTree&ctree=portal.com.eje.carpelect.jerarquia.TreeOrganicaPerfilada',
				'GET',
				{"tipoOrg":tipoOrganica},
				'html',
				function(dataTree){				
					dataTree = jQuery.parseJSON(dataTree);
					

						$tree = $("#tree").tree({
							  data: dataTree,
							  dragAndDrop: true,
							  autoOpen: 0,
							  selectable: true,
							  saveState: true,
							  onCanSelectNode: function(node) {
							        selectedNode(node);
									return true;
							    }
							});	
						
						window.parent.parent.redimencionaMain(window.document);

						
						$tree.bind(
							    'tree.open',
							    function(e) {
							    	 window.parent.parent.redimencionaMain(window.document);
							       
							    }
							);
						
						
						$tree.bind(
							    'tree.close',
							    function(e) {
							    	 window.parent.parent.redimencionaMain(window.document);
							       
							    }
							);
					
				});
	}

	
	
	function selectedNode(node) {

		loadJSONforMain('../servlet/EjeCore?claseweb=portal.com.eje.carpelect.mantenedor.Organica&accion=select&thing=nodo',
				'GET',{"id":node.id,"name":node.name},'html',
				function(data){

					$("#TFmodZone").html(data);
					showTablaTrabajadoresUnidad(node.id);
				});
	}
	
	var dTableTrabajadoresUnidad = "";
	function showTablaTrabajadoresUnidad(unidId) {
		$("#divTablaTrabajadoresUnidad").html("<table cellpadding='0' cellspacing='0' border='0' class='display' id='divTablaConetenedorTrabajadoresUnidad' style='width: 100%;'></table>");
		
		dTableTrabajadoresUnidad = $('#divTablaConetenedorTrabajadoresUnidad').dataTable( {
		    "bProcessing": true, 
		    "sAjaxSource": "../servlet/EjeCore?claseweb=portal.com.eje.carpelect.data.Trabajadores&accion=select&thing=trabajadoresunidad&unidad="+unidId,
		    "aoColumns": [
				{ "sTitle": "Rut" 		  ,  "bVisible": true , "sWidth": "45px" , "bSearchable": true , "bSortable": true},
				{ "sTitle": "Dv"		  ,  "bVisible": true , "sWidth": "15px"},
				{ "sTitle": "Nombres" 	  ,  "bVisible": false , "sWidth": "0px"},
				{ "sTitle": "Ape. Paterno",  "bVisible": false , "sWidth": "0px"},
				{ "sTitle": "Nombre"	  ,  "bVisible": true , "mData": null 		, "sWidth": "320px" , "fnRender": function(obj) {
																																		var nombre = obj.aData[2]+' '+ obj.aData[3]+' '+ obj.aData[4];
																																		return nombre;
																																	}},
				{ "sTitle": "Accion"	  ,  "bVisible": true , "mData": null , "fnRender": function(obj) {
																												var sReturn =  "<img src='../servlet/Tool?images/carpelect/business-info24.png'    class='imgBoton' title='Ver Carpeta Electronica'	lang='carpeta' />";
																												return sReturn;
																											}}
				
						]
				});
	}



	

