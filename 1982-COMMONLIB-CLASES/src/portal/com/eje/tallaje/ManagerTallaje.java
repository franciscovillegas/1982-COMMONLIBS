package portal.com.eje.tallaje;


import java.sql.Connection;
import java.util.List;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.tallaje.mgr.ManagerTallajeTrabajador;
import freemarker.template.SimpleHash;

public class ManagerTallaje extends AbsClaseWeb {
	
	public ManagerTallaje(IOClaseWeb ioClaseWeb) {
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
		String htm	  = super.getIoClaseWeb().getParamString("htm","Tallaje/showTallaje.htm");
		String rut = super.getIoClaseWeb().getParamString("rut",super.getIoClaseWeb().getUsuario().getRutId());
		 
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		
		if("show".equals(accion)) {
			if("cargaTallaje".equals(thing)) {
				modelRoot.put("tallajes",ManagerTallajeTrabajador.getInstance().getTallajesTrabajadores(super.getIoClaseWeb().getConnection("portal"), rut));
				super.getIoClaseWeb().retTemplate("Tallaje/showTallaje.htm",modelRoot);
			} 
		}
		else if("save".equals(accion)) {
			
			List<String> conceptos = super.getIoClaseWeb().getListParamString("conceptos", "-1");
			saveTallajeTrabajador(rut,conceptos,super.getIoClaseWeb().getConnection("portal"));
			modelRoot.put("mensaje", "Datos de tallaje actualizados.");
			modelRoot.put("tallajes",ManagerTallajeTrabajador.getInstance().getTallajesTrabajadores(super.getIoClaseWeb().getConnection("portal"), rut));			
			super.getIoClaseWeb().retTemplate("Tallaje/showTallaje.htm",modelRoot);
		}
		
	}
	
	private void saveTallajeTrabajador(String rut, List<String> conceptos, Connection con) {
		
		String cargo = ManagerTallajeTrabajador.getInstance().getCargoTrabajador(con, rut);
		
		for(int i=0;i<conceptos.size();i++) {
			String id = conceptos.get(i);
			String campo = "concepto" + conceptos.get(i);
			String valor = super.getIoClaseWeb().getParamString(campo,"-1");
			
			if(ManagerTallajeTrabajador.getInstance().getTieneTalla(con,rut,cargo,id)) {
				ManagerTallajeTrabajador.getInstance().updateTallaTrabajador(con, rut, cargo, id, valor);	
			}
			else {
				ManagerTallajeTrabajador.getInstance().insertTallaTrabajador(con, rut, cargo, id, valor);
			}
			
		}

	}
	
}



