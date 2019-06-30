// agrega elementos a la lista
function listaAddElemento (lista, valor, texto) {
	//var optionObj = new Option("dddddddddd", valor);
	var largo = lista.length;
	//alert("largo " + largo);
	lista.options[largo] = new Option();
	lista.options[largo].value = valor;
	lista.options[largo].text = texto;
}

function listaQuitarElemento(lista, indice){
	if (indice >=0)  {
		for(x=indice; x< lista.options.length-1; x++) {
			lista.options[x] = new Option(lista.options[x+1].text, lista.options[x+1].value);
		}
		lista.options.length = lista.options.length-1;
	}
}

function listaQuitarElemSel(lista) {
	listaQuitarElemento(lista, lista.selectedIndex);
}

//
function listaExisteValor(lista, valor) {
	var existe = false;
	for(x=0; x< lista.options.length; x++) {
		if (lista.options[x].value == valor) {
			existe = true;
			break;
		}
	}
	return existe;
}

function listaCreaCadena(lista) {
	var cadena = "";
	for(x=0; x< lista.options.length; x++) {
		cadena += lista.options[x].value + ",";
	}
	cadena = cadena.substring(0, cadena.length-1);
	//alert(cadena);
	return cadena;
}

function listaLlenar(lista, array, opcionExtra){
	lista.length = 0;
	l=0;
	if (opcionExtra) {
		lista.options[l] = new Option();
		lista.options[l].value = opcionExtra.value;
		lista.options[l].text = opcionExtra.text;
		l++;
	}
	if (array) {
		for (i = 0; i<array.length;i++, l++) {
			lista.options[l] = new Option();
			lista.options[l].value = array[i].value;
			lista.options[l].text = array[i].text;
		}
	}
}