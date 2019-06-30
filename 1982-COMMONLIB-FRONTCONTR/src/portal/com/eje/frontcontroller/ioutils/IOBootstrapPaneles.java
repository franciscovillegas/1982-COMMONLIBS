package portal.com.eje.frontcontroller.ioutils;

import cl.eje.bootstrap.ifaces.IPanelResource;
import cl.eje.bootstrap.ifacetemplatesetter.IPanel;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.IOClaseWeb;

class IOBootstrapPaneles implements IIOBootstrapPaneles {

	private IOClaseWeb io;
	
	
	public IOBootstrapPaneles(IOClaseWeb io) {
		super();
		this.io = io;
	}


	@Override
	public void retPanel(IPanelResource container, SimpleHash modelRoot, IPanel panel) throws Exception {
	 
		FreemakerTool.getInstance().setDataFromVo(modelRoot, panel);
		io.retTemplate(container.getPath(), modelRoot, container.isFromTemplatePath());
	}

}
