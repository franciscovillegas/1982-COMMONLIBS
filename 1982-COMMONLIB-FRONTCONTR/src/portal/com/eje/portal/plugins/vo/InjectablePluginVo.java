package portal.com.eje.portal.plugins.vo;

import portal.com.eje.portal.plugins.ifaces.IInjectable;
import portal.com.eje.portal.plugins.ifaces.IPluginPpm;

public class InjectablePluginVo {
	private IPluginPpm plugin;
	private Class<? extends IInjectable> injectableClass;
	private IInjectable instancia;

	public InjectablePluginVo(IPluginPpm plugin, Class<? extends IInjectable> injectableClass) {
		super();
		this.plugin = plugin;
		this.injectableClass = injectableClass;
	}

	public IPluginPpm getPlugin() {
		return plugin;
	}

	public void setPlugin(IPluginPpm plugin) {
		this.plugin = plugin;
	}

	public Class<? extends IInjectable> getInjectableClass() {
		return injectableClass;
	}

	public void setInjectableClass(Class<? extends IInjectable> injectableClass) {
		this.injectableClass = injectableClass;
	}

	public IInjectable getInstancia() {
		return instancia;
	}

	public void setInstancia(IInjectable instancia) {
		this.instancia = instancia;
	}

}
