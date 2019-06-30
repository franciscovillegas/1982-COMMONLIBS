package portal.com.eje.portal.trabajador.util;

import java.util.ArrayList;
import java.util.List;

import portal.com.eje.portal.trabajador.enums.EjeGesTrabajadorHistoriaField;

public class EjeGesTrabajadorHistoriaFields {

	public static List<EjeGesTrabajadorHistoriaField> getRutIdTabTrabNombreCargoCCEmpresa() {
		List<EjeGesTrabajadorHistoriaField> normalFields = new ArrayList<EjeGesTrabajadorHistoriaField>();
//		normalFields.add(EjeGesTrabajadorHistoriaField.id_tabtrabh);
		normalFields.add(EjeGesTrabajadorHistoriaField.rut);
		normalFields.add(EjeGesTrabajadorHistoriaField.nombre);
		normalFields.add(EjeGesTrabajadorHistoriaField.cargo);
		normalFields.add(EjeGesTrabajadorHistoriaField.ccosto);
		normalFields.add(EjeGesTrabajadorHistoriaField.wp_cod_empresa);
		
		return normalFields;
	}

	public static List<EjeGesTrabajadorHistoriaField> getRutNombreCargoCCEmpresa() {
		List<EjeGesTrabajadorHistoriaField> normalFields = new ArrayList<EjeGesTrabajadorHistoriaField>();
		normalFields.add(EjeGesTrabajadorHistoriaField.rut);
		normalFields.add(EjeGesTrabajadorHistoriaField.nombre);
		normalFields.add(EjeGesTrabajadorHistoriaField.cargo);
		normalFields.add(EjeGesTrabajadorHistoriaField.ccosto);
		normalFields.add(EjeGesTrabajadorHistoriaField.wp_cod_empresa);
		
		return normalFields;
	}
	 
}
