var concepto = new Array();
concepto[0]  = "P.R.E.";
concepto[1]  = "Sueldo Base";
concepto[2]  = "Bono Gesti?n";
concepto[3]  = "Movilizaci?n";
concepto[4]  = "Bruto Regular";
concepto[5]  = "Neto Regular";
concepto[6]  = "Zona";
concepto[7]  = "Bruto c/Zona";
concepto[8]  = "Neto c/Zona";
concepto[9]  = "Bono Anual";
concepto[10] = "Asig. Caja";
concepto[11] = "Antig?edad";
concepto[12] = "Rta. Variable";
concepto[13] = "Bruto Promedio";
concepto[14] = "Neto Promedio";
concepto[15] = "Traslado";
concepto[16] = "Bruto Total";
concepto[17] = "Neto Total";

function calcular(rut, item, valor) {
	OpcionTitulo.innerHTML = concepto[item]
	LayerOpcion.style.visibility = "visible";
	document.formDatos.valor_real.value = valor;
	document.formDatos.valor_esp.value = "0";
	document.formDatos.que_iten.value = item;
	document.formDatos.valor_esp.select();
	document.formDatos.valor_esp.focus();
	return false;
}

function igualarValor() {
	document.formDatos.valor_esp.value = document.formDatos.valor_real.value;
	return false;
}

var valido = false;

function validaNum(texto) {
	window.valido = false;
	if (texto.value == "") {
		texto.value = "0";
		window.valido = true;
	}
	else if (!isNaN(texto.value)) {
		n = parseInt(texto.value);
		if (n>=0) window.valido = true;
	}
	if (!window.valido) {
		alert('Valor incorrecto');
		texto.select();
	}
}

function envia(form) {
	if (!window.valido) 
		alert('informaci?n incorrecta');
	else {
		open("Tool?htm=Gestion/SimRenta/SimRentaMsg.htm", "ProcSimRta", "width=250,height=20");
		LayerOpcion.style.visibility = "";
		form.action = "../servlet/CalculaRenta ";
		form.target =  "ProcSimRta";
		form.submit();
	}
	return false;
}


