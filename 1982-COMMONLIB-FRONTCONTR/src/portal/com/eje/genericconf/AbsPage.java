package portal.com.eje.genericconf;

import portal.com.eje.genericconf.ifaces.IPage;

public class AbsPage implements IPage {

	private String title;
	private String favIcon;
	
	public AbsPage() {
		setTitle("Peoplemanager");
		setFavIcon("view/images/btns/tree.ico");
		
	}
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return this.title;
	}

	@Override
	public void setTitle(String titulo) {
		this.title = titulo;
	}

	@Override
	public String getFavIcon() {
		return this.favIcon;
	}

	@Override
	public void setFavIcon(String icon) {
		this.favIcon = icon;
		
	}

	

}
