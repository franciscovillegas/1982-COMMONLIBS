package portal.com.eje.genericconf.ifaces;

public interface IUrlAccionable {
	public String getAccionUrl();
	
	/**
	 * Siempre ser� la href o una llamada a una direcci�n, depende de la implementaci�n. Simpre partir� desde "http://www.&lt;dominio&gt;.&lt;ext&gt;/&lt;cliente&gt;/&lt;modulo&gt;/", osea la 
	 * direcci�n debe ir desde la base
	 * 
	 * @author Pancho
	 * @since 29-05-2019
	 * */
	public void setAccionUrl(String accionUrl);
	
	public String getAccionWaitMsg();
	
	public void setAccionWaitMsg(String accionWaitMsg);
	
	public String getAccionOnSuccessMsg();
	
	public void setAccionOnSuccessMsg(String accionOnSuccessMsg);
	
	public String getAccionOnFailureMsg();
	
	public void setAccionOnFailureMsg(String accionOnFailureMsg);
	
	public boolean isAccionConfirmed();
	
	/**
	 * Si la petici�n debe ser confirmafa o no depende de si getAccionConfirmMsg() != null <br/>
	 * si getAccionConfirmMsg!= null, si ser� confirmada <br/>
	 * si getAccionConfirmMsg == null, no se confirmar�
	 * 
	 * @deprecated
	 * */
	public void setAccionConfirmed(boolean accionConfirmed);
	
	public String getAccionConfirmTitle();
	
	public void setAccionConfirmTitle(String accionConfirmTitle);
	
	public String getAccionConfirmMsg();
	
	public void setAccionConfirmMsg(String accionConfirmMsg);
	
}
