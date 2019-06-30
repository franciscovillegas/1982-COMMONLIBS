package cl.eje.bootstrap.abs;

import cl.eje.bootstrap.ifaces.INavigatorResource;

public class AbsNavigatorResource extends AbsPageResource implements INavigatorResource{
 
	private String title;
	private String favIcon;
	


	@Override
	public String getTitle() {
		return this.title;
	}
	
	@Override
	public void setTitle(String titulo) {
		this.title = titulo;
	}

	@Override
	public void setFavIcon(String favIcon) {
		this.favIcon = favIcon;
	}
	
	@Override
	public String getFavIcon() {
		return this.favIcon;
	}

}
