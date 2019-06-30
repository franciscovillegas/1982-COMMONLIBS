function esNum(texto) {
	var valido = false;
	if (texto.value == "") {
		texto.value = "0";
		valido = true;
	}
	else if (!isNaN(texto.value)) {
//		n = parseInt(texto.value);
		valido = true;
	}
	return valido
}

function validaNum(texto) {
	var valido = esNum(texto);
	if (!valido) {
		alert('Valor incorrecto');
		texto.select();
		texto.focus();
	}
}