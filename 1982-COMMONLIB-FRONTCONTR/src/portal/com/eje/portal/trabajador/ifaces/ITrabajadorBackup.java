package portal.com.eje.portal.trabajador.ifaces;

import java.util.List;

public interface ITrabajadorBackup {

	/**
	 * Lleva un grupo de trabajadores a la tabla historia
	 * 
	 * @since 07-11-2018
	 * @author Pancho
	 * */
	public boolean toHistory(Integer rut);
	
	/**
	 * Lleva un grupo de trabajadores a la tabla historia
	 * 
	 * @since 07-11-2018
	 * @author Pancho
	 * */
	public boolean toHistory(List<Integer> listaRuts);
}
