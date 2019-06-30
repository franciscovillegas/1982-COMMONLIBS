var op_periodo = new Array();
var op_rango   = new Array();
var meses      = new Array();

op_periodo[0] = "INF001";
op_periodo[1] = "INF003";
op_periodo[2] = "INF004";
//op_periodo[3] = "INF002";

op_rango[0] = "INF006";
op_rango[1] = "INF007";
op_rango[2] = "INF008";
op_rango[3] = "INF009";

meses[ 0] = "Enero";
meses[ 1] = "Febrero";
meses[ 2] = "Marzo";
meses[ 3] = "Abril";
meses[ 4] = "Mayo";
meses[ 5] = "Junio";
meses[ 6] = "Julio";
meses[ 7] = "Agosto";
meses[ 8] = "Septiembre";
meses[ 9] = "Octubre";
meses[10] = "Noviembre";
meses[11] = "Diciembre";

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

function opcion_informe(info) {
	var hoy = new Date();
	LayerPeri.style.visibility  = "hidden";
	LayerRango.style.visibility = "hidden";
	LayerINF007.style.visibility = "hidden";
	LayerINF009.style.visibility = "hidden";
	
	if (existe_en(info, op_periodo)) {
		LayerPeri.style.visibility = "visible";
	}
	if (existe_en(info, op_rango)) {
		LayerRango.style.visibility = "visible";
		document.formOp.d_ano.value = hoy.getFullYear();
		document.formOp.h_ano.value = hoy.getFullYear();
	}
	if (info == "INF006") {
		document.formOp.d_ano.disabled = "true";
		document.formOp.h_ano.disabled = "true";
	}
	else {
		document.formOp.d_ano.disabled = "";
		document.formOp.h_ano.disabled = "";
	}
	if ((info == "INF007") || (info == "INF009")){
		eval("Layer"+info+".style.visibility = 'visible'");
	}	
}

function CapturaValor(divi,descrip) {
	document.form1.divi.value=divi;
	document.form1.descrip.value=descrip;
}

function Valida(form_info, form_op) {
	var info = parent.parent.topFrame.document.form1.informe.value;
	if (form_info.divi.value != ''){
		if (info != '-1'){
			GeneraInfo(info, form_info, form_op);
		}
		else
			alert('Seleccione un Informe');
  	}   
	else 
		alert ('Seleccione una Divisi&#243;n.');
}

function GeneraInfo(info, form_info, form_op) {
	var valido = true;
	alert('Generando Informe para Divisi&#243;n '+ form_info.descrip.value);
	var criterio = "WHERE (empresa = '"+form_op.empresa.value+"')";
	criterio = criterio +" AND (divi_id = '"+form_info.divi.value+"')";
	
	if (existe_en(info, op_periodo)) {
		form_info.peri_ano.value = form_op.peri_ano.value;
		form_info.peri_mes.value = form_op.peri_mes.value;
		criterio = criterio +" AND (periodo = "+form_op.peri_ano.value+form_op.peri_mes.value+")";
	}
	else {
		form_info.peri_ano.value = "";
		form_info.peri_mes.value = "";
	}
	if (existe_en(info, op_rango)) {
		form_info.fecha_desde.value = form_op.d_ano.value + "-" + form_op.d_mes.value + "-"+ form_op.d_dia.value ;
		form_info.fecha_hasta.value = form_op.h_ano.value + "-" + form_op.h_mes.value + "-"+ form_op.h_dia.value ;
		if (info == "INF006") {//fecha dia y mes
			criterio = criterio +" AND (day(fecha) >= "+form_op.d_dia.value+") AND (MONTH(fecha) >= "+form_op.d_mes.value+")"+
				" AND (day(fecha) <= "+form_op.h_dia.value+") AND (MONTH(fecha) <= "+form_op.h_mes.value+")";
		}
		else {// fecha dia, mes y a&#241;o
			criterio = criterio + " AND (fecha >= CONVERT(DATETIME, '"+form_info.fecha_desde.value+"', 102))"+
				" AND (fecha <= CONVERT(DATETIME, '"+form_info.fecha_hasta.value+"', 102))";
			if (info == "INF007") {
				var opcion_sel;
				for (x=0; x < 4; x++) {
					if (form_op.opcion[x].checked) {
						opcion_sel = form_op.opcion[x].value;
						break;
					}
				}
				//alert(opcion_sel);
				var opcion;
				eval(" opcion = form_op."+opcion_sel);
				if (opcion_sel != "T") {
					if (opcion.value == "-1") {
						alert(opcion.options[opcion.selectedIndex].text);
						opcion.focus();
						valido = false;
					}
					else {
						criterio = criterio + " AND " + opcion_sel + " = '"+opcion.value+"'";
					}
				}
			}
		}
	}
	else {
		form_info.fecha_desde.value = "";
		form_info.fecha_hasta.value = "";
	}
	//alert(criterio);		
	if (valido) { // si valido genera informe
		if (info == "INF009") {
			eval("info = info + form_op.opcion"+info+".value");
		} 
		form_info.cual.value = info;
		form_info.criterio.value = criterio;
		form_info.peri_ano.value = form_op.peri_ano.value;
		form_info.peri_mes.value = form_op.peri_mes.value;
		form_info.action ="/servlet/GeneraInforme";
		form_info.target ="Informe";
		
		v = open('','Informe','width=760,height=520,scrollbars=1, toolbar=1');
		v.focus();
		parent.AbrirVentProcesando();
		form_info.submit();
	}
}