package cl.eje.bootstrap.v3x.modo1.iframeloader;

import cl.eje.bootstrap.abs.AbsPageResource;
import cl.eje.bootstrap.ifaces.IPageResource;
import portal.com.eje.portal.factory.Weak;

public class PanelIFrameFluid extends AbsPageResource {

	public static IPageResource getInstance() {
		return Weak.getInstance(PanelIFrameFluid.class);
	}

	public PanelIFrameFluid() {
		setPath("templates/eje/bootstrap/3x/modo1/paneliframe/PanelIFrameFluid.html");
		setFromTemplatePath(false);
		setTitle(getClass().getSimpleName());
	}
 
}
