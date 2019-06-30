

function loadDirectorio() {
		loadJSONforMain('../servlet/EjeCore?claseweb=portal.com.eje.serhumano.directorio.data.Trabajadores',
				'GET',{},'html',
				function(data){
					$("#contenido").html(data);
					showTableAccesos();
				});
	}
	

	var dTable = "";
	function showTableAccesos() {
		
		$("#divTablaTrabajadoresUnidad").html("<br><br><table cellpadding='0' cellspacing='0' border='0' class='display' id='divTablaConetenedorTrabajadoresUnidad' style='width: 100%;'></table>");
		
		
		dTable = $('#divTablaConetenedorTrabajadoresUnidad').dataTable( {
//			"sDom": 'T<"clear">lfrtip',		/* <-- Contenedor para exportar resultados*/
		    "bProcessing": true,			/* <-- Mensaje que indica "Procesando"*/
		    "sAjaxSource": "../servlet/EjeCore?claseweb=portal.com.eje.serhumano.directorio.data.Trabajadores&accion=select&thing=trabajadores",
			"aoColumns": [
				{ "sTitle": "Rut" 		,  "bVisible": false , "sWidth": "45px" , "bSearchable": true , "bSortable": true},
				{ "sTitle": "Nombre" 	,  "bVisible": true , "sWidth": "420px"},
				{ "sTitle": "Cargo"		,  "bVisible": true , "sWidth": "220px"},
				{ "sTitle": "Unidad"	,  "bVisible": true , "sWidth": "220px"},
				{ "sTitle": "Acci&oacute;n"	  ,  "bVisible": true , "mData": null , "fnRender": function(obj) {
																											var sReturn =  "<img src='../servlet/Tool?images/carpelect/business-info24.png' class='imgBoton' title='Ver Ficha' id='"+ obj.aData[0] +"' lang='ficha' />";
																											return sReturn;
																										}}
						]
				});
			
	}
		
	$(document).ready(function() {
		$('.imgBoton').live("click", function(){

			var rut = $(this).attr("id");
			var $row = $(this).parents('tr');	        	
			if("ficha" == $(this).attr("lang")) {
	    		cargarFicha($row, rut);
	    	}
	    	
	    });
	});

	 
function cargarFicha($row, rut){

 	loadJSONforMain('../servlet/FichaPersonaDirectorio', 'GET',
				{"rut":rut, 
				"htm":"directorio/infoTrabajador.htm"},
				'html',
				function(data){
					$("#fade").show();
					$("#divFicha").show();
					$("#divFicha").html(data);
				});
}

function cerrarModal(){
	$("#fade").hide();
	$("#divFicha").hide();
}
	 
 function showCarpetaPersonal(rut) {
		loadJSONforMain('../servlet/S_PerfilLaboralInformeGestion?rut_org='+rut,
						'GET',{},'html',
							function(data){
								$("#contenidoCarpeta").html(data);
							}); 		
		
	}


