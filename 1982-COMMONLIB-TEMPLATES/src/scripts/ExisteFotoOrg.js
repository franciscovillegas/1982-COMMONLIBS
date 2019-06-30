
function existeFoto(path,foto,reemplazo) {
     //alert(reemplazo);
     if (!reemplazo)
        foto.src =path + "sinfoto.jpg";
     else
       foto.src = reemplazo;    
}

