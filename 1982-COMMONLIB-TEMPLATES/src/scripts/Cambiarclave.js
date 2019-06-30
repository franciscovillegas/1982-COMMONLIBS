function compara_claves(nuevaClave, confirma){
	var valido = true;
   	if (nuevaClave==confirma) {
   		if (nuevaClave.length < 4) {
			alert("El largo minimo es de 4 caracteres.");
			valido = false;
		}
   	}
	else {
		alert("Revise confirmación.");
		valido = false;
	}
	return valido;
}