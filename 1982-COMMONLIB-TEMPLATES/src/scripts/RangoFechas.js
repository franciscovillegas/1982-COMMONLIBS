function RangoValido(dd,md,yd,dh,mh,yh){
 var desde=false;
 var hasta=false;
 /*var diadesde=document.form1.diad.value;
 var mesdesde=document.form1.mesd.value;
 var yeardesde=document.form1.yeard.value;
 
 var diahasta=document.form1.diah.value;
 var meshasta=document.form1.mesh.value;
 var yearhasta=document.form1.yearh.value;*/
 var diadesde=dd;
 var mesdesde=md;
 var yeardesde=yd;
 
 var diahasta=dh;
 var meshasta=mh;
 var yearhasta=yh;
 
 desde=Fecha(diadesde,mesdesde,yeardesde);//validar año bisiesto
 hasta=Fecha(diahasta,meshasta,yearhasta);//validar año bisiesto
 diasd=Dias(diadesde,mesdesde);
 diash=Dias(diahasta,meshasta);
 //alert("Entre!!!");
 //***fechas validas año bisiesto y dias mes(30,31,etc...)
 if ((desde)&&(hasta)&&(diasd)&&(diash))
 {    valor1=Diferencia(diadesde,mesdesde,yeardesde,diahasta,meshasta,yearhasta);
      //alert("val1: "+valor1);
      //valor2=Diferencia(diahasta,meshasta,yearhasta,dre,mre,yre);
      //alert("val2: "+valor2);
 }
 if (valor1)
     return true;
 else
     return false;    
}

function Diferencia(dd,md,yd,dh,mh,yh){
//alert(dd +" " +md +" " +yd +"\n"+dh+" " +mh +" " +yh);
if (yd <= yh){
	//alert("Entre!!"); 
	if (md == mh)
	    return ValidaDias(dd,dh);
	else 
	  if (md < mh)
	     return true;
	  else
	   if (yd < yh)
	     return true;
	   else
	     return false;  
	  
	}else{  
	  //alert("Validacion años");
	   return false;//años
	}
}

function fechaValida(dia, mes, agno) {
	var valido = false;
	mes = mes - 1;
	var fecha = new Date(agno, mes, dia);
	//alert(dia+"/"+mes+"/"+agno+"\nFechaDate: "+fecha)
	if ((dia == fecha.getDate()) && (mes == fecha.getMonth()) && !(dia == fecha.getFullYear()))
		valido = true;
	return valido;
}

function Fecha(dia,mes,year){
   var resto=0;
   resto=year%4;
   if ((resto!=0) && (dia==29) && (mes==2))//no bisiesto
      return false;
    else 
      return true;
}

function Dias(dia,mes){
  //alert ("dia: "+dia+" mes: "+mes);
  var valor = true; 
  switch (mes){
     case "02":if ((dia=="30")||(dia=="31"))  //febrero 
                  valor=false;
     case "09":if (dia=="31")  //septiembre 
                  valor=false;
     case "11":if (dia=="31")  //noviembre 
                  valor=false;
  }
  //alert(valor);
  return valor;
}
function ValidaDias(d1, d2){
 //alert("desde: "+d1+" -->hasta: "+d2);	
 //if (d1 == d2)//dias mismo mes
   //   return false;
 if (d1 <= d2) {
   //  alert("Bien!");
	 return true;
 }
 else
     return false;//diad mayor diah
}