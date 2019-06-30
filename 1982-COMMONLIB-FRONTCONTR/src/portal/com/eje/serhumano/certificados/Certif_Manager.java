package portal.com.eje.serhumano.certificados;

import java.sql.Connection;
import java.sql.SQLException;

import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.web.datos.ConsultaTool;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.serhumano.certificados:
//            Liquida_Manager

public class Certif_Manager
{

    public Certif_Manager(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }

    public int GetTipoCertificado(String rut)
    {
        int intIdTipoReconocimiento = 0;
    	consul = new Consulta(con);
        String sql = "select id_tiporeconocimiento from eje_certantig_data where id_persona=".concat(String.valueOf(String.valueOf(rut)));
        OutMessage.OutMessagePrint("Antiguedad: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        while (consul!=null && consul.next()) {
        	intIdTipoReconocimiento = consul.getInt("id_tiporeconocimiento");
        }
        return intIdTipoReconocimiento;
    }
    
    public Consulta GetCertifAntig(String rut)
    {
        consul = new Consulta(con);
        String sql = "select empresa,empre_rut,hoy,rut,digito_ver,nombre,fecha_ingreso,cargo,sueldo,renta_reg_mensual,tip_contrato,descrip as glosa_empresa from view_InfoRut where rut = ".concat(String.valueOf(String.valueOf(rut)));
        OutMessage.OutMessagePrint("Antiguedad: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetCertifAntigByTipoReconocimiento(String rut)
    {

    	StringBuilder strSQL = new StringBuilder();
    	
    	consul = new Consulta(con);

    	strSQL.append("select v.empresa, v.empre_rut, glosa_empresa=v.descrip, v.hoy, v.rut, v.digito_ver, v.nombre, fecha_ingreso_info=v.fecha_ingreso, \n")
    	.append("v.cargo, v.sueldo, v.renta_reg_mensual, v.tip_contrato, \n")
    	.append("cd.fecha_ingreso, cd.fecha_primer_contrato, cd.fecha_contrato_actual, cd.empresa_rut, cd.empresa_nombre \n")
    	.append("from view_InfoRut v \n")
    	.append("inner join eje_certantig_data cd on cd.id_persona=v.rut \n")
    	.append("where v.rut = ").append(rut).append(" \n");

        OutMessage.OutMessagePrint("Antiguedad: ".concat(String.valueOf(String.valueOf(strSQL.toString()))));
        consul.exec(strSQL.toString());
        return consul;
        
    }
    
    public Consulta GetCertifAntig3meses(String rut, int nro)
    {
        consul = new Consulta(con);
        String sql = "SELECT TOP " + nro + " rut, periodo, planta, empresa, dias_trabajados, " + " cargas_normales, cargas_duplos, cargas_maternas, impos_salud, impos_previs, " + " mto_cancel_impto, uf, comision_1, comision_2, sueldo_base, " + " bono_vacaciones, bono_navidad, bono_fiestas_patrias " + " FROM eje_ges_certif_antiguedad " + " WHERE (dias_trabajados >= 30) AND (rut = " + rut + ") " + " ORDER BY periodo desc";
        OutMessage.OutMessagePrint("Antiguedad: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetPeriodosMis(String key)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("SELECT peri_id=p.periodos,peri_ano=SUBSTRING(LTRIM(RTRIM(p.periodos)),1,4),peri_mes=SUBSTRING(LTRIM(RTRIM(p.periodos)),5,2)")
        							.append(" FROM eje_clientes_periodos P,eje_clientes C WHERE c.id  = p.idcliente  AND c.descripcion = '").append(key).append("'"));
        consul.exec(sql);
        return consul;
    }
    
    public Consulta getPeriodosLiquidacion(String rut)
    //public ConsultaData getPeriodosLiquidacion(String rut)
    {
    	/*
        Liquida_Manager data_periodo = new Liquida_Manager(con);
        String max_peri = data_periodo.getMaxPeriodo();
        StringBuilder sql = new StringBuilder("SELECT DISTINCT top 12 pe.peri_id, pe.peri_ano,pe.peri_mes, th.rut ")
        .append(" FROM eje_ges_certif_histo_liquidacion_cabecera th ")
        .append(" INNER JOIN eje_ges_periodo pe ON th.periodo = pe.peri_id ")
        .append(" WHERE (th.rut = ").append(rut).append(") ORDER BY pe.peri_id DESC");

        ConsultaData data = null;
        
 		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString() );
		} catch (SQLException e) {	
			e.printStackTrace();
		}
        return data;*/
        
        
        Consulta consulPeriodos = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT DISTINCT top 12 pe.peri_id, pe.peri_ano,pe.peri_mes, th.rut ")
        .append(" FROM eje_ges_certif_histo_liquidacion_cabecera th ")
        .append(" INNER JOIN eje_ges_periodo pe ON th.periodo = pe.peri_id ")
        .append(" WHERE (th.rut = ").append(rut).append(") ORDER BY pe.peri_id DESC");
        consulPeriodos.exec(sql.toString());
        return consulPeriodos;
    }
    
    public Consulta getPeriodosLiquidacionAdicional(String rut, String tipoPeriodo)
    //public ConsultaData getPeriodosLiquidacionAdicional(String rut, String tipoPeriodo)
    {
    	/*
        Liquida_Manager data_periodo = new Liquida_Manager(con);
        String max_peri = data_periodo.getMaxPeriodo();
        StringBuilder sql = new StringBuilder("SELECT DISTINCT top 12 pe.peri_id, pe.peri_ano,pe.peri_mes, th.rut ")
        .append(" FROM eje_ges_certif_histo_liquidacion_cabecera th ")
        .append(" INNER JOIN eje_ges_periodo pe ON th.periodo = pe.peri_id ")
        .append(" WHERE (th.rut = ").append(rut).append(") and ORDER BY pe.peri_id DESC");

        ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString() );
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		return data;*/
		
		Consulta consulPeriodos = new Consulta(con);
		StringBuilder sql = new StringBuilder("SELECT DISTINCT top 12 pe.peri_id, pe.peri_ano,pe.peri_mes, th.rut ")
			.append(" FROM eje_ges_certif_histo_liquidacion_cabecera th ")
			.append(" INNER JOIN eje_ges_periodo pe ON th.periodo = pe.peri_id ")
			.append(" WHERE (th.rut = ").append(rut).append(") and ORDER BY pe.peri_id DESC");
        consulPeriodos.exec(sql.toString());
        return consulPeriodos;
		 
    }

    public ConsultaData getPeriodosAdic(String rut, String tipoProceso) {
        StringBuilder sql = new StringBuilder();
        
        sql.append(" SELECT DISTINCT TOP 12 periodo as peri_id,")
        .append(" 	convert(integer,substring(convert(varchar(6),periodo),1,4)) as peri_ano, ")
        .append(" 	convert(integer,substring(convert(varchar(6),periodo),5,6)) as peri_mes, ")
        .append(" 	rut,tipo_proceso ")
        .append(" FROM eje_ges_certif_histo_liquidacion_cabecera_adic ")
        .append(" WHERE rut = ? and tipo_proceso = ? ")
        .append(" ORDER BY periodo desc ");
       
        Object[] params = {rut, tipoProceso};
        ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql.toString(),  params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return data;
    }

    public Consulta GetComboPeriodos()
    {
        consul = new Consulta(con);
        String sql = "SELECT DISTINCT TOP 12 peri_ano FROM eje_ges_periodo ORDER BY peri_ano DESC";
        consul.exec(sql);
        return consul;
    }

    public Consulta GetComboPeriodosMis(String key)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("SELECT DISTINCT peri_ano=SUBSTRING(LTRIM(RTRIM(p.periodos)),1,4)")
				.append(" FROM eje_clientes_periodos P,eje_clientes C WHERE c.id  = p.idcliente  AND c.descripcion = '").append(key).append("'"));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDataRutCursos(String rut)
    {
        consul = new Consulta(con);
        String sql = "select hoy,rut,digito_ver,nombre,id_afp,afp,empresa,cargo,nom_empresa,rut_supdirecto,dig_supdirecto,nom_supdirecto,cargo_supdirecto from view_InfoRut where rut = ".concat(String.valueOf(String.valueOf(rut)));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDataRutEstudios(String rut)
    {
        consul = new Consulta(con);
        String sql = "select hoy,rut,digito_ver,nombre,nombres,ape_paterno,ape_materno,id_afp,afp,empresa,cargo,nom_empresa,rut_supdirecto,dig_supdirecto,nom_supdirecto,cargo_supdirecto from view_InfoRut where rut = ".concat(String.valueOf(String.valueOf(rut)));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDataRutViaje(String rut)
    {
        consul = new Consulta(con);
        String sql = "select hoy,rut,digito_ver,nombre,fecha_ingreso,empresa,cargo,sueldo,renta_reg_mensual,descrip as glosa_empresa from view_InfoRut where rut = ".concat(String.valueOf(String.valueOf(rut)));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetApePaterno(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT ape_paterno=ltrim(rtrim(ape_paterno)) FROM view_rut_all WHERE (rut = ")).append(rut).append(")")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDataRutRtaBruta(String rut)
    {
        consul = new Consulta(con);
        String sql = "select hoy,rut,digito_ver,nombre,nombres,ape_paterno,ape_materno,fecha_ingreso,nom_empresa,sueldo,total_haberes,otros_haberes,rut_supdirecto,dig_supdirecto,nom_supdirecto,cargo_supdirecto from view_InfoRut where rut = ".concat(String.valueOf(String.valueOf(rut)));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDetalleRentas(String rut, String empresa, String periodo)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT empresa, year, rut, mes, renta, leyes_soc, renta_af_neta,imp_retenido, acc_renta, acc_ley, acc_imp, tot_renta, tot_ley,tot_neta, tot_imp, factor, renta_actual, imp_actual FROM vista_Rentas WHERE (empresa = '")).append(empresa).append("')").append(" AND (year = ").append(periodo).append(") AND (rut = ").append(rut).append(")")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetNumCertifRtaSII(String rut, String periodo)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo, numero FROM eje_ges_certifRenta WHERE (periodo = ")).append(periodo).append(") ").append("and (rut=").append(rut).append(")")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDataRutRtaLiquida(String rut)
    {
        consul = new Consulta(con);
        String sql = "select hoy,rut,digito_ver,nombre,nombres,ape_paterno,ape_materno,fecha_ingreso,nom_empresa,sueldo,cargo,rut_supdirecto,dig_supdirecto,nom_supdirecto,cargo_supdirecto from view_InfoRut where rut = ".concat(String.valueOf(String.valueOf(rut)));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetHaberesFijos(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, renta_reg_mensual, gratif_fijo, asig_zona_fijo,comision_fijo, colacion_fijo, moviliza_fijo FROM eje_ges_trabajador WHERE (rut = ")).append(rut).append(")")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDesctosFijos(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut,isapre_fijo, afp_fijo, impuesto_fijo FROM eje_ges_trabajador WHERE (rut = ")).append(rut).append(")")));
        consul.exec(sql);
        return consul;
    }

    public int GetNumCertificado(int anio)
    {
        consul = new Consulta(con);
        int numero = 1;
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT max(numero) as num FROM eje_ges_num_certificado WHERE (anio = ")).append(anio).append(")")));
        OutMessage.OutMessagePrint("*****query: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        if(consul.next())
            numero = consul.getInt("num") + 1;
        sql = String.valueOf(String.valueOf((new StringBuilder("INSERT INTO eje_ges_num_certificado (anio, numero) VALUES (")).append(anio).append(", ").append(numero).append(")")));
        OutMessage.OutMessagePrint("****Insertando BD: ".concat(String.valueOf(String.valueOf(sql))));
        consul.insert(sql);
        return numero;
    }

    public Consulta GetDataRutIsapre(String rut)
    {
        consul = new Consulta(con);
        String sql = "select hoy,rut,digito_ver,nombre,id_isapre,isapre,plan_salud,empresa,cargo,nom_empresa,rut_supdirecto,dig_supdirecto,nom_supdirecto,cargo_supdirecto from view_InfoRut where rut = ".concat(String.valueOf(String.valueOf(rut)));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDataRutSII(String rut)
    {
        consul = new Consulta(con);
        String sql = "select empresa,hoy,rut,digito_ver,nombre,fecha_ingreso,cargo,sueldo,renta_reg_mensual,tip_contrato,nom_empresa, empre_rut, empre_dir, empre_giro,nom_rep, rut_repr from view_InfoRut where rut = ".concat(String.valueOf(String.valueOf(rut)));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetCursos(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM eje_ges_capacitaciones WHERE (rut = ")).append(rut).append(") AND (horas IS NOT NULL) AND ").append("(YEAR(fecha_inicio) > YEAR(GETDATE())- 5) ").append("ORDER BY fecha_inicio desc")));
        OutMessage.OutMessagePrint("Antiguedad: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetPracticantes(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_trabajador.rut, eje_ges_trabajador.digito_ver,eje_ges_trabajador.nombre,eje_ges_trabajador.nombres,eje_ges_trabajador.ape_paterno,eje_ges_trabajador.ape_materno,eje_ges_trabajador.fecha_ingreso,eje_ges_trabajador.fec_ter_cont,eje_ges_trabajador.rel_laboral,eje_ges_areas.area_desc AS area FROM eje_ges_trabajador LEFT OUTER JOIN eje_ges_areas ON eje_ges_trabajador.area = eje_ges_areas.area_id WHERE (eje_ges_trabajador.tip_contrato = 4) AND (eje_ges_trabajador.rut = ")).append(rut).append(")")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetEstudios(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT beca,periodo,carrera,sem_realizados,sem_pendientes,sem_totales,por_finan,monto_trabajador,monto_empresa,monto_total,estado_beca,resultado,org_instructor FROM eje_ges_curso_externo WHERE (rut = ")).append(rut).append(") ORDER BY periodo")));
        OutMessage.OutMessagePrint("Antiguedad: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }
    
    public ConsultaData getTipoLiquidacionAdicionalDisponibles() {
		String sql = "select distinct tipo_proceso,nombre from eje_ges_liq_adic_conf ORDER BY nombre ";

		ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
    
    public String GetDVRut(String rut) {
    	String dv = "";
    	String sql = "select digito_ver from eje_ges_trabajador where rut=" + rut;
    	ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql);
			if(data.next()) {
				dv = data.getString("digito_ver");
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return dv;
    }
    
    public String GetEmpresaRut(String rut) {
    	String empresa = "";
    	String sql = "select wp_cod_empresa from eje_ges_trabajador where rut=" + rut;
    	ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql);
			if(data.next()) {
				empresa = data.getForcedString("wp_cod_empresa");
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return empresa;
    }  
    
    public String GetNombreRut(String rut) {
    	String nombre = "";
    	String sql = "select nombre from eje_ges_trabajador where rut=" + rut;
    	ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql);
			if(data.next()) {
				nombre = data.getForcedString("nombre");
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return nombre;
    }  
    
    public String GetPeriodoSIIRut(String rut) {
    	String periodo = "";
    	String sql = "select max(ano) periodo from eje_ges_certif_rentassii_rentas where numrut=" + rut;
    	ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql);
			if(data.next()) {
				periodo = data.getForcedString("periodo");
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return periodo;
    } 
    
    public ConsultaData GetDatosPlantaRentasSIINew(String empresa) {
    	String sql = String.valueOf(new StringBuilder("SELECT DISTINCT e.descrip as nombre,e.empre_rut as rut, " +
    			"cr.direccion + ' ' + comuna + ' ' + ciudad as dir, e.empre_giro as giro,cr.numrut,cr.digrut FROM " +
    			"eje_ges_certif_rentassii_empresas as cr INNER JOIN eje_ges_empresa as e ON " +
    			"ltrim(rtrim(replace(replace(cast(numrut as varchar) + '-' + digrut,char(13),''),char(10),''))) = " +
    			"ltrim(rtrim(replace(replace(empre_rut,char(13),''),char(10),''))) WHERE cr.cod1='").append(empresa).append("' "));
    	ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
    }
    
    public ConsultaData GetDetalleRentaSIINew(String rut,String periodo,String opcion,String empresa) {
    	String sql=null;
    	if(opcion.equals("1")) { 
    		sql = "select distinct r.mes,r.numcer,cast(r.tothab as int) as tothab,cast(r.llss as int) as llss,cast(r.btrib as int) as btrib " +
    				",cast(r.impto as int) as impto,cast(r.mayor_retencion as int) as mayor_retencion,cast(r.nint as int) as nint, " +
    				"cast(r.zona as int) as zona,factor=convert(decimal(5,3),f.factor), cast((r.btrib*f.factor) as int) as c1, " +
    				"cast((r.impto*f.factor) as int) as c2,cast((r.mayor_retencion*f.factor) as int) as c3,cast((r.nint*f.factor) as int) as c4, " +
    				"cast((r.zona*f.factor) as int) as c5 from eje_ges_certif_rentassii_rentas r inner join eje_ges_certif_rentassii_factores f " +
    				"on r.ano=f.ano and r.mes=f.mes where r.numrut=" + rut + " and r.ano=" + periodo + " and r.caso='S' and numrut_e=" + empresa + 
    				" ORDER BY r.mes"; 
    	}
        else if(opcion.equals("2")) {
        	sql = "select distinct r.mes,r.numcer,cast(r.btrib as int) as btrib,cast(r.impto as int) as impto, " +
        			"factor=convert(decimal(5,3),f.factor),cast((r.btrib*f.factor) as int) as c1,cast((r.impto*f.factor) as int) as c2 " +
        			"from eje_ges_certif_rentassii_rentas r inner join eje_ges_certif_rentassii_factores f on r.ano=f.ano and r.mes=f.mes " +
        			"where r.numrut=" + rut + " and r.ano=" + periodo + " and r.caso='H' and numrut_e=" + empresa + " ORDER BY r.mes"; 
        }
        else if(opcion.equals("3")) {
        	sql = "select distinct r.mes,r.numcer,cast(r.btrib as int) as btrib,poa1=0,cast(r.impto as int) as impto,poa2=0, " +
        			"factor=convert(decimal(5,3),f.factor),cast((r.btrib*f.factor) as int) as c1,c2=0,cast((r.impto*f.factor) as int) as c3 " +
        			",c4=0 from eje_ges_certif_rentassii_rentas r inner join eje_ges_certif_rentassii_factores f on r.ano=f.ano and r.mes=f.mes " +
        			"where r.numrut=" + rut + " and r.ano=" + periodo + " and r.caso='H' and numrut_e=" + empresa + " ORDER BY r.mes";
        }
    	ConsultaData data = null;
		try {
			data = ConsultaTool.getInstance().getData("portal", sql);
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
    }

    public String getError()
    {
        return mensajeError;
    }

    private Connection con;
    private Consulta consul;
    private String mensajeError;
	
}