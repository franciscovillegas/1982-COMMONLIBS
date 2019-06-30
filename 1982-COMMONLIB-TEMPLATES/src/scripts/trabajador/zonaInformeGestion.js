var nodoArbol = null;

function loadTreeZone(tipoOrganica) {
		loadJSONforMain('../servlet/Tool?htm=informegestion/contenedorInformes.html',
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
																nodoArbol = node;
							        							selectedNode(node, tipoOrganica);
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

	

	function selectedNode(node, tipoOrganica) {
		
		
		loadJSONforMain('../servlet/EjeCore?claseweb=portal.com.eje.carpelect.mantenedor.Organica&accion=select&thing=reportesGestion',
				'GET',{"id":node.id,"name":node.name},'html',
				function(data){

					$("#TFmodZone").html(data);
						if (tipoRep == "gestDiaria"){
							showMenuReportes(node.id, "GestionDiariaInformeGestion", tipoOrganica);	
						} else if (tipoRep == "mis") {
							showMenuReportes(node.id, "GestionReportesInformeGestion", tipoOrganica);							
						}
						
				});

	}
	
	
	function showMenuReportes(unidad, reporte, tipoOrganica) {
		loadJSONforMain('../servlet/'+reporte,
						'GET',{"unidad":unidad, "tipoOrganica":tipoOrganica},'html',
							function(data){
								$("#divReportesGestion").html(data);
							});
	}
	


	

