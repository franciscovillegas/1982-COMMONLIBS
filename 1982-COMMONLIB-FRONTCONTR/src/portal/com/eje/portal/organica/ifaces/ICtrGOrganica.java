package portal.com.eje.portal.organica.ifaces;

import java.util.Collection;

import cl.ejedigital.consultor.ConsultaData;
import portal.com.eje.portal.organica.vo.VoTrabajadorUnidad;
import portal.com.eje.portal.organica.vo.VoUnidad;

public interface ICtrGOrganica {

	/**
	 * 
	 * Retorna rut, nombre, cargo y empresa del jefe de la unidad indicada, si dicha
	 * unidad no tiene un jefe asignado retornará null.
	
	 * @author Pancho
	 * @since 18-06-2018
	 * */
	public VoTrabajadorUnidad getJefeUnidad(String unidad);

	/**
	 * Retorna los ruts de todas personas que estan en las unidades dependientes.
	 * @author Pancho
	 * @since 18-06-2018
	 * */
	public Collection<VoTrabajadorUnidad> getTrabajadoresDependientes(String unidad, boolean omiteEstaUnidad);

	/**
	 * Retorna información del trabajador
	 * @author Pancho
	 * @since 18-06-2018
	 * */
	public VoTrabajadorUnidad getTrabajadores(int rutIdInt);

	/**
	 * Retorna las unidades Ascendientes
	 * @author Pancho
	 * @since 18-06-2018
	 * */
	public Collection<VoUnidad> getUnidadesAscendientes(String unid_id);

	public VoTrabajadorUnidad getJefeDelTrabajador(int rutIdInt);

	/**
	 * Retorna una Collections de VoTrabajadorUnidad para un filtro 
	 * @author Pancho
	 * @since 20-06-2018
	 * 
	 * */
	public Collection<VoTrabajadorUnidad> getTrabajadores(String filtro);

	/**
	 * retorna la unidad de un rut
	 * @author Pancho
	 * @since 20-06-2018
	 * */
	public VoUnidad getUnidadFromRut(int rutIdInt);
}
