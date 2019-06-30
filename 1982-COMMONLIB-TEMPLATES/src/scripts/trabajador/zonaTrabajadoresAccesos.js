function loadMantenedorPersonas() {
		loadJSONforMain('../servlet/EjeCore?claseweb=portal.com.eje.carpelect.data.Trabajadores',
				'GET',{},'html',
				function(data){
					$("#contenido").html(data);
					showTableAccesos();
				});
	}
	

	var dTable = "";
	function showTableAccesos() {
		$("#divTablaTrabajadoresUnidad").html("<table cellpadding='0' cellspacing='0' border='0' class='display' id='divTablaConetenedorTrabajadoresUnidad' style='width: 100%;'></table>");
		
		
		dTable = $('#divTablaConetenedorTrabajadoresUnidad').dataTable( {
//			"sDom": 'T<"clear">lfrtip',		/* <-- Contenedor para exportar resultados*/
		    "bProcessing": true,			/* <-- Mensaje que indica "Procesando"*/
		    "sAjaxSource": "../servlet/EjeCore?claseweb=portal.com.eje.carpelect.data.Trabajadores&accion=select&thing=trabajadores",
			"aoColumns": [
				{ "sTitle": "Rut" 		  ,  "bVisible": true , "sWidth": "45px" , "bSearchable": true , "bSortable": true},
				{ "sTitle": "Dv"		  ,  "bVisible": true , "sWidth": "20px" },
				{ "sTitle": "Nombres" 	  ,  "bVisible": true , "sWidth": "220px"},
				{ "sTitle": "Ape. Paterno",  "bVisible": true , "sWidth": "120px"},
				{ "sTitle": "Ape. Materno",  "bVisible": true , "sWidth": "120px"},
				{ "sTitle": "Fec. Ingreso",  "bVisible": true , "sWidth": "120px"},
				{ "sTitle": "Acci&oacute;n"	  ,  "bVisible": true , "mData": null , "fnRender": function(obj) {
																											var sReturn =  "<img src='../servlet/Tool?images/carpelect/business-info24.png'    class='imgBoton' title='Ver Carpeta Electr&oacute;nica'	lang='carpeta' />";
																											return sReturn;
																										}}
						]
				});
			
	}
		
	 $(document).ready(function() {
			$('.imgBoton').live("click", function(){
				
	        	var $row = $(this).parents('tr');	        	
	        	if("carpeta" == $(this).attr("lang")) {
	        		cargarCarpetaPersona($row);
	        	}
	        	
	        });

			 $('select[name=divTablaConetenedorTrabajadoresUnidad_length]').live({
				 change: function() {
					 window.parent.parent.redimencionaMain(window.top.document);	  
			 		    }
			 	});
	    });

	 



	 
	 function cargarCarpetaPersona($row){
	 		var rut 	 = $row.find('td:eq(0)').html();
	 		 
			loadJSONforMain('../servlet/ExpedienteCarpetaElectronica','GET',{"rut":rut},'html',
					function(data){
						 
						$("#contenido").html(data);
					});
	 		
	 }

/* ==========================================================================*/
	 
function showCarpetaPersonal(rut) {
	loadJSONforMain('../servlet/S_PerfilLaboralInformeGestion?rut_org='+rut,
					'GET',{},'html',
						function(data){
							$("#contenidoCarpeta").html(data);
						}); 		
	
}

function Datos(rut,div) {
	if(div==null) {
		div="contenidoCarpeta";
	}
	loadJSONforMain('../servlet/MuestraInfoRutCarpetaElectronica',
					'GET',{"rut":rut},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}

function Remu(rut,div) {
	if(div==null) {
		div="contenidoCarpeta";
	}
	loadJSONforMain('../servlet/InitCertifCarpetaElectronica',
					'GET',{"rut_org":rut},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}

function Prev(rut,div) {
	if(div==null) {
		div="contenidoCarpeta";
	}
	loadJSONforMain('../servlet/DatosPrevCarpetaElectronica',
					'GET',{"rut":rut},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}

function Vaca(rut,div) {
	if(div==null) {
		div="contenidoCarpeta";
	}
	loadJSONforMain('../servlet/CartolaVacaCarpetaElectronica',
					'GET',{"rut":rut},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}


function Lice(rut,div) {
	loadJSONforMain('../servlet/LicenciasMedicasCarpetaElectronica',
					'GET',{"rut":rut},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}

function Perf(rut,div) {
	if(div==null) {
		div="contenidoCarpeta";
	}
	loadJSONforMain('../servlet/S_PerfilLaboralInformeGestion?rut_org='+rut,
					'GET',{},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}

function Grupo(rut,div) {
	if(div==null) {
		div="contenidoCarpeta";
	}
	loadJSONforMain('../servlet/GrupoFamiliarOrga?rut='+rut,
					'GET',{},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}

function Contra(rut,div) {
	if(div==null) {
		div="contenidoCarpeta";
	}
	loadJSONforMain('../servlet/DatosContrato?rut='+rut,
					'GET',{},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}

function Expe(rut,div) {
	if(div==null) {
		div="contenidoCarpeta";
	}
	loadJSONforMain('../servlet/ExperienciaLaboral?rut='+rut,
					'GET',{},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}

function Forma(rut,div) {
	if(div==null) {
		div="contenidoCarpeta";
	}
	loadJSONforMain('../servlet/Formacion2?rut='+rut,
					'GET',{},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}
	
function Capa(rut,div) {
	if(div==null) {
		div="contenidoCarpeta";
	}
	loadJSONforMain('../servlet/Frame_Capacita?rut='+rut,
					'GET',{},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}

function Descargables(rut,div) {
	if(div==null) {
		div="contenidoCarpeta";
	}
	loadJSONforMain('../servlet/ViewFileRepositoryServlet?rut='+rut,
					'GET',{},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}

function DescCargo(rut,div,altUrl) {
	if(div==null) {
		div="contenidoCarpeta";
	}
	if(altUrl==null) {
		altUrl="../servlet/ViewFileRepositoryServlet";
	}
	loadJSONforMain(altUrl+'?rut='+rut,
					'GET',{},'html',
						function(data){
							$("#"+div).html(data);
						}); 		
	
}

	function AbrirBGaleria() {
		//var url2 = "/servlet/JerarquiaOrg?cual=2";
		var url2 = "Tool?htm=Gestion/BusquedaPorFiltros/Busqueda.htm";
		ventmsg2 = open(url2, "Galeria", "width=720,height=428,left=50,top=110");
		//ventmsg2.focus();
	}










