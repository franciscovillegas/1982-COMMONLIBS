package portal.com.eje.portal.administradorportal;

import portal.com.eje.portal.plugins.ifaces.IInjectable;

public interface IToolAdmin extends IInjectable {
	
	public IButtonGroup getGrupo();
	
	public String getNombre();
	
	public String getPrestacion();
	
	public String getIde();
	
	public String getIcon();

}
