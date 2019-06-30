  function AbrirSolicitudVaca() {
	var url2 = "../servlet/SolicitudVaca";
	ventmsg2 = open(url2, "solvaca", "scrollbars=yes,width=1024,height=750,resizable=yes,left=5,top=5");
  }

function AbrirListadoSolVac() {
	var url2 = "../servlet/ListaSolicitudVaca";
	ventmsg2 = open(url2, "listasolvaca", "scrollbars=yes,width=700,height=510,left=5,top=5");
	//ventmsg2.focus();
}

function openTraspaso() {
	parent.mainF.location.href = "../servlet/TraspasoDatos";
}

function AbrirSolLegal() {
	var url2 = "../servlet/com.eje.serhumano.misdatos.sol_vacaciones.S_LlenaSolLegal";
	ventmsg2 = open(url2, "sollegal", "scrollbars=yes,width=750,height=500,left=5,top=5");
}

function AbrirSolProgresivo() {
	var url2 = "../servlet/com.eje.serhumano.misdatos.sol_vacaciones.S_LlenaSolProgresiva";
	ventmsg2 = open(url2, "solprog", "scrollbars=yes,width=750,height=500,left=5,top=5");
}


function Descanso(){
  parent.frames.mainF.location.href='../servlet/Tool?htm=rrhh/descanso.htm'
}


function AbrirActualidad() {
	var url2 = "http://www.emol.com";
	ventmsg2 = open(url2, "actualidad", "toolbar=yes,location=yes,status=yes,menubar=yes,scrollbars=yes,resizable=yes,width=690,height=430,left=5,top=5");
}

function AbrirEventos() {
	var url2 = "http://www.casapiedra.cl";
	ventmsg2 = open(url2, "eventos", "toolbar=yes,location=yes,status=yes,menubar=yes,scrollbars=yes,resizable=yes,width=690,height=430,left=5,top=5");
}

function AbrirCodEtica() {
	var url2 = "Tool?htm=html/paginas/noinfo.html";
	ventmsg2 = open(url2, "codetica", "width=452,height=590,left=5,top=5");
}

function AbrirMision() {
	var url2 = "../quicksite/sitio/rrhh/pagmision.htm";
	ventmsg2 = open(url2, "mision", "width=500,height=400,left=0,top=0");
}

function AbrirVision() {
	var url2 = "../quicksite/sitio/rrhh/pagvision.htm";
	ventmsg2 = open(url2, "vision", "width=500,height=400,left=10,top=10");
}

function AbrirPoliticas() {
	var url2 = "../quicksite/sitio/rrhh/pagpoliticas.htm";
	ventmsg2 = open(url2, "poli", "width=500,height=450,left=0,top=0");
}
function AbrirMarcoValorico() {
	var url2 = "../quicksite/sitio/rrhh/pagvalores.htm";
	ventmsg2 = open(url2, "marcovalorico", "width=500,height=450,left=0,top=0");
}

function AbrirOrganigrama() {
	var url2 = "Tool?htm=html/miscelaneos/organigrama.htm";
	ventmsg2 = open(url2, "organica", "width=800,height=570,left=5,top=5");
}

function AbrirCotPrevision() {
	var url2 = "../servlet/Sit_Previsional";
	ventmsg2 = open(url2, "previsional", "scrollbars=yes,width=700,height=500,left=5,top=5");
}

function AbrirCartolaVaca() {
	var url2 = "../servlet/CartolaVaca";
	ventmsg2 = open(url2, "cartolavaca", "scrollbars=yes,width=1024,height=750,resizable=yes,left=5,top=5");
}

function AbrirMetas() {
	var urlmetas = "../servlet/ServletVerEvaluacionColaborador";
	ventmsg2 = open(urlmetas, "metas", "scrollbars=yes,width=785,height=600,left=5,top=5");
}


function AbrirFormBeneficios() {
	var url2 = "../servlet/FormBeneficios";
	ventmsg2 = open(url2, "beneficios", "width=700,height=450,left=5,top=5");
}

function AbrirFormPermisos() {
	var url2 = "../servlet/FormPermiso";
	ventmsg2 = open(url2, "permisos", "scrollbars=yes,width=1024,height=750,resizable=yes,left=5,top=5");
}

function AbrirBOrganica() {
	var url2 = "Tool?htm=buscar/organica/frame_arbol.htm";
	ventmsg2 = open(url2, "organica", "width=800,height=480,left=5,top=5");
}

function AbrirBGaleria() {
    var url2 = "Tool?htm=buscar/grafica/frame_grafica.htm";
	ventmsg2 = open(url2, "Galeria", "width=720,height=450,left=10,top=80");
}

function AbrirAlfabetic() {
    var url2 = "../servlet/S_SearchAlfabetic?Operacion=1";
	ventmsg2 = open(url2, "alfabetica", "width=465,height=360,left=130,top=150");
}

function openCertificados() {
	parent.mainF.location.href = "../servlet/InitCertif";
	return false
}

function openAdmin() {
    var url2 = "../servlet/Tool?htm=admin/cap.htm";
	ventmsg2 = open(url2, "Administrador", "width=800,height=450,left=5,top=5");
}

function openModPerfiles() {
	parent.mainF.location.href = "../servlet/EjeCore?claseweb=portal.com.eje.permiso.ServletModPerfil";
}

function openAsocPerfiles() {
	parent.mainF.location.href = "../servlet/ServletAsocPerfil";
}

function VentAfp(rut) {
	var url = "../servlet/HistoriaAfp?rut="+rut;
	var win=open(url, "HistoriaAfp", "width=458, height=180");
}
function VentIsapre(rut) {
	var url = "../servlet/HistoriaIsapre?rut="+rut;
	var win=open(url, "HistoriaIsapre", "width=458, height=180");
}

function ventana(url, name){
	var win = open(url, name, "toolbar=no,resizable=no,width=790,height=510,left=5,top=5");
	win.focus();
}

function openAnexos() {
	ventana("../servlet/Tool?htm=app/anexos.htm", "App");
}

function openDataFolder() {
	var url2 = "../servlet/Tool?htm=app/datafolder.htm";
	ventmsg2 = open(url2, "App", "menubar=no,toolbar=no,resizable=no,width=780,height=500,left=5,top=5");
}

function openWorkFlow() {
	ventana("../servlet/Tool?htm=app/workflow.htm", "App");
}

function openMIS() {
	ventana("../servlet/Tool?htm=app/mis.htm", "App");
}

function openVigilantes() {
	ventana("../servlet/Tool?htm=app/vigilantes.htm", "App");
}

function openDocumentacion() {
	ventana("../servlet/Tool?htm=app/documentacion.htm", "App");
}

function cerrarSesion() {
	parent.parent.parent.location.href = "../servlet/EndSession?htm=/portalrrhh";
}

function cerrarSesionAdmin() {
	parent.parent.parent.location.href = "../servlet/EndSession?htm=../admin.html";
}

function openInfoRut(rut){
	var url = "../servlet/InfoRut?rut=" + rut;
	var win = open(url, rut, "toolbar=no,resizable=no,width=391,height=321,left=50,top=50");
	win.focus();
}

function openInfoCarga(rut,rutcarga){
	var url = "../servlet/FichaCarga?rutcarga=" + rutcarga+"&rut="+rut;
	var win = open(url, rutcarga, "toolbar=no,resizable=no,width=391,height=221,left=50,top=50");
	win.focus();
}

function openHistoria() {
	parent.mainF.location.href = "Tool?htm=html/miscelaneos/historia.htm";

}

function openMision() {
	parent.mainF.location.href = "Tool?htm=html/miscelaneos/mision.htm";
}

function openMarco() {
	parent.mainF.location.href = "Tool?htm=html/miscelaneos/valorico.htm";
}

function openEtica() {
	parent.mainF.location.href = "Tool?htm=html/etica.htm";
}

function openPolitica() {
	parent.mainF.location.href = "Tool?htm=html/rrhh/politicas.htm";
}

function openIndicadores() {
	parent.mainF.location.href = "Tool?htm=html/rrhh/indicadores.htm";
}

function OpenRecHum() {
	parent.mainF.location.href = "Tool?htm=html/miscelaneos/organigrama.htm";
}

function openLinks() {
	parent.mainF.location.href = "Tool?htm=html/rrhh/links.htm";
}

function openRRHH() {
	alert("En Construcci?n");
}

function openPublicas() {
	parent.mainF.location.href = "Tool?htm=noticias/noticias.htm";
}

function verPersonal(unidad,empresa){
	var url = "../servlet/VerPersonal?unidad=" + unidad+"&emp="+empresa;
	var win = open(url, "verpersonal", "toolbar=no,resizable=no,width=478,height=478,left=50,top=50");
	win.focus();
	return false;
}

function openBienvenida(){
	var url = "../servlet/Inicio";
	var win = open(url, "inicio", "toolbar=no,resizable=no,width=293,height=212,left=150,top=250");
	win.focus();
	return false;
}

function openCambiarClave(regla){
	/*
	var url = 
	var win = open(url, "CambiarClave", "toolbar=no,resizable=no,width=293,height=212,left=150,top=150");
	win.focus();
	return false;
	*/
	 parent.frames['mainF'].document.location = "../servlet/CambiarClave?regla="+regla;
}

function AbrirListaBeneficios() {
    var url2 = "../servlet/ListaBeneficios";
	ventmsg2 = open(url2, "ListaBeneficios", "width=465,height=360,left=130,top=150");
}

function AbrirInfoSolBenef() {
    var url2 = "../servlet/InfoSolBenef";
	ventmsg2 = open(url2, "InfoSolBenef", "width=465,height=360,left=130,top=150");
}

function MenuAdministrar(){
  parent.frames.mainF.location.href='../servlet/Tool?htm=admin/menu_administrar.html'
}


function AbrirQSDiario() {
	ventmsg2 = open("../servlet/Tool?htm=app/diario.htm", "diario", "width=500,height=400,left=130,top=150");
}
function AbrirQSNormas() {
	ventmsg2 = open("http://192.168.4.6:6060/qs/sitio/normas/framenormas.htm", "normas", "width=720,height=490,left=10,top=10");
}

function AbrirQSRevista() {
	ventmsg2 = open("../quicksite/sitio/noticias/pag2200.htm", "2200", "width=450,height=500,left=5,top=5,scrollbars=yes");
}

function AbrirQSNoticias() {
	ventmsg2 = open("../quicksite/sitio/general/paghome_rev.htm");
}

function AbrirQSNoti() {
	ventmsg2 = open("../quicksite/sitio/noticias/pagnoti.htm", "noti", "width=450,height=500,left=5,top=5,scrollbars=yes");
}

function AbrirQSBenef() {
	ventmsg2 = open("../quicksite/sitio/general/paghome.htm");
}

function AbrirQSDiarioAdmin() {
	ventmsg2 = open("../servlet/Tool?htm=app/diario.htm&adm=yes", "diarioAdm", "width=500,height=400,left=130,top=150");
}

function AbrirQSRevistaAdmin() {
	ventmsg2 = open("../servlet/Tool?htm=app/revista.htm&adm=yes", "revistaAdm", "width=500,height=400,left=130,top=150");
}

function AbrirQSNoticiasAdmin() {
	ventmsg2 = open("../servlet/Tool?htm=app/noticias.htm&adm=yes", "noticiasAdm", "width=500,height=400,left=130,top=150");
}

function AbrirQSBenefAdmin() {
	ventmsg2 = open("../servlet/Tool?htm=app/benef.htm&adm=yes", "benefAdm", "width=500,height=400,left=130,top=150");
}

function AbrirListadoTrack2() {
	ventmsg2 = open("../servlet/Tool?htm=tracking/info_main.html&adm=yes", "trackAdm", "width=500,height=400,left=130,top=150");
}


function AbrirListadoTrack() {
    var url2 = "../servlet/InfoSolTrack";
	ventmsg2 = open(url2, "InfoSolBenef", "width=465,height=360,left=130,top=150");
}

function AbrirResumenTrack() {
    var url2 = "../servlet/ReporteTracking";
	ventmsg2 = open(url2, "ReporteTracking", "width=750,height=550,left=130,top=150,scrollbars=1");
}

function AbrirMaestroPersonal() {
    var url2 = "../servlet/MaestroPersonal";
	ventmsg2 = open(url2, "MaestroPersonal", "width=465,height=360,left=130,top=150");
}

function AbrirMaestroCalidad() {
    var url2 = "../servlet/MaestroCalidad";
	ventmsg2 = open(url2, "MaestroCalidad", "width=1100,height=600,left=10,top=50,scrollbars=1");
}

function PeriodosInformeRems(){
    document.location.href="../servlet/PeriodosInformeRems";
}

function GeneraInforme(){
         document.location.href="../servlet/GeneraInformeGestion1";
}
function openSIDApplication(){

	var url = '../servlet/S_ContratoTrabajo';
	var title = 'Aplicacion_SID';
	var properties = 'scrollbars=yes,width=800,height=600';
	ventmsg3 = open(url, title, properties);        
}

function SubirFotos() {
    document.location.href="../servlet/UploadingImagesShowServlet";
}

function conceptoRepositorio() {
	document.location.href="../servlet/ConceptRepositoryServlet";
}

function ModificarLiquidacionAdicional() {
    document.location.href="../servlet/EjeCore?claseweb=portal.com.eje.liquidadic.SAdmLiqAdicional";
}

function listaUsuariosClaves() {
	document.location.href = "../servlet/Tool?htm=user/resumen.html";
	return false;
}

function bloqueoUsuarios(){
	var url = "../servlet/BloqueoUsuarios";
	var win = open(url, "BloqueoUsuarios", "scrollbar=yes,toolbar=no,resizable=no,width=700,height=350,left=150,top=150");
	win.focus();
}

function cargaGerentes(){
	var url = "../servlet/cargaGerentes";
	var win = open(url, "BloqueoUsuarios", "scrollbar=yes,toolbar=no,resizable=no,width=700,height=350,left=150,top=150");
	win.focus();
}
function reiniciarBD(){
	function confimar(){
		var preg=confirm ("Estas seguro que quieres reiniciar la base de datos");
		if (preg==true){
			alert("se borro");
		}
		else{
			alert("NO se borro");
		}
	}
}

function showTallaje() { 
	parent.frames['mainF'].document.location = '../servlet/EjeCore?claseweb=portal.com.eje.tallaje.ManagerTallaje&accion=show&thing=buscarTallaje'; 
}

function AbrirTallaje() {
	var url2 = "Tool?htm=Tallaje/popup_tallaje.htm";
	ventmsg2 = open(url2, "tallaje", "scrollbars=yes,width=535,height=285,left=5,top=5");
}