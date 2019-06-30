package portal.com.eje.frontcontroller.ioutils;

import java.util.List;

import cl.eje.bootstrap.ifaces.INavigatorResource;
import cl.eje.helper.AZonePage;
import cl.eje.view.servlets.page.ServletPage;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import portal.com.eje.bootstrap.BootstrapAZoneUtils;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.genericconf.ifaces.IButton;

class IOBootstrapNavigator implements IIOBootstrapNavigator {
	private final String SENCHA_OPENER = AZonePage.buildUrlFolderFormat(ServletPage.class, BootstrapAZoneUtils.buttonProcesor);
	private IOClaseWeb io;

	IOBootstrapNavigator(IOClaseWeb io) {
		super();
		this.io = io;
	}
	
	public void retNavigator(INavigatorResource nav, SimpleHash modelRoot, List<IButton> botones) throws Exception {
		FreemakerTool.buttons.putButtonListGroupByGrupo(modelRoot, "grupos", "subgrupos", botones);
		modelRoot.put("sencha_opener", SENCHA_OPENER);
		io.retTemplate(nav.getPath(), modelRoot, nav.isFromTemplatePath());
	}
	
}
