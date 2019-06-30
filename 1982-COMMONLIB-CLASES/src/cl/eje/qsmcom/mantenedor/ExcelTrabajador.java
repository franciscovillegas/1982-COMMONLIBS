package cl.eje.qsmcom.mantenedor;

import java.util.List;

import cl.eje.qsmcom.managers.ManagerAcceso;
import cl.eje.qsmcom.managers.ManagerTrabajador;
import cl.eje.qsmcom.tool.ExcelMap;
import cl.eje.qsmcom.tool.ExcelMapping;
import cl.eje.qsmcom.tool.IExcelMappingAction;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.consultor.IFieldManipulator;
import cl.ejedigital.consultor.ManipulatorSqlServer2000;
import cl.ejedigital.tool.validar.Validar;



public class ExcelTrabajador extends ExcelMapping implements IExcelMappingAction {

	public static ExcelMap num_rut = getInstance().getMapById(0);
	public static ExcelMap rut_dve = getInstance().getMapById(1);
	public static ExcelMap nombre = getInstance().getMapById(2);
	public static ExcelMap nombres = getInstance().getMapById(3);
	public static ExcelMap apePaterno = getInstance().getMapById(4);
	public static ExcelMap apeMaterno = getInstance().getMapById(5);
	public static ExcelMap cargo_codig = getInstance().getMapById(6);
	public static ExcelMap cargo_glosa = getInstance().getMapById(7);
	public static ExcelMap sucur_codigo = getInstance().getMapById(8);
	public static ExcelMap sucur_glosa = getInstance().getMapById(9);
	public static ExcelMap estr_cuizon = getInstance().getMapById(10);
	public static ExcelMap fec_ingeso = getInstance().getMapById(11);
	public static ExcelMap licencia = getInstance().getMapById(12);
	public static ExcelMap vacaciones = getInstance().getMapById(13);
	public static ExcelMap centro_costo = getInstance().getMapById(14);
	public static ExcelMap division = getInstance().getMapById(15);
	public static ExcelMap empresa = getInstance().getMapById(16);
	public static ExcelMap ofi_email = getInstance().getMapById(17);
	
	private static ExcelTrabajador instance;
	
	public ExcelTrabajador() {
		
	}
	
	private void loadColumns() {
		ExcelTrabajador.getInstance().addMap(0, "num_rut"			, "rut");
		ExcelTrabajador.getInstance().addMap(1, "rut_dve"			, "digito_ver");
		ExcelTrabajador.getInstance().addMap(2, "nombre"			, "nombre");
		ExcelTrabajador.getInstance().addMap(3, "nombres"			, "nombres");
		ExcelTrabajador.getInstance().addMap(4, "apePaterno"		, "ape_paterno");
		ExcelTrabajador.getInstance().addMap(5, "apeMaterno"		, "ape_materno");
		ExcelTrabajador.getInstance().addMap(6, "cargo_codig"		, "cargo");
		ExcelTrabajador.getInstance().addMap(7, "cargo_glosa"		, null);
		ExcelTrabajador.getInstance().addMap(8, "sucur_codigo"		, "unidad");
		ExcelTrabajador.getInstance().addMap(9, "sucur_glosa"		, null);
		ExcelTrabajador.getInstance().addMap(10, "estr_cuizon"		, "dependencia");
		ExcelTrabajador.getInstance().addMap(11, "fec_ingeso"		, "fecha_ingreso");
		ExcelTrabajador.getInstance().addMap(12, "licencia"			, null);
		ExcelTrabajador.getInstance().addMap(13, "vacaciones"		, null);
		ExcelTrabajador.getInstance().addMap(14, "centro_costo"		, "ccosto");
		ExcelTrabajador.getInstance().addMap(15, "division"			, "division");
		ExcelTrabajador.getInstance().addMap(16, "empresa"			, null);
		ExcelTrabajador.getInstance().addMap(17, "ofi_email"		, "mail");
	}
	
	public static ExcelTrabajador getInstance() {
		if(instance == null) {
			synchronized (ExcelTrabajador.class) {
				if(instance == null) {
					instance = new ExcelTrabajador();
					instance.loadColumns();
				}
			}
		}
		
		return instance;
	}


	public boolean doInsert(DataFields campos) {
		ManagerTrabajador q = ManagerTrabajador.getInstance();
		IFieldManipulator field = new ManipulatorSqlServer2000(); 
		

		String rut = field.getForcedString(getField(campos,num_rut));
		int rutInt = Validar.getInstance().validarInt(rut, -1);

		boolean ok = false;
		
		if(rutInt != -1) {
			ok = q.insertTrabajador(
					rutInt,
				field.getForcedString(getField(campos,rut_dve)), 
				field.getForcedString(getField(campos,nombre)),
				field.getForcedString(getField(campos,nombres)),
				field.getForcedString(getField(campos,apePaterno)),
				field.getForcedString(getField(campos,apeMaterno)),
				field.getForcedString(getField(campos,cargo_codig)), 
				field.getForcedString(getField(campos,sucur_codigo)),
				field.getForcedString(getField(campos,estr_cuizon)),
				field.getForcedString(getField(campos,fec_ingeso)),
				field.getForcedString(getField(campos,centro_costo)),
				field.getForcedString(getField(campos,division)),
				field.getForcedString(getField(campos,ofi_email)));
			
			if(ok) {
				if(q.existeContrasena(rutInt)) {
					q.updateContrasena(String.valueOf(rutInt), rutInt);
				}
				else {
					q.insertContrasena(String.valueOf(rutInt), rutInt);
				}
				
				List<String> privilegios = ManagerAcceso.getInstance().getPrivilegios(rutInt);
				if(privilegios.indexOf("sh") == -1) {
					ManagerAcceso.getInstance().addPrivilegios(rutInt, "sh");
				}
			}
				
		}
		
		return ok;
	}


	public boolean doUpdate(DataFields campos) {
		ManagerTrabajador q = ManagerTrabajador.getInstance();
		IFieldManipulator field = new ManipulatorSqlServer2000(); 
		
		String rut = field.getForcedString(getField(campos,num_rut));
		int rutInt = Validar.getInstance().validarInt(rut, -1);

		boolean ok = false;
		
		if(rutInt != -1) {
				ok = q.updateTrabajador(
				rutInt,
				field.getForcedString(getField(campos,rut_dve)), 
				field.getForcedString(getField(campos,nombre)),
				field.getForcedString(getField(campos,nombres)),
				field.getForcedString(getField(campos,apePaterno)),
				field.getForcedString(getField(campos,apeMaterno)),
				field.getForcedString(getField(campos,cargo_codig)), 
				field.getForcedString(getField(campos,sucur_codigo)),
				field.getForcedString(getField(campos,estr_cuizon)),
				field.getForcedString(getField(campos,fec_ingeso)),
				field.getForcedString(getField(campos,centro_costo)),
				field.getForcedString(getField(campos,division)),
				field.getForcedString(getField(campos,ofi_email)));
		}
		
		return ok;
	}


	public boolean doDelete(DataFields campos) {
		ManagerTrabajador q = ManagerTrabajador.getInstance();
		IFieldManipulator field = new ManipulatorSqlServer2000(); 
		
		String rut = field.getForcedString(getField(campos,num_rut));
		int rutInt = Validar.getInstance().validarInt(rut, -1);
		
		boolean ok = false;
		
		if(rutInt != -1) {
			ok = q.delTrabajador(rutInt);
		}
		
		return ok;
	}


	public boolean doExist(DataFields campos) {
		ManagerTrabajador q = ManagerTrabajador.getInstance();
		IFieldManipulator field = new ManipulatorSqlServer2000(); 

		String rut = field.getForcedString(getField(campos,num_rut));
		int rutInt = Validar.getInstance().validarInt(rut, -1);
		ConsultaData data = null;
		
		if(rutInt != -1) {
			data = q.getTrabajador(rutInt);
		}
		
		return data != null && data.size() > 0;
	}
	
	private Field getField(DataFields campos, ExcelMap m) {
		return campos.get(campos.keySet().toArray()[m.getPos()]);
	}

}