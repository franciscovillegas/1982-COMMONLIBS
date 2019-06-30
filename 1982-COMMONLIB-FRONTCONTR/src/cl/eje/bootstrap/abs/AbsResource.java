package cl.eje.bootstrap.abs;

import cl.eje.bootstrap.ifaces.IResource;

public class AbsResource implements IResource{
	
	private boolean fromTemplatePath;
	private String path;
	
	public AbsResource() {
		 
	}
	
	@Override
	public boolean isFromTemplatePath() {
		return fromTemplatePath;
	}

	@Override
	public void setFromTemplatePath(boolean fromTemplatePath) {
		this.fromTemplatePath = fromTemplatePath;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
		
	}
	 
}
