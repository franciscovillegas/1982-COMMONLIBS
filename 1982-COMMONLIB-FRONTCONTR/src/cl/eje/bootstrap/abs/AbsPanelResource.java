package cl.eje.bootstrap.abs;

import cl.eje.bootstrap.ifaces.IPanelResource;

public class AbsPanelResource implements IPanelResource {
	private String path;
	private boolean fromTemplatePath;
	
	public String getPath() {
		return path;
	}

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
