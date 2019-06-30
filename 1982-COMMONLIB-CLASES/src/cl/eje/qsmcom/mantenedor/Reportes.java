package cl.eje.qsmcom.mantenedor;

import java.util.ArrayList;
import java.util.List;

import cl.eje.qsmcom.managers.ManagerQSM;
import cl.eje.qsmcom.managers.ManagerReport;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.JSarrayTableDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.consultor.output.jquerytable.JqueryTableTool;
import intranet.com.eje.qsmcom.manager.QSMComManager;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.tools.escapeJavascript;

public class Reportes extends AbsClaseWeb {

	public Reportes(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","principal");
		String htm	  = super.getIoClaseWeb().getParamString("htm","mantenedor/mainReportes.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		
		if(!super.getIoClaseWeb().getUsuario().tieneApp("cntrll")) {
			super.getIoClaseWeb().retTexto("Sin acceso");
		}
		
		if("show".equals(accion)) {
			if("principal".equals(thing)) {
				super.getIoClaseWeb().retTemplate(htm);
			}
			else if("pageDetalle".equals(thing)) {
				modelRoot.put("funcExec", super.getIoClaseWeb().getParamString("funcExec", ""));
				super.getIoClaseWeb().retTemplate("mantenedor/mainReporteDetalle.html",modelRoot);
			}
			else if("detalleTicket".equals(thing)) {
				modelRoot.put("funcExec", super.getIoClaseWeb().getParamString("funcExec", ""));
				super.getIoClaseWeb().retTemplate("mantenedor/mainReporteDetalle.html",modelRoot);
			}
		}
		else if("select".equals(accion)) {
			if("general".equals(thing)) {
				returnReporteGeneral();
			}
			else if("detalleSubida".equals(thing)) {
				returnReporteDetalle();
			}
			else if("detalleTicket".equals(thing)) {
				returnReporteDetalleTicket();
			}
			else if("ultimoCalculo".equals(thing)) {
				returnReporteUltimosCalculos();
			}
			else if("glosaTicket".equals(thing)) {
				returnGlosa();
			}
		}
		else if("insert".equals(accion)) {

		}
		else if("update".equals(accion)) {

		}
		else if("delete".equals(accion)) {

		}
		else if("upload".equals(accion)) {
			
		}
		
	}

	private void returnGlosa() {
		ConsultaData data = ManagerReport.getInstance().getTicket(super.getIoClaseWeb().getParamString("idTicket", "-1"));
		if(data.next()) {
			super.getIoClaseWeb().retTexto(data.getString("glosa"));
		}
		else {
			super.getIoClaseWeb().retTexto("Error 101 : No Data ");
		}
		
		
	}

	private void returnReporteUltimosCalculos() {
		// TODO Auto-generated method stub
		
	}

	public void returnReporteGeneral() {
		List<String> filtro = new ArrayList<String>();
		filtro.add("fecha_ini");
		filtro.add("fecha_fin");
		filtro.add("tipoEtapa");
		filtro.add("tipoCarga");
		filtro.add("glosa_periodo");
		filtro.add("tiempo_proceso");
		filtro.add("cargaId");
		filtro.add("cargaFecha");
		filtro.add("id_plantilla");
		filtro.add("nombre");
		filtro.add("total_registros");
		filtro.add("total_ruts");
		filtro.add("total_respondidos");
		filtro.add("detalle");
				
		JSarrayTableDataOut out = new JSarrayTableDataOut(ManagerReport.getInstance().getReporteGeneral());
		out.setFilter(filtro);
		out.setEscape(new escapeJavascript());
		JqueryTableTool table = new JqueryTableTool();		
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());
	}
	
	public void returnReporteDetalle() {
		List<String> filtro = new ArrayList<String>();
		filtro.add("rut");
		filtro.add("digito_ver");
		filtro.add("nombres");
		filtro.add("ape_paterno");
		filtro.add("ape_materno");
		filtro.add("id_ticket");
		filtro.add("tipoTicket");
		filtro.add("fechaFinRequerimiento");
		filtro.add("detalle");
		int idSubida = super.getIoClaseWeb().getParamNum("idSubida", -1);
		
		JSarrayTableDataOut out = new JSarrayTableDataOut(ManagerReport.getInstance().getReporteDetalle(idSubida));
		out.setFilter(filtro);
		out.setEscape(new escapeJavascript());
		JqueryTableTool table = new JqueryTableTool();		
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());
	}
	
	public void returnReporteDetalleTicket() {
		List<String> filtro = new ArrayList<String>();
		filtro.add("actual_transaccion_id");
		filtro.add("hora");
		filtro.add("accion");
		filtro.add("nombre_rol_antes");
		filtro.add("nombre_rol_despues");
		filtro.add("comentario");
		int idSubida = super.getIoClaseWeb().getParamNum("idTicket", -1);
		
		JSarrayTableDataOut out = new JSarrayTableDataOut(ManagerReport.getInstance().getHistoriaTicket(idSubida));
		out.setFilter(filtro);
		out.setEscape(new escapeJavascript());
		JqueryTableTool table = new JqueryTableTool();		
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());
	}
	
}
