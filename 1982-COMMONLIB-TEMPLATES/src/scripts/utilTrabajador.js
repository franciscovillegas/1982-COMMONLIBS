$(document).ready(function() {
	var trabajador = new Trabajador();
	trabajador.checkAllBody();
	
		
	$("<style type='text/css'> .fotoContainer 		 { background-image:url(../servlet/Tool?images/fondo_usuario_280x97.png); width:280px; height:97px; float:right; position: fixed ; top: 10px; right: 50px;} </style>").appendTo("head");
	$("<style type='text/css'> .fotoContainerIntern  { width:63px; height:78px; border: #000 1px solid; background-color:#FFF; margin-top:10px; margin-left:10px; float:left;}  </style>").appendTo("head");
	$("<style type='text/css'> .imgTrabajador:hover  { cursor: pointer;}  </style>").appendTo("head");
	$("body").append("<form name='formImgTrabajador'><input style='display:none;' type='file' name='fileImgTrabajador' id='fileImgTrabajador' onchange='(function() { var t = new Trabajador(); t.endChangeImg(); })()' /></form> ");
});

function Trabajador() {
	
	this.checkAllBody = function() {
		var trabajador = new Trabajador();
		console.log("Loading Imagenes");
		
		$.each($(".fotoTrabajador"), function(k,v) {
			var tag = $(this).prop("tagName");
			
			if(tag == "IMG") {
				console.log("Found a img :"+$(this).prop("title"));
				trabajador.getImagePath($(this), $(this).prop("title"));
			}
			else {
				console.log("Found a div :");
				trabajador.loadImagen($(this));
			}
			
		});
	};
	
	this.getImagePath = function(imgJquery, rut) {
		var formData = new FormData();
		formData.append("claseweb"	, "portal.com.eje.usuario.UsuarioImagen");
		formData.append("accion"	, "select");
		formData.append("thing"		, "foto");
		formData.append("rut"		, rut);
				 
	    $.ajax({
	        url : '../servlet/EjeCore', //Server script to process data
	        type : 'POST',
	        data: formData,
	        datatype : "html",
	        success : function(data) {
	        	//console.log(data);
	        	data = JSON.parse(data);
	        	
	        	if(data != null && data != undefined) {
	        		data = data[0];
	        		$(imgJquery).prop("src", "../"+data.path);
	        	}
	        	
	        },
	        cache: false,
	        contentType:false,
	        processData: false
	    });
	};
	
	this.loadImagen = function(divJquery) {
		
		 var formData = new FormData();
		 formData.append("claseweb"	, "portal.com.eje.usuario.UsuarioImagen");
		 formData.append("accion"	, "select");
		 formData.append("thing"	, "foto");
				 
	    $.ajax({
	        url : '../servlet/EjeCore', //Server script to process data
	        type : 'POST',
	        data: formData,
	        datatype : "json",
	        success : function(data) {
	        	//console.log(data);
	        	data = JSON.parse(data);
	        	
	        	
	        	if(data != null && data != undefined) {
	        		data = data[0];
	        		
	        		var str = "";
	        		str += "<div class='fotoContainer'>";
	        		str +=" 	<div class='fotoContainerIntern'>";
	        		str += "		<table style='border-collapse:collapse; width: 280px;'>";
	        		str += "			<tr><td style='width:67px;'>";
	        		str += "					<img class='imgTrabajador' src='../"+data.path+"'  style=\"width:62px; height:76px;\"  onclick='(function() { var t = new Trabajador(); t.beginChangeImg(this);  })()'/>";
	        		str += "				</td>";
	        		str += "				<td style='text-transform:capitalize; font-size: 9px; vertical-align: top;'>";
	        		str += "				"+data.nombre+"<br/>";
	        		str += "				"+data.cargodesc+"<br/>";
	        		str += "				Ingresos:"+data.cantIngresos+"<br/>";
	        		str += "				</td>";
	        		str += "			</tr>";
	        		str += "		</table>";
	        		str += "	</div>";
	        		str += "</div>";
	        		
	        		$(divJquery).html(str);
	        	}
	        },
	        cache: false,
	        contentType:false,
	        processData: false
	    });
		
	};
	
	this.beginChangeImg = function(tagImg) {
		console.log("Emula Click"+tagImg);
		$("#fileImgTrabajador").trigger("click");
	};
	
	this.endChangeImg = function() {
		console.log("Uploading FILE");
		
		var formData = new FormData($("form[name='formImgTrabajador']")[0]);
		 formData.append("claseweb"	, "portal.com.eje.usuario.UsuarioImagen");
		 formData.append("accion"	, "upload");
		 formData.append("thing"	, "foto");
		 
		$.ajax({
	        url : '../servlet/EjeCore', //Server script to process data
	        type : 'POST',
	        data: formData,
	        datatype : "json",
	        success : function(data) {
	        	var trabajador = new Trabajador();
	        	trabajador.checkAllBody();	
	        	
	    		$("#fileImgTrabajador").val("");
	        },
	        cache: false,
	        contentType:false,
	        processData: false
	    });
		
		$("#fileImgTrabajador").val("");
	};
}