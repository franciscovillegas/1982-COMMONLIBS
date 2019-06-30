package cl.eje.qsmcom.mantenedor;

import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.permiso.PerfilMngr;

public class Plantillas extends AbsClaseWeb {

	public Plantillas(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","mantenedor/mantPlantilla.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
		if("show".equals(accion)) {
			super.getIoClaseWeb().retTemplate(htm,modelRoot);			
		}
		else if("select".equals(accion)) {

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

}
