package portal.com.eje.serhumano.certificados;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

import java.sql.Connection;

public class Certif_Manager_webmatico {

    public Certif_Manager_webmatico(Connection conex) {
        con = conex;
        mensajeError = "";
    }
    
    public Consulta GetCertifAntig(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select distinct empresa,empre_rut,hoy,rut,digito_ver,nombre,fecha_ingreso,cargo, ")
        	.append("sueldo,renta_reg_mensual,desc_tip_contrato as tip_contrato,descrip as glosa_empresa,total_haberes, ")
        	.append("fecha_nacim,afp,isapre,empre_dir,wp_cod_planta from view_InfoRut where rut = ").append(rut);
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetDatosPlanta(String empresa,String planta) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT distinct a.codigo,a.descripcion,a.wp_cod_mutual,a.descripcion_mutual, ")
        	.append("b.empresa,b.empre_dir,b.fono,b.fax FROM eje_ges_sociedad a,eje_ges_empresa b WHERE ")
        	.append("wp_cod_mutual is not null AND a.codigo = b.cod_planta AND b.empresa ='").append(empresa)
        	.append("' AND a.codigo = '").append(planta).append("' and a.wp_cod_empresa=b.empresa");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetDatosPlanta(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT distinct wp_cod_empresa,wp_cod_planta,unidad FROM eje_ges_trabajador ")
        	.append("WHERE  rut = '").append(rut).append("'"); 
        consul.exec(sql.toString());
        return consul;
    }
    
    public Consulta GetUltimaLiquidacion(String rut) {
    	consul = new Consulta(con);
    	StringBuilder sql = new StringBuilder("SELECT      TOP 1 periodo ,")
        		.append("            empresa , unidad , rut , causa_pago , fec_pago , forma_pago , cuenta , imp_tribut ,")
        		.append("            imp_no_tribut , no_imp_tribut , no_imp_no_tribut , reliq_rentas ,")
        		.append("            tot_haberes=coalesce(tot_haberes, 0),")
        		.append("            tot_desctos=coalesce(tot_desctos, 0),")
        		.append("            liquido=coalesce(liquido, 0),")
        		.append("            id_forma_pago , tope_imp , val_uf , dctos_varios , dctos_legales , dctos_impagos ,")
        		.append("            banco , wp_cod_empresa , wp_cod_planta , wp_tot_imponible , wp_afecto_imponible ,")
        		.append("            wp_ndias_trab , base_tribut , n_cargas , sobregiro , tramo , wp_ndias_lic , isapre ,")
        		.append("            afp , tot_aportaciones ")
        		.append("FROM 		 eje_ges_certif_histo_liquidacion_cabecera ")
        		.append("WHERE rut LIKE ").append(rut).append(" ")
        		.append("ORDER BY periodo DESC");
        consul.exec(sql.toString());
        return consul;
    }
    
    public String GetPorcentajeUnidad(String rut, String unidad) {
    	consul = new Consulta(con);
    	StringBuilder sql = new StringBuilder("SELECT porcentaje = liquido/(0.01*(select sum(liquido) ")
    		.append("from eje_ges_certif_histo_liquidacion_cabecera a where	rut in (select distinct rut from eje_ges_trabajador ")
    		.append("where unidad = '").append(unidad).append("') and periodo = (select top 1 periodo from ")
    		.append("eje_ges_certif_histo_liquidacion_cabecera where rut = ").append(rut).append(" order by periodo desc)))" )
    		.append("FROM eje_ges_certif_histo_liquidacion_cabecera b WHERE	rut = ").append(rut)
    		.append(" AND periodo = (select top 1 periodo from eje_ges_certif_histo_liquidacion_cabecera where rut = ")
    		.append(rut).append(" order by periodo desc)");
    	consul.exec(sql.toString());
    	consul.next();
        return consul.getString("porcentaje");
    }

    
    public Consulta GetPeriodosMis(String key) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT peri_id=p.periodos,peri_ano=SUBSTRING(LTRIM(RTRIM(p.periodos)),1,4), ")
        	.append("peri_mes=SUBSTRING(LTRIM(RTRIM(p.periodos)),5,2) FROM eje_clientes_periodos P,eje_clientes C WHERE ")
        	.append("c.id  = p.idcliente  AND c.descripcion = '").append(key).append("'");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetPeriodos(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT DISTINCT top 12 pe.peri_id, pe.peri_ano,pe.peri_mes, th.rut FROM ")
        	.append("eje_ges_certif_histo_liquidacion_cabecera th INNER JOIN eje_ges_periodo pe ON th.periodo = pe.peri_id ")
        	.append("WHERE (th.rut = ").append(rut).append(") ORDER BY pe.peri_id DESC");
        consul.exec(sql.toString());
        return consul;
    }
    
    public Consulta GetLastPeriodo(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select top 1 cast(SUBSTRING(cast(periodo as varchar),1,4) as int) ano, ")
        	.append("cast(SUBSTRING(cast(periodo as varchar),5,6) as int) mes from eje_ges_certif_histo_liquidacion_cabecera ") 
        	.append("where rut=").append(rut).append(" order by periodo desc");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta getPeriodosAdic(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT DISTINCT TOP 12 periodo as peri_id,substring(convert(varchar(6),periodo),1,4) peri_ano, ")
        	.append("substring(convert(varchar(6),periodo),5,6) peri_mes,rut FROM eje_ges_certif_histo_liquidacion_cabecera_adic ")
        	.append("WHERE rut = ").append(rut);
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetComboPeriodos() {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT DISTINCT TOP 12 peri_ano FROM eje_ges_periodo ORDER BY peri_ano DESC");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetDataRutCursos(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select hoy,rut,digito_ver,nombre,id_afp,afp,empresa,cargo,nom_empresa, ")
        	.append("rut_supdirecto,dig_supdirecto,nom_supdirecto,cargo_supdirecto from view_InfoRut where rut = ")
        	.append(rut);
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetDataRutEstudios(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select hoy,rut,digito_ver,nombre,nombres,ape_paterno,ape_materno,id_afp,afp, ")
        	.append("empresa,cargo,nom_empresa,rut_supdirecto,dig_supdirecto,nom_supdirecto,cargo_supdirecto from view_InfoRut ")
        	.append("where rut = ").append(rut);
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetDataRutViaje(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select hoy,rut,digito_ver,nombre,fecha_ingreso,empresa,cargo,sueldo, ")
        	.append("renta_reg_mensual,descrip as glosa_empresa from view_InfoRut where rut = ").append(rut);
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetApePaterno(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT ape_paterno FROM view_rut_all WHERE (rut = ").append(rut).append(")");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetDataRutRtaBruta(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select hoy,rut,digito_ver,nombre,nombres,ape_paterno,ape_materno,fecha_ingreso, ")
        	.append("nom_empresa,sueldo,total_haberes,otros_haberes,rut_supdirecto,dig_supdirecto,nom_supdirecto, ")
        	.append("cargo_supdirecto from view_InfoRut where rut = ").append(rut);
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetDetalleRentas(String rut, String empresa, String periodo) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT empresa, year, rut, mes, renta, leyes_soc, renta_af_neta,imp_retenido, ")
        	.append("acc_renta, acc_ley, acc_imp, tot_renta, tot_ley,tot_neta, tot_imp, factor, renta_actual, imp_actual FROM ")
        	.append("vista_Rentas WHERE (empresa = '").append(empresa).append("')").append(" AND (year = ").append(periodo)
        	.append(") AND (rut = ").append(rut).append(")");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetNumCertifRtaSII(String rut, String periodo) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT periodo, numero FROM eje_ges_certifRenta WHERE (periodo = ").append(periodo)
        		.append(") ").append("and (rut=").append(rut).append(")");
        consul.exec(sql.toString());
        return consul;
    }
    
    public Consulta GetDataRutRtaLiquida(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select hoy,rut,digito_ver,nombre,nombres,ape_paterno,ape_materno,fecha_ingreso, ")
        	.append("nom_empresa,sueldo,cargo,rut_supdirecto,dig_supdirecto,nom_supdirecto,cargo_supdirecto from view_InfoRut ")
        	.append("where rut = ").append(rut);
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetHaberesFijos(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT rut, renta_reg_mensual, gratif_fijo, asig_zona_fijo,comision_fijo, ")
        	.append("colacion_fijo, moviliza_fijo FROM eje_ges_trabajador WHERE (rut = ").append(rut).append(")");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetDesctosFijos(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT rut,isapre_fijo, afp_fijo, impuesto_fijo FROM eje_ges_trabajador WHERE ")
        	.append("(rut = ").append(rut).append(")");
        consul.exec(sql.toString());
        return consul;
    }

    public int GetNumCertificado(int anio) {
        consul = new Consulta(con);
        int numero = 1;
        StringBuilder sql = new StringBuilder("SELECT max(numero) as num FROM eje_ges_num_certificado WHERE (anio = ")
        	.append(anio).append(")");
        consul.exec(sql.toString());
        if(consul.next()) {
        	numero = consul.getInt("num") + 1;
        }
        sql = new StringBuilder("INSERT INTO eje_ges_num_certificado (anio, numero) VALUES (").append(anio).append(", ")
        		.append(numero).append(")");
        OutMessage.OutMessagePrint("****Insertando BD: ".concat(String.valueOf(String.valueOf(sql))));
        consul.insert(sql.toString());
        return numero;
    }

    public Consulta GetDataRutIsapre(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select hoy,rut,digito_ver,nombre,id_isapre,isapre,plan_salud,empresa,cargo, ")
        	.append("nom_empresa,rut_supdirecto,dig_supdirecto,nom_supdirecto,cargo_supdirecto from view_InfoRut where rut = ")
        	.append(rut);
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetDataRutSII(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select empresa,hoy,rut,digito_ver,nombre,fecha_ingreso,cargo,sueldo, ")
        	.append("renta_reg_mensual,tip_contrato,nom_empresa, empre_rut, empre_dir, empre_giro,nom_rep, rut_repr from ")
        	.append("view_InfoRut where rut = ").append(rut);
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetCursos(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT * FROM eje_ges_capacitaciones WHERE (rut = ").append(rut)
        		.append(") AND (horas IS NOT NULL) AND (YEAR(fecha_inicio) > YEAR(GETDATE())- 5) ")
        		.append("ORDER BY fecha_inicio desc");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetPracticantes(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT eje_ges_trabajador.rut, eje_ges_trabajador.digito_ver, ")
        	.append("eje_ges_trabajador.nombre,eje_ges_trabajador.nombres,eje_ges_trabajador.ape_paterno, ")
        	.append("eje_ges_trabajador.ape_materno,eje_ges_trabajador.fecha_ingreso,eje_ges_trabajador.fec_ter_cont, ")
        	.append("eje_ges_trabajador.rel_laboral,eje_ges_areas.area_desc AS area FROM eje_ges_trabajador LEFT OUTER JOIN ")
        	.append("eje_ges_areas ON eje_ges_trabajador.area = eje_ges_areas.area_id WHERE ")
        	.append("(eje_ges_trabajador.tip_contrato = 4) AND (eje_ges_trabajador.rut = ").append(rut).append(")");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetEstudios(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT beca,periodo,carrera,sem_realizados,sem_pendientes,sem_totales, ")
        	.append("por_finan,monto_trabajador,monto_empresa,monto_total,estado_beca,resultado,org_instructor FROM ")
        	.append("eje_ges_curso_externo WHERE (rut = ").append(rut).append(") ORDER BY periodo");
        consul.exec(sql.toString());
        return consul;
    }

    public String getError() {
        return mensajeError;
    }

    public Consulta existeTrabajador(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT 1 as output FROM eje_ges_usuario WHERE login_usuario = '").append(rut);
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetDetalleRentaSIINew(String rut,String periodo,String opcion,String empresa) {
        consul = new Consulta(con);
        StringBuilder sql=null;
        if(opcion.equals("1")) {
        	sql = new StringBuilder("select distinct r.mes,r.numcer,cast(r.tothab as int) tothab,cast(r.llss as int) llss, ")
        		.append("cast(r.btrib as int) btrib,cast(r.impto as int) impto,cast(r.mayor_retencion as int) mayor_retencion, ")
        		.append("cast(r.nint as int) nint,cast(r.zona as int) zona,factor=convert(decimal(5,3),f.factor), ")
        		.append("cast((r.btrib*f.factor) as int) c1,cast((r.impto*f.factor) as int) c2, ")
        		.append("cast((r.mayor_retencion*f.factor) as int) c3,cast((r.nint*f.factor) as int) c4, ")
        		.append("cast((r.zona*f.factor) as int) c5 from eje_ges_certif_rentassii_rentas r inner ")
        		.append("join eje_ges_certif_rentassii_factores f on r.ano=f.ano and r.mes=f.mes where r.numrut=").append(rut)
        		.append("and r.ano=").append(periodo).append(" and r.caso='S' and numrut_e=").append(empresa)
        		.append("ORDER BY r.mes"); 
        }
        else if(opcion.equals("2")) {
        	sql = new StringBuilder("select distinct r.mes,r.numcer,cast(r.btrib as int) as btrib,cast(r.impto as int) as impto, ")
        		.append("factor=convert(decimal(5,3),f.factor),cast((r.btrib*f.factor) as int) c1, ")
        		.append("cast((r.impto*f.factor) as int) c2 from eje_ges_certif_rentassii_rentas r inner join ")
        		.append("eje_ges_certif_rentassii_factores f on r.ano=f.ano and r.mes=f.mes where r.numrut=").append(rut)
        		.append(" and r.ano=").append(periodo).append(" and r.caso='H' and numrut_e=").append(empresa)
        		.append(" ORDER BY r.mes"); 
        }
        else if(opcion.equals("3")) {
        	sql = new StringBuilder("select distinct r.mes,r.numcer,cast(r.btrib as int) btrib,poa1=0,cast(r.impto as int) impto, ")
        		.append("poa2=0,factor=convert(decimal(5,3),f.factor),cast((r.btrib*f.factor) as int) c1,c2=0, ")
        		.append("cast((r.impto*f.factor) as int) c3,c4=0 from eje_ges_certif_rentassii_rentas r inner join ")
        		.append("eje_ges_certif_rentassii_factores f on r.ano=f.ano and r.mes=f.mes where r.numrut=").append(rut)
        		.append("and r.ano=").append(periodo).append(" and r.caso='H' and numrut_e=").append(empresa)
        		.append(" ORDER BY r.mes");
        }
        consul.exec(sql.toString());
        return consul;
    }
    
    public Consulta GetDatosEmpresaRentasSIINew(String rut,String opcion,String periodo) {
        consul = new Consulta(con);
        String caso=null;
        if(opcion.equals("1")) {
        	caso="S";
        }
        else if(opcion.equals("2")) {
        	caso="H";
        }
        StringBuilder sql = new StringBuilder("SELECT top 1 numrut_e from eje_ges_certif_rentassii_rentas where numrut=")
        	.append(rut).append(" and ano=").append(periodo).append(" and caso='").append(caso).append("'");
        consul.exec(sql.toString());
        return consul;
    }    
    
    public Consulta GetDatosPlantaRentasSIINew(String empresa) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("SELECT DISTINCT e.descrip as nombre,e.empre_rut as rut, ")
        	.append("cr.direccion + ' ' + comuna + ' ' + ciudad as dir, e.empre_giro as giro,cr.numrut,cr.digrut FROM ")
        	.append("eje_ges_certif_rentassii_empresas as cr INNER JOIN eje_ges_empresa as e ON ")
        	.append("ltrim(rtrim(replace(replace(cast(numrut as varchar) + '-' + digrut,char(13),''),char(10),''))) = ")
        	.append("ltrim(rtrim(replace(replace(empre_rut,char(13),''),char(10),''))) WHERE cr.numrut='").append(empresa)
        	.append("' ");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetPeriodosRentasSIINew(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select distinct top 1 ano from eje_ges_certif_rentassii_rentas where numrut='")
        	.append(rut).append("' order by ano desc");
        consul.exec(sql.toString());
        return consul;
    }
    
    public Consulta GetLastPeriodosRentasSIINew(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select distinct top 1 ano from eje_ges_certif_rentassii_rentas where numrut='")
        	.append(rut).append("' order by ano desc");
        consul.exec(sql.toString());
        return consul;
    }    
    
    public Consulta GetEmpresasRentasSIINew(String rut,String periodo, String opcion) {
        consul = new Consulta(con);
        String caso=null;
        if(opcion.equals("1")) {
        	caso="S";
        }
        else if(opcion.equals("2")) {
        	caso="H";
        }
        StringBuilder sql = new StringBuilder("select distinct r.numrut_e,e.nombre from eje_ges_certif_rentassii_rentas as r ")
        	.append("inner join eje_ges_certif_rentassii_empresas as e on r.numrut_e=e.numrut where r.numrut='").append(rut)
        	.append("' and r.ano=").append(periodo).append(" and r.caso='").append(caso).append("' order by e.nombre");
        consul.exec(sql.toString());
        return consul;
    }

    public Consulta GetCasosRentasSIINew(String rut, String periodo) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select distinct caso from eje_ges_certif_rentassii_rentas where numrut='")
        	.append(rut).append("' and ano=").append(periodo).append(" ");
        consul.exec(sql.toString());
        return consul;
    }
    
    public Consulta GetIsSARentasSIINew(String empresa) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select sa from eje_ges_certif_rentassii_empresas where cod1=").append(empresa);
        consul.exec(sql.toString());
        return consul;
    }
    
    public Consulta GetEmpresaRut(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select wp_cod_empresa from eje_ges_trabajador where rut=").append(rut);
        consul.exec(sql.toString());
        return consul;
    }    

    public Consulta GetNombreRut(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select nombre from eje_ges_trabajador where rut=").append(rut);
        consul.exec(sql.toString());
        return consul;
    }
    
    public Consulta GetDVRut(String rut) {
        consul = new Consulta(con);
        StringBuilder sql = new StringBuilder("select digito_ver from eje_ges_trabajador where rut=").append(rut);
        consul.exec(sql.toString());
        return consul;
    }

    private Connection con;
    private Consulta consul;
    private String mensajeError;
}