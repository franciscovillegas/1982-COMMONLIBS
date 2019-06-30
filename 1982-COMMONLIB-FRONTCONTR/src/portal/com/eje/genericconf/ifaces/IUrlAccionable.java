package portal.com.eje.genericconf.ifaces;

public interface IUrlAccionable {
	public String getAccionUrl();
	
	/**
	 * Siempre será la href o una llamada a una dirección, depende de la implementación. Simpre partirá desde "http://www.&lt;dominio&gt;.&lt;ext&gt;/&lt;cliente&gt;/&lt;modulo&gt;/", osea la 
	 * dirección debe ir desde la base
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
	 * Si la petición debe ser confirmafa o no depende de si getAccionConfirmMsg() != null <br/>
	 * si getAccionConfirmMsg!= null, si será confirmada <br/>
	 * si getAccionConfirmMsg == null, no se confirmará
	 * 
	 * @deprecated
	 * */
	public void setAccionConfirmed(boolean accionConfirmed);
	
	public String getAccionConfirmTitle();
	
	public void setAccionConfirmTitle(String accionConfirmTitle);
	
	public String getAccionConfirmMsg();
	
	public void setAccionConfirmMsg(String accionConfirmMsg);
	
}
