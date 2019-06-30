package portal.com.eje.genericconf.ifaces;

public interface IPage {

	public String getTitle();
	
	public void setTitle(String titulo);
	
	public String getFavIcon();
	
	/**
	 * La ruta parte desde la base de la página, se recomienda que siempre la página parta desde dos niveles por sobre la base del webcontent, por eso 
	 * se debe ocupar Page/modulo/thing/
	 * 
	 * @author Pancho
	 * @since 24-06-2019
	 * */
	public void setFavIcon(String icon);
}
