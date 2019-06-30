$(document).ready(function(){
			var papa = window.opener;

			if(papa != null) {
				papa.location.reload(true); 
			}

			$('#carousel_ul li:first').before($('#carousel_ul li:last')); 
	        $('#right_scroll img').click(function(){	
				var item_width = $('#carousel_ul li').outerWidth() + 10;
	            var left_indent = parseInt($('#carousel_ul').css('left')) - item_width;
	            $('#carousel_ul:not(:animated)').animate({'left' : left_indent},500,function(){    
	                $('#carousel_ul li:last').after($('#carousel_ul li:first')); 
	                $('#carousel_ul').css({'left' : '-210px'});
	            }); 
			});
	        
	        $('#left_scroll img').click(function(){
	            var item_width = $('#carousel_ul li').outerWidth() + 10;
	            var left_indent = parseInt($('#carousel_ul').css('left')) + item_width;
	            $('#carousel_ul:not(:animated)').animate({'left' : left_indent},500,function(){    
	            $('#carousel_ul li:first').before($('#carousel_ul li:last')); 
	            $('#carousel_ul').css({'left' : '-210px'});
	            });
	        });
	        
	        
	        $.get('../servlet/Tool?xml/noticias.xml', function(xml) {
	        	$(xml).find('noticia').each(function(){
	        	  var $noticia = $(this);   
	                var titulo = $noticia.find("titulo").text();  
	                var texto = $noticia.find('texto').text(); 
	                var icono = $noticia.find('icono').text();
	                var enlace = $noticia.find('enlace').text();
	                var html = '<li>';  
	                html += '<a href="#' + enlace + '" rel="prettyPhoto[inline]">';  
	                html += '<table width="200" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">';  
	                html += '<tr>';  
	                html += '	<td width="50" height="70"><img src="https://www.ejedigital.cl/noticias/' + icono + '"></td>';  
	                html += '	<td width="150" style="text-align:justify; padding-left:5px; padding-right:5px; padding-top:1px;"><strong>' + titulo + '</strong><br>';
	                html += '	<p>' + texto + '</p>';
	                html += '	<p>&nbsp;</p>';
	                html += '	</td>';
	                html += '</tr>';
	                html += '</table>'; 
	                html += '</a>';
	                html += '</li>';
	                $('#carousel_ul').append($(html));
	                
	                
	                var titulo2 = $noticia.find("titulo2").text();  
	                var texto2 = $noticia.find('texto2').text(); 
	                var icono2 = $noticia.find('icono2').text();
	                var html2 = '<div id="' + enlace + '" style="display:none;">';
	                html2 += '<h1><img src="https://www.ejedigital.cl/noticias/' + icono + '"> ' + titulo2 + '</h1>';
	                html2 += '<p>' + texto2 + '</p>';
	                html2 += '<p><img src="https://www.ejedigital.cl/noticias/' + icono2 + '"></p>';
	                html2 += '</div>';
	                $('#main').append($(html2));
	                
	        	});
	        		
	        	$("area[rel^='prettyPhoto']").prettyPhoto();
				$(".gallery:first a[rel^='prettyPhoto']").prettyPhoto({animation_speed:'normal',theme:'light_square',slideshow:3000, autoplay_slideshow: false});
				$(".gallery:gt(0) a[rel^='prettyPhoto']").prettyPhoto({animation_speed:'fast',slideshow:10000, hideflash: true});
				$("#custom_content a[rel^='prettyPhoto']:first").prettyPhoto({
					custom_markup: '<div id="map_canvas" style="width:260px; height:265px"></div>',
					changepicturecallback: function(){ initialize(); }
				});
				$("#custom_content a[rel^='prettyPhoto']:last").prettyPhoto({
					custom_markup: '<div id="bsap_1259344" class="bsarocks bsap_d49a0984d0f377271ccbf01a33f2b6d6"></div><div id="bsap_1237859" class="bsarocks bsap_d49a0984d0f377271ccbf01a33f2b6d6" style="height:260px"></div><div id="bsap_1251710" class="bsarocks bsap_d49a0984d0f377271ccbf01a33f2b6d6"></div>',
					changepicturecallback: function(){ _bsap.exec(); }
				});
				
				
	        	        	
	        });
		});
	

function  boton_numerico(digito) {
		f = document.form1;
		if (f.rut.value.length < 8) {
			temp = f.rut.value;
			f.rut.value = temp + digito;
			if (f.rut.value.length == 8) {
				f.dig.focus();			
			}
		} 
		else if (f.dig.value == "") {
			f.dig.focus();
			temp_dig = f.dig.value;
			f.dig.value = temp_dig + digito;
			f.clave.focus();
		} 
		else {
			f.clave.focus();
			temp_clave = f.clave.value;
			f.clave.value = temp_clave + digito;
		}
	}

	function borrar () {
		f = document.form1;
		f.rut.value = "";
		f.dig.value = "";
		f.clave.value = "";		
		f.rut.focus();
	}

	function loginIngreso(f) {
		var valor = document.getElementById("param").value;
		if (!checkDigitoV(f.rut.value, f.dig.value)) {
			alert('Rut no v�lido...');
			f.rut.focus();
		}
		else {
			var dom = window.location;			
			dom = replaceAll( dom, ':', '_' );
			dom = replaceAll( dom, '.', '_' );
			dom = replaceAll( dom, '/', '_' );

			if(valor=="true"){
				var ventanalogin = open("about:blank", dom  , "toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=780,height=500,left=5,top=5");         	
			}
			
			f.target= dom ;

			f.submit();

			if(window.name != dom) {
				//setTimeout ("window.document.location.reload();", 2000); 
				
			}
		}
		return false;
	}
	
	
	function loginIngresoMismaVentana(f) {
		console.log(f);
		if (!checkDigitoV(f.rut.value, f.dig.value)) {
			alert('Rut no v�lido...');
			f.rut.focus();
		}
		else {	
			var a = $("#htm").val() +"?rut="+$("#rut").val();
			$("#htm").val(a);
			f.submit();

			
			//setTimeout ("window.document.location.reload();", 2000); 

		}
		return false;
	}
	
	

	function replaceAll( text, busca, reemplaza ){
		while (text.toString().indexOf(busca) != -1) {
				text = text.toString().replace(busca,reemplaza);
		}

		return text;
	}
	

	function inicio(){
		document.form1.rut.focus();
	}

	function MM_preloadImages() { //v3.0
		document.form1.rut.focus();
		var d=document; 
		if(d.images){ 
			if(!d.MM_p) 
				d.MM_p=new Array();
    	var i,j=d.MM_p.length,a=MM_preloadImages.arguments; 
    	for(i=0; i<a.length; i++)
    		if (a[i].indexOf("#")!=0){ 
        		d.MM_p[j]=new Image; 
        		d.MM_p[j++].src=a[i];
        	}
    	}
	}
	
	function recuperaClave() {
		jPrompt('Ingrese el rut de la persona que desea recuperar clave', "11111111-1", 'Recuperar clave', function(r) {
			var rut = r; 
			if (Valida_rut(rut)) {
				if(r != null && r != '') {
					loadJSONforMain('../servlet/EjeCoreI?claseweb=cl.eje.mail.modulos.RecuperaClave',
							'GET',{"rut":r,"accion":"recovery", "thing":"pass"},'json',
							function(data){
								var msg = " " + data.msg;
								
								jAlert(msg,"Contacto");
								if(data.estado == "false") {
									jPrompt('Ingrese su correo electr�nico, a este correo lo contactaremos', "correo@ejemplo.cl", 'Contacto', function(r) {
										if(r != null && r != '') {
											var correo = r;
											jPrompt('Ingrese alg�n n�mero de contacto, esta ser� la segunda opci�n en caso que no funcione el correo', "09 9 9876 5432", 'Contacto', function(r) {
												if(r != null && r != '') {
													var fono = r;
													
													loadJSONforMain('../servlet/EjeCoreI?claseweb=cl.eje.mail.modulos.RecuperaClave',
															'GET',{"rut":rut,"correo":correo,"fono":fono ,"accion":"help", "thing":"pass"},'json',
															
															function(data){
																jAlert("Agradecemos tu tiempo, nos contactaremos contigo r�pidamente.","Contacto");
															});
												}
											});
										}
									});
								}
							});
				}
			}
		});
	}
//-->

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

//VALIDACION CAMPO RUT

function Valida_rut(texto)
{	
	var tmpstr = "";	
	for (i=0; i < texto.length ; i++ )		
		if ( texto.charAt(i) != ' ' && texto.charAt(i) != '.' && texto.charAt(i) != '-' )
			tmpstr = tmpstr + texto.charAt(i);	
	texto = tmpstr;	
	largo = texto.length;	

	if ( largo < 2 )	
	{		
		jAlert("El Rut ingresado es inv�lido","Atenci�n");	
		window.document.form1.rut.focus();		
		window.document.form1.rut.select();		
		return false;	
	}	

	for (i=0; i < largo ; i++ )	
	{			
		if ( texto.charAt(i) !="0" && texto.charAt(i) != "1" && texto.charAt(i) !="2" && texto.charAt(i) != "3" && texto.charAt(i) != "4" && texto.charAt(i) !="5" && texto.charAt(i) != "6" && texto.charAt(i) != "7" && texto.charAt(i) !="8" && texto.charAt(i) != "9" && texto.charAt(i) !="k" && texto.charAt(i) != "K" )
 		{			
			jAlert("El Rut ingresado es inv�lido","Atenci�n");		
			window.document.form1.rut.focus();			
			window.document.form1.rut.select();			
			return false;		
		}	
	}	
	var invertido = "";	
	for ( i=(largo-1),j=0; i>=0; i--,j++ )		
		invertido = invertido + texto.charAt(i);	
	var dtexto = "";	
	dtexto = dtexto + invertido.charAt(0);	
	dtexto = dtexto + '-';	
	cnt = 0;	

	for ( i=1,j=2; i<largo; i++,j++ )	
	{		
		if ( cnt == 3 )		
		{			
			dtexto = dtexto + '.';			
			j++;			
			dtexto = dtexto + invertido.charAt(i);			
			cnt = 1;		
		}		
		else		
		{				
			dtexto = dtexto + invertido.charAt(i);			
			cnt++;		
		}	
	}	
	invertido = "";	
	for ( i=(dtexto.length-1),j=0; i>=0; i--,j++ )		
		invertido = invertido + dtexto.charAt(i);	

	window.document.form1.rut.value = invertido.toUpperCase();		

	if ( revisarDigito2(texto) )		
		return true;	

	return false;
}

function revisarDigito( dvr )
{	
	dv = dvr + "";	
	if ( dv != '0' && dv != '1' && dv != '2' && dv != '3' && dv != '4' && dv != '5' && dv != '6' && dv != '7' && dv != '8' && dv != '9' && dv != 'k'  && dv != 'K')	
	{		
		jAlert("El Rut ingresado es inv�lido","Atenci�n");	
		window.document.form1.rut.focus();		
		window.document.form1.rut.select();		
		return false;	
	}	
	return true;
}

function revisarDigito2( crut )
{	
	largo = crut.length;	
	if ( largo < 2 )	
	{		
		jAlert("El Rut ingresado es inv�lido","Atenci�n");	
		window.document.form1.rut.focus();		
		window.document.form1.rut.select();		
		return false;	
	}	
	if ( largo > 2 )		
		rut = crut.substring(0, largo - 1);	
	else		
		rut = crut.charAt(0);	
	dv = crut.charAt(largo-1);	
	revisarDigito( dv );	

	if ( rut == null || dv == null )
		return 0;	

	var dvr = '0';	
	suma = 0;	
	mul  = 2;	

	for (i= rut.length -1 ; i >= 0; i--)	
	{	
		suma = suma + rut.charAt(i) * mul;		
		if (mul == 7)			
			mul = 2;		
		else    			
			mul++;	
	}	
	res = suma % 11;	
	if (res==1)		
		dvr = 'k';	
	else if (res==0)		
		dvr = '0';	
	else	
	{		
		dvi = 11-res;		
		dvr = dvi + "";	
	}
	if ( dvr != dv.toLowerCase() )	
	{		
		jAlert("El Rut ingresado es inv�lido","Atenci�n");	
		window.document.form1.rut.focus();		
		window.document.form1.rut.select();		
		return false;	
	}

	return true;
}