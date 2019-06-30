package portal.com.eje.portal.plugins.ifaces;

import java.util.List;

import cl.eje.helper.IInterface;

public interface IPlugeable extends IInterface {

	//public List<Class<AZoneUtil>> getServices();
	
	public List<Class<IInterface>> getImplements();
}
