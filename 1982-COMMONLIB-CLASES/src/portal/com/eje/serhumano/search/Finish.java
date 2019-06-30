package portal.com.eje.serhumano.search;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import portal.com.eje.datos.Consulta;

public class Finish
{

    public Finish(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }

    public Consulta ConsulFinish()
    {
        String strFechaHoy = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd,yyyy");
        GregorianCalendar Fecha = new GregorianCalendar();
        int mes = Fecha.get(3);
        String sql = "SELECT ret.nombre AS nom, MONTH(ret.fec_retiro) AS fec, un.unid_desc AS uni, MONTH(GETDATE()) AS mes FROM         dbo.eje_sh_unidades un INNER JOIN     dbo.eje_ges_personal_retirado ret ON un.unid_id = ret.unidad AND un.unid_empresa = ret.empresa WHERE     (MONTH(ret.fec_retiro) = MONTH(DATEDIFF(m, 1, GETDATE()))) ";
        if(mes == 1)
            sql = String.valueOf(String.valueOf(sql)).concat("AND (YEAR(ret.fec_retiro) = YEAR(GETDATE())-1)");
        else
            sql = String.valueOf(String.valueOf(sql)).concat("AND (YEAR(ret.fec_retiro) = YEAR(GETDATE()))");
        System.err.println("SQL FINI ".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulFinishPeriodo(String periodo)
    {
        String sql = "SELECT t.nombre as nom, t.rut, t.wp_cod_empresa, t.wp_cod_planta, un.unid_desc AS uni FROM eje_ges_trabajador_historia as t, eje_sh_unidades un WHERE un.unid_id = t.unidad AND un.unid_empresa = t.empresa AND t.rut NOT IN (SELECT rut FROM eje_ges_trabajador) AND t.periodo = " + periodo + " order by t.nombre";
        System.err.println("SQL FINI ".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulFinishPeriodo(String periodo_anterior, String periodo_actual)
    {
        String sql = "SELECT t.nombre as nom, t.rut, t.wp_cod_empresa, t.wp_cod_planta, un.unid_desc AS uni FROM eje_ges_trabajador_historia as t, eje_sh_unidades un WHERE un.unid_id = t.unidad AND un.unid_empresa = t.empresa AND t.rut NOT IN (SELECT rut FROM eje_ges_trabajador) AND t.periodo = " + periodo_anterior + " order by t.nombre";
        System.err.println("SQL FINI ".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    private Connection con;
    private String mensajeError;
    private Consulta consul;
}