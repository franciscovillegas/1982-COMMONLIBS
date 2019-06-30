var defaultEmptyOK = false
var checkNiceness = true;
var digits = "0123456789";
var lowercaseLetters = "abcdefghijklmnopqrstuvwxyz???????"
var uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ??????"
var whitespace = " \t\n\r";
var phoneChars = "()-+ ";
var mMessage = "Error: No puede dejar este campo vacio"
var mSeleccion = "Error: No puede dejar esta opci?n sin seleccionar"
var pPrompt = "Error: ";
var pAlphanumeric = "Ingrese un texto que contenga solo letras y/o numeros";
var pAlphabetic   = "Ingrese un texto que contenga solo letras";
var pInteger = "Ingrese un numero entero";
var pNumber = "Ingrese un numero";
var pPhoneNumber = "Ingrese un n?mero de tel?fono";
var pEmail = "Ingrese una direcci?n de correo electr?nico v?lida";
var pName = "Ingrese un texto que contenga solo letras, numeros o espacios";
var pNice = "No puede utilizar comillas aqui";
var pRut = "Rut no V?lido";
var pFecha = "Error:La fecha ingresada no es correcta.\n(Formato: dd/mm/aaaa)";
//isAlphabetic(1),isAlphanumeric (2),isInteger(3),isNumber(4),isEmail(5),isPhoneNumber(6),isName(7)

function anyoBisiesto(anyo){
	if (anyo < 100){var fin = anyo + 1900;}	else {var fin = anyo ;}
	if (fin % 4 != 0) { return false;}
	else {
		if (fin % 100 == 0)	{
			if (fin % 400 == 0)	{return true;} else	{return false;}
		}
		else{ return true; }
	}
}

function isRutDigit (c)
{
	return ((c >= "0") && (c <= "9") || (c=="k") || (c=="K") )
}


function isRut (s)
{
    if (isEmpty(s)) 
       if (isRut.arguments.length == 1) return defaultEmptyOK;
       else return (isRut.arguments[1] == true);
    if (isWhitespace(s)) return false;
    var i = 1;
    var sLength = s.length;

    while ((i < sLength) && (s.charAt(i) != "-") && (isDigit(s.charAt(i))) )
    { i++
    }
    
    
    if ( (s.charAt(i) == "-") && ( (i+1)==(sLength-1) ) && (isRutDigit(s.charAt(i+1)) ) && valida_rut(s) )return true;
    else return false;
}
/*
                nombre =formulario.elements[i].name;
				var elem = new RegExp("rut","gi");
				if (elem.test(nombre)){
				    if ((isEmpty(formulario.elements[i].value)) && (formulario.elements[i].type!="HIDDEN") ) {
					   window.open("../mantenedores/buscador_personas.asp","v2","scrollbars=yes toolbar=no menubar=no width=600 height=600") 
					   return false;}
				 }
*/
function valida_rut(rut)
{
   var IgStringVerificador, IgN, IgSuma, IgDigito, IgDigitoVerificador, rut;
   for( i=10-rut.length; i>0; i-- ) rut = '0' + rut; 
   IgStringVerificador = '32765432';
   IgSuma = 0;
   for( IgN = 0; IgN < 8 && IgN < rut.length; IgN++)
      IgSuma = eval(IgSuma + '+' + rut.substr(IgN, 1) + '*' + IgStringVerificador.substr(IgN, 1) + ';');
   IgDigito = 11 - IgSuma % 11;
   IgDigitoVerificador = IgDigito==10?'K':IgDigito==11?'0':IgDigito;
   if (rut.substr(rut.length-1) == IgDigitoVerificador) {return true;}
   else {return false;}
}

function makeArray(n) {
   for (var i = 1; i <= n; i++) {
      this[i] = 0
   } 
   return this
}

function isEmpty(s)
{   return ((s == null) || (s.length == 0))
}

function isNull(s) {   
	return (typeof(s)=="undefined" || s===null)
}

function isWhitespace (s)
{   var i;
    if (isEmpty(s)) return true;
    for (i = 0; i < s.length; i++)
    {   
        var c = s.charAt(i);
        if (whitespace.indexOf(c) == -1) return false;
    }
    return true;
}
function isFecha(fecha) {
 var str, mes, dia, anyo, febrero;  
 expr = /^[0-9]{2}\/[0-9]{2}\/[0-9]{4}$/;
 str = fecha;
 if((m = str.match(expr))==null) {
	esFecha1 = false;
 }
 else{	
     dia  = fecha.split("/")[0];
     mes  = fecha.split("/")[1];
     anyo = fecha.split("/")[2];   				
	 
     if(anyoBisiesto(anyo)) {febrero=29;} else { febrero=28;}
	 if ((mes<1) || (mes>12)){
		   esFecha1 = false;
	 }
	 else{
	    if ((mes==2) && ((dia<1) || (dia>febrero))){
			   esFecha1 = false;
		}
		else {
		   if (((mes==1) || (mes==3) || (mes==5) || (mes==7) || (mes==8) || (mes==10) || (mes==12)) && ((dia<1) || (dia>31))){
		 	  esFecha1 = false;
		   }
		   else{
			   if (((mes==4) || (mes==6) || (mes==9) || (mes==11)) && ((dia<1) || (dia>30))){
				   esFecha1 = false;
			   }
			   else{
				  esFecha1 = true;
			   }	  
		   }
		}
	  }
   }
   return ( esFecha1 );
}

function stripCharsInBag (s, bag)
{   var i;
    var returnString = "";
 
    for (i = 0; i < s.length; i++)
    {   var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }

    return returnString;
}


function stripCharsNotInBag (s, bag)
{   var i;
    var returnString = "";
    for (i = 0; i < s.length; i++)
    {   
        var c = s.charAt(i);
        if (bag.indexOf(c) != -1) returnString += c;
    }

    return returnString;
}


function stripWhitespace (s)
{   return stripCharsInBag (s, whitespace)
}

function charInString (c, s)
{   for (i = 0; i < s.length; i++)
    {   if (s.charAt(i) == c) return true;
    }
    return false
}

function stripInitialWhitespace (s)
{   var i = 0;
    while ((i < s.length) && charInString (s.charAt(i), whitespace))
       i++;
    return s.substring (i, s.length);
}

function isLetter (c)
{
    return( ( uppercaseLetters.indexOf( c ) != -1 ) ||
            ( lowercaseLetters.indexOf( c ) != -1 ) )
}

function isDigit (c)
{   return ((c >= "0") && (c <= "9"))
}

function isLetterOrDigit (c)
{   return (isLetter(c) || isDigit(c))
}

function isInteger (s)
{   var i;
    if (isEmpty(s)) 
       if (isInteger.arguments.length == 1) return defaultEmptyOK;
       else return (isInteger.arguments[1] == true);
    
    for (i = 0; i < s.length; i++)
    {   
        var c = s.charAt(i);
        if( i != 0 ) {
            if (!isDigit(c)) return false;
        } else { 
            if (!isDigit(c) && (c != "-") || (c == "+")) return false;
        }
    }
    return true;
}


function isNumber (s)
{   var i;
    var dotAppeared;
    dotAppeared = false;
    if (isEmpty(s)) 
       if (isNumber.arguments.length == 1) return defaultEmptyOK;
       else return (isNumber.arguments[1] == true);
    
    for (i = 0; i < s.length; i++)
    {   
        var c = s.charAt(i);
        if( i != 0 ) {
            if ( c == "." ) {
                if( !dotAppeared )
                    dotAppeared = true;
                else
                    return false;
            } else     
                if (!isDigit(c)) return false;
        } else { 
            if ( c == "." ) {
                if( !dotAppeared )
                    dotAppeared = true;
                else
                    return false;
            } else     
                if (!isDigit(c) && (c != "-") || (c == "+")) return false;
        }
    }
    return true;
}

function isAlphabetic (s)
{   var i;

    if (isEmpty(s)) 
       if (isAlphabetic.arguments.length == 1) return defaultEmptyOK;
       else return (isAlphabetic.arguments[1] == true);
    for (i = 0; i < s.length; i++)
    {   
        var c = s.charAt(i);

        if (!isLetter(c))
        return false;
    }
    return true; 
}

function isAlphanumeric (s)
{   var i;

    if (isEmpty(s)) 
       if (isAlphanumeric.arguments.length == 1) return defaultEmptyOK;
       else return (isAlphanumeric.arguments[1] == true);

    for (i = 0; i < s.length; i++)
    {   
        var c = s.charAt(i);
        if (! (isLetter(c) || isDigit(c) ) )
        return false;
    }

    return true;
}


function isName (s)
{
    if (isEmpty(s)) 
       if (isName.arguments.length == 1) return defaultEmptyOK;
       else return (isAlphanumeric.arguments[1] == true);
    
    return( isAlphanumeric( stripCharsInBag( s, whitespace ) ) );
}

function isPhoneNumber (s)
{   var modString;
    if (isEmpty(s)) 
       if (isPhoneNumber.arguments.length == 1) return defaultEmptyOK;
       else return (isPhoneNumber.arguments[1] == true);
    modString = stripCharsInBag( s, phoneChars );
    return (isInteger(modString))
}

function isEmail (s)
{
    if (isEmpty(s)) 
       if (isEmail.arguments.length == 1) return defaultEmptyOK;
       else return (isEmail.arguments[1] == true);
    if (isWhitespace(s)) return false;
    var i = 1;
    var sLength = s.length;
    while ((i < sLength) && (s.charAt(i) != "@"))
    { i++
    }

    if ((i >= sLength) || (s.charAt(i) != "@")) return false;
    else i += 2;

    while ((i < sLength) && (s.charAt(i) != "."))
    { i++
    }

    if ((i >= sLength - 1) || (s.charAt(i) != ".")) return false;
    else return true;
}

function isNice(s)
{
        var i = 0;
        var sLength = s.length;
        var b = 1;
        while(i<sLength) {
                if( (s.charAt(i) == "\"") || (s.charAt(i) == "'" ) ) b = 0;
                i++;
        }
        return b;
}

function statBar (s)
{   window.status = s
}

//-------------------------------------------------------------------
// isArray(obj)
// Returns true if the object is an array, else false
//-------------------------------------------------------------------
function isArray(obj){return(typeof(obj.length)=="undefined")?false:true;}

//-------------------------------------------------------------------
// replaceAll(string, string, string)
// Reemplaza todas las coincidencias de orig en string por dest
//-------------------------------------------------------------------
function replaceAll (string, orig, dest) {
	auxString = string.split(orig)
	string = auxString.join(dest)
	return string;
}

//-------------------------------------------------------------------
// compareFechas(string, string)
// Devuelve true si fecha1 > fecha 2, formato dd/mm/yyyy
//-------------------------------------------------------------------
function compareFechas (fecha1, fecha2) {
	auxfec1=fecha1.split('/');
	auxfec2=fecha2.split('/');
	if (auxfec1[1].length == 1)
		auxfec1[1] = '0' + auxfec1[1];
	if (auxfec1[2].length == 1)
		auxfec1[2] = '0' + auxfec1[2];
	if (auxfec2[1].length == 1)
		auxfec2[1] = '0' + auxfec2[1];
	if (auxfec2[2].length == 1)
		auxfec2[2] = '0' + auxfec2[2];
	fecha1 = auxfec1[2] + auxfec1[1] + auxfec1[0];
	fecha2 = auxfec2[2] + auxfec2[1] + auxfec2[0];
	fecha1 = parseInt(fecha1);
	fecha2 = parseInt(fecha2);
	return (fecha1 > fecha2);
}

function warnEmpty (theField)
{   theField.focus()
    msg=mMessage
    if(theField.type=="select-one") {msg=mSeleccion;}
    alert(msg);
    statBar(msg)
    return false
}

function warnInvalid (theField, s)
{   theField.focus()
    theField.select()
    alert(s)
    statBar(pPrompt + s)
    return false
}

function isAll (s)
{   return true
}

function checkField (theField, theFunction, emptyOK, s)
{   
    var msg;
	theFunction=theFunction.toUpperCase();
	emptyOK=emptyOK.toUpperCase();
    if (checkField.arguments.length < 3) emptyOK = defaultEmptyOK;
    if (checkField.arguments.length == 4) {
        msg = s;
    } else {
        if( theFunction == "LE" ) msg = pAlphabetic;
        if( theFunction == "LN" ) msg = pAlphanumeric;
        if( theFunction == "IN" ) msg = pInteger;
        if( theFunction == "NU" ) msg = pNumber;
        if( theFunction == "EM" ) msg = pEmail;
        if( theFunction == "TE" ) msg = pPhoneNumber;
        if( theFunction == "NO" ) msg = pName;
		if( theFunction == "RU" ) msg = pRut;
		if( theFunction == "FE" ) msg = pFecha;
    }
	
	if( theFunction == "TO" ) theFunction = isAll;
	if( theFunction == "LE" ) theFunction = isAlphabetic;
	if( theFunction == "LN" ) theFunction = isAlphanumeric;
	if( theFunction == "IN" ) theFunction = isInteger;
	if( theFunction == "NU" ) theFunction = isNumber;
	if( theFunction == "EM" ) theFunction = isEmail;
	if( theFunction == "TE" ) theFunction = isPhoneNumber;
	if( theFunction == "NO" ) theFunction = isName;
	if( theFunction == "RU" ) theFunction = isRut;
	if( theFunction == "FE" ) theFunction = isFecha;	
	
	if( emptyOK == "S" ) {emptyOK = true;}else{emptyOK = false;}
    
    if ((emptyOK == true) && (isEmpty(theField.value))) return true;

    if ((emptyOK == false) && (isEmpty(theField.value))) 
        return warnEmpty(theField);

    if ( checkNiceness && !isNice(theField.value))
        return warnInvalid(theField, pNice);

    if (theFunction(theField.value) == true) 
        return true;
    else
        return warnInvalid(theField,msg);

}

function preValidaFormulario(formulario){
var num_elementos=formulario.length
for (i=0;i < num_elementos;i++){
	 if (formulario.elements[i].type!="button" && formulario.elements[i].type!="hidden" && formulario.elements[i].disabled==false){
	    s=formulario.elements[i].id;
		if (s != ''){ // Para que no se caiga al ir un ID de un elemento del formulario vac?o.
			if (!checkField( formulario.elements[i], s.charAt(0)+s.charAt(1), s.charAt(3) )){
				 return false;
			  }
			}
	    }		
	}
	return true;
}


function CA(d){
 for (var i=0;i<d.elements.length;i++){
    var e = d.elements[i];
    if ((e.name != 'check_all') && (e.type=='checkbox')){
       e.checked = d.check_all.checked;
       if (d.check_all.checked){
          hL(e);
       }
       else {
          dL(e);
       }
    }
  }
}

function CCA(CB){
   if (CB.checked)
      hL(CB);
   else
      dL(CB);
}
function hL(E){
 while (E.tagName!="TR"){
      E=E.parentElement;
   }
 E.className = "H";
}
function dL(E){
 while (E.tagName!="TR"){
    E=E.parentElement;
 }
E.className = "";
}
function comilla(palabra){	
  largo = palabra.length;		
  for (i=0; i < largo ; i++ )
  { 
	 var caracter = palabra.charAt(i)	
	 var comilla_simple = caracter.search( /[']/i );
	 if( comilla_simple != -1 ){ 
   		 return true;
	 }
  }
  return false;
}

function roundFun(number,noOfPlaces){
    val = (Math.round(number*Math.pow(10,noOfPlaces)))/Math.pow(10,noOfPlaces)
    val = val.toString()
    if(val.indexOf(".")==-1)
         val = val.toString()// +      ".00"
    return(val)
}

function buscar_persona()
{
var v2;
v2=window.open("../mantenedores/buscador_personas.asp","v2","scrollbars=yes toolbar=no menubar=no width=600 height=600");
}

function buscar_alumno()
{
var v1;
v1=window.open("../mantenedores/buscador_alumnos.asp","v2","scrollbars=yes toolbar=no menubar=no width=600 height=600");
}