package portal.com.eje.portal.trabajador.ifaces;

import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.ArrayFactory;
import portal.com.eje.portal.trabajador.enums.EjeGesTrabajadorField;

public interface ITrabajadorInfoPersonal {

	/**
	 * 
	 * Retorna una lista de trabajadores según los Fields que se le pase y que estén contenidos en la lista de ruts
	 * @author Pancho
	 * @since 23-06-2018
	 * */
	public ConsultaData getData(ArrayFactory ruts, List<EjeGesTrabajadorField> fields);
	
	/**
	 * 
	 * Retorna una lista de trabajadores según los Fields que se le pase,que estén contenidos en la lista de ruts y que además su nombre y cargo contenga palabras del filtro
	 * @author Pancho
	 * @since 23-06-2018
	 * */		
	public ConsultaData getData(ArrayFactory ruts, List<EjeGesTrabajadorField> fields, String filtro);
	
	/**
	 * 
	 * Retorna una lista de trabajadores según los Fields que se le pase,que estén contenidos en la lista de ruts y que además su nombre y cargo contenga palabras del filtro
	 * @author Pancho
	 * @since 23-06-2018
	 * */		
	public ConsultaData getData(ArrayFactory ruts, List<EjeGesTrabajadorField> fields, String filtro, ConsultaData orderBy);
	

	
	/**
	 * 
	 * Retorna la data de un trabajador según la cantidad de fields que se le pasen
	 * @author Pancho
	 * @since 23-06-2018
	 * */
	public ConsultaData getData(Integer rut, List<EjeGesTrabajadorField> fields);
}
