package cl.eje.bootstrap.ifaces;

import portal.com.eje.genericconf.ifaces.IPage;

/**
 * Es un recurso html
 * */
public interface ITemplateResource  {

	public String getPath();

	public void setPath(String path);

	boolean isFromTemplatePath();

	void setFromTemplatePath(boolean isFromTemplatePath);
}
