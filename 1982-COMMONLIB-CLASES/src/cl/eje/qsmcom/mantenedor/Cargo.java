package cl.eje.qsmcom.mantenedor;

import java.util.ArrayList;
import java.util.List;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.permiso.PerfilMngr;
import cl.eje.qsmcom.managers.ManagerCargo;
import cl.eje.qsmcom.managers.ManagerQSM;
import cl.ejedigital.consultor.output.JSarrayTableDataOut;
import cl.ejedigital.consultor.output.jquerytable.JqueryTableTool;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;

public class Cargo extends AbsClaseWeb {

	public Cargo(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","mantenedor/mantCargo.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
		if("show".equals(accion)) {
			showPaginaCargos(htm,modelRoot);
		}
		else if("select".equals(accion)) {
			if("cargos".equals(thing)) {
				selectCargos();
			}
		}
		else if("insert".equals(accion)) {
			if("cargo".equals(thing)) {
				insertCargo();
			}
		}
		else if("update".equals(accion)) {
			
		}
		else if("delete".equals(accion)) {
			if("cargo".equals(thing)) {
				deleteCargo();
			}
		}
		else if("upload".equals(accion)) {
			
		}
		
	}

	private void showPaginaCargos(String htm, SimpleHash modelRoot) {
		FreemakerTool free = new FreemakerTool();
		modelRoot.put("plantillas", free.getListData(ManagerQSM.getInstance().getPlantillas()));
		super.getIoClaseWeb().retTemplate(htm,modelRoot);			
		
	}

	private void insertCargo() {
		String nombre = super.getIoClaseWeb().getParamString("nombre", "");
		String id = super.getIoClaseWeb().getParamString("id", "");
		String idplantilla = super.getIoClaseWeb().getParamString("idplantilla", "");
		
		super.getIoClaseWeb().retTexto(String.valueOf(ManagerCargo.getInstance().insertCargo(id,nombre,idplantilla)));
		
	}

	private void deleteCargo() {
		String id = super.getIoClaseWeb().getParamString("id", "");
		super.getIoClaseWeb().retTexto(String.valueOf(ManagerCargo.getInstance().updateCargo(id, "vigente", "N")));
		super.getIoClaseWeb().retTexto(String.valueOf(ManagerCargo.getInstance().updateCargo(id, "cargo",  id)));
	}

	private void selectCargos() {
		
		JSarrayTableDataOut out = new JSarrayTableDataOut(ManagerCargo.getInstance().getCargos());
		JqueryTableTool table = new JqueryTableTool();
		
		List<String> filtro = new ArrayList<String>();
		filtro.add("cargo");
		filtro.add("descrip");
		filtro.add("vacio_1");
		filtro.add("vacio_2");
		filtro.add("nombre");
		filtro.add("fecha_creacion");
		filtro.add("path_excel");
		filtro.add("path_htm");
		filtro.add("path_img");
		
		out.setFilter(filtro);
		
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());
		
	}

	@Override
	public void doPost() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
