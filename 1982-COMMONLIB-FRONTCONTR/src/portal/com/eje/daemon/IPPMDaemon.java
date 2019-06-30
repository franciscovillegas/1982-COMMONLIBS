package portal.com.eje.daemon;

public interface IPPMDaemon {

	/**
	 * Debe retornar el mensaje a logear
	 * @author Pancho
	 * @since 19-10-2018
	 * */
	public abstract CharSequence doActions() throws Exception;
	
}
