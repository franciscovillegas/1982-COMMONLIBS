//opciones de lik's

function VerFonosRut(rut, htm) {
	var url = "../servlet/VerFonosRut?rut="+rut;
	if (htm) url += "&htm="+htm;
   	var pagina = window.open(url, "VerFonosRut", "width=420,height=325");
	return false;
}

function VerRut(rut, htm) {
	url = "../servlet/VerRutDF?rut="+rut;
	if (htm) url += "&htm="+htm;
   	var pagina = window.open(url, "VerRutDF", "left=200,top=100,width=500,height=300");
	return false;
}

function VerRutContraloria(rut,modo) {
   	var pagina = window.open("../servlet/VerRutDF?rut="+rut+"&modo="+modo, "VerRutDF", "left=350,top=170,scrollbars=yes,width=415,height=302");
	return false;
}

function openBuscarAnexo(url){
	var newVent = open(url, "BuscarAnexo", "width=645,height=360,resizable=no,toolbar=no");
}

function buscarAnexo(anexo){
	var url = "../servlet/BuscarAnexos?anexo="+anexo;
	openBuscarAnexo(url);
	return false;
}

function buscarAnexoRut(rut){
	var url = "../servlet/BuscarAnexos?rut="+rut;
	openBuscarAnexo(url);
	return false;
}

function ventAnexoRel(){
	var url = "../servlet/BuscarAnexos";
	openBuscarAnexo(url);
	return false;
}

function verDetDotacion(unidad, empresa){
	var url = "../servlet/DetDotacion?unidad="+unidad+"&empresa="+empresa;
	var newVent = open(url, "DetDotacion", "width=410,height=310,resizable=no,toolbar=no");
	return false;
}

function verAnotaciones(rut){
	var url = "../servlet/MantenedorHojaVidaDF?buscar=1&rut="+rut;
	var newVent = open(url, "Anotaciones", "width=750,height=400,resizable=no,toolbar=no");
	return false;
}
function verExpediente(rut){
	var url = "/servlet/datafolder.expediente.TopExpediente?rut="+rut;
	var newVent = open(url, "mainFrame", "");
	return false;
}

/*function AbrirBGrafica() {
	//var url2 = "/servlet/JerarquiaOrg?cual=2";
	var url2 = "/Templates/Gestion/InfoUsuario/Jerarquia/JerarquiaOrg.htm";
	ventmsg2 = open(url2, "OrganigramaDF", "width=700,height=400,left=50,top=110");
	//ventmsg2.focus();
}*/



function AbrirBAlfa() {
	var url2 = "../servlet/Buscar_por_Nombre?Operacion=1";
	ventmsg2 = open(url2, "BusAlfa", "width=640,height=355,left=50,top=110");
	//ventmsg2.focus();
}

function Dependencias(unidad, empresa) {
	var url2 = "../servlet/Dependencias?unidad="+unidad+"&empresa="+empresa;
	ventDep = open(url2, "Dependencias", "width=500,height=300,left=50,top=110");
	//ventmsg2.focus();
}

function AnalisisDot(unidad, empresa) {
	var url2 = "../servlet/AnalisisDot?unidad="+unidad+"&empresa="+empresa;
	ventDep = open(url2, "AnalisisDot", "width=500,height=300,left=50,top=110");
	//ventmsg2.focus();
}

function InformeVisitas(tipo) {
	var cual="";
	if (tipo=="1")
	   cual="INFU011";//del dia
	else
	   cual="INFU010";//del mes   
	var url2 = "/servlet/GeneraInforme?cual="+cual;
	ventDep = open(url2, "Visitas_tipo", "width=770,height=317,left=50,top=110");
	//ventmsg2.focus();
}

function ModuloVisitas(){
    var url2 = "Tool?htm=Gestion/RegistroVisitas/frameset.htm";
    ventDep = open(url2, "Visitas", "width=704,height=460,left=20,top=100");
	
}

function ModuloAnexos(){
    var url2 = "/Anexos/frameanexos2.htm";
    ventDep = open(url2, "MantAnexos", "width=790,height=470,left=20,top=100");
	
}
