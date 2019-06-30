package portal.com.eje.carpelect.data;


import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import freemarker.template.SimpleHash;

public class ManagerCarpeta extends AbsClaseWeb {
	
	
	
	public ManagerCarpeta(IOClaseWeb ioClaseWeb) {
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
		String htm	  = super.getIoClaseWeb().getParamString("htm","CarpetaElectronica/buscarTrabajador.htm");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		
		if("show".equals(accion)) {
						
			if("buscarTrabajador".equals(thing)) {
				super.getIoClaseWeb().retTemplate("CarpetaElectronica/buscarTrabajador.htm");
			} else if ("organicaCarpeta".equals(thing)){
				super.getIoClaseWeb().retTemplate("CarpetaElectronica/organicaCarpeta.htm");				
			}
			else {
				super.getIoClaseWeb().retTemplate(htm,modelRoot);		
			}
			
		}
		
	}
}



