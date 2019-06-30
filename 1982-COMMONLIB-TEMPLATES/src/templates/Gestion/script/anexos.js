function ventBuscarAnexo(anexo, rut, qv){
	var url = genUrlAnexo(anexo, null, rut, qv);
	var newVent = open(url, "InfoAnexo", "width=645,height=360,resizable=no,toolbar=no");
	//newVent.focus();
}

function ventInfoAnexo(empresa, unidad, anexo, htm){
	var url = "../servlet/BuscarAnexos?anexo="+anexo;
	url = url + "&empresa=" + empresa + "&unidad=" + unidad;
//	url = url + "&QV=" + qv;
	if (htm) url += "&htm="+htm;
	//alert(url);
	var newVent = open(url, "InfoAnexo", "width=645,height=360,resizable=no,toolbar=no");
	//newVent.focus();
	return false;
}

function linkInfoAnexo(empresa, unidad, anexo, htm){
	var url = "../servlet/BuscarAnexos?anexo="+anexo;
	url = url + "&empresa=" + empresa + "&unidad=" + unidad;
	url = url + "&volver=yes";
	if (htm) url += "&htm="+htm;
	//alert(url);
	document.location.href = url;
	return false;
}

function ventMantAnexoRel(empresa, unidad, desc, anexo){
	var url = "/servlet/datafolder.anexos.MantAnexoRel?anexo="+anexo;
	//url += "&empresa=" + empresa ;
	url += "&unidad=" + unidad+"&unidad_desc="+desc;
	var newVent = open(url, "MantAnexoRel", "width=430,height=255,resizable=no,toolbar=no");
	//newVent.focus();
}

function SeleccionarAnexosUnidad(empresa, unidad, desc, anexo){
	var url = "/servlet/datafolder.anexos.MantAnexoRel?ver=AU&anexo="+anexo;
	//url += "&empresa=" + empresa ;
	url += "&unidad=" + unidad+"&unidad_desc="+desc+ "&unidad_sel=" + unidad;
//	alert(url);
	var newVent = open(url, "SelAnexoRel", "width=310,height=245,resizable=no,toolbar=no");
	//newVent.focus();
}

function AsignarAnexoRut(ver, rut, empresa, unidad, desc){
	var url = "../servlet/AsignarAnexo?ver="+ver;
	url += "&empresa=" + empresa ;
	url += "&unidad=" + unidad+"&unidad_desc="+desc+ "&rut=" + rut;
	//alert(url);
	var newVent = open(url, "AsignarAnexoRut", "width=430,height=325,resizable=no,toolbar=no");
	//newVent.focus();
}

/*

function ver(anexo, unidad, rut, qv){
	var url = genUrlAnexo(anexo, unidad, rut, qv);
	document.location.href = url;
	return false;
}*/

//mantener



function existeAnexo(anexo) {
	var existe = false;
	for(x=0; x< anexExist.length; x++) {
		if (anexExist[x] == anexo) {
			existe = true;
			break;
		}
	}
	return existe;
}


/***** Crear Anexos *****/
function ventCrearAnexoUnidad(id, desc, accion) {
	//alert("crearAnexo");
	var url = "/servlet/datafolder.anexos.CreaAnexoUnidad?accion="+accion+"&unidad="+id+"&unidad_desc="+desc;
	var newVent = open(url, "CreaAnexos", "width=370,height=135,resizable=no,toolbar=no");
	return false;
}

function ventModifPublico(id, desc, accion, valor, publico) {
	//alert("ModifAnexo");
	var url = "/servlet/datafolder.anexos.CreaAnexoUnidad?accion="+accion+"&unidad="+id+"&unidad_desc="+desc;
	url += "&valor=" + valor + "&publico=" + publico;
	//alert(url);
	var newVent = open(url, "CreaAnexos", "width=370,height=135,resizable=no,toolbar=no");
	return false;
}


/***** Crear Anexos *****/
/*function ventModifAnexo(anexo, fax, unidad, empresa) {
	//alert("crearAnexo");
	var url = "/servlet/datafolder.anexos.CreaAnexos?accion=M&anexo="+anexo+"&fax="+fax+"&unidad="+unidad+"&empresa="+empresa;
	var newVent = open(url, "CreaAnexos", "width=370,height=135,resizable=no,toolbar=no");
	return false;
}

function modifAnexo(form) {
	if (confirm("?Desea aplicar estos Cambios...?"))  {
		if (validaNuevoAnexo(form)) {
			//form.accion.value = "M";
			form.action ="/servlet/datafolder.anexos.CreaAnexos";
			form.submit();
		}
	}

	return false;
} */