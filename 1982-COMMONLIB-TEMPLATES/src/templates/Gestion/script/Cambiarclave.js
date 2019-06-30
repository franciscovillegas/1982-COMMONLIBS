function Valida() { 
var rutAnt=document.form1.x1.value;//BD
var claveAnt=document.form1.x2.value;//BD
var rutIngre=document.form1.mRut.value;
var claveIngre=document.form1.mClave.value;
var nuevaClave=document.form1.nClave.value;
var confirma=document.form1.repit.value;
if (rutAnt==rutIngre){
  //alert("en BD: "+claveAnt+"Ingresada : "+claveIngre); 
  if (claveAnt==claveIngre){
     if (nuevaClave==confirma){
       if ((nuevaClave=="")||(nuevaClave==""))
          alert("El uso de vacio no es v&#225;lido.");  
       else{ //validar largo de la clave...
         if (nuevaClave.length < 4){
           alert("El largo minimo es de 4 caracteres.");  
           document.form1.nClave.value="";
           document.form1.repit.value=""; 
          }
         else {
            //alert("Los datos han sido registrados.");
		    document.form1.submit();
          }
        }
     }else{
        alert("Revise confirmaci&#243;n.");
        document.form1.nClave.value="";
        document.form1.repit.value="";
     }
  }
  else {
    
		alert("La clave ingresada es distinta a la registrada.");
		document.form1.mClave.value="";   
  }
}
else{
  alert("El rut ingresado es distinto al registrado.");
  document.form1.mRut.value="";
 }
}

function compara_claves(nuevaClave, confirma){
	var valido = true;
   	if (nuevaClave==confirma) {
   		if (nuevaClave.length < 4) {
			alert("El largo minimo es de 4 caracteres.");
			valido = false;
		}
   	}
	else {
		alert("Revise confirmaci&#243;n.");
		valido = false;
	}
	return valido;
}