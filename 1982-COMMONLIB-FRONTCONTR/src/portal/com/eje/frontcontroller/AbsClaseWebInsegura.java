package portal.com.eje.frontcontroller;

public abstract class AbsClaseWebInsegura extends IOLog {
	private IOClaseWeb ioClaseWeb;
	
	public AbsClaseWebInsegura(IOClaseWeb ioClaseWeb) {
		this.ioClaseWeb = ioClaseWeb;
	}
	
	protected IOClaseWeb getIoClaseWeb() {
		return ioClaseWeb;
	}
	
	public abstract void doPost() throws Exception ;
    
	public abstract void doGet() throws Exception ;
	
	

}
