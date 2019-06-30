package cl.eje.bootstrap.v3x.modo1;

 
import cl.eje.bootstrap.alert.IAlerts;
import cl.eje.bootstrap.ifaces.IBootstrapTemplates;
import cl.eje.bootstrap.ifaces.IContainers;
import cl.eje.bootstrap.ifaces.IFrameLoaders;
import cl.eje.bootstrap.ifaces.INavigators;
import cl.eje.bootstrap.ifaces.IPanelsResources;
import portal.com.eje.portal.factory.Weak;

public class BootstrapTemplates implements IBootstrapTemplates {
	
	public static IBootstrapTemplates getInstance() {
		return Weak.getInstance(BootstrapTemplates.class);
	}
	
	public IFrameLoaders getIFrameLoaders() {
		return  FrameLoaders.getIntance();
	}
	
	public INavigators getNavigators() {
		return Navigators.getIntance();
	}
	
	public IPanelsResources getPaneles() {
		return Paneles.getIntance();
	}

	@Override
	public IContainers getContainers() {
		return Containers.getIntance();
	}

	@Override
	public IAlerts getAlertas() {
		return Alerts.getIntance();
	}
	
	public static IAlerts alertas() {
		return getInstance().getAlertas();
	}
	
	public static IContainers containers() {
		return getInstance().getContainers();
	}
	
	public static IPanelsResources paneles() {
		return getInstance().getPaneles();
	}
	
	public static INavigators nagivators() {
		return getInstance().getNavigators();
	}
	
	public static IFrameLoaders iframeLoaders() {
		return getInstance().getIFrameLoaders();
	}
}
