
function existeFoto(foto,reemplazo) {
    //alert(reemplazo);
    
       foto.onerror = null;
       
      
   	   var urlConCero = "../../../temporal/imgtrabajadores/0"+foto.src.substring(foto.src.lastIndexOf("/") + 1);
   	   var urlSinCero = "../../../temporal/imgtrabajadores/"+foto.src.substring(foto.src.lastIndexOf("/") + 1);
 
	   imageExists( urlSinCero , function(exists) {
		  
		  if( exists ) {
			  foto.src = urlSinCero;
		  }
		  else {
			  imageExists( urlConCero , function(exists) {
				  if( exists ) {
					  foto.src = urlConCero;
				  }
				  else {
					  if (!reemplazo) {
						  foto.src = "../../../servlet/Tool?images/sinfoto.jpg";
					  } else {
					      foto.src = reemplazo;
					  }
				  }
			  });
			  
		  }
	   });

   
}

function imageExists(url, callback) {
	  var img = new Image();
	  img.onload = function() { callback(true); };
	  img.onerror = function() { callback(false); };
	  img.src = url;
}