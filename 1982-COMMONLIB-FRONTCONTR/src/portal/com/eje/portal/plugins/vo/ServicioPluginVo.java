package portal.com.eje.portal.plugins.vo;

import cl.eje.helper.AZoneUtil;
import portal.com.eje.portal.plugins.ifaces.IPluginPpm;

public class ServicioPluginVo {
	private IPluginPpm plugin;
	private Class<? extends AZoneUtil> servicioClass;
	private AZoneUtil instancia;

	public ServicioPluginVo(IPluginPpm plugin, Class<? extends AZoneUtil> servicioClass) {
		super();
		this.plugin = plugin;
		this.servicioClass = servicioClass;
	}

	public IPluginPpm getPlugin() {
		return plugin;
	}

	public void setPlugin(IPluginPpm plugin) {
		this.plugin = plugin;
	}

	public Class<? extends AZoneUtil> getServicioClass() {
		return servicioClass;
	}

	public void setServicioClass(Class<? extends AZoneUtil> servicioClass) {
		this.servicioClass = servicioClass;
	}

	public AZoneUtil getInstancia() {
		return instancia;
	}

	public void setInstancia(AZoneUtil instancia) {
		this.instancia = instancia;
	}

	 

 

}
