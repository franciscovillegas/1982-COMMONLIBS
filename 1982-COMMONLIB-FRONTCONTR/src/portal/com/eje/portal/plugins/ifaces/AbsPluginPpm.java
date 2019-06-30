package portal.com.eje.portal.plugins.ifaces;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;

import cl.eje.helper.AZoneUtil;
import portal.com.eje.portal.plugins.vo.VersionPlugin;

public class AbsPluginPpm implements IPluginPpm {

	@Override
	public List<Class<? extends ITool>> getTools() {
		return new ArrayList<Class<? extends ITool>>();
	}

	@Override
	public List<Class<? extends AZoneUtil>> getZoneUtils() {
		return new ArrayList<Class<? extends AZoneUtil>>();
	}

	// @Override
	// public List<Class<? extends AZoneUtil>> getServicios() {
	// return new ArrayList<Class<? extends AZoneUtil>>();
	// }

	@Override
	public List<Class<? extends IInjectable>> getInjectables(Class<? extends IInjectable> clase) {
		return new ArrayList<Class<? extends IInjectable>>();
	}

	@Override
	public List<String> getRecursos() {
		return new ArrayList<String>();
	}

	@Override
	public String getNombre() {
		return "PLUGIN GENERICO";
	}

	@Override
	public VersionPlugin getVersion() {
		return new VersionPlugin(1, 0, 0);
	}

	
	

}
