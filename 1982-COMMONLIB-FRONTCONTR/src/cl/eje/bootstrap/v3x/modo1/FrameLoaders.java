package cl.eje.bootstrap.v3x.modo1;

import cl.eje.bootstrap.ifaces.IFrameLoaders;
import cl.eje.bootstrap.ifaces.IPageResource;
import cl.eje.bootstrap.v3x.modo1.iframeloader.PanelIFrameFluid;
import cl.eje.bootstrap.v3x.modo1.iframeloader.PanelIFrameFluidNoBottomNoTop;
import portal.com.eje.portal.factory.Weak;

class FrameLoaders implements IFrameLoaders {

	public static FrameLoaders getIntance() {
		return Weak.getInstance(FrameLoaders.class);
	}
	
	@Override
	public IPageResource getDefault() {
		return PanelIFrameFluid.getInstance();
	}

	@Override
	public IPageResource getNoBottomNoTop() {
		return PanelIFrameFluidNoBottomNoTop.getInstance();
	}	
}
