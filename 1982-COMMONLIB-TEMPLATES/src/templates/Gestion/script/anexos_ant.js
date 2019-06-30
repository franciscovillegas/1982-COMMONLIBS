function genUrlAnexo(anexo, rut, qv){
	var url = "/servlet/datafolder.anexos.BuscarAnexos?anexo="+anexo;
	if (rut != null)
		url = url + "&rut=" + rut;
	if (qv != null)
		url = url + "&QV=" + qv;
	return url;
}

function ventBuscarAnexo(anexo, rut, qv){
	var url = genUrlAnexo(anexo, rut, qv);
	var newVent = open(url, "BuscarAnexo", "width=645,height=360,resizable=no,toolbar=no");
	//newVent.focus();
}

function ver(anexo, rut, qv){
	var url = genUrlAnexo(anexo, rut, qv);
	document.location.href = url;
	return false;
}

//mantener

function quitarAnexoRel(form) {
	if (confirm("&#191;Desea quitar estos anexos de la relaci&#243;n?")) {
		form.accion.value = "QAR";
		form.submit();
	}
	return false;
}

function cambiarTodosAnexos(form, anexo){
	var nuevo_anexo = prompt("Cambiar el anexo para todos los trabajadores listados.\n(M&#225;x. 5 d&#237;gitos)", anexo);
	if (nuevo_anexo != null) {
		if (nuevo_anexo.length > 5)
			alert("El anexo tiene un m&#225;ximo de 5 d&#237;gitos, incluyendo los espacios..");
		else if (!existeAnexo(nuevo_anexo))
			alert("El anexo '"+nuevo_anexo+"' no existe...");
		else
			cambiarAnexos(form, nuevo_anexo);
	}
}

function aplicarCambios(form) {
	form.submit();
	return false;
}

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

function cambiarAnexo(nombre, anexo){
	var nuevo_anexo = prompt("Cambiar el anexo de '"+nombre+"'.\n(M&#225;x. 5 d&#237;gitos)", anexo.value);
	if (nuevo_anexo != null) {
		if (nuevo_anexo.length > 5)
			alert("Este anexo tiene un m&#225;ximo de 5 d&#237;gitos, incluyendo los espacios...");
		else if (!existeAnexo(nuevo_anexo))
			alert("El anexo '"+nuevo_anexo+"' no existe...");
		else
			anexo.value = nuevo_anexo;
	}
}

/********************  lista de anexos relacionados ********************/

function agregarAnexosRel(form) {
	if (form.anexo_rel.options.length > 0)	{
		if (confirm("&#191;Desea Agregar estos anexos a la relaci&#243;n?")) {
			form.accion.value = "AAR";
			var lista = form.anexo_rel;
			//lista.multiple = true;
			var lista_gen = "";
			for(x=0; x< lista.options.length; x++) {
				//lista.options[x].selected = true;
				lista_gen = lista_gen + lista.options[x].value + ",";
			}
			if (lista_gen.length > 0) {
				lista_gen = lista_gen.substr(0,lista_gen.length-1);
			}
			form.lista_anexo_rel.value = lista_gen;
			//alert(form.lista_anexo_rel.value);
			form.submit();
		}
	}
	else
		alert ("Debe indicar los anexos que desea agregar...");
	return false;
}

function agregarAnexo(lista, anexo_rel_ext, anexo){
	//var nuevo_anexo = prompt("Indique el anexo a agregar (M&#225;x. 5 d&#237;gitos)", '');
	var nuevo_anexo = anexo.value;
	if (nuevo_anexo != null) {
		if (nuevo_anexo == anexo)
			alert("No puede relacionar el anexo con sigo mismo.");
		else if (nuevo_anexo.length > 5)
			alert("El anexo tiene un m&#225;ximo de 5 d&#237;gitos, incluyendo los espacios..");
		else {
			var seagrega = true;
			//verifica que el anexo no exista en la relaci&#243;n
			for(x=0; x< anexRelExist.length; x++) {
				if (anexRelExist[x] == nuevo_anexo) {
					seagrega = false;
					break;
				}
			}
			if (seagrega) {
				//verifica que el anexo no exista en la nueva lista
				for(x=0; x< lista.options.length; x++) {
					if (lista.options[x].value == nuevo_anexo) {
						seagrega = false;
						break;
					}
				}
				// a&#241;ade elementos nuestro combobox
				if (seagrega) {
					var optionObj = new Option(nuevo_anexo, nuevo_anexo);
					lista.options[lista.length] = optionObj;
					anexo.value ="";
					anexo.focus();
				}
				else 
					alert("El anexo '"+nuevo_anexo+"' ya existe en la lista.");
			}
			else
				alert("El anexo '"+nuevo_anexo+"' ya existe en la relaci&#243;n.");
		}
	}
}

function quitarOpcion(lista, indice){
	if (indice >=0)  {
		for(x=indice; x< lista.options.length-1; x++) {
			//alert(indice + " -- " + x);
			lista.options[x] = new Option(lista.options[x+1].text, lista.options[x+1].value);
		}
		lista.options.length = lista.options.length-1;
	}
}

function quitarAnexo(lista){
	quitarOpcion(lista, lista.selectedIndex)
}

/***** Crear Anexos *****/
function ventCrearAnexo() {
	//alert("crearAnexo");
	var url = "/servlet/datafolder.anexos.CreaAnexos?accion=C";
	var newVent = open(url, "CreaAnexos", "width=370,height=135,resizable=no,toolbar=no");
	return false;
}

function validaNuevoAnexo(form) {
	var seguir = false;
	if (form.nuevo_anexo.value == "") {
		alert("Debe indicar un Anexo");
		form.nuevo_anexo.focus();
	}
	else if ((form.nuevo_anexo.value != form.anexo.value) &&  (existeAnexo(form.nuevo_anexo.value)))	{
		alert("El anexo '"+form.nuevo_anexo.value+"' existe...");
		form.nuevo_anexo.focus();
	}
	else if (form.unidad.value == "-1")	{
		alert("Debe indicar una Unidad");
		form.unidad.focus();
	}
	else	{
		seguir = true;
	}
	return seguir;
} 

function crearAnexo(form) {
	if (validaNuevoAnexo(form)) {
		//form.accion.value = "C";
		form.action ="/servlet/datafolder.anexos.CreaAnexos";
		form.submit();
	}
	return false;
} 

/***** Crear Anexos *****/
function ventModifAnexo(anexo, fax, unidad, empresa) {
	//alert("crearAnexo");
	var url = "/servlet/datafolder.anexos.CreaAnexos?accion=M&anexo="+anexo+"&fax="+fax+"&unidad="+unidad+"&empresa="+empresa;
	var newVent = open(url, "CreaAnexos", "width=370,height=135,resizable=no,toolbar=no");
	return false;
}

function modifAnexo(form) {
	if (confirm("&#191;Desea aplicar estos Cambios...?"))  {
		if (validaNuevoAnexo(form)) {
			//form.accion.value = "M";
			form.action ="/servlet/datafolder.anexos.CreaAnexos";
			form.submit();
		}
	}

	return false;
} 