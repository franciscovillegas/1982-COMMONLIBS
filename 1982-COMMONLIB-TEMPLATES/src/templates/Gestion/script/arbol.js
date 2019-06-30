var nodoSeleccionado = null;

function SeleccionarNodo(nodo){
	NoSeleccionarNodo();
	nodo.className = "textoNodoSel";
	nodoSeleccionado = nodo;
}

function NoSeleccionarNodo(){
	if (nodoSeleccionado != null) {
		nodoSeleccionado.className = "textoNodo";
	}
	nodoSeleccionado = null;
}

function exp_colap(img, submenu) {
	//alert(img + " " + submenu);
	var objImg     = eval(img);
	var objSubMenu = eval('document.all.'+submenu);
	//alert(objSubMenu);
	if (objSubMenu) { //si existe el submenu
		if (objSubMenu.style.display == "none") {//si oculto
			objSubMenu.style.display = ""
			objImg.src = "../servlet/Tool?images/Gestion/images_arbol/av.gif";
		}
		else {//si visible
			objSubMenu.style.display = "none"
			objImg.src = "../servlet/Tool?images/Gestion/images_arbol/an.gif";
		}
	}
	/*else
		alert("El submenu '" + submenu + "', no existe.");
	*/
	return false;
}

function busNodo(emp, uni) {
	var nodo = "nodo_"+emp+"_"+uni;
	var nodoObj = eval("document.all."+nodo);
	if (nodoObj) {
		document.location.href ="#"+nodo;
		nodoObj.onclick() ;
	}
	else
		alert("Este nodo no existe");
}
