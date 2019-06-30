package cl.eje.bootstrap.ifaces;

public interface IResource {

	public boolean isFromTemplatePath();
	
	public void setFromTemplatePath(boolean is);
	
	public String getPath();
	
	public void setPath(String path);
	
}
