/**
 *  llamada doGet
 *  formName = nombre del objeto form
 *  dataTyp = ['html','json']
 *  funcion = funci�n que se ejecuta como respuesta
 * */
function loadJSONforMain(url, paramType, params, recepcionType, func) {
	
		$.ajax({
			  url: url,
			  data: params,
			  type: paramType,
			  success: eval (func),
			  dataType: recepcionType
			});
}

/**
 *  llamada doPost sin Archivos
 *  formName = nombre del objeto form
 *  dataTyp = ['html','json']
 *  funcion = funci�n que se ejecuta como respuesta
 * */
 

function implementationWOUTFiles(formName, dataType, funcion) {
    var formData = $("form[name='" + formName + "']").serialize() ;

    $.ajax({
        url : '../servlet/EjeCore', //Server script to process data
        type : 'POST',
        datatype : dataType,
        data : formData,
        success : eval(funcion)
    });
}

/**
 *  llamada doPost CON Archivos
 *  formName = nombre del objeto form
 *  dataTyp = ['html','json']
 *  funcion = funci�n que se ejecuta como respuesta
 * */
function implementationWFiles(formName, dataType, funcion) {
    var formData = new FormData($("form[name='" + formName + "']")[0]);

    $.ajax({
        url : '../servlet/EjeCore', //Server script to process data
        type : 'POST',
        datatype : dataType,
        success : eval(funcion),
        data: formData,
        cache: false,
        contentType:false,
        processData: false
    });

} 


function alerta(msg) {
	jAlert(msg, 'Portal de c�lculo');
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

function toPdf(divContent, stylesPage, stylesOther, nombreFile) {
	var content = $("#"+divContent).html();
	var rnd = Math.floor(Math.random()*11);
	var contentName= "content"+rnd;
	
	while($("#"+contentName).length != 0 ) {
		rnd = Math.floor(Math.random()*11);
		contentName= "content"+rnd;
	}

	var formName = "form"+rnd;
	var fileName = "fileName"+rnd;
	
	var strS = "";
	if(stylesPage != null || stylesOther != null) {
		strS ="<style type='text/css'>   \n";
		strS = strS + "@page { \n";
			$.each(stylesPage, function(key, val) {
				strS = strS + key+" :  " + val + " ;\n";
		  	});
		strS = strS + "} \n";	
		
		strS = strS + "body { \n";
		$.each(stylesOther, function(key, val) {
			strS = strS + key+" :  " + val + " ;\n";
	  	});
		strS = strS + "} \n";	
	
		strS = strS + "</style>";
	}
	
	var partName = "";
	
	if(nombreFile != null) {
		partName = "<input type='hidden' name='tipo' 	 id='"+fileName+"' >";
	}

	$("body").append("<form name='"+formName+"' action='../servlet/ServletTransformPdf' method='post'>"+
			"<input type='hidden' name='content' id='"+contentName+"' >"+partName+"</form>");
	$("#"+contentName).val(strS+"\n"+content);
	
	if(nombreFile != null) {
		$("#"+fileName).val(nombreFile);
	}
	
	//console.log($("form[name="+formName+"]").html());
	$("form[name="+formName+"]").submit();
	$("form[name="+formName+"]").remove();
	
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



function UrlTool() {
	this.getUrlParameter = function(sParam) {

		    var sPageURL = window.location.search.substring(1);
		    var sURLVariables = sPageURL.split('&');
		    
		    for (var i = 0; i < sURLVariables.length; i++) {
		        var sParameterName = sURLVariables[i].split('=');
		        
		        if (sParameterName[0] == sParam) {
		            return sParameterName[1];
		        }
		    }
	};
}


function consoleLog(log) {
	try {
		console.log(log);
	}
	catch(e) {
		
	}
}

function window_setFavico(ico) {
    var link = document.createElement('link');
    link.type = 'image/x-icon';
    link.rel = 'shortcut icon';
    link.href = ico; //assumes favicon is in the app root as it should be
    document.getElementsByTagName('head')[0].appendChild(link);
}

function jAlertMsg(json) {
	
	var data = json.data;
	var funcOk = json.success;
	var funcError = json.failure;
	var ok = data != null && data.success;
	
	if(ok) {
		jAlertInfo(data, funcOk);
	}
	else {
		jAlertError(data, funcError);
	}
}

function jAlertError(data,  func) {
    var mensg='<p>Ha ocurido un error al generar su solicitud.</p> ';
    mensg=mensg+'<p>Por favor int\u00e9ntelo nuevamente.</p> ';

    if(data != null) {
        if(data.message != null) {
             mensg=mensg+'<p>'+data.message+'</p> ';
        }
        else if(isString(data)){
            mensg=mensg+'<p>'+data+'</p> ';
        }
    }

    jAlert(mensg,"Error", func);
}

function jAlertInfo(data, func) {
		var mensg="Acci\u00F3n realizada correctamente";

        if(data != null) {
            if(data.message != null) {
                 mensg=mensg+'<p>'+data.message+'</p> ';
            }
            else if(isString(data) && data != null){
                mensg=mensg+'<p>'+data+'</p> ';
            }
        }
        
        jAlert(mensg,"Mensaje", func);
}

function isString (obj) {
	  return (Object.prototype.toString.call(obj) === '[object String]');
	}