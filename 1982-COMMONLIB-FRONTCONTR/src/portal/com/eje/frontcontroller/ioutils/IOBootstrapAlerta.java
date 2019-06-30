package portal.com.eje.frontcontroller.ioutils;

import cl.eje.bootstrap.alert.IAlert;
import cl.eje.bootstrap.alert.IAlertResource;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.IOClaseWeb;

class IOBootstrapAlerta implements IIOBootstrapAlert {
	private IOClaseWeb io;

	IOBootstrapAlerta(IOClaseWeb io) {
		super();
		this.io = io;
	}
	
 

	@Override
	public void retAlert(IAlertResource alert, SimpleHash modelRoot, IAlert alerta) throws Exception {
		
		FreemakerTool.getInstance().setDataFromVo(modelRoot, alerta);
		io.retTemplate(alert.getPath(), modelRoot, alert.isFromTemplatePath());
		
	}
	
}
