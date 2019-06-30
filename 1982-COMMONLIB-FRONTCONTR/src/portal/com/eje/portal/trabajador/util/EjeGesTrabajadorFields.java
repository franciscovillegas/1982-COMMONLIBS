package portal.com.eje.portal.trabajador.util;

import java.util.ArrayList;
import java.util.List;

import portal.com.eje.portal.trabajador.enums.EjeGesTrabajadorField;

public class EjeGesTrabajadorFields {

	
	public static List<EjeGesTrabajadorField> getRut() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
		normalFields.add(EjeGesTrabajadorField.rut);
		
		return normalFields;
	}
	
	public static List<EjeGesTrabajadorField> geRuttNombre() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
		normalFields.add(EjeGesTrabajadorField.rut);
		normalFields.add(EjeGesTrabajadorField.nombre);
		
		return normalFields;
	}
	
	public static List<EjeGesTrabajadorField> geRuttNombreMails() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
		normalFields.add(EjeGesTrabajadorField.rut);
		normalFields.add(EjeGesTrabajadorField.nombre);
		normalFields.add(EjeGesTrabajadorField.mail);
		normalFields.add(EjeGesTrabajadorField.e_mail);
		
		return normalFields;
	}
	
	public static List<EjeGesTrabajadorField> getRutNombreCargo() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
		normalFields.add(EjeGesTrabajadorField.rut);
		normalFields.add(EjeGesTrabajadorField.nombre);
		normalFields.add(EjeGesTrabajadorField.cargo);
		
		return normalFields;
	}
	
	public static List<EjeGesTrabajadorField> getRutNombreCargoCC() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
		normalFields.add(EjeGesTrabajadorField.rut);
		normalFields.add(EjeGesTrabajadorField.nombre);
		normalFields.add(EjeGesTrabajadorField.cargo);
		normalFields.add(EjeGesTrabajadorField.ccosto);
		return normalFields;
	}
	
	public static List<EjeGesTrabajadorField> getRutNombreCargoCCEmpresa() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
		normalFields.add(EjeGesTrabajadorField.rut);
		normalFields.add(EjeGesTrabajadorField.nombre);
		normalFields.add(EjeGesTrabajadorField.cargo);
		normalFields.add(EjeGesTrabajadorField.ccosto);
		normalFields.add(EjeGesTrabajadorField.wp_cod_empresa);
		return normalFields;
	}	
	
	public static List<EjeGesTrabajadorField> getRutIdTabTrabNombreCargoCCEmpresa() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
//		normalFields.add(EjeGesTrabajadorField.id_tabtrab);
		normalFields.add(EjeGesTrabajadorField.rut);
		normalFields.add(EjeGesTrabajadorField.nombre);
		normalFields.add(EjeGesTrabajadorField.cargo);
		normalFields.add(EjeGesTrabajadorField.ccosto);
		normalFields.add(EjeGesTrabajadorField.wp_cod_empresa);
		return normalFields;
	}
	
	public static List<EjeGesTrabajadorField> getRutNombreUnidadMails() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
		normalFields.add(EjeGesTrabajadorField.rut);
		normalFields.add(EjeGesTrabajadorField.nombre);
		normalFields.add(EjeGesTrabajadorField.unidad);
		normalFields.add(EjeGesTrabajadorField.mail);
		normalFields.add(EjeGesTrabajadorField.e_mail);
		
		return normalFields;
	}
	
	public static List<EjeGesTrabajadorField> getRutNombreUnidad() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
		normalFields.add(EjeGesTrabajadorField.rut);
		normalFields.add(EjeGesTrabajadorField.nombre);
		normalFields.add(EjeGesTrabajadorField.unidad);
		
		return normalFields;
	}
	
	public static List<EjeGesTrabajadorField> getNormalFields() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
		normalFields.add(EjeGesTrabajadorField.rut);
		normalFields.add(EjeGesTrabajadorField.digito_ver);
		normalFields.add(EjeGesTrabajadorField.nombre);
		normalFields.add(EjeGesTrabajadorField.ape_paterno);
		normalFields.add(EjeGesTrabajadorField.ape_materno);
		normalFields.add(EjeGesTrabajadorField.cargo);
		normalFields.add(EjeGesTrabajadorField.fecha_ingreso);
		normalFields.add(EjeGesTrabajadorField.unidad);
		
		return normalFields;
	}
	
	public static List<EjeGesTrabajadorField> getNombres() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
		normalFields.add(EjeGesTrabajadorField.rut);
		normalFields.add(EjeGesTrabajadorField.digito_ver);
		normalFields.add(EjeGesTrabajadorField.nombre);
		normalFields.add(EjeGesTrabajadorField.nombres);
		normalFields.add(EjeGesTrabajadorField.ape_paterno);
		normalFields.add(EjeGesTrabajadorField.ape_materno);;
		return normalFields;
	}
	
	public static List<EjeGesTrabajadorField> getNormalFieldsAndCC() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
		normalFields.add(EjeGesTrabajadorField.rut);
		normalFields.add(EjeGesTrabajadorField.digito_ver);
		normalFields.add(EjeGesTrabajadorField.nombre);
		normalFields.add(EjeGesTrabajadorField.ape_paterno);
		normalFields.add(EjeGesTrabajadorField.ape_materno);
		normalFields.add(EjeGesTrabajadorField.cargo);
		normalFields.add(EjeGesTrabajadorField.fecha_ingreso);
		normalFields.add(EjeGesTrabajadorField.unidad);
		normalFields.add(EjeGesTrabajadorField.ccosto);
		return normalFields;
	}
	
	public static List<EjeGesTrabajadorField> getRutNombresCargoFecIngUnidadCC() {
		List<EjeGesTrabajadorField> normalFields = new ArrayList<EjeGesTrabajadorField>();
		normalFields.add(EjeGesTrabajadorField.rut);
		normalFields.add(EjeGesTrabajadorField.digito_ver);
		normalFields.add(EjeGesTrabajadorField.nombre);
		normalFields.add(EjeGesTrabajadorField.nombres);
		normalFields.add(EjeGesTrabajadorField.ape_paterno);
		normalFields.add(EjeGesTrabajadorField.ape_materno);
		normalFields.add(EjeGesTrabajadorField.cargo);
		normalFields.add(EjeGesTrabajadorField.fecha_ingreso);
		normalFields.add(EjeGesTrabajadorField.unidad);
		normalFields.add(EjeGesTrabajadorField.ccosto);
		return normalFields;
	}
}
