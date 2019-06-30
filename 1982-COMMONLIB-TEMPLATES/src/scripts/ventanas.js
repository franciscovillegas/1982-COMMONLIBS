function AbirVentana(url, nombre, caract) {
	var ventana = open(url, nombre, caract);
	ventana.focus();
}

function VentUnidadRelativa(rut,accion) { 
	var url = "../servlet/AsignaUniRel?rut="+rut+"&accion="+accion;
	   AbirVentana(url, "UniRel", "width=300, height=300");
}

function VentUnidadVer(rut) { 
	var url = "../servlet/AsignaUnidadesVer?rut="+rut;
	   AbirVentana(url, "AsignaUnidadesVer", "width=565, height=290");
}

function VentCargosVer(rut) { 
	var url = "../servlet/AsignaCargosVer?rut="+rut;
	   AbirVentana(url, "AsignaCargosVer", "width=565, height=290");}