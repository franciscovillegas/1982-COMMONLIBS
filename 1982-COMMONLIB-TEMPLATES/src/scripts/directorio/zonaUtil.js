
function changeMain(newContent) {
	destroyPreviousUploadify();
	
	$("#contenido").html(newContent);
}

function destroyPreviousUploadify() {
	destroyUploadify("#file_upload");
	destroyUploadify("#file_upload1");
	destroyUploadify("#file_upload2");
}

function destroyUploadify(key) {
	try {

		if( $(key).length  != 0 ) {
			$(key).uploadify('destroy');
		}
	} catch (e) {
		toConsole(e);
	}
}

function loadJSONforMain(url, paramType, params, recepcionType, func) {
	
		$.ajax({
			  url: url,
			  data: params,
			  type: paramType,
			  success: function(data) {
				  var funcion = eval(func);
				  funcion(data);
				  
				  //autoLoadEndSession();
			  },
			  dataType: recepcionType
			});
	}

function alerta(msg) {
	jAlert(msg, 'Portal de cálculo');
}

function showPdf(idContenedor,fileName){
	var rnd = Math.random();
	rnd = "formPdf_" + rnd.toString().replace('.','');
	
	var str = "<form name='"+rnd+"' id='"+rnd+"' method='post' action='../servlet/ServletTransformPdf'>";
	str += "		<input name='content' id='"+rnd+"content' type='hidden'>";  
	str += "		<input type='hidden' name='name' value='"+fileName+"'> ";
	str += "</form>";
	
	$("body").append(str);
	
	$('#'+rnd+"content").val($('#'+idContenedor).html());
	$('#'+rnd).submit();  
}

function showPdfAfterCallBack(fileName,params) {
	loadJSONforMain('../servlet/EjeCore',
			'GET',params,'html',
			function(data){
				var rnd = Math.random();
				rnd = "formPdf_" + rnd.toString().replace('.','');
				
				var str = "<form name='"+rnd+"' id='"+rnd+"' method='post' action='../servlet/ServletTransformPdf'>";
				str += "		<input name='content' id='"+rnd+"content' type='hidden'>";  
				str += "		<input type='hidden' name='name' value='"+fileName+"'> ";
				str += "</form>";
				
				$("body").append(str);
				$("#divPlantilla").html(data);	
				$('#'+rnd+"content").val($('#divPlantillaPDF').html());
				$('#'+rnd).submit();  
			});
}

function autoLoadEndSession() {
	if($("#sessionCounter").length > 0) {
		$.ajax({
			  url: "../servlet/EjeCoreI?claseweb=portal.com.eje.serhumano.user.SessionMgr&accion=select&thing=jsonLiveSeconds",
			  data: {},
			  type: "get",
			  success: function(data) {
				  confEndSession(new Date(0, 0, 0, data.hours , data.minutes , data.seconds, data.milliseconds ));
			  },
			  dataType: "json"
			});
	}
}

var dateSession = null;
var refreshIntervalId = null;
function confEndSession(dateSession) {

	if(refreshIntervalId != null) {
		clearInterval(refreshIntervalId);
	}
		

	refreshIntervalId = setInterval(function(){
		h = dateSession.getHours();
		m = dateSession.getMinutes(); 
		s = dateSession.getSeconds();
		
		$("#sessionCounter").html(addOneChar(h)+":"+addOneChar(m)+":"+addOneChar(s));
		
		if(h == 0 && m == 0 && s == 0) {
			alert("Su sesi&#243;n ha finalizado.");
			location.href='../';
		}
		else {
			dateSession.setSeconds(dateSession.getSeconds()-1);
		}
	}, 1000);	

}



function addOneChar(v) {
	if(v != null) {
		if(v.toString().length == 1) {
			return "0" + v;
		}
	}
	
	return v;
}