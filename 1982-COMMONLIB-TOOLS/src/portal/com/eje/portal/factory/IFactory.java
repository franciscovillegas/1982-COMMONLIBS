package portal.com.eje.portal.factory;

import java.util.Map;

public interface IFactory {

	public <T extends Object> T getInstance(String c);
	
	public <T extends Object> T getInstance(Class<T>  c);
		
	public Map<String, SingleFactoryObject> getObjects();
	
	public boolean getStateOnOff();

	public void setStateOnOff(boolean onOff);
}
