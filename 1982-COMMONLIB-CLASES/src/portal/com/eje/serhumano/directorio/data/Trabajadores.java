package portal.com.eje.serhumano.directorio.data;


import java.util.ArrayList;
import java.util.List;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.permiso.PerfilMngr;
import portal.com.eje.serhumano.directorio.mgr.ManagerTrabajador;
import portal.com.eje.serhumano.user.Usuario;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.IDataOut;
import cl.ejedigital.consultor.output.JSarrayTableDataOut;
import cl.ejedigital.consultor.output.jquerytable.JqueryTableTool;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;


public class Trabajadores extends AbsClaseWeb {
	
	
	public Trabajadores(IOClaseWeb ioClaseWeb) {
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
		String htm	  = super.getIoClaseWeb().getParamString("htm","directorio/listaPersonas.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
		if("show".equals(accion)) {
				super.getIoClaseWeb().retTemplate(htm,modelRoot);		
		}
		if("select".equals(accion)) {
			if("trabajadores".equals(thing)) {
				selectTrabajadores();
			}
			else if("trabajador".equals(thing)) {
				Integer rut = super.getIoClaseWeb().getParamNum("rut", 0);	
				selectTrabajadores(rut, htm);
			}
		}
	}

	private void selectTrabajadores() {
		List<String> filtro = new ArrayList<String>();
		filtro.add("rut");
		filtro.add("nombre");
		filtro.add("desc_cargo");
		filtro.add("desc_unidad");
		filtro.add("options");
		
		Usuario user = super.getIoClaseWeb().getUsuario();
		IDataOut out = new JSarrayTableDataOut(ManagerTrabajador.getInstance().getTrabajadores());
		
		out.setFilter(filtro);
		JqueryTableTool table = new JqueryTableTool();		
		
		System.out.println("Antes de Caer...");
		super.getIoClaseWeb().retTexto(table.getServerResponde(out,1).getListData());
	}
	
	private void selectTrabajadores(Integer rut, String htm) {

		SimpleHash modelRoot = new SimpleHash();
		FreemakerTool tool = new FreemakerTool();
		
		ConsultaData datosTrabajadorData = ManagerTrabajador.getInstance().getTrabajadores(rut);

		if (datosTrabajadorData.next()){
			modelRoot.put("rut", String.valueOf(datosTrabajadorData.getInt("rut")) );
			modelRoot.put("nombre", datosTrabajadorData.getString("nombre") );
			modelRoot.put("ape_paterno", datosTrabajadorData.getString("ape_paterno") );
			modelRoot.put("ape_materno", datosTrabajadorData.getString("ape_materno") );
			modelRoot.put("desc_cargo", datosTrabajadorData.getString("desc_cargo") );
			modelRoot.put("desc_unidad", datosTrabajadorData.getString("desc_unidad") );
			modelRoot.put("telefono", datosTrabajadorData.getString("telefono") );
			modelRoot.put("celular", datosTrabajadorData.getString("celular") );
			modelRoot.put("anexo", datosTrabajadorData.getString("anexo") );
			modelRoot.put("mail", datosTrabajadorData.getString("mail") );
		}

		super.getIoClaseWeb().retTemplate(htm,modelRoot);		


		
	}
	
}



