package cl.ejedigital.tool.cache;


public interface ICacheListener {

	public Object put(Object arg0, Object arg1);
	
	
	public Object get(Object arg0);
	
	
	public Object remove(Object arg0);
}
