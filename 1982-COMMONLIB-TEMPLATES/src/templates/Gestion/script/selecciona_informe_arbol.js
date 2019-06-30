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
	LayerPeri.style.visibility       = "hidden";
	LayerRango.style.visibility    = "hidden";
	LayerINFU007.style.visibility = "hidden";
	LayerINFU009.style.visibility = "hidden";
	LayerINFU001.style.visibility = "hidden";
	LayerINFU012.style.visibility = "hidden";
	LayerRut.style.visibility        = "hidden";
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
	}
}

function Valida(form_info, form_op) {
	var info = parent.topFrame.document.form1.informe.value;
	if (info != '-1'){
		if (info == "INFU013") {
			if ( form_op.rutcomp.value == "")
				alert("Ingrese un rut");
			else
				GeneraInfo(info, form_info, form_op);
		}
		else if (form_info.descrip.value != "") 
			GeneraInfo(info, form_info, form_op);
		else
			alert('Seleccione una Unidad.');
  	}   
	else 
		alert('Seleccione un Informe');
}

function GeneraInfo(info, form_info, form_op) {
	var valido = true;
	//alert('Generando Informe para la Unidad '+ form_info.descrip.value + "'");
	var criterio = "";
	if (info == "INFU013")
		criterio = "WHERE (rutcomp = '"+form_op.rutcomp.value+"')";
	else {
		if (form_op.filtro_adic.value != "") //si tiene un filtro adicional (ramas)
			criterio = "as info WHERE " + form_op.filtro_adic.value + " and ";
		else
			criterio = "WHERE ";
			
		criterio += "(empresa = '"+form_info.empresa.value+"')";
		criterio += " AND (unidad = '"+form_info.unidad.value+"')";

		var tipo = "";
		//alert(form_info.tipo[0].checked);
		if (form_info.tipo[0].checked) tipo = form_info.tipo[0].value;
		if (form_info.tipo[1].checked) tipo = form_info.tipo[1].value;

		criterio += " AND (tipo = '"+tipo+"')";
	}
	//alert(criterio);

	if (info == "INFU012"){
		if (form_op.opcionINFU012.value=="_6"){
			var desde=form_op.netodesde.value;
			var hasta=form_op.netohasta.value;
			
			if (hasta==""){
				//alert("Debe Ingresar el valor HASTA");
				hasta=0;
				}
			if (desde==""){
				//alert("Debe Ingresar el valor HASTA");
				desde=0;
					}
			
			
			form_info.renta_desde.value=desde;
			form_info.renta_hasta.value=hasta;
			
					
			var tipo ="";
			
			for (i=0;i<=5;i++){
				if (form_op.radiobutton[i].checked==true)
					tipo =form_op.radiobutton[i].value;
			}
			
			
			//alert(tipo);
			if(tipo=="nt"){
				criterio += " AND ( n_total >= "+desde+") AND ( n_total <= "+hasta+")";
				form_info.tipo_renta.value="Neto Total"
				}
			if(tipo=="nz"){
				criterio += " AND ( n_zona >= "+desde+") AND ( n_zona <= "+hasta+")";
				form_info.tipo_renta.value="Neto Zona"
				}
			if(tipo=="nr"){
				criterio += " AND ( n_reg >= "+desde+") AND ( n_reg <= "+hasta+")";
				form_info.tipo_renta.value="Neto Regular"
				}
			if(tipo=="bt"){
				criterio += " AND ( b_total >= "+desde+") AND ( b_total <= "+hasta+")";
				form_info.tipo_renta.value="Bruto Total"
				}
			if(tipo=="bz"){
				criterio += " AND ( b_zona >= "+desde+") AND ( b_zona <= "+hasta+")";
				form_info.tipo_renta.value="Bruto Zona"
				}
			if(tipo=="br"){
				criterio += " AND ( b_reg >= "+desde+") AND ( b_reg <= "+hasta+")";
				form_info.tipo_renta.value="Bruto Regular"
				}
				
		}//_6		
		if ((form_op.opcionINFU012.value=="_2") || (form_op.opcionINFU012.value=="_4") || (form_op.opcionINFU012.value=="_7"))		{
			if (form_op.colab_cargo.value != "T")
				criterio += " AND ( cargo_id = '"+form_op.colab_cargo.value+"')";
		}//_2, _4, _7

		if ((form_op.opcionINFU012.value=="_3") || (form_op.opcionINFU012.value=="_5"))		{
			if (form_op.colab_nivel.value != "T") {
				criterio += " AND ( solo_nivel = '"+form_op.colab_nivel.value+"')";
			}
		}//_3, _5
		if (form_op.opcionINFU012.value=="_7") 		{
			//informe colaboradores, rentas promedio
			criterio += " GROUP BY cargo_id, cargo_desc";
		}

	} //infu012
	
	
	//infu 0009
	if (info == "INFU009"){
		if (form_op.opcionINFU009.value=="_2")
			criterio += " and unidad_orig <> unidad_dest ";
		if (form_op.opcionINFU009.value=="_3")
			criterio += " and cargo_orig <> cargo_dest ";
	//alert(criterio);
	}
	if (existe_en(info, op_periodo)) {
		form_info.peri_ano.value = form_op.peri_ano.value;
		form_info.peri_mes.value = form_op.peri_mes.value;
		if (info != "INFU002")
			criterio += " AND (periodo = "+form_op.peri_ano.value+form_op.peri_mes.value+")";
	}
	else {
		form_info.peri_ano.value = "";
		form_info.peri_mes.value = "";
	}
	if (existe_en(info, op_rango)) {
		form_info.fecha_desde.value = form_op.d_ano.value + "-" + form_op.d_mes.value + "-"+ form_op.d_dia.value ;
		form_info.fecha_hasta.value = form_op.h_ano.value + "-" + form_op.h_mes.value + "-"+ form_op.h_dia.value ;
		/*if (info == "INFU006") {//fecha dia y mes
			criterio = criterio +" AND (day(fecha) >= "+form_op.d_dia.value+") AND (MONTH(fecha) >= "+form_op.d_mes.value+")"+
				" AND (day(fecha) <= "+form_op.h_dia.value+") AND (MONTH(fecha) <= "+form_op.h_mes.value+")";
		}
		else {// fecha dia, mes y a?o*/
			criterio = criterio + " AND (fecha >= CONVERT(DATETIME, '"+form_info.fecha_desde.value+"', 102))"+
				" AND (fecha <= CONVERT(DATETIME, '"+form_info.fecha_hasta.value+"', 102))";
			if (info == "INFU007") {
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
		//}
	}
	else {
		form_info.fecha_desde.value = "";
		form_info.fecha_hasta.value = "";
	}
	if (info == "INFU001") { // informe de dortacion
		var op_i1 = form_op.opcionINFU001.value;
		if (op_i1 == "_2")  { // sobredotacion
			criterio += " AND sobredotacion > 0"
		} 
		else if (op_i1 == "_3")  { // vacantes
			criterio += " AND vacantes > 0"
		}
	} 
	//***Dependiendo del Informe seleccionado,
	//**se controla el accesso correspondiente
	//*******Remuneraciones
	if ((info=="INFU002")||(info=="INFU004")||(info=="INFU008")||(info=="INFU012")){	
	       if (form_op.puede_ver_remu.value=="N"){
	         criterio=criterio+" AND (rut IN " +
                              "(SELECT rut " +
                              "FROM eje_ges_airh_df_trabajadores " +
                              "WHERE " +
                                  "(rut NOT IN " +//**Los que no puede ver
	                                  "(SELECT uar.rut_trab " +
	                                  "FROM eje_ges_airh_df_usuario_acceso_rut uar INNER JOIN " +
	                                  "eje_ges_airh_df_usuario_acceso ua ON " +
	                                  "uar.rut_usuario = ua.rut_usuario AND " +
	                                  "uar.acc_id = ua.acc_id " +
	                                  "WHERE (ua.acc_tipo = 'R') AND " +
	                                  "(ua.acc_id = 'df_exp_remu') AND " +
	                                  "(ua.rut_usuario = "+form_op.rut_user.value+") AND " +
	                                  "(ua.acc_puede_ver = 'N')"+
	                                 ")"+
	                         ")))";
	         }//Informe
	       if (form_op.puede_ver_remu.value=="S"){ 
                   criterio=criterio+" AND (rut IN " +
                              "(SELECT rut " +
                              "FROM eje_ges_airh_df_trabajadores " +
                              "WHERE " +
                                  "(rut IN " +//**Los que no puede ver
	                                  "(SELECT uar.rut_trab " +
	                                  "FROM eje_ges_airh_df_usuario_acceso_rut uar INNER JOIN " +
	                                  "eje_ges_airh_df_usuario_acceso ua ON " +
	                                  "uar.rut_usuario = ua.rut_usuario AND " +
	                                  "uar.acc_id = ua.acc_id " +
	                                  "WHERE (ua.acc_tipo = 'R') AND " +
	                                  "(ua.acc_id = 'df_exp_remu') AND " +
	                                  "(ua.rut_usuario = "+form_op.rut_user.value+") AND " +
	                                  "(ua.acc_puede_ver = 'S')"+
	                            ")"+
	                        ")))";
                 }
            }     
         //*******Horas Extras
	if (info=="INFU003"){	
	       if (form_op.puede_ver_adm.value=="N"){
	         criterio=criterio+" AND (rut IN " +
                              "(SELECT rut " +
                              "FROM eje_ges_airh_df_trabajadores " +
                              "WHERE " +
                                  "(rut NOT IN " +//**Los que no puede ver
	                                  "(SELECT uar.rut_trab " +
	                                  "FROM eje_ges_airh_df_usuario_acceso_rut uar INNER JOIN " +
	                                  "eje_ges_airh_df_usuario_acceso ua ON " +
	                                  "uar.rut_usuario = ua.rut_usuario AND " +
	                                  "uar.acc_id = ua.acc_id " +
	                                  "WHERE (ua.acc_tipo = 'R') AND " +
	                                  "(ua.acc_id = 'df_exp_adm_tiempo') AND " +
	                                  "(ua.rut_usuario = "+form_op.rut_user.value+") AND " +
	                                  "(ua.acc_puede_ver = 'N')"+
	                                 ")"+
	                         ")))";
	         }//Informe
	       if (form_op.puede_ver_adm.value=="S"){ 
                   criterio=criterio+" AND (rut IN " +
                              "(SELECT rut " +
                              "FROM eje_ges_airh_df_trabajadores " +
                              "WHERE " +
                                  "(rut IN " +//**Los que no puede ver
	                                  "(SELECT uar.rut_trab " +
	                                  "FROM eje_ges_airh_df_usuario_acceso_rut uar INNER JOIN " +
	                                  "eje_ges_airh_df_usuario_acceso ua ON " +
	                                  "uar.rut_usuario = ua.rut_usuario AND " +
	                                  "uar.acc_id = ua.acc_id " +
	                                  "WHERE (ua.acc_tipo = 'R') AND " +
	                                  "(ua.acc_id = 'df_exp_adm_tiempo') AND " +
	                                  "(ua.rut_usuario = "+form_op.rut_user.value+") AND " +
	                                  "(ua.acc_puede_ver = 'S')"+
	                            ")"+
	                        ")))";
                 }
            }  
             //*******Datos Personales
	if ((info=="INFU005")||(info=="INFU006")||(info=="INFU009")){	
	       if (form_op.puede_ver_pers.value=="N"){
	         criterio=criterio+" AND (rut IN " +
                              "(SELECT rut " +
                              "FROM eje_ges_airh_df_trabajadores " +
                              "WHERE " +
                                  "(rut NOT IN " +//**Los que no puede ver
	                                  "(SELECT uar.rut_trab " +
	                                  "FROM eje_ges_airh_df_usuario_acceso_rut uar INNER JOIN " +
	                                  "eje_ges_airh_df_usuario_acceso ua ON " +
	                                  "uar.rut_usuario = ua.rut_usuario AND " +
	                                  "uar.acc_id = ua.acc_id " +
	                                  "WHERE (ua.acc_tipo = 'R') AND " +
	                                  "(ua.acc_id = 'df_exp_dat_pers') AND " +
	                                  "(ua.rut_usuario = "+form_op.rut_user.value+") AND " +
	                                  "(ua.acc_puede_ver = 'N')"+
	                                 ")"+
	                         ")))";
	         }//Informe
	       if (form_op.puede_ver_pers.value=="S"){ 
                   criterio=criterio+" AND (rut IN " +
                              "(SELECT rut " +
                              "FROM eje_ges_airh_df_trabajadores " +
                              "WHERE " +
                                  "(rut IN " +//**Los que no puede ver
	                                  "(SELECT uar.rut_trab " +
	                                  "FROM eje_ges_airh_df_usuario_acceso_rut uar INNER JOIN " +
	                                  "eje_ges_airh_df_usuario_acceso ua ON " +
	                                  "uar.rut_usuario = ua.rut_usuario AND " +
	                                  "uar.acc_id = ua.acc_id " +
	                                  "WHERE (ua.acc_tipo = 'R') AND " +
	                                  "(ua.acc_id = 'df_exp_dat_pers') AND " +
	                                  "(ua.rut_usuario = "+form_op.rut_user.value+") AND " +
	                                  "(ua.acc_puede_ver = 'S')"+
	                            ")"+
	                        ")))";
                 }
            }      
                
	
	
	//alert(criterio);
	//alert("Valido: "+valido);		
	if (valido) { // si valido genera informe
		if ( (info == "INFU009") || (info == "INFU001") || (info == "INFU012")) {
			eval("info = info + form_op.opcion"+info+".value");
			//alert("info ahora:"+info);
		} 
		form_info.cual.value = info;
				
		form_info.criterio.value = criterio;
		form_info.peri_ano.value = form_op.peri_ano.value;
		form_info.peri_mes.value = form_op.peri_mes.value;
		form_info.action ="../servlet/GeneraInforme";
		form_info.target ="_blank";
		
		//Remuneraciones
		if ((info=="INFU002")||(info=="INFU004")||(info=="INFU008")||(info=="INFU012_1")){	
		    if (form_op.puede_ver_remu.value!="NADA"){
		          // v = open('','Informe','width=760,height=520,scrollbars=1, toolbar=1');
			   //v.focus();
			   //parent.AbrirVentProcesando();		
			   form_info.submit();
	   	    }else
			alert("No tiene acceso a este Informe.");   
                 }
                 //Horas Extras
		if (info=="INFU003"){	
		    if (form_op.puede_ver_adm.value!="NADA"){
		           //v = open('','Informe','width=760,height=520,scrollbars=1, toolbar=1');
			   //v.focus();
			   //parent.AbrirVentProcesando();		
			   form_info.submit();
	   	    }else
			alert("No tiene acceso a este Informe.");   
                 }
                 //**
                 if ((info=="INFU001_1")||(info=="INFU007")){	
		    //if (form_op.puede_ver_adm.value!="NADA"){
		           v = open('','Informe','width=760,height=520,scrollbars=1, toolbar=1');
			   v.focus();
			   parent.AbrirVentProcesando();		
			   form_info.submit();
	   	   // }else
			//alert("No tiene acceso a este Informe.");   
                 }
                 //Datos Personales
                 if ((info=="INFU005")||(info=="INFU006")||(info=="INFU009_1")){	
                    //alert("Info= "+info);                 	
		    if (form_op.puede_ver_pers.value!="NADA"){
		           v = open('','Informe','width=760,height=520,scrollbars=1, toolbar=1');
			   v.focus();
			   parent.AbrirVentProcesando();		
			   form_info.submit();
	   	    }else
			alert("No tiene acceso a este Informe.");   
                 }
                 
                 
	}
}