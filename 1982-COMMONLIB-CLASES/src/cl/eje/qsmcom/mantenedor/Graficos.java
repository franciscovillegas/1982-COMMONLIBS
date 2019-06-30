package cl.eje.qsmcom.mantenedor;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import freemarker.template.SimpleHash;

public class Graficos extends AbsClaseWeb {

	public Graficos(IOClaseWeb ioClaseWeb) {
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
		String htm	  = super.getIoClaseWeb().getParamString("htm","mantenedor/mainGraficos.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		
		if(!super.getIoClaseWeb().getUsuario().tieneApp("cntrll")) {
			super.getIoClaseWeb().retTexto("Sin acceso");
		}
		
		if("show".equals(accion)) {
			if("principal".equals(thing)) {
				super.getIoClaseWeb().retTemplate(htm);
			}
			
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
