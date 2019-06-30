package portal.com.eje.serhumano.directorio.mgr;


import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import freemarker.template.SimpleHash;

public class ManagerDirectorio extends AbsClaseWeb {
	
	
	
	public ManagerDirectorio(IOClaseWeb ioClaseWeb) {
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
		String htm	  = super.getIoClaseWeb().getParamString("htm","directorio/mainDirectorio.htm");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		
		if("show".equals(accion)) {
			if("directorio".equals(thing)) {
				super.getIoClaseWeb().retTemplate("directorio/mainDirectorio.htm");
			}
			else {
				super.getIoClaseWeb().retTemplate(htm,modelRoot);		
			}
			
		}
		
	}
}



