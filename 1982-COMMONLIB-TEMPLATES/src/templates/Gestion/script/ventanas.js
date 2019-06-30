function AbirVentana(url, nombre, caract) {
	var ventana = open(url, nombre, caract);
	ventana.focus();
}

function VentEncargado(unidad, unidad_desc, peri, empresa) {
	var url = "../servlet/EncargadoUnidad?VDD=1&unidad="+unidad+"&unidad_desc="+unidad_desc+"&peri="+peri;
	if (empresa)
		url += "&empresa="+empresa;
	AbirVentana(url, "Encargado", "width=390, height=270");
}


function VentCandidato(unidad,rut) {
	var url = "../servlet/EncargadoUnidad?unidad="+unidad+"&rut="+rut;
	if (rut!="-1")
	   AbirVentana(url, "Empleado", "width=390, height=270");
}

function VentEmpleados(unidad,desc, empresa) {
	var url = "../servlet/EmpleadosU?unidad="+unidad+"&desc="+desc+"&empresa="+empresa;
	//if (rut!="-1")
	   AbirVentana(url, "Personal", "width=460, height=220");
}

function VentContraloria(unidad,desc,modo,empresa) {
	var url = "../servlet/Contraloria?unidad="+unidad+"&descrip="+desc+"&modo="+modo+"&empresa="+empresa+"&boton=1";
	//if (rut!="-1")
	   AbirVentana(url, "Personal", "width=320, height=240");
}

function VentInformeContraloria(criterio,cual,titulo) {
	var url = "../servlet/GeneraInforme?cual="+cual+"&criterio="+criterio+"&titulo="+titulo;
	//if (rut!="-1")
	   AbirVentana(url, "Personal", "width=800, height=600");
}

function VentAfp(rut) {
	var url = "../servlet/HistoriaAfp?rut="+rut;
	   AbirVentana(url, "HistoriaAfp", "width=458, height=180");
}
function VentIsapre(rut) {
	var url = "../servlet/HistoriaIsapre?rut="+rut;
	   AbirVentana(url, "HistoriaIsapre", "width=458, height=180");
}

function VentUnidadRelativa(rut,accion) { 
	var url = "../servlet/AsignaUniRel?rut="+rut+"&accion="+accion;
	   AbirVentana(url, "UniRel", "width=300, height=300");
}

function VentUnidadVer(rut) { 
	var url = "../servlet/AsignaUnidadesVer?rut="+rut;
	   AbirVentana(url, "AsignaUnidadesVer", "width=500, height=290");
}