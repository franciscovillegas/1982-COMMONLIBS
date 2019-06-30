var nodoSeleccionado = null;

function SeleccionarNodo(nodo){
	NoSeleccionarNodo();
	nodo.className = "textoGraficoSel";
	nodoSeleccionado = nodo;
}

function NoSeleccionarNodo(){
	if (nodoSeleccionado != null) {
		nodoSeleccionado.className = "textoGrafico";
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
			objImg.src = "../servlet/Tool?images/images_arbol_2/av.jpg";
		}
		else {//si visible
			objSubMenu.style.display = "none"
			objImg.src = "../servlet/Tool?images/images_arbol_2/an.jpg";
		}
	}
	/*else
		alert("El submenu '" + submenu + "', no existe.");
	*/
	return false;
}

function ver(url) {
	//alert(url);
	parent.frames.mainFrame.location.href = url;
	//document.location.href = url;
}