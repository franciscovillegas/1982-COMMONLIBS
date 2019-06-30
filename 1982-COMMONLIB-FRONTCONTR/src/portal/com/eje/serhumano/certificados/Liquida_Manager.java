package portal.com.eje.serhumano.certificados;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.datosdf.datosRut;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;

public class Liquida_Manager
{

    public Liquida_Manager(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }
    
    public int GetCountEmpresasRut(String rut, String periodo) {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("SELECT count(distinct empresa) existe FROM eje_ges_certif_histo_liquidacion_cabecera where rut=").append(rut).append(" and periodo=").append(periodo));
        OutMessage.OutMessagePrint("Empresas del Trabajador Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql); consul.next();
        return consul.getInt("existe");
    }
    
    public Consulta GetSociedadEmpresaRut(String rut, String periodo, String empresa) {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select s.descripcion as nom_sociedad from " +
        		     "eje_ges_certif_histo_liquidacion_cabecera lc inner join eje_ges_sociedad s " +
        		     "on lc.wp_cod_empresa=s.wp_cod_empresa and lc.wp_cod_planta=s.codigo where lc.rut=" + rut + 
        		     " and lc.periodo=" + periodo + " and lc.empresa=" + empresa));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }
    
    public Consulta GetEmpresasRut(String rut, String periodo) {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("SELECT distinct empresa FROM eje_ges_certif_histo_liquidacion_cabecera where rut=").append(rut).append(" and periodo=").append(periodo));
        OutMessage.OutMessagePrint("Empresas del Trabajador Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetCabeceraEmpresaRut(String rut, String periodo, String empresa) {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT empresa.empre_rut, isnull(liq_cab.wp_ndias_lic,0) as wp_ndias_lic, isnull(liq_cab.tramo, 0) as tramo,  isnull(liq_cab.sobregiro,0) as sobregiro, isnull(liq_cab.n_cargas,0) as n_cargas, isnull(liq_cab.base_tribut,0) as base_tribut, isnull(liq_cab.wp_cod_empresa,0) as wp_cod_empresa, liq_cab.wp_afecto_imponible as rta_impon, liq_cab.wp_tot_imponible, liq_cab.wp_cod_planta, liq_cab.wp_ndias_trab as ndias_trab, liq_cab.periodo, liq_cab.empresa,empresa.descrip AS nom_empre, liq_cab.unidad,liq_cab.rut, trab.digito_ver, trab.nombre,liq_cab.causa_pago,liq_cab.fec_pago,liq_cab.forma_pago, liq_cab.cuenta,liq_cab.imp_tribut, liq_cab.imp_no_tribut,liq_cab.no_imp_tribut, liq_cab.no_imp_no_tribut,liq_cab.reliq_rentas, liq_cab.tot_haberes,liq_cab.tot_desctos, liq_cab.liquido,liq_cab.id_forma_pago, liq_cab.tope_imp,liq_cab.val_uf as val_uf,liq_cab.dctos_varios,liq_cab.dctos_legales, liq_cab.dctos_impagos,liq_cab.banco, trab.fecha_ingreso, trab.estado_civil, trab.sexo, cargo.descrip as nom_cargo, trab.fec_ter_cont, trab.valor_adic_1, trab.valor_adic_2, tot_aportaciones=isnull(tot_aportaciones,0) FROM eje_ges_certif_histo_liquidacion_cabecera liq_cab INNER JOIN eje_ges_trabajador trab ON liq_cab.rut = trab.rut INNER JOIN eje_ges_cargos cargo ON trab.cargo = cargo.cargo AND trab.empresa = cargo.empresa INNER JOIN eje_ges_empresa empresa ON liq_cab.empresa = empresa.empresa WHERE (liq_cab.periodo = ")).append(periodo).append(") ").append("AND (liq_cab.wp_cod_empresa = '").append(empresa).append("') ").append("AND (liq_cab.rut = ").append(rut).append(") and trab.empresa=").append(empresa)));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetEmpresaEmpresaRut(String rut, String periodo,String empresa) {
        consul = new Consulta(con);
        String sql = "select e.descrip as nom_empresa, e.empre_dir from eje_ges_certif_histo_liquidacion_cabecera lc " + 
                     "inner join eje_ges_empresa e on lc.empresa=e.empresa where lc.rut=" + rut + " and " +
                     "lc.periodo=" + periodo + " and lc.empresa=" + empresa;
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetEmpresa(String rut, String periodo) {
    	 consul = new Consulta(con);
         String sql = "SELECT emp.descrip as nom_empresa FROM eje_ges_empresa emp, eje_ges_trabajador trab WHERE trab.rut = " + rut;
         
         if(periodo != null) {
        	 sql += " and emp.empresa = trab.empresa "; 
         }
         
         OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
         consul.exec(sql);
         return consul;
    }
    
    /**
     * Malo, debe ser rut, periodo
     * @DEPRECATE
     * @AUTOR FRANCISCO
     * @since 06-DIC-2016
     * */
    public Consulta GetEmpresa(String rut)
    {
    	return GetEmpresa(rut, null);
    }

    /**
     * Malo, debe ser rut, periodo
     * @DEPRECATE
     * @AUTOR FRANCISCO
     * @since 06-DIC-2016
     * */
    public Consulta GetSociedad(String rut) {
    	return GetSociedad(rut, null);
    }
    
    
    public Consulta GetSociedad(String rut, String periodo)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("SELECT soc.descripcion as nom_sociedad FROM eje_ges_sociedad soc, eje_ges_trabajador trab WHERE soc.codigo = trab.sociedad AND soc.wp_cod_empresa = trab.empresa AND trab.rut = " + rut));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetUnidad(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("select u.unid_desc as nom_unidad from eje_ges_trabajador t inner join eje_ges_unidades u on u.unid_id = t.unidad where t.rut = " + rut));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetUnidadLiquidacion(String rut, String peri)
    {
        consul = new Consulta(con); 
        Validar val = new Validar();
        int periodo = val.validarNum(peri,0);
        String sql = String.valueOf(new StringBuilder("SELECT unidad FROM eje_ges_certif_histo_liquidacion_cabecera WHERE periodo=" + periodo + " AND rut=" + rut));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        consul.next();
        return consul;
    }

    public Consulta GetCentroCosto(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("SELECT cc.centro_costo,cc.descrip as nom_centro_costo FROM eje_ges_centro_costo cc, eje_ges_trabajador trab WHERE cc.wp_cod_empresa = trab.empresa AND cc.centro_costo = trab.ccosto AND trab.rut = " + rut));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetAfp(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("SELECT a.descrip as nom_afp FROM eje_ges_afp a, eje_ges_trabajador trab WHERE a.afp = trab.afp AND trab.rut = " + rut));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }
    
    public Consulta GetAfp(String rut, String periodo) {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("SELECT a.descrip as nom_afp FROM eje_ges_afp a inner join eje_ges_certif_histo_liquidacion_cabecera lc on a.afp = lc.afp WHERE lc.rut = " + rut + " and lc.periodo=" + periodo));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetIsapre(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("SELECT i.descrip as nom_isapre FROM eje_ges_isapres i, eje_ges_trabajador trab WHERE i.isapre = trab.isapre AND trab.rut = " + rut));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }
    
    public Consulta GetIsapre(String rut, String periodo) {
        consul = new Consulta(con);
        String sql = String.valueOf(new StringBuilder("SELECT i.descrip as nom_isapre FROM eje_ges_isapres i inner join eje_ges_certif_histo_liquidacion_cabecera lc on i.isapre = lc.isapre WHERE lc.rut = " + rut + " and lc.periodo=" + periodo));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }
  

    public Consulta GetCabecera(String rut)
    {
        consul = new Consulta(con);
        userRut = new datosRut(con, rut);
        peri = getMaxPeriodo();
        empresa = userRut.Empresa;
        unidad = GetUnidadLiquidacion(rut, peri).getString("unidad");
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT liq_cab.wp_cod_empresa, liq_cab.wp_cod_planta,liq_cab.periodo, liq_cab.empresa,empresa.descrip AS nom_empre, liq_cab.unidad,liq_cab.rut, trab.digito_ver, trab.nombre,liq_cab.causa_pago,liq_cab.fec_pago,liq_cab.forma_pago, liq_cab.cuenta,liq_cab.imp_tribut, liq_cab.imp_no_tribut,liq_cab.no_imp_tribut, liq_cab.no_imp_no_tribut,liq_cab.reliq_rentas, liq_cab.tot_haberes,liq_cab.tot_desctos, liq_cab.liquido,liq_cab.id_forma_pago, liq_cab.tope_imp,liq_cab.val_uf,liq_cab.dctos_varios,liq_cab.dctos_legales, liq_cab.dctos_impagos,liq_cab.banco, trab.fecha_ingreso FROM eje_ges_certif_histo_liquidacion_cabecera liq_cab INNER JOIN eje_ges_trabajador trab ON liq_cab.rut = trab.rut AND liq_cab.empresa = trab.empresa INNER JOIN eje_ges_empresa empresa ON liq_cab.empresa = empresa.empresa WHERE (liq_cab.periodo = ")).append(peri).append(") ").append("AND (liq_cab.empresa = '").append(empresa).append("') ").append("AND (liq_cab.unidad = '").append(unidad).append("') ").append("AND (liq_cab.rut = ").append(rut).append(")")));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetCabecera(String rut, String periodo)
    {
        consul = new Consulta(con);
        userRut = new datosRut(con, rut);
        empresa = userRut.Empresa;
        unidad = GetUnidadLiquidacion(rut, periodo).getString("unidad");
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT liq_cab.wp_cod_empresa, liq_cab.wp_afecto_imponible as rta_impon, liq_cab.wp_tot_imponible, liq_cab.wp_cod_planta, liq_cab.wp_ndias_trab as ndias_trab, liq_cab.periodo, liq_cab.empresa,empresa.descrip AS nom_empre, liq_cab.unidad,liq_cab.rut, trab.digito_ver, trab.nombre,liq_cab.causa_pago,liq_cab.fec_pago,liq_cab.forma_pago, liq_cab.cuenta,liq_cab.imp_tribut, liq_cab.imp_no_tribut,liq_cab.no_imp_tribut, liq_cab.no_imp_no_tribut,liq_cab.reliq_rentas, liq_cab.tot_haberes,liq_cab.tot_desctos, liq_cab.liquido,liq_cab.id_forma_pago, liq_cab.tope_imp,liq_cab.val_uf as val_uf,liq_cab.dctos_varios,liq_cab.dctos_legales, liq_cab.dctos_impagos,liq_cab.banco, trab.fecha_ingreso, trab.estado_civil, trab.sexo, cargo.descrip as nom_cargo FROM eje_ges_certif_histo_liquidacion_cabecera liq_cab INNER JOIN eje_ges_trabajador trab ON liq_cab.rut = trab.rut AND liq_cab.empresa = trab.empresa INNER JOIN eje_ges_cargos cargo ON trab.cargo = cargo.cargo AND trab.empresa = cargo.empresa INNER JOIN eje_ges_empresa empresa ON liq_cab.empresa = empresa.empresa WHERE (liq_cab.periodo = ")).append(periodo).append(") ").append("AND (liq_cab.empresa = '").append(empresa).append("') ").append("AND (liq_cab.unidad = '").append(unidad).append("') ").append("AND (liq_cab.rut = ").append(rut).append(")")));
        OutMessage.OutMessagePrint("Cabecera Liquidacion: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetCabeceraAdicional(String rut, String periodo,String tipo_proceso)
    {
        consul = new Consulta(con);
        userRut = new datosRut(con, rut);
        empresa = userRut.Empresa;
        unidad = GetUnidadLiquidacion(rut, periodo).getString("unidad");
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT liq_cab.wp_cod_empresa, liq_cab.wp_afecto_imponible as rta_impon, liq_cab.wp_tot_imponible, liq_cab.wp_cod_planta, liq_cab.wp_ndias_trab as ndias_trab, liq_cab.periodo, liq_cab.empresa,empresa.descrip AS nom_empre, liq_cab.unidad,liq_cab.rut, trab.digito_ver, trab.nombre,liq_cab.causa_pago,liq_cab.fec_pago,liq_cab.forma_pago, liq_cab.cuenta,liq_cab.imp_tribut, liq_cab.imp_no_tribut,liq_cab.no_imp_tribut, liq_cab.no_imp_no_tribut,liq_cab.reliq_rentas, liq_cab.tot_haberes,liq_cab.tot_desctos, liq_cab.liquido,liq_cab.id_forma_pago, liq_cab.tope_imp,liq_cab.val_uf as val_uf,liq_cab.dctos_varios,liq_cab.dctos_legales, liq_cab.dctos_impagos,liq_cab.banco, trab.fecha_ingreso, trab.estado_civil, trab.sexo, cargo.descrip as nom_cargo FROM eje_ges_certif_histo_liquidacion_cabecera_adic liq_cab INNER JOIN eje_ges_trabajador trab ON liq_cab.rut = trab.rut AND liq_cab.empresa = trab.empresa INNER JOIN eje_ges_cargos cargo ON trab.cargo = cargo.cargo AND trab.empresa = cargo.empresa INNER JOIN eje_ges_empresa empresa ON liq_cab.empresa = empresa.empresa WHERE (liq_cab.periodo = ")).append(periodo).append(") ").append("AND (liq_cab.empresa = '").append(empresa).append("') ").append("AND (liq_cab.rut = ").append(rut).append(") AND (liq_cab.tipo_proceso = '").append(tipo_proceso).append("')")));
        OutMessage.OutMessagePrint("Cabecera Liquidacion Adicional: ".concat(String.valueOf(String.valueOf(sql))));
        System.out.println("Cabecera Liquidacion Adicional: ".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public String getMaxPeriodo()
    {
        consul = new Consulta(con);
        String periodo = "";
        String sql = "select  peri_id, peri_mes, peri_ano from eje_ges_periodo where peri_id = (select max(peri_id) as a from eje_ges_periodo)";
        consul.exec(sql);
        if(consul.next())
            periodo = consul.getString("peri_id");
        return periodo;
    }

    public String getError()
    {
        return mensajeError;
    }

    public String getPeriodo()
    {
        return peri;
    }

    public String getEmpresa()
    {
        return empresa;
    }

    public String getUnidad()
    {
        return unidad;
    }

    public String getIsapre()
    {
        return isapre;
    }

    public String getAfp()
    {
        return afp;
    }

    public String getCentroCosto()
    {
        return centro_costo;
    }

    private Connection con;
    private Consulta consul;
    private String mensajeError;
    private datosRut userRut;
    private String peri;
    private String unidad;
    private String sociedad;
    private String empresa;
    private String centro_costo;
    private String afp;
    private String isapre;
}