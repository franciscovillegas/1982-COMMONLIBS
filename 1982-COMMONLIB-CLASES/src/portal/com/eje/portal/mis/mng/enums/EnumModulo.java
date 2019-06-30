package portal.com.eje.portal.mis.mng.enums;

import java.lang.reflect.Field;

public enum EnumModulo {
	
	Periodos (1, "Periodos"),
	TrabajadoresHistoria (2, "Trabajadores Historia"),
	Trabajadores (3, "Trabajadores"),
	Usuario (4, "Usuario"),
	Fotos (5, "Fotos"),
	Afp (6, "Afp"),
	Isapre (7, "Isapre"),
	Cargos (8, "Cargos"),
	CentrosdeCosto (9, "Centros de Costo"),
	Ciudades (10, "Ciudades"),
	Comunas (11, "Comunas"),
	Dependencias (12, "Dependencias"),
	GrupoFamiliar (13, "Grupo Familiar"),
	Institutos (14, "Institutos"),
	Regiones (15, "Regiones"),
	Sindicatos (16, "Sindicatos"),
	Sucursales (17, "Sucursales"),
	UnidadesAdministrativas (18, "Unidades Administrativas"),
	PersonalRetiro (19, "Personal Retiro"),
	TP (20, "TP"),
	LicenciasMédicas (21, "Licencias Médicas"),
	Vacaciones (22, "Vacaciones"),
	VacacionesDet (23, "Vacaciones Det"),
	VacacionesDev (24, "Vacaciones Dev"),
	HorasExtras (25, "Horas Extras"),
	Remuneraciones (26, "Remuneraciones"),
	RemuneracionesAdicionales (27, "Remuneraciones Adicionales"),
	RemuneracionesDetalleDescuento (28, "Remuneraciones Detalle Descuento"),
	RemuneracionesDetalleDescuentoAdicional (29, "Remuneraciones Detalle Descuento Adicional"),
	RemuneracionesDetalleHaberes (30, "Remuneraciones Detalle Haberes"),
	RemuneracionesDetalleHaberesAdicional (31, "Remuneraciones Detalle Haberes Adicional"),
	HaberesTrabajador (32, "Haberes Trabajador"),
	Certificado3Meses (33, "Certificado 3 Meses"),
	DotaciónHaberesDescuentos (34, "Dotación Haberes Descuentos"),
	DotaciónEgresos (35, "Dotación Egresos"),
	HonorariosSAPS (36, "Honorarios SAPS"),
	ActualizacionBases (37, "Actualizacion Bases");

	
	
	private int intId;
	private String strNombre;
	
	EnumModulo(int intId, String strNombre) {
		this.intId = intId;
		this.strNombre = strNombre;
	}
	
	public int getId() {
		return intId;
	}
	public String getNombre() {
		return strNombre;
	}
	
	public void setNombre(String strNombre) {
		this.strNombre = strNombre;
	}
	
	public static EnumModulo fromInteger(int id) {
		Field[] fields = EnumModulo.class.getDeclaredFields();
		for(Field f : fields) {
			if( !f.getType().isAssignableFrom(EnumModulo.class) ) {
				continue;
			}
			
			EnumModulo enm = Enum.valueOf(EnumModulo.class, f.getName());
			if(enm.getId() == id) {
				return enm;
			}
			
		}
		
		return null;
	}
	
	
}
