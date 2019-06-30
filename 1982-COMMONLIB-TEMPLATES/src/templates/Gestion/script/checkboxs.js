function Marca(objeto) { 
var x=0;

/*for (x=0;x<objeto.length;x++){
    objeto[x].checked=true;
}*/

for (x=0;x<objeto.length;x++){
     if (objeto[x].checked==false)
       objeto[x].checked=true;
     else
       objeto[x].checked=false;
 }   
}


