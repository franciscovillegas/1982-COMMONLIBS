package cl.eje.bootstrap.ifaces;

import cl.eje.bootstrap.alert.IAlerts;

public interface IBootstrapTemplates {

	IFrameLoaders getIFrameLoaders();

	INavigators getNavigators();
	
	public IPanelsResources getPaneles();
	
	public IContainers getContainers();
	
	public IAlerts getAlertas();

}