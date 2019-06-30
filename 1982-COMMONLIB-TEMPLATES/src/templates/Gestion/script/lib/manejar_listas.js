// agrega elementos a la lista

function Agrega(origen,destino,jefe) { 
	alert("agrega");
	var rutOrigen,nomOrigen,posOrigen;
	var posDestino;
	var itemSel=origen.value; 
	var largoo=origen.length;
	var largod=destino.length;
	var nombre=origen.options[origen.selectedIndex].text;
	//recorre la lista origen,y traspasa **************************
	//al destino todos los que hayan sido seleccionados*******************
        if (itemSel!="-1"){
	   for (x=0;x<largoo;x++){//recorre la lista de origen
		 itemOrigen=origen.options[x];
		 nomOrigen=itemOrigen.text;
		 rutOrigen=itemOrigen.value;
		 //Item seleccionado en el origen y que no exista en el destino
		 if (itemOrigen.selected){//seleccionado
			if(!Existe(rutOrigen,nomOrigen,destino))//si no existe
			   if (jefe!=rutOrigen)
			     listaAddElemento (destino, rutOrigen, nomOrigen); 
			   else
			    alert("No se puede relacionar un Jefe consigo mismo.");    
			else 
                          alert(nomOrigen+"\nEste Empleado ya existe.");
		 }       
	   }//****for***********
   }else
       alert("Seleccione un campo de la lista."); 
}//Fin************************

function Existe(rut,nombre,destino){
  var valor=false;
  var itemDestino;
  var z;
  var largode=destino.length;
  for (z=0;z<largode;z++){
      itemDestino=destino.options[z];
      if (rut==itemDestino.value){      
          valor=true;
          break;
      }
  }
 
  return valor;
}


function Elimina(lista){
   var pos=lista.selectedIndex;
   var largo=lista.length;
   if (largo>0){
      if (pos!="-1"){
	   var nombre=lista.options[pos].text;
	   lista.options[pos]=null;
      }else 
	  alert("Seleccione el Empleado a eliminar.");
   }else alert("No hay Empleados en la Lista.");
}

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