package cl.ejedigital.web.usuario;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.parametroconfig.ConfigParametro;
import cl.ejedigital.web.parametroconfig.ConfigParametroKey;
import cl.ejedigital.web.parametroconfig.ConfigParametroManager;
import cl.ejedigital.web.usuario.vo.VoEmpresa;
import cl.ejedigital.web.usuario.vo.VoTrabajador;
import cl.ejedigital.web.usuario.vo.VoUnidad;
import cl.ejedigital.web.usuario.vo.VoUsuario;
import cl.ejedigital.web.usuario.vo.VoUsuarioBloqueo;
import cl.ejedigital.web.usuario.vo.VoUsuarioPerfil;
import cl.ejedigital.web.usuario.vo.VoUsuarioUltAcceso;
import freemarker.template.SimpleHash;


public class UsuarioUtilsBase implements IUsuarioUtils {

	public UsuarioUtilsBase() {
		
	}
	
	
	public void cambiaPass(IUsuario user, String nuevaClave) {
		ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_upd_ejegesusuario_cpasswordusuario_xrutusuario_xwpcodempresa);
		Object[] parametros = {user.getDatosTrabajador().getRut(), user.getDatosUsuario().getWp_cod_empresa()};
		
		try {
			ConsultaTool.getInstance().update("portal",p.getSelectedKey(),parametros);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public SimpleHash toHash(IUsuario user) {
		return null;
		
	}


	public boolean updCantIngresosMasUno(IUsuario user) {
		ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_upd_ejegesusuario_ccantingresos_cant_xrutusuario_xwpcodempresa);
		Object[] parametros = {user.getDatosTrabajador().getRut(), user.getDatosUsuario().getWp_cod_empresa()};
		
		try {
			return ConsultaTool.getInstance().update("portal",p.getSelectedKey(),parametros) > 0;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}


	public boolean updUltAcceso(IUsuario user) {
		ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_del_ejegesusuarioultacceso_xrut);
		Object[] parametrosDel = {user.getDatosTrabajador().getRut()};
		
		try {
			ConsultaTool.getInstance().update("portal",p.getSelectedKey(),parametrosDel);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_add_ejegesusuarioultacceso);
		
		Object[] parametrosAdd = {user.getDatosTrabajador().getRut(), user.getDatosUsuario().getPassword_usuario(), "" };
		
		try {
			ConsultaTool.getInstance().update("portal",p.getSelectedKey(),parametrosAdd);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}


	public boolean resetAccesoFallidos(IUsuario user) {
		ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_del_ejegesbloqueoaccesousuario_xrut);
		Object[] parametrosDel = {user.getDatosTrabajador().getRut()};
		int regMods = 0;
		
		try {
			regMods = ConsultaTool.getInstance().update("portal",p.getSelectedKey(),parametrosDel);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return regMods != 0;
	}

	public boolean resetAccesoFallidosHaceXMinutos(IUsuario user) {
		ConfigParametro pSql = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_del_ejegesbloqueoaccesousuario_xrut_xtiempo);
		ConfigParametro pMinutos = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.generico_acceso_intentos_entiempo);
		
		Object[] parametrosDel = {user.getDatosTrabajador().getRut(), pMinutos.getSelectedKey()};
		int regMods = 0;
		
		try {
			regMods = ConsultaTool.getInstance().update("portal",pSql.getSelectedKey(),parametrosDel);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return regMods != 0;
	}

	public boolean registraUsuarioCaducado(IUsuario user) {
		ConfigParametro pSql = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_get_ejegesbloqueoaccesousuario_xrut);
		ConfigParametro strCuentaCaducada = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.generico_string_cuentacaducada);
		ConfigParametro pMaxMinutos = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.generico_acceso_caducidad_enminutos);
		ConfigParametro pSqlUltIngresoEnMinutos = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_get_ejegesusuarioultacceso_ultingreso_xrut);
		
		Object[] parametros = {user.getDatosTrabajador().getRut()};
		boolean ok = false;
		Validar val = new Validar();
		
		int maximoDeMinutosParaCaducir = val.validarInt(pMaxMinutos.getSelectedKey(), 2628000); // 5 Años si es null
		int minutosSinIngresar = 0;
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",pSqlUltIngresoEnMinutos.getSelectedKey(),parametros);
			if(data.next()) {
				minutosSinIngresar = data.getInt(0);
			}
		
			if(minutosSinIngresar >= maximoDeMinutosParaCaducir) {
		
				data = ConsultaTool.getInstance().getData("portal",pSql.getSelectedKey(),parametros);

				Date hoy = new Date(Calendar.getInstance().getTimeInMillis());
				
				if(data.next()) {
					pSql = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_upd_ejegesbloqueoaccesousuario_cintentosfallidos_xrut);
					
					Object[] parametrosUpd = {data.getInt("intentos_fallidos") + 1,"S", strCuentaCaducada.getSelectedKey(),hoy, user.getDatosTrabajador().getRut()};
					ok = ConsultaTool.getInstance().update("portal",pSql.getSelectedKey(),parametrosUpd) > 0;
				}
				else {
					pSql = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_add_ejegesbloqueoaccesousuario);
					
					Object[] parametrosAdd = {user.getDatosTrabajador().getRut(),"S",hoy,strCuentaCaducada.getSelectedKey()};
					ok = ConsultaTool.getInstance().update("portal",pSql.getSelectedKey(),parametrosAdd) > 0;	
				}
				
				
			}
			
		}
		catch (SQLException e) {
			ok = false;
			e.printStackTrace();
		}
		
		return ok;	
	}
 
	public VoUsuarioBloqueo getDatosUsuarioBloqueo(int rut) {
		ConfigParametro pSql = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_get_ejegesbloqueoaccesousuario_xrut);
		VoUsuarioBloqueo vo = null;
		Object[] parametros = {rut};
		
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",pSql.getSelectedKey(),parametros);
			
			if(data.next()) {
				vo = new VoUsuarioBloqueo(data.getInt("rut"),data.getInt("intentos_fallidos"),data.getString("bloqueado"),data.getTimestamp("fecha_bloqueo"),data.getTimestamp("fecha_ultimo_fallo"),data.getString("tipo_bloqueo"));
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			vo = null;
		}
		
		return vo;
		
	}

	public boolean registraIntentoFallido(IUsuario user) {
		Validar val = new Validar();
		
		ConfigParametro cantIntentosMaximos = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.generico_acceso_intentos_cantidad);
		ConfigParametro strIntenFallidos = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.generico_string_intentosfallidos);
		int cantMax = val.validarInt(cantIntentosMaximos.getSelectedKey(),0);		
		String tipo = null;
		Date date = null;
		boolean ok = false;
		
		try {
			
			VoUsuarioBloqueo vo = getDatosUsuarioBloqueo(user.getDatosTrabajador().getRut());
			
			if(vo != null) {
				ConfigParametro pSql = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_upd_ejegesbloqueoaccesousuario_cintentosfallidos_xrut);
				String estado = "N";
				
				if( (cantMax - 1) <= vo.getIntentos_fallidos()) {
					estado = "S";
					date = new Date(Calendar.getInstance().getTimeInMillis());
					tipo = strIntenFallidos.getSelectedKey();
				}
				
				Object[] parametrosUpd = {vo.getIntentos_fallidos() + 1,estado ,tipo , date, user.getDatosTrabajador().getRut()};
				
				ok = ConsultaTool.getInstance().update("portal",pSql.getSelectedKey(),parametrosUpd) > 0;
			}
			else {
				ConfigParametro pSql = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_add_ejegesbloqueoaccesousuario);
				String estado = "N";
				
				
				if( (cantMax - 1) <= 1 ) {
					estado = "S";
					date = new Date(Calendar.getInstance().getTimeInMillis());
					tipo = strIntenFallidos.getSelectedKey();
				}
				
				Object[] parametrosAdd = {user.getDatosTrabajador().getRut(),estado,date,strIntenFallidos.getSelectedKey()};
				
				ok = ConsultaTool.getInstance().insert("portal",pSql.getSelectedKey(),parametrosAdd);
			}
			
		}
		catch (SQLException e) {
			ok = false;
			e.printStackTrace();
		}
		
		return ok;		
	}





	public VoTrabajador getDatosTrabajador(int rut) {
		ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_get_ejegestrabajador_xrut);
		Object[] params = {rut};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",p.getSelectedKey(), params);
			VoTrabajador vo = null;
			
			if(data.next()) {
				vo = new VoTrabajador(data.getInt("rut"),data.getString("digito_ver"),data.getString("nombre"),data.getString("cargo"),data.getInt("dependencia"),data.getInt("ccosto"),data.getBigDecimal("sueldo"),data.getTimestamp("fecha_nacim"),data.getTimestamp("fecha_ingreso"),data.getString("estado_civil"),
					data.getString("domicilio"),data.getString("comuna"),data.getString("ciudad"),data.getString("telefono"),data.getString("celular"),data.getString("anexo"),data.getString("sindicato"),data.getString("afp"),data.getTimestamp("fec_afi_sist"),data.getTimestamp("fec_ing_afp"),
					data.getDouble("cot_afp"),data.getString("mo_cot_afp"),data.getDouble("cot_adic"),data.getString("mo_cot_adic"),data.getInt("ah_volunt"),data.getString("mo_ah_volunt"),data.getString("jubilado"),data.getInt("dep_conven"),data.getString("mon_dep_conven"),
					data.getString("afp_histo"),data.getString("isapre"),data.getTimestamp("fec_ing_isap"),data.getString("plan_salud"),data.getTimestamp("fec_con_salud"),data.getTimestamp("venc_salud"),data.getString("mon_salud"),data.getDouble("cot_salud"),data.getString("mon_adic_salud"),
					data.getBigDecimal("adic_salud"),data.getString("isap_histo"),data.getString("cambio_plan"),data.getString("segsalcom"),data.getString("codigo"),data.getBigDecimal("sobretiempo"),data.getInt("monto_sobretiempo"),data.getString("tip_reg_asistencia"),data.getInt("horas_jornormal"),
					data.getBigDecimal("tot_imponible"),data.getBigDecimal("tot_haberes"),data.getString("ape_paterno"),data.getString("ape_materno"),data.getString("nombres"),data.getString("e_mail"),data.getInt("zona"),data.getInt("region"),data.getInt("holding"),data.getString("empresa"),
					data.getString("division"),data.getInt("unidad_negocio"),data.getInt("sucursal"),data.getInt("directorio"),data.getInt("gerencia"),data.getInt("subgerencia"),data.getInt("departamento"),data.getInt("seccion"),data.getInt("edad"),data.getTimestamp("fecha_cargo"),
					data.getInt("antig_empresa"),data.getInt("antig_cargo"),data.getString("pais"),data.getString("usuario_nt"),data.getString("usuario_notes"),data.getInt("renta_reg_mensual"),data.getInt("bruto_promedio"),data.getDouble("punto_medio_escala"),data.getInt("bruto_regular"),
					data.getInt("neto_regular"),data.getInt("neto_promedio"),data.getInt("bruto_total"),data.getInt("neto_total"),data.getTimestamp("fecha_ccosto"),data.getInt("sociedad"),data.getInt("corporacion"),data.getTimestamp("fecha_corporacion"),data.getBigDecimal("antig_ccosto"),
					data.getBigDecimal("pre"),data.getInt("bruto_zona"),data.getInt("neto_zona"),data.getString("fam_cargo"),data.getString("nivel"),data.getString("sexo"),data.getString("grupo_sangre"),data.getInt("num_matrimonios"),data.getInt("talla_vestuario"),data.getInt("num_cargas"),
					data.getString("sit_contractual"),data.getInt("tip_contrato"),data.getTimestamp("fec_ter_cont"),data.getString("tip_jor_lab"),data.getString("mot_ingreso"),data.getString("ubic_fisica"),data.getString("categ_cargo"),data.getTimestamp("fec_ing_cargo"),
					data.getString("area"),data.getTimestamp("fec_ing_hold"),data.getInt("ant_hold"),data.getTimestamp("fec_ing_cco"),data.getInt("rut_supdirecto"),data.getString("dig_supdirecto"),data.getString("nom_supdirecto"),data.getString("cargo_supdirecto"),data.getString("conv_laboral"),
					data.getString("poder"),data.getString("tipo_poder"),data.getTimestamp("fec_otorga_poder"),data.getString("sit_doc_cont"),data.getTimestamp("fec_ult_cont"),data.getInt("anos_rec_otros"),data.getTimestamp("fec_rec_otros"),data.getInt("dias_trab"),data.getBigDecimal("tot_descuento"),
					data.getBigDecimal("liquido"),data.getBigDecimal("rango_desde"),data.getBigDecimal("rango_hasta"),data.getString("rango_concepto"),data.getString("rango_situacion"),data.getString("forma_pago"),data.getTimestamp("fec_retiro"),data.getInt("mot_retiro"),
					data.getBigDecimal("costo_contrato"),data.getString("unidad"),data.getTimestamp("fec_unidad"),data.getString("especialidad"),data.getInt("otros_haberes"),data.getInt("num_certifSII"),data.getInt("costo_finiquito"),data.getString("cod_postal"),data.getInt("pen_normal"),
					data.getInt("pen_progresivo"),data.getInt("pen_convenio"),data.getInt("total_pendiente"),data.getString("cta_cte"),data.getString("banco"),data.getString("rel_laboral"),data.getString("mail"),data.getDouble("horas_trab"),data.getString("signo"),data.getInt("bono_gestion"),
					data.getInt("movilizacion"),data.getInt("bono_zona"),data.getInt("asig_caja"),data.getInt("bono_antig"),data.getInt("rta_variable"),data.getInt("traslado"),data.getInt("bono_anual"),data.getInt("id_centro_trabajo"),data.getTimestamp("fec_centro_trabajo"),
					data.getTimestamp("fec_ing_sindicato"),data.getTimestamp("fec_ret_sindicato"),data.getInt("condicion"),data.getString("shortname"),data.getInt("periodo"),data.getInt("wp_cod_empresa"),data.getInt("wp_cod_planta"),data.getInt("wp_cod_sucursal"),data.getInt("wp_num_cargas_normale"),
					data.getBigDecimal("sueldo_base_mensual"),data.getString("moneda_sueldo_base_mensual"),data.getString("aplica_seguro_des"),data.getTimestamp("fec_ini_seguro_des"),data.getString("desc_tip_contrato"),data.getInt("cod_lug_prestacion"),data.getInt("wp_num_cargas_duplo"),
					data.getInt("wp_num_cargas_materna"),data.getString("valor_adic_1"),data.getString("valor_adic_2"),data.getString("cod_tipo_trabajado"));
			}
			
			return vo;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
 
		return null;
	}


	public VoUsuarioPerfil getPerfilPortal(int rut) {
		ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_get_ejegenericoperfil_xrut);
		Object[] params = {rut};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",p.getSelectedKey(), params);
			VoUsuarioPerfil vo = null;
			
			if(data.next()) {
				vo = new VoUsuarioPerfil(data.getInt("id"),data.getInt("id_perfil_creo"),data.getString("nombre"),data.getBoolean("estado"),data.getTimestamp("fecha_creacion"));
			}
			
			return vo;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
 
		return null;
	}


	public VoEmpresa getDatosEmpresa(int rut) {
		ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_get_ejegesunidad_xrut);
		Object[] params = {rut};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",p.getSelectedKey(), params);
			VoEmpresa vo = null;
			
			if(data.next()) {
				vo = new VoEmpresa(data.getString("empresa"),data.getString("descrip"),data.getString("empre_rut"),data.getString("empre_dir"),data.getString("empre_giro"),data.getString("rut_repr"),data.getString("nom_rep"),data.getString("padre_unidad"),data.getString("padre_empresa"),
									data.getString("id_act_econom"),data.getInt("orden"),data.getString("compania"));
			}
			
			return vo;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
 
		return null;
	}


	public VoUnidad getDatosUnidad(int rut) {
		ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_get_ejegesunidad_xrut);
		Object[] params = {rut};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",p.getSelectedKey(), params);
			VoUnidad vo = null;
			
			if(data.next()) {
				vo = new VoUnidad(data.getString("unid_empresa"),data.getString("unid_id"),data.getString("unid_desc"),data.getString("area"),data.getString("vigente").charAt(0));
			}
			
			return vo;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
 
		return null;
	}


	public VoUsuario getDatosUsuario(int rut) {
		ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_get_ejegesusuario_xrutusuario);
		Object[] params = {rut};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",p.getSelectedKey(), params);
			VoUsuario vo = null;
			
			if(data.next()) {
				vo = new VoUsuario(data.getString("login_usuario"),data.getString("password_usuario"),data.getInt("rut_usuario"),data.getString("emp_rel"),data.getString("uni_rel"),data.getInt("error"),data.getString("passw_cambiar"),data.getTimestamp("passw_ult_cambio"),data.getString("tipo_rel"),
					data.getInt("cant_ingresos"),data.getInt("wp_cod_empresa"),data.getInt("wp_cod_planta"),data.getString("md5"));
			}
			
			return vo;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
 
		return null;
	}


	public VoUsuarioUltAcceso getDatosUsuarioUltAcceso(int rut) {
		ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_get_ejegesusuarioultacceso_xrut);
		Object[] params = {rut};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",p.getSelectedKey(), params);
			VoUsuarioUltAcceso vo = null;
			
			if(data.next()) {
				vo = new VoUsuarioUltAcceso(data.getString("rut"),data.getTimestamp("fecha_ult_acceso"),data.getString("clave"),data.getString("clave_enc"));
			}
			
			return vo;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
 
		return null;
	}
	
	public ConsultaData getPrivilegios(int rut) {
		ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_get_ejegenericoperfilapp_xrut);
		Object[] params = {rut};
		
		try {
			return ConsultaTool.getInstance().getData("portal",p.getSelectedKey(), params);
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
 
		return null;
	}


	public VoTrabajador getDatosTrabajadorExterno(int rut) {
		ConfigParametro p = ConfigParametroManager.getInstance().getConfigParametro(ConfigParametroKey.sql_get_ejegestrabajadorexterno_xrut);
		Object[] params = {rut};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",p.getSelectedKey(), params);
			VoTrabajador vo = null;
			
			if(data.next()) {
				vo = new VoTrabajador(data.getInt("rut"),data.getString("digito_ver"),data.getString("nombre"),data.getString("cargo"),data.getInt("dependencia"),data.getInt("ccosto"),data.getBigDecimal("sueldo"),data.getTimestamp("fecha_nacim"),data.getTimestamp("fecha_ingreso"),data.getString("estado_civil"),
					data.getString("domicilio"),data.getString("comuna"),data.getString("ciudad"),data.getString("telefono"),data.getString("celular"),data.getString("anexo"),data.getString("sindicato"),data.getString("afp"),data.getTimestamp("fec_afi_sist"),data.getTimestamp("fec_ing_afp"),
					data.getDouble("cot_afp"),data.getString("mo_cot_afp"),data.getDouble("cot_adic"),data.getString("mo_cot_adic"),data.getInt("ah_volunt"),data.getString("mo_ah_volunt"),data.getString("jubilado"),data.getInt("dep_conven"),data.getString("mon_dep_conven"),
					data.getString("afp_histo"),data.getString("isapre"),data.getTimestamp("fec_ing_isap"),data.getString("plan_salud"),data.getTimestamp("fec_con_salud"),data.getTimestamp("venc_salud"),data.getString("mon_salud"),data.getDouble("cot_salud"),data.getString("mon_adic_salud"),
					data.getBigDecimal("adic_salud"),data.getString("isap_histo"),data.getString("cambio_plan"),data.getString("segsalcom"),data.getString("codigo"),data.getBigDecimal("sobretiempo"),data.getInt("monto_sobretiempo"),data.getString("tip_reg_asistencia"),data.getInt("horas_jornormal"),
					data.getBigDecimal("tot_imponible"),data.getBigDecimal("tot_haberes"),data.getString("ape_paterno"),data.getString("ape_materno"),data.getString("nombres"),data.getString("e_mail"),data.getInt("zona"),data.getInt("region"),data.getInt("holding"),data.getString("empresa"),
					data.getString("division"),data.getInt("unidad_negocio"),data.getInt("sucursal"),data.getInt("directorio"),data.getInt("gerencia"),data.getInt("subgerencia"),data.getInt("departamento"),data.getInt("seccion"),data.getInt("edad"),data.getTimestamp("fecha_cargo"),
					data.getInt("antig_empresa"),data.getInt("antig_cargo"),data.getString("pais"),data.getString("usuario_nt"),data.getString("usuario_notes"),data.getInt("renta_reg_mensual"),data.getInt("bruto_promedio"),data.getDouble("punto_medio_escala"),data.getInt("bruto_regular"),
					data.getInt("neto_regular"),data.getInt("neto_promedio"),data.getInt("bruto_total"),data.getInt("neto_total"),data.getTimestamp("fecha_ccosto"),data.getInt("sociedad"),data.getInt("corporacion"),data.getTimestamp("fecha_corporacion"),data.getBigDecimal("antig_ccosto"),
					data.getBigDecimal("pre"),data.getInt("bruto_zona"),data.getInt("neto_zona"),data.getString("fam_cargo"),data.getString("nivel"),data.getString("sexo"),data.getString("grupo_sangre"),data.getInt("num_matrimonios"),data.getInt("talla_vestuario"),data.getInt("num_cargas"),
					data.getString("sit_contractual"),data.getInt("tip_contrato"),data.getTimestamp("fec_ter_cont"),data.getString("tip_jor_lab"),data.getString("mot_ingreso"),data.getString("ubic_fisica"),data.getString("categ_cargo"),data.getTimestamp("fec_ing_cargo"),
					data.getString("area"),data.getTimestamp("fec_ing_hold"),data.getInt("ant_hold"),data.getTimestamp("fec_ing_cco"),data.getInt("rut_supdirecto"),data.getString("dig_supdirecto"),data.getString("nom_supdirecto"),data.getString("cargo_supdirecto"),data.getString("conv_laboral"),
					data.getString("poder"),data.getString("tipo_poder"),data.getTimestamp("fec_otorga_poder"),data.getString("sit_doc_cont"),data.getTimestamp("fec_ult_cont"),data.getInt("anos_rec_otros"),data.getTimestamp("fec_rec_otros"),data.getInt("dias_trab"),data.getBigDecimal("tot_descuento"),
					data.getBigDecimal("liquido"),data.getBigDecimal("rango_desde"),data.getBigDecimal("rango_hasta"),data.getString("rango_concepto"),data.getString("rango_situacion"),data.getString("forma_pago"),data.getTimestamp("fec_retiro"),data.getInt("mot_retiro"),
					data.getBigDecimal("costo_contrato"),data.getString("unidad"),data.getTimestamp("fec_unidad"),data.getString("especialidad"),data.getInt("otros_haberes"),data.getInt("num_certifSII"),data.getInt("costo_finiquito"),data.getString("cod_postal"),data.getInt("pen_normal"),
					data.getInt("pen_progresivo"),data.getInt("pen_convenio"),data.getInt("total_pendiente"),data.getString("cta_cte"),data.getString("banco"),data.getString("rel_laboral"),data.getString("mail"),data.getDouble("horas_trab"),data.getString("signo"),data.getInt("bono_gestion"),
					data.getInt("movilizacion"),data.getInt("bono_zona"),data.getInt("asig_caja"),data.getInt("bono_antig"),data.getInt("rta_variable"),data.getInt("traslado"),data.getInt("bono_anual"),data.getInt("id_centro_trabajo"),data.getTimestamp("fec_centro_trabajo"),
					data.getTimestamp("fec_ing_sindicato"),data.getTimestamp("fec_ret_sindicato"),data.getInt("condicion"),data.getString("shortname"),data.getInt("periodo"),data.getInt("wp_cod_empresa"),data.getInt("wp_cod_planta"),data.getInt("wp_cod_sucursal"),data.getInt("wp_num_cargas_normale"),
					data.getBigDecimal("sueldo_base_mensual"),data.getString("moneda_sueldo_base_mensual"),data.getString("aplica_seguro_des"),data.getTimestamp("fec_ini_seguro_des"),data.getString("desc_tip_contrato"),data.getInt("cod_lug_prestacion"),data.getInt("wp_num_cargas_duplo"),
					data.getInt("wp_num_cargas_materna"),data.getString("valor_adic_1"),data.getString("valor_adic_2"),data.getString("cod_tipo_trabajado"));
			}
			
			return vo;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
 
		return null;
	}

}
