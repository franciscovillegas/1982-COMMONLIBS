package cl.eje.bootstrap.abs;

import cl.eje.bootstrap.ifaces.IPageResource;
import portal.com.eje.genericconf.AbsPage;

public abstract class AbsPageResource extends AbsPage implements IPageResource {
	private String path;
	private boolean fromTemplatePath;
	
	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
		
	}

	public boolean isFromTemplatePath() {
		return fromTemplatePath;
	}

	public void setFromTemplatePath(boolean fromTemplatePath) {
		this.fromTemplatePath = fromTemplatePath;
	}
 
	
	
	
	
}
