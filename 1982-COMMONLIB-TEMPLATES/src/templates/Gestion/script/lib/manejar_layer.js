var layerVistaAct ="";

function layerOcultarUltimo(){
	if (layerVistaAct != "") {
		var layer = eval(layerVistaAct);
		layer.style.visibility = 'hidden';
	}
	layerVistaAct ="";
}

function layerVer(cual){
	layerOcultarUltimo();
	var layer = eval(cual);
	layer.style.visibility = 'visible';
	layerVistaAct = cual;
}

function layerMovContHor(layer, des) {
	layer.style.clip.left += des;
	layer.style.clip.right += des;
	layer.style.left += des;
}