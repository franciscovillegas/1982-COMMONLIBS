package portal.com.eje.serhumano.menu;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringEscapeUtils;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.datos.ConsultaTool;
import cl.ejedigital.web.datos.DBConnectionManager;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.menu.vo.EjeGesEmpresa;
import portal.com.eje.vistas.QueryPortal;
import portal.com.eje.vistas.QueryPortal.QueryPortalType;
import portal.com.eje.vistas.QueryPortal.QueryPortalWhere;

public class menuManager
{

	public menuManager(){
		  
	}
	  
    public menuManager(Connection connection)
    {
        con = connection;
        mensajeError = "";
    }

    public Consulta GetEmpresa(String rut)
    {
        consul = new Consulta(con);
        String sql = "SELECT emp.descrip as nom_empresa FROM eje_ges_empresa emp, eje_ges_trabajador trab WHERE emp.empresa = trab.empresa AND trab.rut = " + rut;
        consul.exec(sql);
        return consul;
    }

    public Consulta GetSociedad(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("SELECT soc.descripcion as nom_sociedad FROM eje_ges_sociedad soc, eje_ges_trabajador trab WHERE soc.codigo = trab.sociedad AND soc.wp_cod_empresa = trab.empresa AND trab.rut = " + rut));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetUnidad(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select u.unid_desc as nom_unidad from eje_ges_trabajador t inner join eje_ges_unidades u on u.unid_id = t.unidad where t.rut = " + rut));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetCentroCosto(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("SELECT cc.descrip as nom_centro_costo, cc.centro_costo as id_cco FROM eje_ges_centro_costo cc, eje_ges_trabajador trab WHERE cc.wp_cod_empresa = trab.empresa AND cc.centro_costo = trab.ccosto AND trab.rut = " + rut));
        consul.exec(sql);
        return consul;
    }

    	/**
    	 * @deprecated
    	 * 
    	 * */
    public Consulta getNumCargas(String s)
    {
        consul = new Consulta(con);
        String s1 = "select count(*) as cargas from eje_ges_grupo_familiar where es_carga = 'S' and rut= " + s;
        consul.exec(s1);
        return consul;
    }
    
    public int getNumCargasInt(String s)
    {
        int cargas = 0;
        String s1 = "select count(*) as cargas from eje_ges_grupo_familiar where es_carga = 'S' and rut= " + s;
        try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",s1);
			if(data != null && data.next()) {
				cargas = data.getInt("cargas");
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return cargas;
    }

    public Consulta getDatos(String s)
    {
        consul = new Consulta(con);
        String s1 = String.valueOf(String.valueOf((new StringBuilder("select wp_num_cargas_normale, wp_num_cargas_duplo, wp_num_cargas_materna from eje_ges_trabajador where (rut= ")).append(s).append(")")));
        consul.exec(s1);
        return consul;
    }

    public ConsultaData getInfo(String s)
    {
    	QueryPortalWhere[] wheres = {QueryPortalWhere.viewInfoRut_rut};
    	String[] whereValues = {s};
    	
    	ConsultaData consulta = null;
		try {
			consulta = ConsultaTool.getInstance().getData(con, QueryPortal.getInstance().getQuery(QueryPortalType.viewInfoRut,wheres), whereValues );
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
        return consulta;
    }
    
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public Consulta getInfo_before(String s) {
		
		/*
		 * 20170109
		 * A esta fecha se hacía la consulta sobre view_ges_InfoRut
		 * */
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder();
			s1.append(" SELECT digito_ver,nombre,afp,isapre,fecha_nacim,estado_civil,telefono,nacionalidad, ")
			.append("	Xedad,grupo_sangre,domicilio,comuna,ciudad,id_foto,cargo,id_unidad, unidad,anexo, mail, e_mail, mail_particular, wp_num_cargas_normale, ")
			.append("	tip_contrato,term,fecha_ingreso,antiguedad,xcorp,xunidad,xcargo,inghold,fec_unidad,ingcargo, segsalcom, ")
			.append("	id_centro_trabajo, sueldo_base_mensual,moneda_sueldo_base_mensual, wp_num_cargas_normale, cod_lug_prestacion, ")
			.append("	aplica_seguro_des, fec_ini_seguro_des, desc_tip_contrato, wp_cod_empresa, wp_cod_planta, wp_cod_sucursal, ")
			.append("	nom_empresa, glosasociedad as nom_sociedad, sexo, cta_cte, banco, tip_jor_lab, profesion, niveleduc, celular ")
			.append(" FROM VIEW_GES_INFORUT ")
			.append(" WHERE (rut = ").append(s).append(")");
		
 
		
		
		consul.exec(s1.toString());
		return consul;
	}
	
	

    public Consulta getDatosContrato(String s)
    {
        consul = new Consulta(con);
        String s1 = String.valueOf(String.valueOf((new StringBuilder("select tip_jor_lab,digito_ver,id_foto,e_mail,anexo,nombre,id_unidad, unidad,area,cargo,ccosto,conv_laboral,sindicato,renta_reg_mensual,rut_supdirecto,dig_supdirecto,nom_supdirecto,cargo_supdirecto,ubic_fisica,division,categ_cargo,tipo_poder,tip_contrato,term,fecha_ingreso,antiguedad,xcorp,xunidad,xcargo,inghold,fec_unidad,ingcargo, segsalcom,id_centro_trabajo, sueldo_base_mensual,moneda_sueldo_base_mensual, wp_num_cargas_normale, cod_lug_prestacion, aplica_seguro_des, fec_ini_seguro_des, desc_tip_contrato=coalesce(desc_tip_contrato,''), wp_cod_empresa, wp_cod_planta, wp_cod_sucursal, nom_empresa, glosasociedad as nom_sociedad from view_InfoRut where (rut = ")).append(s).append(")")));
        consul.exec(s1);
        return consul;
    }
    
    public Consulta getDatosCertificadoRentaCabecera(String s)
    {
        consul = new Consulta(con);
        String s1 = String.valueOf(String.valueOf((new StringBuilder("SELECT wp_ndias_trab, tot_haberes, periodo FROM eje_ges_certif_histo_liquidacion_cabecera WHERE periodo = (SELECT MAX(peri_id) FROM eje_ges_periodo) AND (rut = ")).append(s).append(")")));
        consul.exec(s1);
        return consul;
    }
    
    public ConsultaData getDatosCertificadoRentaCabecera(int s)
    {
        String s1 = "SELECT wp_ndias_trab, tot_haberes, periodo FROM eje_ges_certif_histo_liquidacion_cabecera WHERE periodo = (SELECT MAX(peri_id) FROM eje_ges_periodo) AND (rut = ".concat(String.valueOf(s)).concat(")");
        try {
			return ConsultaTool.getInstance().getData("portal",s1);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
        
        return null;
    }
    
    public Consulta getDatosCertificadoRentaDetalle(String s)
    {
        consul = new Consulta(con);
        String s1 = String.valueOf(String.valueOf((new StringBuilder("SELECT glosa_haber, val_haber FROM eje_ges_certif_histo_liquidacion_detalle WHERE periodo = (SELECT MAX(peri_id) FROM eje_ges_periodo) AND id_tp = 'H' AND (glosa_haber LIKE '%gratif%' OR glosa_haber LIKE '%colaci%' OR glosa_haber LIKE '%movil%') AND (rut = ")).append(s).append(")")));
        consul.exec(s1);
        return consul;
    }

    public Consulta getHorasExtras(String s)
    {
        consul = new Consulta(con);
        String s1 = "SELECT a.rut AS rut, a.digito_ver, a.nombre AS nombre, a.anexo, a.unidad, a.e_mail, a.cargo AS cargo, a.sueldo AS sueldo, a.bruto_regular AS tothaber, b.valhoras AS valor, b.num_horas AS nhoras,b.num_minutos AS nmin, a.id_foto AS foto FROM view_InfoRut a LEFT OUTER JOIN eje_ges_sobretiempos b ON a.rut = b.rut WHERE (a.rut = " + s + ") ORDER BY b.periodo desc";
        consul.exec(s1);
        return consul;
    }

    public Consulta getDeudas(String s)
    {
        consul = new Consulta(con);
        String s1 = "SELECT distinct b.periodo,b.total_haberes AS tothaber,b.valhoras AS valor,b.num_horas AS nhoras,b.num_minutos AS nmin,b.tipo AS tip,eje_ges_trab_haberes_historia.monto AS sueldo,eje_ges_periodo.peri_ano,eje_ges_periodo.peri_mes FROM eje_ges_sobretiempos b LEFT OUTER JOIN eje_ges_periodo ON b.periodo = eje_ges_periodo.peri_id LEFT OUTER JOIN eje_ges_trab_haberes_historia ON b.rut = eje_ges_trab_haberes_historia.rut AND b.compania = eje_ges_trab_haberes_historia.empresa AND b.periodo = eje_ges_trab_haberes_historia.periodo WHERE (eje_ges_trab_haberes_historia.haber = '1003') AND (b.rut = " + s + ") " + "ORDER BY b.periodo desc";
        consul.exec(s1);
        return consul;
    }

    public Consulta getCargas(String s)
    {
        consul = new Consulta(con);
        String s1 = "select count(nombre) as cargas from eje_ges_grupo_familiar where rut=" + s;
        consul.exec(s1);
        return consul;
    }

    public Consulta getDatosPrev(String s)
    {
        consul = new Consulta(con);
        
        String s1 = "select nombre,afp,fec_afi_sist,fec_ing_afp,cargo,cot_afp,mo_cot_afp,cot_adic,mo_cot_adic,ah_volunt,mo_ah_volunt,jubilado=coalesce(jubilado,'N'),dep_conven,mon_dep_conven,afp_histo,id_foto,isapre,fec_ing_isap,plan_salud,mon_adic_salud,fec_con_salud,venc_salud,mon_salud,cot_salud,adic_salud,isap_histo,cambio_plan,e_mail,anexo,unidad, banco, cta_cte from view_InfoRut where rut=" + s;
        consul.exec(s1);
        return consul;
    }

    public Consulta getFormacion(String s)	
    {
        consul = new Consulta(con);
        String s1 = "select nombre,afp,fec_afi_sist,fec_ing_afp,cargo,cot_afp,cot_adic,mo_cot_adic,ah_volunt,mo_ah_volunt,jubilado=coalesce(jubilado,'N'),dep_conven,mon_dep_conven,afp_histo,id_foto,isapre,fec_ing_isap,plan_salud,fec_con_salud,venc_salud,mon_salud,cot_salud,adic_salud,isap_histo,cambio_plan,e_mail,anexo,unidad, fecha_ingreso from view_InfoRut where rut=" + s;
        consul.exec(s1);
        return consul;
    }
    
    public Consulta getDatosPago(String rut) {
    	return getDatosPago(rut, null);
    }
    
    public Consulta getDatosPago(String rut, String periodo) {
        consul = new Consulta(con);
        String strSQL="";
        if (periodo==null) {
        	strSQL = "select forma_pago, banco, cta_cte from eje_ges_trabajador where rut="+rut;
        }else {
        	strSQL = "select forma_pago, banco, cta_cte from eje_ges_trabajador_historia where rut="+rut+" and periodo="+periodo+" ";
        }
        consul.exec(strSQL);
        return consul;
    }

    public Consulta getEstudios(String strRut)
    {
        consul = new Consulta(con);
        
        StringBuffer strSQL = new StringBuffer();
        
        strSQL.append("select distinct * from ( \n")
		.append("	select e.rut, nombre=rtrim(ltrim(t.nombres)) +' '+ rtrim(ltrim(t.ape_paterno)) +' '+ rtrim(ltrim(t.ape_materno)), \n")
		.append("	e.cod_nivel, nivel=pcn.descrip, \n")
		.append("	e.cod_nivel_instucion, nivel_instucion=pni.descrip, \n")
		.append("	e.cod_tipo_programa, tipo_programa=ptp.descrip, \n")
		.append("	e.institucion, desde=e.fecha_desde, hasta=e.fecha_hasta, \n")
		.append("	e.cod_finalizacion, finalizacion=pcf.descrip, \n")
		.append("	e.cod_forma_estudio, forma_estudio=pfe.descrip, \n")
		.append("	e.titulo \n")
		.append("	from eje_ges_estudios_personas e \n")
		.append("	inner join eje_ges_trabajador t on t.rut=e.rut  \n")
		.append("	left join eje_ges_parametro pcn on pcn.tipo='nivel_educ' and pcn.cod_param=e.cod_nivel \n")
		.append("	left join eje_ges_parametro pni on pni.tipo='cod_nivel_institucion' and pni.cod_param=e.cod_nivel_instucion \n")
		.append("	left join eje_ges_parametro ptp on ptp.tipo='cod_tipo_programa' and ptp.cod_param=e.cod_tipo_programa \n")
		.append("	left join eje_ges_parametro pcf on pcf.tipo='final_nivel_ed' and pcf.cod_param=e.cod_finalizacion \n")
		.append("	left join eje_ges_parametro pfe on pfe.tipo='cod_forma_estudio' and pfe.cod_param=e.cod_forma_estudio \n")
		.append("	where e.rut=").append(strRut).append(" \n")	
		.append(") x \n")
		.append("order by desde, hasta \n");
        
        consul.exec(strSQL.toString());
        return consul;
    }
    
    public Consulta getTrayectoriaLaboral(String strRut)
    {
        consul = new Consulta(con);
        
        StringBuffer strSQL = new StringBuffer();
        
        strSQL.append("select distinct e.empresa, e.fecha_inicio, e.fecha_termino, e.cargo, e.antiguedad, \n")
		.append("nombre=rtrim(ltrim(t.nombres)) +' '+ rtrim(ltrim(t.ape_paterno)) +' '+ rtrim(ltrim(t.ape_materno))  \n")
		.append("from eje_ges_exp_laboral e \n")
		.append("inner join eje_ges_trabajador t on t.rut=e.rut \n")
		.append("where e.rut=").append(strRut).append(" \n")	
		.append("order by coalesce(e.fecha_inicio, e.fecha_termino)");
        
        consul.exec(strSQL.toString());
        return consul;
    }
    
    public Consulta getDescForm(String s)
    {
        consul = new Consulta(con);
        String s1 = "SELECT ti.descrip AS titulo, tipo.descrip AS tipo, inst.descrip AS instituto, fp.fecha, fp.estado FROM eje_ges_formacion_profesional fp LEFT OUTER JOIN eje_ges_tipo_titulos tipo ON fp.tipo = tipo.tipo LEFT OUTER JOIN eje_ges_institutos inst ON fp.instituto = inst.instituto LEFT OUTER JOIN eje_ges_titulos ti ON fp.titulo = ti.titulo WHERE (rut = " + s + ")" + " ORDER BY orden ";
        consul.exec(s1);
        return consul;
    }

    public Consulta getCapacitaciones(String s)
    {
        consul = new Consulta(con);
        String s1 = "SELECT curso,fecha_inicio,fecha_termino,organismo,valor,asistencia,nota_aprob,resul_texto,horas FROM eje_ges_capacitaciones WHERE (rut = " + s + ") ORDER BY periodo desc";
        consul.exec(s1);
        return consul;
    }

    public Consulta getCargasFamiliares(String s)
    {
        consul = new Consulta(con);
        String s1 = "SELECT rut, nombre, cargo, fecha_ingreso,digito_ver,id_foto,afp,isapre,estado_civil,anexo,area,e_mail,id_unidad, unidad,division FROM view_InfoRut WHERE (rut = " + s + ")";
        consul.exec(s1);
        return consul;
    }

    public Consulta getListaCargasFamiliares(String s)
    {
        consul = new Consulta(con);
        String s1 = "SELECT numero,nombre,fecha_nacim,rut_carga,dv_carga,es_carga,es_carga_salud,num_matrim,actividad,fecha_inicio,fecha_termino,sexo,rtrim(parentesco) as parentesco,(DATEDIFF(month, fecha_nacim, GETDATE()) / 12) AS edad FROM eje_ges_grupo_familiar WHERE (rut = " + s + ") order by numero asc";
        consul.exec(s1);
        return consul;
    }

    public Consulta getListaCargasFamiliares(String rut, String empresa)
    {
        int codempresa = Integer.parseInt(empresa);
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT secuencia, numero=row_number() over(order by fecha_nacim asc), nombre, fecha_nacim, rut_carga, dv_carga, es_carga, es_carga_salud, num_matrim, actividad, fecha_inicio, fecha_termino, sexo, rtrim(parentesco) as parentesco, (DATEDIFF(month, fecha_nacim, GETDATE()) / 12) AS edad FROM eje_ges_grupo_familiar WHERE (rut = ")).append(rut).append(")")) + " and wp_cod_empresa =" + codempresa);
        consul.exec(sql);
        System.out.println(sql);
        return consul;
    }

    //Se separa el nombre en Apellido Paterno, Apellido Materno y Nombres.
    public Consulta getListaCargasFamiliaresV2(String rut, String empresa)
    {
        int codempresa = Integer.parseInt(empresa);
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT secuencia, numero=row_number() over(order by fecha_nacim asc), nombre=(coalesce(rtrim(ltrim(nombres)),''))+coalesce(' '+rtrim(ltrim(ape_paterno)),'')+coalesce(' '+rtrim(ltrim(ape_materno)),''), fecha_nacim, rut_carga, dv_carga, es_carga, es_carga_salud, num_matrim, actividad, fecha_inicio, fecha_termino, sexo, rtrim(parentesco) as parentesco, (DATEDIFF(month, fecha_nacim, GETDATE()) / 12) AS edad FROM eje_ges_grupo_familiar WHERE (rut = ")).append(rut).append(")")) + " and wp_cod_empresa =" + codempresa);
        consul.exec(sql);
        System.out.println(sql);
        return consul;
    }
    
    public Consulta getLicenciasMedicas(String s)
    {
        consul = new Consulta(con);
        String s1 = "select rut, fecha_inicio, fecha_termino,diagnostico,dias,grupo_enfermedad,rut_profesional,profesional,espe_profesional,id_tipo_licencia,tipo_licencia,subsidio,wp_cod_empresa, wp_cod_planta from eje_ges_licencias_medicas WHERE (rut = " + s + ") order by  fecha_inicio desc";
        consul.exec(s1);
        return consul;
    }
    
    public ConsultaData getLicenciasMedicasConsultaData(String s)
    {
    	ConsultaData data = null;
    	
        Connection conn = DBConnectionManager.getInstance().getConnection("");
        try {
        	String s1 = "select rut, fecha_inicio, fecha_termino,diagnostico,dias,grupo_enfermedad,rut_profesional,profesional,espe_profesional,id_tipo_licencia,tipo_licencia,subsidio,wp_cod_empresa, wp_cod_planta from eje_ges_licencias_medicas WHERE (rut = " + s + ") order by  fecha_inicio desc";
        	data = ConsultaTool.getInstance().getData("portal", s1);
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        
        return data;
    }

    public Consulta getCargaLicencia(String s)
    {
        consul = new Consulta(con);
        String s1 = "SELECT cc.centro_costo as id_centro_costo, cc.descrip as centro_costo, v.rut, v.nombre, v.cargo, v.fecha_ingreso,v.digito_ver,v.id_foto,v.afp,v.isapre,v.estado_civil,v.anexo,v.area,v.e_mail,v.id_unidad, v.unidad,v.division, v.inghold as fecha_antiguedad FROM view_InfoRut as v, eje_ges_centro_costo as cc WHERE v.ccosto = cc.centro_costo and v.wp_cod_empresa = cc.wp_cod_empresa and (v.rut = " + s + ") order by v.fecha_ingreso desc";
        System.err.println(s1);
        consul.exec(s1);
        return consul;
    }
    
    public int getTotalHaberes(String rut) {
		String sql = "select tot_haberes from eje_ges_certif_histo_liquidacion_cabecera where rut = ? and periodo=(select max(periodo) from eje_ges_certif_histo_liquidacion_cabecera)";
		Object[] params = {rut};
		try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal", sql, params);
			if(data != null && data.next()) {
				return Validar.getInstance().validarInt(data.getForcedString("tot_haberes"),0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
    
    
    public String getRentaBrutaMensual(String s) {
    	String valor="0";
    	ResourceBundle proper = ResourceBundle.getBundle("db");
    	String codigos = proper.getString("portal.rentabrutamensual");
    	consul = new Consulta(con);
        String s1 = String.valueOf(String.valueOf((new StringBuilder("select isnull(sum(val_haber),0) renta_bruta_mensual from eje_ges_certif_histo_liquidacion_detalle where rut=").append(s).append(" and periodo=(select max(peri_id) from eje_ges_periodo) and id_tp='H' and wp_indic_papeleta='S' and haber_variable='N' "))));
        
        // and orden in (").append(codigos).append(")"))));
        consul.exec(s1); consul.next();
        valor = consul.getString("renta_bruta_mensual");
        return valor;
    }
    
    public String getRentaBrutaPromedio3Meses(String rut) {
    	ResourceBundle proper = ResourceBundle.getBundle("db");
    	String haberes = proper.getString("portal.rentabrutamensual3meses");
    	int valor=0;
    	consul = new Consulta(con);
        String query = String.valueOf(String.valueOf((new StringBuilder("select isnull(SUM(val_haber),0) total from ")
        	.append("eje_ges_certif_histo_liquidacion_detalle where id_tp='H' and rut=").append(rut)
        	.append(" and periodo=(select MAX(peri_id) from eje_ges_periodo) and haber_variable='N' ")
        	.append("and wp_indic_papeleta='S'"))));
        consul.exec(query); 
        if(consul.next()) {
        	valor += Integer.parseInt(consul.getString("total"));	
        }
        query = String.valueOf(String.valueOf((new StringBuilder("select isnull(cast((SUM(isnull(val_haber,0))/3) as int),0) total from ")
        	.append("eje_ges_certif_histo_liquidacion_detalle where id_tp='H' and rut=").append(rut) 
        	.append(" and periodo in (select top 3 peri_id from eje_ges_periodo order by peri_id desc) ")
        	.append("and haber_variable='S' and wp_indic_papeleta='S' and ")
        	.append("upper(glosa_haber) not in ( " + haberes + ")"))));
        consul.exec(query); 
        if(consul.next()) {
        	valor += Integer.parseInt(consul.getString("total"));	
        }
        return String.valueOf(valor);
    }

    public String getEmpresaRentaBrutaPromedio3Meses(String rut) {
    	String valor="";
    	consul = new Consulta(con);
        String query = String.valueOf(String.valueOf((new StringBuilder("select empre_rut from eje_ges_empresa ")
        	.append("where empresa = (select wp_cod_empresa from eje_ges_trabajador where rut=").append(rut).append(")"))));
        consul.exec(query); 
        if(consul.next()) {
        	valor = consul.getString("empre_rut");	
        }
        return String.valueOf(valor);
    }

    
    public String getError()
    {
        return mensajeError;
    }
    
    public EjeGesEmpresa getEjeGesEmpresa(int rut) {
    	String[] params = {String.valueOf(rut)};
    	String sql = "select * from  eje_ges_empresa where empresa in (select top 1 empresa from eje_ges_trabajador where rut = ?)";
    	EjeGesEmpresa e = new EjeGesEmpresa();
    	
    	try {
			ConsultaData data = ConsultaTool.getInstance().getData("portal",sql, params);
			
			if(data != null && data.next()) {
				e.setEmpresa(data.getForcedString("empresa"));
				e.setDescrip(data.getForcedString("descrip"));
				e.setEmpreRut(data.getForcedString("empre_rut"));
				e.setEmpreDir(data.getForcedString("empre_dir"));
				e.setEmpreGiro(data.getForcedString("empre_giro"));
				e.setRutRepr(data.getForcedString("rut_repr"));
				e.setNomRep(data.getForcedString("nom_rep"));
				e.setPadreUnidad(data.getForcedString("padre_unidad"));
				e.setPadreEmpresa(data.getForcedString("padre_empresa"));
				e.setId_actEconom(data.getForcedString("id_act_econom"));
				e.setOrden(data.getForcedString("orden"));
				e.setFono(data.getForcedString("fono"));				
				e.setFax(data.getForcedString("fax"));
				e.setEmail(data.getForcedString("email"));
				e.setCompania(data.getForcedString("compania"));
				e.setCod_planta(data.getForcedString("cod_planta"));

			}
		}
		catch (SQLException e2) {
			e2.printStackTrace();
		}
    	
    	return e;
    }
    
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
    
	 public Consulta getCustomization(String rut) {
	    	String valor="No Definido";
	    	consul = new Consulta(con);
	    	
	        StringBuilder query = new StringBuilder("select distinct c.rut_responsable,c.img_firma,c.img_logo, ")
	        		.append("isnull(rtrim(ltrim(t.nombre)),'N/A') nombre,isnull(rtrim(ltrim(c1.descrip)),'N/A') cargo ")
	        		.append("from eje_generico_customization c left outer join eje_ges_trabajador t on ")
	        		.append("c.rut_responsable=t.rut left outer join eje_ges_cargos c1 on t.cargo=c1.cargo where c.empresa=")
	        		.append("(select top 1 wp_cod_empresa from eje_ges_trabajador where rut=").append(rut).append(")");
	        consul.exec(query.toString()); 
	        return consul;    		
	 }
	 
	 public Consulta getCustomizationByCargoTrablaTrabajador(String rut) {
	    	String valor="No Definido";
	    	consul = new Consulta(con);
	        StringBuilder strSQL = new StringBuilder();
	        
	        strSQL.append("select c.rut_responsable, c.img_firma, c.img_logo, nombre=coalesce(tr.nombre, 'N/A'), cargo=coalesce(cg.descrip, 'N/A') \n")
	        .append("from eje_generico_customization c \n")
	        .append("inner join eje_ges_trabajador t on t.empresa=c.empresa \n")
	        .append("inner join eje_ges_trabajador tr on c.rut_responsable=tr.rut \n")
	        .append("inner join eje_ges_cargos cg on cg.empresa=tr.empresa and cg.cargo=tr.cargo \n")
	        .append("where t.rut=").append(rut).append(" \n");
	        consul.exec(strSQL.toString()); 
	        return consul;	        
	 }
	 
	 
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public String getCcostoByRut(String rut) {
		String valor="No Definido";
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("select distinct top 1 rtrim(ltrim(c.descrip)) nombre from eje_ges_trabajador t ")
			.append("inner join eje_ges_centro_costo c on t.ccosto=c.centro_costo and t.wp_cod_empresa=c.wp_cod_empresa ")
			.append("where t.rut=").append(rut);
		consul.exec(s1.toString());
		if(consul.next()) {
			valor = consul.getString("nombre");
		}
		return valor;
	}
	 
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public String getRentaBrutaPromedio3MesesPortal(String rut) {
    	int valor=0;
    	consul = new Consulta(con);
        StringBuilder query = new StringBuilder("select cast(round(SUM(isnull(val_haber,0))/3,0,2) as int) valor ")
        		.append("from eje_ges_certif_histo_liquidacion_detalle where rut=").append(rut)
        		.append(" and periodo in (select distinct top 3 periodo from eje_ges_certif_histo_liquidacion_cabecera ")
        		.append("where wp_ndias_trab=30 and rut=").append(rut).append(" order by periodo desc) and id_tp='H' and ")
        		.append("rtrim(ltrim(glosa_haber)) in (select rtrim(ltrim(descrip)) from eje_ges_haber where campo2='S')");
        consul.exec(query.toString()); 
        if(consul.next()) {
        	valor = Validar.getInstance().validarInt(consul.getString("valor"),0);	
        }
        return String.valueOf(valor);
	 }
	 
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	 public String getGratif_Modalidad(String rut) {
    	String valor="No Definido";
    	consul = new Consulta(con);
        StringBuilder query = new StringBuilder("select shortname from eje_ges_trabajador where rut=").append(rut);
        consul.exec(query.toString()); 
        if(consul.next()) {
        	valor = consul.getString("shortname");	
        }
        return String.valueOf(valor);
	 }

    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public Consulta getDatosLugarTrabajo(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("select b.descrip as ciudad,c.descrip as comuna,'NR' representan_legal from eje_ges_trabajador d, ")
			.append("eje_ges_lugar_prestacion a,eje_ges_ciudades b,eje_ges_comunas c where a.vigente= 'S' and ")
			.append("d.cod_lug_prestacion = a.cod_lugar_prestacion and a.cod_ciudad = b.ciudad and a.cod_comuna = c.comuna and d.rut = ")
			.append(s);
		consul.exec(s1.toString());
		return consul;
	}
	
	
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public String getLiquidacionAnterior(String s){
		StringBuilder s1= new StringBuilder("select top 2 liquido from  eje_ges_certif_histo_liquidacion_cabecera where rut= ")
			.append(s).append(" order by periodo desc");
		consul.exec(s1.toString());
		consul.next(); consul.next();
		return consul.getString("liquido");
	}

    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public String getLiquidacionAnteriorAnterior(String s){
		StringBuilder s1= new StringBuilder("select top 3 liquido from eje_ges_certif_histo_liquidacion_cabecera where rut= ")
			.append(s).append(" order by periodo desc");
		consul.exec(s1.toString());
		consul.next(); consul.next(); consul.next();
		return consul.getString("liquido");
	}

	
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public String getMontoAfp(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder(" SELECT cot_afp FROM eje_ges_trabajador where rut = ").append(s);
		consul.exec(s1.toString());
		if(consul.next()) {
			return consul.getString(1);
		}
		return "0";
	}

	public String getCotAfp(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder(" SELECT mo_cot_afp FROM eje_ges_trabajador where rut = ").append(s);
		consul.exec(s1.toString());
		if(consul.next()) {
			return consul.getString(1);
		}
		return "0";
	}
	
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public String getPlanSalud(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("SELECT plan_salud FROM eje_ges_trabajador where rut = ").append(s);
		consul.exec(s1.toString());
		consul.next();
		return consul.getString("plan_salud");
	}
	
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public String getUltimoMontoAfp(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("select TOP 1 monto_afp = liquido / (1*(SELECT CASE  WHEN cot_afp <= 0 THEN 1 WHEN cot_afp > 0 ")
			.append("THEN cot_afp END FROM eje_ges_trabajador where rut = ").append(s).append(")) FROM eje_ges_certif_histo_liquidacion_cabecera b ")
			.append("WHERE rut = ").append(s).append("order by periodo desc");
		consul.exec(s1.toString());
		consul.next();
		return consul.getString("monto_afp");
	}

	
	
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public String getMontoIsapre(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("SELECT mon_salud FROM eje_ges_trabajador where rut = ").append(s);
		consul.exec(s1.toString());
		consul.next();
		return consul.getString("mon_salud");
	}
	
	 
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public String getPorcentajeIsapre(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("select CASE WHEN t.mon_salud = 0 THEN 0 ")
			.append("ELSE (t.mon_salud/c.liquido)*100 END promedio from eje_ges_trabajador t ")
			.append("inner join eje_ges_certif_histo_liquidacion_cabecera c on t.rut=c.rut and ")
			.append("c.periodo=(select MAX(peri_id) from eje_ges_periodo) and t.rut=").append(s);
		consul.exec(s1.toString());
		consul.next();
		return consul.getString("promedio");
	}

    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public String getCotSalud(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("SELECT mon_salud FROM eje_ges_trabajador where rut = ").append(s);
		consul.exec(s1.toString());
		consul.next();
		return consul.getString("mon_salud");
	}
	
	public String getMontoSalud(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("SELECT cot_salud FROM eje_ges_trabajador where rut = ").append(s);
		consul.exec(s1.toString());
		consul.next();
		return consul.getString("cot_salud");
	}
 
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public Consulta getDatosPrev2(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("select top 1 rtrim(ltrim(glosa_descuento)) concepto1, ")
				.append("substring(rtrim(ltrim(glosa_descuento)),1,6) concepto2, val_descuento concepto3 ")
				.append("from eje_ges_certif_histo_liquidacion_detalle where glosa_descuento like '%Cotiz%' ")
				.append("and periodo=(select max(periodo) from eje_ges_certif_histo_liquidacion_cabecera ")
				.append("where rut=").append(s).append(") and id_tp='D' and wp_indic_papeleta='S' and rut =").append(s);
		consul.exec(s1.toString());
		return consul;
	}
	public String getAhorroVoluntario(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("SELECT ah_volunt FROM eje_ges_trabajador where rut = ").append(s);
		consul.exec(s1.toString());
		consul.next();
		return consul.getString("ah_volunt");
	}
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public Consulta getYearLicenciaMedica(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("select top 3 datepart(year,fecha_inicio) as year_licencia ,sum(dias) as total_dias from ")
			.append("eje_ges_licencias_medicas where rut= ").append(s).append(" group by datepart(year,fecha_inicio) ")
			.append("order by datepart(year,fecha_inicio) desc");
		consul.exec(s1.toString());
		return consul;
	}

    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public String getNumeroAFP(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("select count (afp) as numafp FROM eje_ges_trabajador WHERE ")
			.append("afp = (select afp from eje_ges_trabajador where rut = ").append(s).append(") AND ")
			.append("unidad = (select unidad from eje_ges_trabajador where rut = ").append(s).append(")");
		consul.exec(s1.toString());
		consul.next();
		return consul.getString("numafp");

	}
	
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public String getNumeroIsapre(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("select count (isapre) as numisapre FROM eje_ges_trabajador WHERE ")
			.append("isapre = (select isapre from eje_ges_trabajador where rut = ").append(s).append(") AND ")
			.append("unidad = (select unidad from eje_ges_trabajador where rut = ").append(s).append(")");
		consul.exec(s1.toString());
		consul.next();
		return consul.getString("numisapre");

	}
	
    /**
     * Integración con webmatico
     * @since 6-dic-2016
     * */
	public Consulta getRankingVisitados(String s) {
		consul = new Consulta(con);
		StringBuilder s1 = new StringBuilder("select TOP 5 b.descripcion, COUNT (*) AS num_vis FROM eje_ges_tracking a ")
			.append("INNER JOIN eje_ges_tracking_func b ON a.direc_rel = b.url AND a.rut = ").append(s)
			.append(" GROUP BY a.rut, a.direc_rel, b.url, b.descripcion ORDER BY num_vis DESC");
		consul.exec(s1.toString());
		return consul;
	}
	
	/**
     * Tipos de Parrafos para certificados
     * @since 5-Nov-2018
     * */
	public ConsultaData getTiposParrafos(String strUso) {

		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select tp.id_tipoparrafo, tp.tipoparrafo \n");
		strSQL.append("from eje_generico_tipoparrafo tp \n");
		strSQL.append("where tp.uso='").append(strUso).append("' and tp.vigente=1 \n");
		strSQL.append("order by tp.tipoparrafo \n");
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
		
	}
	
    /**
     * Parrafos para certificados
     * @since 5-Nov-2018
     * */
	public ConsultaData getParrafos(String strTipo) {

		StringBuilder strSQL = new StringBuilder();
		
		strSQL.append("select id_parrafo, contenido \n");
		strSQL.append("from eje_generico_tipoparrafo_parrafo p \n");
		strSQL.append("inner join eje_generico_tipoparrafo tp on tp.id_tipoparrafo=p.id_tipoparrafo \n");
		strSQL.append("where tp.uso='").append(strTipo).append("' and tp.vigente=1 --and p.vigente=1 \n");
		strSQL.append("order by p.contenido \n");
		
		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", strSQL.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
		
	}
	
	
    private Connection con;
    private Consulta consul;
    private String mensajeError;
	
}