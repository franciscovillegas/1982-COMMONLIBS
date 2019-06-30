package portal.com.eje.serhumano.search;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;

public class Welcome
{

    public Welcome(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }

    public Consulta ConsulWelcome()
    {
        String sql = "SELECT tr.rut AS rut, tr.digito_ver AS dig, tr.nombre AS nom,  tr.fecha_ingreso, YEAR(tr.fecha_ingreso) AS fec_agno, MONTH(tr.fecha_ingreso) AS fec_mes, DAY(tr.fecha_ingreso) AS fec_dia,  uni.unid_desc AS uni, tr.e_mail AS email, anexos.anexo AS anex  FROM eje_ges_trabajador tr INNER JOIN  eje_sh_unidades uni ON tr.empresa = uni.unid_empresa AND tr.unidad = uni.unid_id LEFT OUTER JOIN  eje_ges_trabajadores_anexos anexos ON tr.rut = anexos.rut  WHERE (MONTH(tr.fecha_ingreso) = MONTH(GETDATE())) AND (YEAR(tr.fecha_ingreso) = YEAR(GETDATE())) ";
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulWelcomePeriodo(String periodo)
    {
        String sql = "SELECT t.nombre as nom, t.rut, t.wp_cod_empresa, t.wp_cod_planta, un.unid_desc AS uni FROM eje_ges_trabajador as t, eje_sh_unidades un WHERE un.unid_id = t.unidad AND un.unid_empresa = t.empresa AND t.rut NOT IN (SELECT rut FROM eje_ges_trabajador_historia WHERE periodo = " + periodo + ") order by t.nombre";
        System.err.println("SQL  ".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    private Connection con;
    private String mensajeError;
    private Consulta consul;
}