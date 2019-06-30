package portal.com.eje.portal.plugins.vo;

import cl.eje.helper.AZoneUtil;
import portal.com.eje.portal.plugins.ifaces.IPluginPpm;
import portal.com.eje.portal.plugins.ifaces.ITool;

public class ToolPluginVo {
	private ITool instancia;
	private IPluginPpm plugin;
	private Class<? extends ITool> toolClass;

	public ToolPluginVo(IPluginPpm plugin, Class<? extends ITool> toolClass) {
		super();
		this.plugin = plugin;
		this.toolClass = toolClass;
	}

	public ITool getInstancia() {
		return instancia;
	}

	public void setInstancia(ITool instancia) {
		this.instancia = instancia;
	}

	public IPluginPpm getPlugin() {
		return plugin;
	}

	public void setPlugin(IPluginPpm plugin) {
		this.plugin = plugin;
	}

	public Class<? extends ITool> getToolClass() {
		return toolClass;
	}

	public void setToolClass(Class<? extends ITool> toolClass) {
		this.toolClass = toolClass;
	}

}
