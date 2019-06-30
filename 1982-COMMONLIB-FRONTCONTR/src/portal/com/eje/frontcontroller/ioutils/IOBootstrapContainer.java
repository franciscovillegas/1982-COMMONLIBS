package portal.com.eje.frontcontroller.ioutils;

import java.util.List;

import cl.eje.bootstrap.ifaces.IContainerResource;
import cl.eje.bootstrap.ifaces.IPanelResource;
import cl.eje.bootstrap.ifacetemplatesetter.IPanel;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import portal.com.eje.frontcontroller.IOClaseWeb;

class IOBootstrapContainer implements IIOBootstrapContainer{

	private IOClaseWeb io;
	IOBootstrapContainer(IOClaseWeb io) {
		super();
		this.io = io;
	}
	
	@Override
	public void retContainer(IContainerResource res, SimpleHash modelRoot, List<IPanel> paneles, IPanelResource formatoPanel) throws Exception {
		modelRoot.put("paneles", FreemakerTool.bootstrapPaneles.getPanelsSimpleList(io , res, paneles, formatoPanel));
		io.retTemplate(res.getPath(), modelRoot, res.isFromTemplatePath());
	}

	

}
