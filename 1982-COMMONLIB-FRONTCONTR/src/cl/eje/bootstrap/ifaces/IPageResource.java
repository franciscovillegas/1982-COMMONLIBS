package cl.eje.bootstrap.ifaces;

import portal.com.eje.genericconf.ifaces.IPage;

public interface IPageResource extends IPage {

	public boolean isFromTemplatePath();
	
	public void setFromTemplatePath(boolean is);
	
	public String getPath();
	
	public void setPath(String path);
}
