package portal.com.eje.tools.onlyone;

import java.util.HashMap;
import java.util.Map;

import portal.com.eje.portal.factory.Static;
import portal.com.eje.tools.onlyone.error.PPMOnlyOneProcessException;

public class OnlyOneProcess {

	public static OnlyOneProcess getInstance() {
		return Static.getInstance(OnlyOneProcess.class);
	}
	
	private Map<String, String> poolOnlyOne;
	
	private OnlyOneProcess() {
		this.poolOnlyOne = new HashMap<String, String>();
	}
	
	public void check(int rut,String key) throws PPMOnlyOneProcessException {
 
		if(exist(rut, key)) {
			throw new PPMOnlyOneProcessException("Ya existe un proceso corriendo.");
		}
		else {
			synchronized (OnlyOneProcess.class) {
				if(exist(rut, key)) {
					throw new PPMOnlyOneProcessException("Ya existe un proceso corriendo.");
				}
				
				start(rut, key);
			}
		}
	 
	}
	
	public void start(int rut,String key) {
		if(key != null) {
			key = key + "_" + rut;
			
			if(poolOnlyOne.get(key) != null) {
				return;
			}
			
			poolOnlyOne.put(key, "true");
		}

	}
	
	public boolean exist(int rut,String key) {
		if(key != null) {
			key = key + "_" + rut;
			
			return poolOnlyOne.get(key) != null;
		}

		return false;
	}
	
	public String end(int rut,String key) {
		if(key != null) {
			key = key + "_" + rut;
		
			return poolOnlyOne.remove(key);
		}
		
		return null;
	}
}
