package portal.com.eje.portal.liquidacion.ifaces;

import java.util.List;

import portal.com.eje.serhumano.user.Usuario;

public interface ILiquidacionAccessPolicy {

	/**
	 * Retorna la totalidad de ruts que la persona tiene acceso a la liquidación,<br/>
	 * incluye ruts por ser jefe y ruts como encargado relativo<br/>
	 * Nunca retorna null
	 * 
	 * @author Pancho
	 * @since 05-07-2018
	 * 
	 * */
	public List<Integer> getPermitidos(Usuario u);
	
	/**
	 * Retorna true si la persona tiene acceso a ver la liquidación de dicho rut <br/>
	 * incluye ruts por ser jefe y ruts como encargado relativo<br/>
	 * Nunca retorna null
	 * 
	 * @author Pancho
	 * @since 05-07-2018
	 * 
	 * */
	public boolean puedoVer(Usuario u, int quiero);
}
