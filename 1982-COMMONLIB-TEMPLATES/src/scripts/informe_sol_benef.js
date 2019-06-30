var op_periodo = new Array();
var op_rango   = new Array();
var meses      = new Array();

op_periodo[0] = "INFU001";
op_periodo[1] = "INFU003";
op_periodo[2] = "INFU004";
op_periodo[3] = "INFU002";

op_rango[0] = "INFU006";
op_rango[1] = "INFU007";
op_rango[2] = "INFU008";
op_rango[3] = "INFU009";
op_rango[4] = "INFU014";


function existe_en(texto, arreglo) {
	var largo = arreglo.length;
	var existe = false;
	for (x = 0; x < largo; x++) {
//		alert(arreglo[x]);
		if (arreglo[x] == texto) {
			existe = true;
			break;
		}
	}
	return existe;
}

/*
function opcion_informe(info) {
	var hoy = new Date();
	
	LayerPeri.style.visibility       = "hidden";
	LayerRango.style.visibility    = "hidden";
	LayerINFU007.style.visibility = "hidden";
	LayerINFU009.style.visibility = "hidden";
	LayerINFU001.style.visibility = "hidden";
	LayerINFU012.style.visibility = "hidden";
	LayerRut.style.visibility        = "hidden";
	LayerSexo.style.visibility        = "hidden";
	LayerArbol.style.visibility      = "visible";
	opcionUR.style.visibility       = "visible";
	
	if (existe_en(info, op_periodo)) {
		LayerPeri.style.visibility = "visible";
	}
	if (existe_en(info, op_rango)) {
		LayerRango.style.visibility = "visible";
		document.formOp.d_ano.value = hoy.getFullYear();
		document.formOp.h_ano.value = hoy.getFullYear();
	}
	if (info == "INFU006") {
		document.formOp.d_ano.disabled = "true";
		document.formOp.h_ano.disabled = "true";
	}
	else {
		document.formOp.d_ano.disabled = "";
		document.formOp.h_ano.disabled = "";
	}
	if ((info == "INFU007") || (info == "INFU009") || (info == "INFU001")|| (info == "INFU012")){
		eval("Layer"+info+".style.visibility = 'visible'");
	}
	if (info == "INFU013") { //perfil laboral
		LayerRut.style.visibility   = "visible";
		LayerArbol.style.visibility = "hidden";
		opcionUR.style.visibility   = "hidden";
	}else   if (info == "INFU014") { //Licencias M?dicas
				LayerSexo.style.visibility   = "visible";
				
			}
}
*/

function Valida(form_info) {
	var info = document.form1.cual.value;
	GeneraInfo(info, form_info);
	
}

function GeneraInfo(info, form_info) {
	var valido = true;
	//alert('Generando Informe para la Unidad '+ form_info.descrip.value + "'");
	//alert("informe: "+info);
	var criterio = "";
	if (form_info.benef_id.value!=""){
		criterio = criterio + " AND (benef_id="+form_info.benef_id.value+")";
	}
	if (info == "SOLBENEF") {
		criterio = criterio + " AND (fecha >= CONVERT(DATETIME, '"+form_info.desde.value+"', 103))"+
			" AND (fecha <= CONVERT(DATETIME, '"+form_info.hasta.value+"', 103))";
	}
                
	
	//*****************************************************************************
	
	if (valido) { // si valido genera informe
		form_info.cual.value = info;
		form_info.criterio.value = "Where (1=1) " + criterio;
		form_info.action ="../servlet/GeneraInformePortal";
		form_info.target ="Informe";

	   v = open('','Informe','width=760,height=520,scrollbars=1, toolbar=1');
	   v.focus();
	   //parent.AbrirVentProcesando();		
	   form_info.submit();
	}
}