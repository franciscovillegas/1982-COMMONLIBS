package portal.com.eje.vistas;

import java.util.HashMap;

public class QueryPortal {
	public enum QueryPortalType {
		viewUnidadEncargado,
		viewInfoRut;
	}
	public enum QueryPortalWhere {
		viewInfoRut_rut("dbo.eje_ges_trabajador.rut");
		
		private String where;
		
		QueryPortalWhere(String where) {
			this.where = where;
		}
		
		public String getWhere() {
			return this.where;
		}
	}
	
	private static QueryPortal instance;
	private HashMap<QueryPortalType,String> querys;
	
	private QueryPortal() {
		querys = new HashMap<QueryPortalType,String>();
		querys.put(QueryPortalType.viewUnidadEncargado, "SELECT a.unid_empresa, a.unid_id, a.periodo, a.rut_encargado, e.nombre, e.cargo AS cargo_id, c.descrip AS cargo_desc, e.anexo, e.e_mail, d.id_foto AS foto, a.mision, a.estado, a.fec_ini, a.rut_cambios, b.nombre AS nom_cambios, a.fec_actualiza FROM dbo.eje_ges_trabajador	e INNER JOIN dbo.eje_ges_unidad_encargado a ON e.rut = a.rut_encargado LEFT OUTER JOIN dbo.eje_ges_trabajador b ON a.rut_cambios = b.rut LEFT OUTER JOIN dbo.eje_ges_cargos c ON e.cargo = c.cargo AND e.empresa = c.empresa LEFT OUTER JOIN dbo.eje_ges_foto_trab d ON e.rut = d.rut");
		querys.put(QueryPortalType.viewInfoRut, "SELECT DISTINCT  dbo.eje_ges_trabajador.rut, dbo.eje_ges_trabajador.digito_ver, DATEDIFF(mm, dbo.eje_ges_trabajador.fecha_ingreso, GETDATE()) AS antiguedad, DATEDIFF(mm, dbo.eje_ges_trabajador.fecha_nacim, GETDATE()) AS Xedad, DATEDIFF(mm, dbo.eje_ges_trabajador.fecha_corporacion, GETDATE()) AS xcorp, DATEDIFF(mm, dbo.eje_ges_trabajador.fecha_cargo, GETDATE()) AS xcargo, DATEDIFF(mm, dbo.eje_ges_trabajador.fec_unidad, GETDATE()) AS xunidad, dbo.eje_ges_trabajador.nombre, dbo.eje_ges_trabajador.sueldo, GETDATE() AS hoy, dbo.eje_ges_trabajador.fecha_nacim, dbo.eje_ges_trabajador.fecha_ingreso,  dbo.eje_ges_trabajador.estado_civil, dbo.eje_ges_trabajador.domicilio, dbo.eje_ges_trabajador.telefono, dbo.eje_ges_trabajador.empresa, dbo.eje_ges_trabajador.edad, dbo.eje_ges_trabajador.fecha_cargo, dbo.eje_ges_trabajador.antig_empresa, dbo.eje_ges_trabajador.antig_cargo, dbo.eje_ges_trabajador.bruto_promedio, dbo.eje_ges_trabajador.antig_ccosto, dbo.eje_ges_trabajador.sexo, dbo.eje_ges_trabajador.nivel, dbo.eje_ges_afp.descrip AS afp, dbo.eje_ges_paises.descrip AS pais, dbo.eje_ges_paises.nacionalidad, dbo.eje_ges_isapres.descrip AS isapre, dbo.eje_ges_cargos.descrip AS cargo, dbo.eje_ges_regiones.descrip AS region, dbo.eje_ges_dependencia.descripcion AS dependencia, dbo.eje_ges_sindicatos.descrip AS sindicato, dbo.eje_ges_trabajador.comuna, dbo.eje_ges_foto_trab.id_foto, dbo.eje_ges_trabajador.grupo_sangre, dbo.eje_ges_trabajador.num_matrimonios, dbo.eje_ges_trabajador.talla_vestuario, dbo.eje_ges_trabajador.num_cargas, dbo.eje_ges_trabajador.sit_contractual, dbo.eje_ges_trabajador.fec_ter_cont AS term, dbo.eje_ges_trabajador.tip_jor_lab, dbo.eje_ges_trabajador.mot_ingreso, dbo.eje_ges_trabajador.categ_cargo, dbo.eje_ges_trabajador.fec_ing_cargo AS ingcargo, dbo.eje_ges_trabajador.fec_ing_hold AS inghold, dbo.eje_ges_trabajador.ant_hold, dbo.eje_ges_trabajador.fec_ing_cco AS ingcco, dbo.eje_ges_supervisor.rut_supdirecto, dbo.eje_ges_supervisor.dig_supdirecto, dbo.eje_ges_supervisor.nom_supdirecto, dbo.eje_ges_supervisor.cargo_supdirecto, dbo.eje_ges_trabajador.conv_laboral, dbo.eje_ges_trabajador.poder, dbo.eje_ges_trabajador.tipo_poder, dbo.eje_ges_trabajador.sit_doc_cont, dbo.eje_ges_trabajador.tot_haberes AS total_haberes, dbo.eje_ges_trabajador.fec_ult_cont AS ultcont, dbo.eje_ges_trabajador.renta_reg_mensual, dbo.eje_ges_trabajador.cargo AS id_cargo, dbo.eje_ges_trabajador.sindicato AS id_sindicato, eje_ges_empresa1.descrip, dbo.eje_ges_trabajador.ccosto, dbo.eje_ges_trabajador.sociedad, dbo.eje_ges_trabajador.anos_rec_otros AS recono, dbo.eje_ges_trabajador.fec_rec_otros AS fecrecono, dbo.eje_ges_sociedad.descripcion AS glosasociedad, dbo.eje_ges_trabajador.dias_trab, dbo.eje_ges_trabajador.tot_descuento, dbo.eje_ges_trabajador.liquido, dbo.eje_ges_trabajador.rango_desde, dbo.eje_ges_trabajador.rango_hasta, dbo.eje_ges_trabajador.rango_concepto, dbo.eje_ges_trabajador.rango_situacion, dbo.eje_ges_trabajador.forma_pago, dbo.eje_ges_trabajador.fec_afi_sist, dbo.eje_ges_trabajador.fec_ing_afp, dbo.eje_ges_trabajador.cot_afp, dbo.eje_ges_trabajador.cot_adic, dbo.eje_ges_trabajador.mon_adic_salud, dbo.eje_ges_trabajador.mo_cot_afp, dbo.eje_ges_trabajador.mo_cot_adic, dbo.eje_ges_trabajador.ah_volunt, dbo.eje_ges_trabajador.mo_ah_volunt, dbo.eje_ges_trabajador.jubilado, dbo.eje_ges_trabajador.dep_conven, dbo.eje_ges_trabajador.mon_dep_conven, dbo.eje_ges_trabajador.afp_histo, dbo.eje_ges_trabajador.fec_ing_isap, dbo.eje_ges_trabajador.plan_salud, dbo.eje_ges_trabajador.fec_con_salud, dbo.eje_ges_trabajador.venc_salud, dbo.eje_ges_trabajador.mon_salud, dbo.eje_ges_trabajador.cot_salud, dbo.eje_ges_trabajador.adic_salud, dbo.eje_ges_trabajador.isap_histo, dbo.eje_ges_trabajador.cambio_plan, dbo.eje_ges_trabajador.fec_retiro, dbo.eje_ges_trabajador.bruto_total, dbo.eje_ges_trabajador.sobretiempo, dbo.eje_ges_trabajador.monto_sobretiempo, dbo.eje_ges_trabajador.tip_reg_asistencia, dbo.eje_ges_trabajador.horas_jornormal, dbo.eje_ges_trabajador.costo_contrato, dbo.eje_ges_tip_contrato.tipo_desc AS tip_contrato, dbo.eje_ges_division.div_desc AS division, dbo.eje_ges_areas.area_desc AS area, unidades.unid_desc AS unidad, dbo.eje_ges_trabajador.fecha_corporacion, dbo.eje_ges_trabajador.fec_unidad, dbo.eje_ges_trabajador.division AS id_division, dbo.eje_ges_ubicacion_fisica.descrip AS ubic_fisica, dbo.eje_ges_trabajador.afp AS id_afp, dbo.eje_ges_trabajador.unidad AS id_unidad, dbo.eje_ges_trabajador.isapre AS id_isapre, dbo.eje_ges_trabajador.ubic_fisica AS id_ubicacion, eje_ges_empresa1.descrip AS nom_empresa, dbo.eje_ges_trabajador.celular, dbo.eje_ges_trabajador.otros_haberes, eje_ges_empresa1.empre_rut, eje_ges_empresa1.empre_dir, eje_ges_empresa1.empre_giro, eje_ges_empresa1.rut_repr, eje_ges_empresa1.nom_rep, dbo.eje_ges_mot_retiro.motivo_desc AS mot_retiro, dbo.eje_ges_trabajador.e_mail, dbo.eje_ges_trabajador.ciudad, dbo.eje_ges_trabajador.cod_postal, dbo.eje_ges_trabajador.cta_cte, dbo.eje_ges_trabajador.banco, dbo.eje_ges_trabajador.bono_gestion, dbo.eje_ges_trabajador.movilizacion, dbo.eje_ges_trabajador.bono_zona, dbo.eje_ges_trabajador.asig_caja, dbo.eje_ges_trabajador.bono_antig, dbo.eje_ges_trabajador.rta_variable, dbo.eje_ges_trabajador.traslado, dbo.eje_ges_trabajador.bruto_regular, dbo.eje_ges_trabajador.neto_regular, dbo.eje_ges_trabajador.bruto_zona, dbo.eje_ges_trabajador.neto_zona, dbo.eje_ges_trabajador.neto_promedio, dbo.eje_ges_trabajador.neto_total, dbo.eje_ges_trabajador.bono_anual, dbo.eje_ges_trabajador.segsalcom, dbo.eje_ges_trabajador.pre, dbo.eje_ges_trabajador.fec_ret_sindicato, dbo.eje_ges_trabajador.fec_ing_sindicato, dbo.eje_ges_trabajador.fec_centro_trabajo, dbo.eje_ges_trabajador.fec_otorga_poder, dbo.eje_ges_trabajador.sueldo_base_mensual, dbo.eje_ges_trabajador.wp_num_cargas_normale, dbo.eje_ges_trabajador.moneda_sueldo_base_mensual, dbo.eje_ges_trabajador.cod_lug_prestacion, dbo.eje_ges_trabajador.aplica_seguro_des, dbo.eje_ges_trabajador.fec_ini_seguro_des, dbo.eje_ges_trabajador.desc_tip_contrato, dbo.eje_ges_trabajador.wp_cod_empresa, dbo.eje_ges_trabajador.wp_cod_planta, dbo.eje_ges_trabajador.wp_cod_sucursal, centro_trabajo.unid_desc AS centro_trabajo, dbo.eje_ges_trabajador.id_centro_trabajo, dbo.eje_ges_trabajador.anexo, dbo.eje_ges_trabajador.sucursal, dbo.eje_ges_trabajador.fecha_ccosto, dbo.eje_ges_trabajador.corporacion, dbo.eje_ges_trabajador.tip_contrato AS Expr1 FROM         dbo.eje_ges_areas RIGHT OUTER JOIN dbo.eje_sh_unidades AS centro_trabajo RIGHT OUTER JOIN dbo.eje_ges_trabajador LEFT OUTER JOIN dbo.eje_sh_unidades AS eje_sh_unidades_1 ON dbo.eje_ges_trabajador.unidad = eje_sh_unidades_1.unid_id AND  dbo.eje_ges_trabajador.empresa = eje_sh_unidades_1.unid_empresa ON centro_trabajo.unid_id = dbo.eje_ges_trabajador.id_centro_trabajo AND  centro_trabajo.unid_empresa = dbo.eje_ges_trabajador.empresa LEFT OUTER JOIN dbo.eje_ges_supervisor ON dbo.eje_ges_trabajador.rut = dbo.eje_ges_supervisor.rut LEFT OUTER JOIN dbo.eje_ges_division ON dbo.eje_ges_trabajador.empresa = dbo.eje_ges_division.div_empresa AND  dbo.eje_ges_trabajador.division = dbo.eje_ges_division.div_id ON dbo.eje_ges_areas.area_empresa = dbo.eje_ges_trabajador.empresa AND  dbo.eje_ges_areas.area_id = dbo.eje_ges_trabajador.area LEFT OUTER JOIN dbo.eje_ges_cargos ON dbo.eje_ges_trabajador.empresa = dbo.eje_ges_cargos.empresa AND dbo.eje_ges_trabajador.cargo = dbo.eje_ges_cargos.cargo LEFT OUTER JOIN dbo.eje_ges_empresa AS eje_ges_empresa1 ON dbo.eje_ges_trabajador.empresa = eje_ges_empresa1.empresa LEFT OUTER JOIN dbo.eje_ges_mot_retiro ON dbo.eje_ges_trabajador.mot_retiro = dbo.eje_ges_mot_retiro.motivo_id LEFT OUTER JOIN dbo.eje_ges_ubicacion_fisica ON dbo.eje_ges_trabajador.ubic_fisica = dbo.eje_ges_ubicacion_fisica.ubicacion LEFT OUTER JOIN dbo.eje_ges_tip_contrato ON dbo.eje_ges_trabajador.tip_contrato = dbo.eje_ges_tip_contrato.tipo_id LEFT OUTER JOIN dbo.eje_ges_sociedad ON dbo.eje_ges_trabajador.sociedad = dbo.eje_ges_sociedad.codigo AND  dbo.eje_ges_trabajador.wp_cod_empresa = dbo.eje_ges_sociedad.wp_cod_empresa LEFT OUTER JOIN dbo.eje_ges_foto_trab ON dbo.eje_ges_trabajador.rut = dbo.eje_ges_foto_trab.rut LEFT OUTER JOIN dbo.eje_ges_dependencia ON dbo.eje_ges_trabajador.dependencia = dbo.eje_ges_dependencia.codigo LEFT OUTER JOIN dbo.eje_ges_sindicatos ON dbo.eje_ges_trabajador.sindicato = dbo.eje_ges_sindicatos.sindicato LEFT OUTER JOIN dbo.eje_ges_isapres ON dbo.eje_ges_trabajador.isapre = dbo.eje_ges_isapres.isapre LEFT OUTER JOIN dbo.eje_ges_paises ON dbo.eje_ges_trabajador.pais = dbo.eje_ges_paises.pais LEFT OUTER JOIN dbo.eje_ges_afp ON dbo.eje_ges_trabajador.afp = dbo.eje_ges_afp.afp LEFT OUTER JOIN dbo.eje_ges_regiones ON dbo.eje_ges_trabajador.region = dbo.eje_ges_regiones.region LEFT OUTER JOIN eje_ges_unidades unidades on unidades.unid_id = dbo.eje_ges_trabajador.unidad");
	}
	
	public static QueryPortal getInstance() {
		if(instance==null) {
			synchronized (QueryPortalType.class) {
				if(instance==null) {
					instance = new QueryPortal();
				}
			}
		}
		
		return instance;
	}
	
	public String getQuery(QueryPortalType t) {
		return this.querys.get(t);
	}
	
	public String getQuery(QueryPortalType t, QueryPortalWhere[] where) {
		StringBuilder str = new StringBuilder();
		
		int pos=0;
		for(QueryPortalWhere w : where) {
			if(pos != 0) {
				str.append(" and ");
			}
			str.append(" ").append(w.getWhere()).append("= ? ");
			pos++;
		}
		
		StringBuilder strFinal = new StringBuilder();
		if(pos != 0) {
			strFinal.append(this.querys.get(t)).append(" \n WHERE ").append(str.toString());
		}
		else {
			strFinal.append(this.querys.get(t));
		}
		
		return strFinal.toString();
	}
	
}


