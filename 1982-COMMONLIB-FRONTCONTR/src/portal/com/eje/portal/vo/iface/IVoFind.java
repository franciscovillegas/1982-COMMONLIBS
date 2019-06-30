package portal.com.eje.portal.vo.iface;

import java.util.Collection;

public interface IVoFind<T> {

	
	public <T>boolean filter(Collection<T> o);
}
