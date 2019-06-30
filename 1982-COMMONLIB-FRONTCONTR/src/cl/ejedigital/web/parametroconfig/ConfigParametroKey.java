package cl.ejedigital.web.parametroconfig;


public class ConfigParametroKey {
	public final static ConfigParametroKey ggd_calculo_metodo =  new ConfigParametroKey("ggd.calculo.metodo");
	
	public final static ConfigParametroKey generico_string_cuentacaducada =  new ConfigParametroKey("generico.string.cuentacaducada");
	public final static ConfigParametroKey generico_string_intentosfallidos =  new ConfigParametroKey("generico.string.intentosfallidos");
	
	
	public final static ConfigParametroKey generico_session_segundoscaducidad =  new ConfigParametroKey("generico.session.segundoscaducidad");
	public final static ConfigParametroKey generico_session_usuario =  new ConfigParametroKey("generico.sesion.usuario");
	public final static ConfigParametroKey generico_session_manager =  new ConfigParametroKey("generico.sesion.manager");
	public final static ConfigParametroKey generico_sesion_usuario_util =  new ConfigParametroKey("generico.sesion.usuario.util");
	public final static ConfigParametroKey generico_acceso_intentos_cantidad =  new ConfigParametroKey("generico.acceso.intentos.cantidad");
	public final static ConfigParametroKey generico_acceso_intentos_entiempo =  new ConfigParametroKey("generico.acceso.intentos.entiempo");
	public final static ConfigParametroKey generico_acceso_caducidad_enminutos =  new ConfigParametroKey("generico.acceso.caducidad.enminutos");
	
	public final static ConfigParametroKey sql_get_ejegesusuario_xrutusuario =  new ConfigParametroKey("sql.get.ejegesusuario.xrutusuario");
	public final static ConfigParametroKey sql_get_ejegestrabajador_xrut =  new ConfigParametroKey("sql.get.ejegestrabajador.xrut");
	public final static ConfigParametroKey sql_get_ejegestrabajadorexterno_xrut =  new ConfigParametroKey("sql.get.ejegestrabajadorexterno.xrut");
	public final static ConfigParametroKey sql_get_ejegesunidad_xrut =  new ConfigParametroKey("sql.get.ejegesunidad.xrut");
	public final static ConfigParametroKey sql_get_ejegesempresa_xempresa =  new ConfigParametroKey("sql.get.ejegesempresa.xempresa");
	public final static ConfigParametroKey sql_get_ejegenericoperfil_xrut =  new ConfigParametroKey("sql.get.ejegenericoperfil.xrut");
	public final static ConfigParametroKey sql_get_ejegenericoperfilapp_xrut =  new ConfigParametroKey("sql.get.ejegenericoperfilapp.xrut");
	public final static ConfigParametroKey sql_get_ejegesbloqueoaccesousuario_xrut =  new ConfigParametroKey("sql.get.ejegesbloqueoaccesousuario.xrut");
	public final static ConfigParametroKey sql_get_ejegesusuarioultacceso_xrut =  new ConfigParametroKey("sql.get.ejegesusuarioultacceso.xrut");
	public final static ConfigParametroKey sql_get_ejegesusuarioultacceso_ultingreso_xrut =  new ConfigParametroKey("sql.get.ejegesusuarioultacceso.ultingreso.xrut");
	public final static ConfigParametroKey sql_upd_ejegesusuario_cpasswordusuario_xrutusuario_xwpcodempresa =  new ConfigParametroKey("sql.upd.ejegesusuario.cpasswordusuario.xrutusuario.xwpcodempresa");
	public final static ConfigParametroKey sql_upd_ejegesusuario_ccantingresos_cant_xrutusuario_xwpcodempresa =  new ConfigParametroKey("sql.upd.ejegesusuario.ccantingresos.cant.xrutusuario.xwpcodempresa");
	public final static ConfigParametroKey sql_upd_ejegesbloqueoaccesousuario_cintentosfallidos_xrut =  new ConfigParametroKey("sql.upd.ejegesbloqueoaccesousuario.cintentosfallidos.xrut");
	
	public final static ConfigParametroKey sql_del_ejegesusuarioultacceso_xrut =  new ConfigParametroKey("sql.del.ejegesusuarioultacceso.xrut");
	public final static ConfigParametroKey sql_del_ejegesbloqueoaccesousuario_xrut =  new ConfigParametroKey("sql.del.ejegesbloqueoaccesousuario.xrut");
	public final static ConfigParametroKey sql_del_ejegesbloqueoaccesousuario_xrut_xtiempo =  new ConfigParametroKey("sql.del.ejegesbloqueoaccesousuario.xrut.xtiempo");
	
	public final static ConfigParametroKey sql_add_ejegesusuarioultacceso =  new ConfigParametroKey("sql.add.ejegesusuarioultacceso");
	public final static ConfigParametroKey sql_add_ejegesbloqueoaccesousuario =  new ConfigParametroKey("sql.add.ejegesbloqueoaccesousuario");
	
	private String key;
	
	public ConfigParametroKey(String key) {
		this.key = key;
	}

	public String toString() {
		return key;
	}
	
}
