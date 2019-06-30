/*
parent.AbrirVentProcesando();
*/

function SE(quitar) {
	var link = '../servlet/SetEstado';
	if (quitar)
		link += "?q=yes";
	//alert(link);
	var win = open(link, 'finsesion', "top=100,left=100,width=1,height=1");
	return true;
}

/**********************************************/
var beIE = document.all?true:false

function openFrameless(url, nomvent, windowW,windowH){
	var s = "scrollbars=no, width="+windowW+",height="+windowH;
	var windowX = (screen.width/16)-(windowW/16);
    var windowY = (screen.height/16)-(windowH/16);

	if (beIE){
		NFW = window.open("",nomvent,"width=0,height=0;"+s)     
		//NFW.blur()
		//window.focus()       
		NFW.resizeTo(windowW,windowH)
		NFW.moveTo(windowX,windowY)
		var frameString="<html><head><title></title></head>"+
			"<frameset rows='*,0' framespacing=0 border=0 frameborder=0>"+
			"<frame name='top' src='"+url+"' scrolling='no'>"+
			"<frame name='bottom' src='about:blank' scrolling='no'>"+
			"</frameset>"+
		"</html>"
		NFW.document.open();
		NFW.document.write(frameString);
		NFW.document.close();
		//NFW.document.location.href = url;
	} 
	else {
		NFW=window.open(url, nomvent, s);
		NFW.blur()
		window.focus() 
		NFW.resizeTo(windowW,windowH)
		NFW.moveTo(windowX,windowY)
	}   
	NFW.focus()
	return NFW;
}
/**********************************************/

function AbrirVentProcesando() {
	var url = "../servlet/procesando";
	//ventmsg = open(url, "Procesando", "top=250,left=300,width=240,height=120");
	ventmsg = openFrameless(url, "Procesando", 250, 130);
	//ventmsg.focus();
}

function AbrirVentProcesandoPer(empresa,unidad) {
	var url = "../servlet/procesando?empresa="+empresa+"&unidad="+unidad;
	ventmsg = open(url, "Procesando", "top=250,left=300,width=240,height=120");
	//ventmsg.focus();
}

function AbrirVentProcesandoBuscar(empresa,unidad,rut,nombre,paterno,materno) {
	var url = "../servlet/procesando?empresa="+empresa+"&unidad="+unidad+"&rut="+rut+"&nombre="+nombre+"&amaterno="+materno+"&apaterno="+paterno;
	//alert(url);
	ventmsg = open(url, "Procesando", "top=250,left=300,width=240,height=120");
	//ventmsg.focus();
}

function CerrarVentProcesando() {
	//ventmsg = open("", "Procesando", "top=250,left=300,width=240,height=120");
	if (!ventmsg.closed){
		ventmsg.close();
	}
}

function AbrirBGrafica() {
	var url2 = "Tool?htm=Gestion/InfoUsuario/BusquedaGrafica/JerarquiaOrg.htm";
	ventmsg2 = open(url2, "OrganigramaDF", "width=700,height=400,left=50,top=110");
	//ventmsg2.focus();
}

function AbrirBGaleria() {
	//var url2 = "/servlet/JerarquiaOrg?cual=2";
	var url2 = "Tool?htm=Gestion/BusquedaPorFiltros/Busqueda.htm";
	ventmsg2 = open(url2, "Galeria", "width=720,height=428,left=50,top=110");
	//ventmsg2.focus();
}

function AbrirBAlfa() {
	var url2 = "../servlet/Buscar_por_Nombre?Operacion=1";
	ventmsg2 = open(url2, "BusAlfa", "width=640,height=355,left=50,top=110");
	//ventmsg2.focus();
}


function Simulacion(rut){
	var url2 = "../servlet/SimulaRenta?rut="+rut;
	ventmsg2 = open(url2, "Simulacion", "scrollbars=yes,width=680,height=500,left=50,top=110");


}
function PerfilLaboral(rut){
	var url2 = "../servlet/GeneraInforme?criterio=where rut="+rut+"&cual=INFU013";
	ventmsg2 = open(url2, "PerfilLaboral", "scrollbars=yes,width=800,height=500,left=50,top=110");
}

/*<script language="JavaScript">
parent.CerrarVentProcesando();
</script>*/
