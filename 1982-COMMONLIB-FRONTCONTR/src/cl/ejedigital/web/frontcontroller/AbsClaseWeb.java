package cl.ejedigital.web.frontcontroller;

/**
 * @deprecated
 * @since 2015-06-23
 * @author Francisco
 * */

public abstract class AbsClaseWeb {
	private IOClaseWeb ioClaseWeb;
	
	public AbsClaseWeb(IOClaseWeb ioClaseWeb) {
		this.ioClaseWeb = ioClaseWeb;
	}
	
	protected IOClaseWeb getIoClaseWeb() {
		return ioClaseWeb;
	}
	
	public abstract void doPost() throws Exception ;
    
	public abstract void doGet() throws Exception ;

}
