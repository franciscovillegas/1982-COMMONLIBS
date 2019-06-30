package portal.com.eje.portal.trabajador.ifaces;

import java.util.List;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.strings.ArrayFactory;
import portal.com.eje.portal.trabajador.enums.EjeGesTrabajadorHistoriaField;

public interface ITrabajadorInfoPersonalHistoria {

 
	/**
	 * 
	 * Retorna una lista de trabajadores según los Fields que se le pase,que estén contenidos en la lista de ruts y que además su nombre y cargo contenga palabras del filtro
	 * @author Pancho
	 * @since 23-06-2018
	 * */		
	public ConsultaData getData(ArrayFactory ruts, List<EjeGesTrabajadorHistoriaField> fields, ConsultaData orderBy, Integer periodo);
	
	public ConsultaData getData(int rut, List<EjeGesTrabajadorHistoriaField> fields, Integer periodo);
	
	public List<Integer> getPeriodos(ArrayFactory ruts);
	
	public List<Integer> getPeriodos(int rut);
}
