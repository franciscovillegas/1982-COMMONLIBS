package portal.com.eje.permiso;


public class PermisoPortal {
	private String appId;
	public static PermisoPortal ORG_TOOL_ASIGNAR_TRABAJADOR_UNIDAD = new PermisoPortal("herr_org_asignar_unidad_trabajador");
	public static PermisoPortal ADMIN_MOD_USUARIOS_BLOQUEADOS= new PermisoPortal("op_adm_mod_user_bloqueados");
	public static PermisoPortal ADMIN_GET_INFO_TRACKING= new PermisoPortal("op_adm_get_list_tracking");
	public static PermisoPortal ADMIN_GET_RESUMEN_TRACKING= new PermisoPortal("op_adm_get_resumen_tracking");
	
	/*20170911*/
	public static PermisoPortal MiEstrucLikeABOSS= new PermisoPortal("miestruc_likeboss");
	
	/*20171018*/
	public static PermisoPortal Admin_Participa  = new PermisoPortal("participa_adm");
	
	public static PermisoPortal CAJACHILE_ADM = new PermisoPortal("agen_adm");
	public static PermisoPortal BOTON_GESTION = new PermisoPortal("btn_gestion");
	
	private PermisoPortal(String appId) {
		this.appId = appId;
	}
	
	public String toString() {
		return this.appId;
	}
}
