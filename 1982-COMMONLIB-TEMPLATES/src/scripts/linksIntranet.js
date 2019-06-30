
var localRut = "";
var localDigito = "";
var localClave = "";

$(document).ready(function(){
	$.ajax({
		  dataType: "json",
		  url: "../../../servlet/EjeCore?claseweb=portal.com.eje.usuario.UsuarioInfo",
		  data: {},
		  success: function(data) {			  
			  localRut    = data.rut;
			  localDigito = data.digito_ver;
			  localClave  = data.clave;
			  
			  if(data.jefeUnidad == "true") {
				  
			  }		  
			  else {
				  $.each(data.privilegios, function(index, value) {
					  if("app_adm" == value.app) {
						  $("#aGGD").show();
					  }
					});
			  }
			 
		  }
		});
		
});



function loadGGD() {
	var forma = "<form action='../../../../ggd/servlet/StartSession' method='post' name='ggd' target='ventanaPerfiladorggd'> ";
	forma += "<input  name='param'       type='hidden'   value='' /> ";
	forma += "<input name='passline_enc' type='hidden'   value='V0htc3lHK2ZJWW8wUVNLSkhhaVZNVkFJYXE5YjVPdnlHcWxEZmYvQXJUSlUveXFxQXZIRCt3PT0=' /> ";
	forma += "<input name='rut'          type='hidden'   value='"+localRut+"'> ";
	forma += "<input name='dig'          type='hidden'   value='"+localDigito+"'> ";
	forma += "<input name='clave'        type='hidden'   value='"+localClave+"'> ";
	forma += "<input name='htm'          type='hidden'   value='../templates/perfilador/top_modulos.htm'> ";
	forma += "<input name='passline'     type='hidden'   value='hjne'> ";
	forma += "</form>";

	$("body").append(forma);
	$("form[name=ggd]").submit();

}

function loadPerfCargo() {
	var forma = "<form action='../../../../perfcargo/servlet/StartSession' method='post' name='formCargo' target='ventanaPerfiladorCargo'> ";
	forma += "<input  name='param'       type='hidden'   value='' /> ";
	forma += "<input name='passline_enc' type='hidden'   value='V0htc3lHK2ZJWW8wUVNLSkhhaVZNVkFJYXE5YjVPdnlHcWxEZmYvQXJUSlUveXFxQXZIRCt3PT0=' /> ";
	forma += "<input name='rut'          type='hidden'   value='"+localRut+"'> ";
	forma += "<input name='dig'          type='hidden'   value='"+localDigito+"'> ";
	forma += "<input name='clave'        type='hidden'   value='"+localClave+"'> ";
	forma += "<input name='htm'          type='hidden'   value='../templates/perfilador/top_modulos.htm'> ";
	forma += "<input name='passline'     type='hidden'   value='hjne'> ";
	forma += "</form>";

	$("body").append(forma);
	$("form[name=formCargo]").submit();

}
