package portal.com.eje.genericconf.ifaces;

import portal.com.eje.portal.administradorportal.IButtonGroup;
import portal.com.eje.portal.app.enums.EnumTypeAppIde;

public interface IButton  {
	
	public int getPosition();
	
	/**
	 * Por defecto es JAVASCRIPT
	 * */
	public EnumTypeAppIde getType();
	
	/**
	 * por defecto es Base De Datos
	 * */
	public IButtonGroup getGroup();
	
	/**
	 * Por defecto es botón
	 * */
	public String getNombre();
	
 
	public String getIde();
	
	public String getIcon();
	
	public int getStartWidth();
	
	public int getStartHeight();
	
	public String getAccionUrl();
	
	public String getAccionWaitMsg();
	
	public String getAccionOnSuccessMsg();
	
	public String getAccionOnFailureMsg();
	
	public boolean isDisabled();
	
	public boolean isAccionConfirmed();
	
	public String getAccionConfirmTitle();
	
	public String getAccionConfirmMsg();
	
	public IButtonGroup getSubGroup();
	
	public void setSubGroup(IButtonGroup subGroup);
	
	public boolean isAccesible(); 
	

}
