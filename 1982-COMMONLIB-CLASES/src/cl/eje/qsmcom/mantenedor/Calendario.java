package cl.eje.qsmcom.mantenedor;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.permiso.PerfilMngr;
import portal.com.eje.tools.escapeJavascript;
import cl.eje.qsmcom.managers.ManagerQSM;
import cl.eje.qsmcom.tipo.TipoCarga;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.DataList;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.output.JSarrayTableDataOut;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.consultor.output.jquerytable.JqueryTableTool;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;

public class Calendario extends AbsClaseWeb {


	public Calendario(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","mantenedor/mantCalendar.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
		if("show".equals(accion)) {
			showCalendar(htm, modelRoot);		
		}
		else if("select".equals(accion)) {
			if("procesos".equals(thing)) {
				selectProcesos();
			}
		}
		else if("insert".equals(accion)) {
			if("procesos".equals(thing)) {
				insertProceso();
			}
		}
		else if("update".equals(accion)) {
			
		}
		else if("delete".equals(accion)) {
			if("proceso".equals(thing)) {
				deleteProceso();
			}
		}
		else if("upload".equals(accion)) {
			
		}
	}

	private void showCalendar(String htm,SimpleHash modelRoot) {
		ConsultaData dataCarga = ManagerQSM.getInstance().getTiposDeCarga();
		ConsultaData dataProc  = ManagerQSM.getInstance().getTiposDeProceso();
		FreemakerTool tool = new FreemakerTool();
		modelRoot.put("tiposDeCarga", tool.getListData(dataCarga));
		modelRoot.put("tiposDeProceso", tool.getListData(dataProc));
		super.getIoClaseWeb().retTemplate(htm,modelRoot);	
	}
	
	private void deleteProceso() {
		int id = super.getIoClaseWeb().getParamNum("id", -1);
		super.getIoClaseWeb().retTexto(String.valueOf(ManagerQSM.getInstance().delProceso(id)));
		
	}

	private void insertProceso() {

		
		String nombre = super.getIoClaseWeb().getParamString("nombre", "");
		int tipo = super.getIoClaseWeb().getParamNum("tipo", 0);
		int tipoCarga = super.getIoClaseWeb().getParamNum("tipoCarga", 0);
		Date fecIni = Formatear.getInstance().toDate(super.getIoClaseWeb().getParamString("fec_ini", null),"dd/MM/yyyy");
		Date fecFin = Formatear.getInstance().toDate(super.getIoClaseWeb().getParamString("fec_fin", null),"dd/MM/yyyy");
		
		
		boolean ok = false;
		
		TipoCarga t =  null;
		if(tipoCarga == TipoCarga.calculo.getId()) {
			t = TipoCarga.calculo;
		}
		else if(tipoCarga == TipoCarga.produccion.getId()) {
			t = TipoCarga.produccion;
		}
		
		if(ManagerQSM.getInstance().isFechaProcesoViable(fecIni, fecFin,t)) {
			 ok = ManagerQSM.getInstance().insertProceso(nombre,fecIni,	fecFin, tipo, tipoCarga);
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("estado",  String.valueOf(ok));
		if(!ok) {
			map.put("mensaje",  "No es posible crear una periodo para dichas fechas, intentelo nuevamente con otras." );
		}
		
		super.getIoClaseWeb().retJson(map);
	}

	private void selectProcesos() {
		List<String>  filtro = new ArrayList<String>();
		filtro.add("id_periodo");
		filtro.add("glosa_periodo");
		filtro.add("tipo_carga");
		filtro.add("nombre");
		filtro.add("fecha_ini");
		filtro.add("fecha_fin");
		filtro.add("options");
		
		JSarrayTableDataOut out = new JSarrayTableDataOut(ManagerQSM.getInstance().getProcesos());
		out.setFilter(filtro);
		out.setEscape(new escapeJavascript());
		JqueryTableTool table = new JqueryTableTool();		
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());

	}
}
