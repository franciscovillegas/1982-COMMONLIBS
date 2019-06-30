package portal.com.eje.portal.generico_appid;

/**
 * Los AppId, sin acciones, ver, modificar, etc, más el modulo o zona, aquellos que solo se identifica la zona, con el tiempo se le irán agregando otros verbos a la misma zona
 * 	
 * @author Pancho
 * @since 17-05-2019
 * */
public enum EnumAppId {
	ADM							(EnumAppIdAtributo.ADM , "Administrador de la plataforma, funciona para todos los módulos"),
	ADM_BENEFICIOS				(EnumAppIdAtributo.ADM ,	"administrar Beneficios", true),
	ADM_CLAVE					(EnumAppIdAtributo.ADM ,	"Administrador de Cambios Clave", true),
	ADM_CP						(EnumAppIdAtributo.ADM ,	"Administrador Control de Procesos", true),
	ADM_GP						(EnumAppIdAtributo.ADM ,	"Gestor de Procesos", true),
	ADM_MASTER					(EnumAppIdAtributo.ADM ,	"Administrador Maestro Personal", true),
	ADM_OS 						(EnumAppIdAtributo.ADM ,	"Administrador de Estructura Organizacional"),
	ADM_SOLVAC					(EnumAppIdAtributo.ADM ,	"Administrador Solicitudes de Vacaciones", true),
	ADM_TRACK					(EnumAppIdAtributo.ADM ,	"Informacion de Tracking"),
	ANEX						(EnumAppIdAtributo.ADM ,	"Anexos", true),
	CERT						(EnumAppIdAtributo.ADM ,	"Certificados", true),
	DF							(EnumAppIdAtributo.ADM ,	"Gestion", true),
	GGD_ADM						(EnumAppIdAtributo.ADM ,	"Administrador de GGD"),
	MIESTRUC_LIKEBOSS			(EnumAppIdAtributo.ADM ,	"Puede ver la estructura dependiente"),
	MIS							(EnumAppIdAtributo.ADM ,	"RRHH MIS"),
	PARTICIPA_ADM				(EnumAppIdAtributo.ADM ,	"Participa Adm."),
	PRFL_CAPACITACION			(EnumAppIdAtributo.VER ,	"Capacitación"),
	PRFL_CAPACITACION_ADM		(EnumAppIdAtributo.ADM ,	"Capacitación admin"),
	PRFL_AMONESTA				(EnumAppIdAtributo.VER ,	"WF amonestaciones"),
	PRFL_WFDOTACION				(EnumAppIdAtributo.VER ,	"WF de dotación"),
	PRFL_EGRESO					(EnumAppIdAtributo.VER ,	"WF de Egreso"),
	PRFL_FPERSONA				(EnumAppIdAtributo.VER ,	"WF de ficha"),
	PRFL_RELLAB_ADM				(EnumAppIdAtributo.VER ,	"Relaciones Laborares Adm."),
	PRFL_RELLAB_FORMS			(EnumAppIdAtributo.VER ,	"Relaciones Laborares usuario"),
	PRFL_TRASLD					(EnumAppIdAtributo.VER ,	"WF de Traslado"),
	PUB							(EnumAppIdAtributo.ADM ,	"Publicación", true),
	PUB_DIA						(EnumAppIdAtributo.ADM ,	"Diario Mural", true),
	PUB_NOT						(EnumAppIdAtributo.ADM ,	"Noticias", true),
	PUB_REV						(EnumAppIdAtributo.ADM ,	"Revista Institucional", true),
	SH							(EnumAppIdAtributo.VER ,	"Atributo de logeo, permite logearse"),
	SID							(EnumAppIdAtributo.VER , 	"Sistema SID", true),
	TRASP						(EnumAppIdAtributo.ADM, 	"Administrador de Traspasos", true),
	USERADM						(EnumAppIdAtributo.ADM, 	"Usuario con privilegio de adm", true),
	VIG							(EnumAppIdAtributo.ADM, 	"Vigilantes", true),
	WFV							(EnumAppIdAtributo.VER,		"WF Vacaciones", true),
	PRFL_QSMSELECCION			(EnumAppIdAtributo.VER, 	"Permite acceder a QSMSELECCIÓN."),
	PRFL_WFINGRESO				(EnumAppIdAtributo.VER, 	"WF de Ingreso"),
	PRFL_GGD					(EnumAppIdAtributo.VER,		"GGD gestión del desempeño"),
	PRFL_ADM_VERIF_DATOS_PERS	(EnumAppIdAtributo.ADM,  	"Adm. de Verificación de datos personales"),
	TABLE_FACTORIA_ADM			(EnumAppIdAtributo.ADM, 	"Adm de table factoría"),
	TABLE_BUSCADOR_ADM			(EnumAppIdAtributo.ADM,		"Adm de table buscador"),
	TABLE_BLOG					(EnumAppIdAtributo.VER,		"Ver table blog"),
	TABLE_BLOG_ADM				(EnumAppIdAtributo.ADM,		"Adm de table blog"),
	MOD_DATOS_PERSONALES		(EnumAppIdAtributo.ADM,		"Modificar los datos personales"),
	PERFILADOR_ADM				(EnumAppIdAtributo.ADM,		"Adm Perfiles de Peoplemanager");
	
	
	private EnumAppIdAtributo atributo;
	private String descripcion;
	private boolean deprecate;
	
	EnumAppId(EnumAppIdAtributo atrib, String descripcion) {
		this.atributo = atrib;
		this.descripcion = descripcion;
		this.deprecate = false;
	}
	
	EnumAppId(EnumAppIdAtributo atrib, String descripcion, boolean deprecate) {
		this.atributo = atrib;
		this.descripcion = descripcion;
		this.deprecate = deprecate;
	}
	
	@Override
	public String toString() {
	 
		return this.name();
	}
	
	public String getAppId() {
		// TODO Auto-generated method stub
		return super.toString().toLowerCase();
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public boolean isDeprecate() {
		return this.deprecate;
	}

	public EnumAppIdAtributo getAtributo() {
		return atributo;
	}
	
	
}
