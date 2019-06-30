package cl.ejedigital.tool.properties;

import java.util.List;
import java.util.ResourceBundle;

public interface IPropertiesTools {

	public boolean existsBundle(String bundleName);
	
	public String getString(ResourceBundle bundle,String key, String value);
	
	public int getInt(ResourceBundle bundle,String key, int value);
	
	public List<String> getKeys(ResourceBundle bundle, String keyStart);
	
	public List<String> getKeys(ResourceBundle bundle);
}
