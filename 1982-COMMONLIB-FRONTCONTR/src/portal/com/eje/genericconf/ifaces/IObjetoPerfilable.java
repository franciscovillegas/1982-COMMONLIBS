package portal.com.eje.genericconf.ifaces;

import portal.com.eje.portal.generico_appid.EnumAppId;

public interface IObjetoPerfilable {

	/**
	 * Retorna el App_id que requiere el botón para ser visto, le definición si se ve o no depende del programa que muestra los botones, se recomienda que se pongan 
	 * disabled=true cuando no se tenga el App_id
	 * 
	 * @author Pancho
	 * @since 17-05-2019
	 * */
	public EnumAppId getAppIdRequerido();
	
	
	public void setAppIdRequerido(EnumAppId appIdRequerido);
	
	public boolean isVisible();
	
	public void setVisible(boolean visible);
	
}
