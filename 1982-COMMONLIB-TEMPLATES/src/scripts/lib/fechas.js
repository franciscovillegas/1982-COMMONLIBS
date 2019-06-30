var dias = new Array();
var meses = new Array();
var meses_num = new Array();
var anos = new Array();

function carga_dias(){ 
    dias.length=0;
	for (var x=1;x<=31 ; x++)
	{
		dias[dias.length] = new Option(x,x);
	}
}

function carga_meses(){
    meses.length=0;
	meses[meses.length] = new Option('Enero','1');
	meses[meses.length] = new Option('Febrero','2');
	meses[meses.length] = new Option('Marzo','3');
	meses[meses.length] = new Option('Abril','4');
	meses[meses.length] = new Option('Mayo','5');
	meses[meses.length] = new Option('Junio','6');
	meses[meses.length] = new Option('Julio','7');
	meses[meses.length] = new Option('Agosto','8');
	meses[meses.length] = new Option('Septiembre','9');
	meses[meses.length] = new Option('Octubre','10');
	meses[meses.length] = new Option('Noviembre','11');
	meses[meses.length] = new Option('Diciembre','12');

}

function carga_meses_num(){
    meses_num.length=0;
	meses_num[meses_num.length] = new Option('1','1');
	meses_num[meses_num.length] = new Option('2','2');
	meses_num[meses_num.length] = new Option('3','3');
	meses_num[meses_num.length] = new Option('4','4');
	meses_num[meses_num.length] = new Option('5','5');
	meses_num[meses_num.length] = new Option('6','6');
	meses_num[meses_num.length] = new Option('7','7');
	meses_num[meses_num.length] = new Option('8','8');
	meses_num[meses_num.length] = new Option('9','9');
	meses_num[meses_num.length] = new Option('10','10');
	meses_num[meses_num.length] = new Option('11','11');
	meses_num[meses_num.length] = new Option('12','12');

}

function carga_anos(ano_ini,ano_ter){
     anos.length=0;
	for (var x=ano_ini;x<=ano_ter ; x++)
	{
		anos[anos.length] = new Option(x,x);
	}
}


function listaLlenarFechas(lista, array, opcionExtra){
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

function CargaDias(obj_lista){
	carga_dias();
	listaLlenarFechas(obj_lista, dias, new Option('', ''));
}
function CargaMeses(obj_lista){
	carga_meses();
	listaLlenarFechas(obj_lista, meses, new Option('', ''));
}

function CargaMesesNum(obj_lista){
	carga_meses_num();
	listaLlenarFechas(obj_lista, meses_num, new Option('', ''));
}
function CargaAnos(obj_lista,ano_ini,ano_ter){
	carga_anos(ano_ini,ano_ter);
	listaLlenarFechas(obj_lista, anos, new Option('', ''));
}

function Revisa(){
 var valor1=false; //*****returnValue
 var valor2=false; //*****returnValue
 var desde=false;
 var hasta=false;
 var reintegro=false;
 var dias=false;
 var diadesde=0,mesdesde=0,yeardesde=0;
 var diahasta=0,meshasta=0,yearhasta=0;
 var dre=0,mre=0,yre=0;

 diadesde=parseInt(document.form1.desdedia.value);
 mesdesde=parseInt(document.form1.desdemes.value);
 yeardesde=parseInt(document.form1.desdeyear.value);
 
 diahasta=parseInt(document.form1.hastadia.value);
 meshasta=parseInt(document.form1.hastames.value);
 yearhasta=parseInt(document.form1.hastayear.value);

 dre=parseInt(document.form1.dr.value);//fecha reintegro
 mre=parseInt(document.form1.mr.value);
 yre=parseInt(document.form1.yr.value);
 
 desde=Fecha(diadesde,mesdesde,yeardesde);//validar año bisiesto
 hasta=Fecha(diahasta,meshasta,yearhasta);//validar año bisiesto
 reintegro=Fecha(dre,mre,yre);//validar año bisiesto
 
 diasd=Dias(diadesde,mesdesde);
 diash=Dias(diahasta,meshasta);
 diasr=Dias(dre,mre);
 if ((desde)&&(hasta)&&(reintegro)&&(diasd)&&(diash)&&(diasr))//***fechas validas año bisiesto y dias mes(30,31,etc...)
 {    valor1=Diferencia(diadesde,mesdesde,yeardesde,diahasta,meshasta,yearhasta);
      valor2=Diferencia(diahasta,meshasta,yearhasta,dre,mre,yre);
 }
 if ((valor1)&&(valor2))
     return true;
 else
     return false;    
}

function Dias(dia,mes){
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


function Diferencia(dd,md,yd,dh,mh,yh){
//alert(dd +" " +md +" " +yd +"\n"+dh+" " +mh +" " +yh);
    if (yd <= yh){
      if (md == mh)
        return ValidaDias(dd,dh);
      else if (md < mh)
              return true;
    }else  return false;//años
}

function ValidaDias(d1,d2){
//alert(d1+"  "+d2);	
 if (d1 == d2)//dias mismo mes
      return false;
 if (d1 < d2) 
     return true;
 else
     return false;//diad mayor diah
}


function Fecha(dia,mes,year){
   var resto=0;
   resto=year%4;
   if ((resto!=0) && (dia==29) && (mes==2))//no bisiesto
      return false;
    else 
      return true;
}
