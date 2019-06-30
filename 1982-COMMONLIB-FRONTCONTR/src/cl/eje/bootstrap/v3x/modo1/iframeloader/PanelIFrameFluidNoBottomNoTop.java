package cl.eje.bootstrap.v3x.modo1.iframeloader;

import cl.eje.bootstrap.abs.AbsPageResource;
import cl.eje.bootstrap.ifaces.IPageResource;
import portal.com.eje.portal.factory.Weak;

public class PanelIFrameFluidNoBottomNoTop extends AbsPageResource {

	public static IPageResource getInstance() {
		return Weak.getInstance(PanelIFrameFluidNoBottomNoTop.class);
	}

	public PanelIFrameFluidNoBottomNoTop() {
		setPath("templates/eje/bootstrap/3x/modo1/paneliframe/PanelIFrameFluidNoBottomNoTop.html");
		setFromTemplatePath(false);
		setTitle(getClass().getSimpleName());
	}

	 

}
