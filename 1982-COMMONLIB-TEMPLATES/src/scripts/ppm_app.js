var intervalFinder = null;
var counterSecondsAfterSubmit= 0;

function loadPPM(contexto, pathRelAdicionales, pageToOpen, pageName){
	
	/*
	 * Una aplicacion tiene 
	 * */
	var isApp =(contexto == "/qs" ||
			   contexto == "/movil" ||
			   contexto == "/portalrrhh" |
			   contexto == "/webmatico" ||
			   contexto == "/ggd" ||
			   contexto == "/metas" ||
			   contexto == "/capacitacion" ||
			   contexto == "/wfv" ||
			   contexto == "/wffacturas" ||
			   contexto == "/wfaumentorenta" ||
			   contexto == "/wfausencialaboral" ||
			   contexto == "/wfcambiocargo" ||
			   contexto == "/wffacturas" ||
			   contexto == "/wfhorasextras" ||
			   contexto == "/wftraslados" ||
			   contexto == "/wfhonorarios" ||
			   contexto == "/wffondosporrendir" ||
			   contexto == "/wfcotizador",
			   contexto == "/webmatico");
	
	var isPath=(contexto == "/eo" || contexto== "/portalrrhh/quicksite" ) ;
	console.log(contexto + " isPath:"+isPath+" isApp:"+isApp);

	if(!isApp && !isPath) {
		if(!isApp) {
			alert("No existe la aplicación o no está registrada:"+contexto);
			return;
		}
		if(!isPath) {
			alert("No existe la ruta o no está registrado:"+contexto);
			return;
		}
	}
	else {
		if(isApp) {
			if(contexto == "/webmatico") {
				loadPPM_app(contexto, pathRelAdicionales,  pageToOpen == null ? "../servlet/Tool?htm=login/v2/index.html?rut=06254398" : pageToOpen, pageName);
			}
			else {
				loadPPM_app(contexto, pathRelAdicionales, pageToOpen == null ? null : pageToOpen, pageName);
			}
		}
		else {
			if(contexto == "/eo") {
				loadPPM_path(contexto,  pageToOpen == null ? "../../../../eo/view/": pageToOpen);
			}
			else {
				loadPPM_path(contexto, pageToOpen == null ? pathRelAdicionales: pageToOpen);	
			}
			
		}
	}
	
}

function loadPPM_path(contexto, pathToOpen ) {
	console.log("loadPPM_path (v1.0.6):"+contexto);
	
	var formName = contexto;
	var action = "";
	
	for(var i=0; i<10;i++) {
		formName =formName.replace("/","X");
	}
		
	if( pathToOpen != null) {
		action = pathToOpen;
	}
	else {
		action = "../.." + contexto
	}
	
	console.log(action);
	
	var forma="<form action='"+action+"' method='GET' name='"+formName+"' id='"+formName+"' target='"+formName+"_ventana'> \n";
	forma+="</form> \n";
	
	if($("#"+formName).length>=0){
		$("#"+formName).remove();
	}
	
	$("body").append(forma);
	$("form[name="+formName+"]").submit();
	
}

function loadPPM_app(contexto, pathRelAdicionales, startPage, pageName) {
	console.log("loadPPM_app (v1.0.1):"+contexto);
	
	var formName = contexto;

	for(var i=0; i<10;i++) {
		formName =formName.replace("/","X");
	}
	
	if(pathRelAdicionales == null) {
		pathRelAdicionales = "";
	}
	
	if(pageName == null) {
		pageName= formName+"_ventana";
	}
	
	$.ajax({
		dataType:"json",
		url:pathRelAdicionales+"servlet/EjeCore?claseweb=portal.com.eje.usuario.UsuarioInfo&methodOut=sencha",
		data:{},
		success:function(data){
			if(data.usuario_is_session_valid) {
				localRut=data.data[0].usuario;
				localDigito=data.data[0].digito_ver;
				localClave=data.data[0].clave;
				
				if( $("#"+formName).length == 0 ) {
					var forma="<form action='"+pathRelAdicionales+"servlet/StartSession' method='post' name='"+formName+"' id='"+formName+"' target='"+pageName+"' role='form' class='form-inline'> \n";
					forma+="<div  class='form-group'>";
					forma+="<input name='param'   		id='param'      				type='hidden'   value='' /> \n";
					forma+="<input name='passline_enc' 	id='passline_enc'				type='hidden'   value='dHBubnZCRTlFV3NJL1FrTStpQ2NKcXF0dUpLbEhJbk1lS0hUL3lleEtneDQxZjdFenY4Y0dnPT0=' /> \n";
					forma+="<input name='rut'   		id='rut'       					type='hidden'   value='"+localRut+"'> \n";
					forma+="<input name='dig'        	id='dig' 						type='hidden'   value='"+localDigito+"'> \n";
					forma+="<input name='clave'       	id='clave'						type='hidden'   value='"+localClave+"'> \n";
					
					if( startPage != null ) {
						forma+="<input name='htm'       id='htm'     					type='hidden'   value='"+startPage+"'> \n";	
					}
					else {
						forma+="<input name='htm'       id='htm'						type='hidden'   value='../servlet/Tool?htm=user/usuario_frame.html'> \n";	
					}
					
					forma+="<input name='passline'     type='hidden'   value='evae'> \n";
					forma+="</div>";
					forma+="</form> \n";
					
					if($("#"+formName).length>=0){
						$("#"+formName).remove();
					}
					
					$("#"+formName).submit();
					console.log("Submit nuevo");
				}
				else {
					$("#"+formName+" #rut").val(localRut);
					$("#"+formName+" #dig").val(localDigito);
					$("#"+formName+" #clave").val(localClave);
					$("#"+formName+" #htm").val(startPage);
					
					
					console.log($("#"+formName+" input[type='submit']"));
					
					if( $("#"+formName+" input[type='submit']").length != 0 ) {
						var id = $("#"+formName+" input[type='submit']").attr("id"); 
						$("#submitButton").click();
					}
					else {
						$("#"+formName).submit();
					}
					
					console.log("Submit existente");
				}

				//intervalFinder = setInterval("checkingToSubmit(\""+formName+"\");",1000);
			}
			else {
				alert(data.message);
			}
		},
		error: function() {
			console.log("error");
		},
		failure: function() {
			console.log("Failure");
		}
	});
}

function s(){
    return false;
}

function submitButton(formName) {
	if( $("#"+formName).length != 0) {
		if(counterSecondsAfterSubmit == 0) {
			console.log($("#"+formName));
			$("#"+formName).submit();
			counterSecondsAfterSubmit+=1;
		}
		else {
			counterSecondsAfterSubmit+=1;
			console.log("t:"+counterSecondsAfterSubmit);
		}
	}
	
	if(counterSecondsAfterSubmit >= 3) {
		console.log("clearInterval");
		clearInterval(intervalFinder);
	}
}
 


$(function() {
	
	$('.fileToUpload').live('change',function() {
		
 
		var selectorImg 	= ( $(this).attr("data-for") );
		var selectorValue	= ( $(this).attr("data-value") );
		
		fileToUpload({
			selector: $(this),
			success: function(idImage) {
				if($(selectorValue).length != 0) {
					$(selectorValue).attr("value",idImage);	
					$(selectorValue).trigger("change");
				}
				
				if($(selectorImg).length != 0) {
					$(selectorImg).attr("src","SLoadFile?enc=true&idfile="+idImage);
				}
			}
		});
	});
	

	function fileToUpload(json) {
		console.log("fileToUpload");
		try {
			var file = $(json.selector)[0].files[0];
			console.log( file );
			var fData = new FormData();
			fData.append( encodeURI(file.name),  $(json.selector)[0].files[0] );
			
			$.ajax({
				  type: "POST",
				  datatype : "json",
				  url: json.url == null ? "EjeS?modulo=bs_participa_descanso&thing=FichaImagen&accion=add" : json.url,
				  data: fData,
				  processData: false,  // tell jQuery not to process the data
				  contentType: false,   // tell jQuery not to set contentType
				  success: function(dataString){
					  var data = JSON.parse(dataString);
					  
					  if(data.success == true) {
						  var f = eval(json.success);
						  if(f != null) {
							  f(data.data[0].id_archivo);
						  }
					  }
					  else {
						  var msg = ' Ha ocurrido un error al intentar subir la imagen <br/> '+data.message;
						  console.log(msg);
						  alertify.error(msg);		
					  }
				  },
				  error: function (error) {
					  var msg = ' Ha ocurrido un error al intentar subir la imagen <br/> '+error;
					  console.log(msg);
					  alertify.error(msg);		
	              }
			});
		}
		catch(e) {
			var msg = ' Ha ocurrido un error inesperado <br/> '+e;
			console.log(msg);
			alertify.error(msg);		
		}
	}

});
