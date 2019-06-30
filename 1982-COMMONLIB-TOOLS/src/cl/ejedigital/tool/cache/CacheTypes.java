package cl.ejedigital.tool.cache;


public abstract class CacheTypes {
	private String name;
	
	protected CacheTypes(String name) {
		this.name = name;
	}
	
	public String toString(){
		return this.name;
	}
}
