
function existeFoto(foto,reemplazo) {
     if (!reemplazo)
        foto.src = "../servlet/Tool?images/trabajadores/sinfoto.jpg";
     else
       foto.src = reemplazo;    
}

