package portal.com.eje.portal.plugins.ifaces;

import java.util.List;

import cl.eje.helper.AZoneUtil;
import portal.com.eje.portal.plugins.vo.VersionPlugin;

public interface IPluginPpm {

	/**
	 * Son las herramientas ubicadas en portal.com.eje.portal.[paqueteplugin]
	 * */
	public List<Class<? extends ITool>> getTools();
	
	/**
	 * Son los servicios ubicados en cl.eje.sencha.[paqueteplugin]
	 * */
	public List<Class<? extends AZoneUtil>> getZoneUtils();
	
	/**
	 * 
	 * Son los servicios ubicados en portal.eje.servicio.[paqueteplugin]
	 * 
	 */
//	public List<Class<? extends AZoneUtil>> getServicios();
	
	public List<Class<? extends IInjectable>> getInjectables(Class<? extends IInjectable> clase);
	
	/**
	 * Deben estar dentro de r/[paqueteplugin]/scripts
	 * */
	public List<String> getRecursos();
	
	public String getNombre();
	
	public VersionPlugin getVersion();
	
}
