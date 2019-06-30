package portal.com.eje.serhumano.unidad;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

public class RegionUnidad
{

    public RegionUnidad(Connection conex)
    {
        con = conex;
        mensajeError = "";
        tot_costos = "";
        tot_empleados = "";
        tot_unidades = "";
        regiond = "";
    }

    public Consulta ConsulUnidad(String region)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT uni_empresa, uni_id, id_region, vigente, unidad, region FROM view_region_unidad WHERE (vigente = 'S') AND (id_region = '")).append(region).append("')  ORDER BY unidad")));
        OutMessage.OutMessagePrint("SQL POR REGION ".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulUnidad(String region, String empresa)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT uni_empresa, uni_id, id_region, vigente, unidad, region FROM view_region_unidad WHERE (vigente = 'S') AND (id_region = '")).append(region).append("') and uni_empresa='").append(empresa).append("'  ORDER BY unidad")));
        OutMessage.OutMessagePrint("SQL FINI ".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulEmpresas()
    {
        String sql = "SELECT     empresa as id, descrip as empresa FROM         eje_ges_empresa ORDER BY orden, descrip";
        OutMessage.OutMessagePrint("SQL ConsulEmpresas ".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulUnidad(String region, String empresa, String unidad_nombre)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT uni_empresa, uni_id, id_region, vigente, unidad, region FROM view_region_unidad WHERE (vigente = 'S') AND (id_region = '")).append(region).append("') and uni_empresa='").append(empresa).append("'")));
        if(unidad_nombre != null)
            sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(" and (unidad LIKE '%").append(unidad_nombre).append("%')")));
        sql = String.valueOf(String.valueOf(sql)).concat("  ORDER BY unidad");
        OutMessage.OutMessagePrint("SQL FINI ".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulRegion()
    {
        String sql = "SELECT region, descrip FROM  eje_ges_regiones";
        OutMessage.OutMessagePrint("SQL FINI ".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulColaboradores(Connection condf, String unidad, String empresa)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, digito_ver, nombre, unidad, empresa FROM eje_ges_trabajador  where unidad='")).append(unidad).append("' and empresa='").append(empresa).append("' ORDER BY nombre")));
        OutMessage.OutMessagePrint("SQL FINI ".concat(String.valueOf(String.valueOf(sql))));
        Consulta consul2 = new Consulta(condf);
        consul2.exec(sql);
        return consul2;
    }

    public Consulta ConsulColaboradores(Connection condf, String unidad, String empresa, String nombres)
    {
        String var = "";
        String order = "  ORDER BY nombre";
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, digito_ver, nombre, unidad, empresa FROM eje_ges_trabajador  where unidad='")).append(unidad).append("' and empresa='").append(empresa).append("' ")));
        if(nombres != null && !"".equals(nombres))
            var = String.valueOf(String.valueOf((new StringBuilder("and nombre like%'")).append(nombres).append("'% ")));
        sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(var).append(order)));
        OutMessage.OutMessagePrint("SQL FINI ".concat(String.valueOf(String.valueOf(sql))));
        Consulta consul2 = new Consulta(condf);
        consul2.exec(sql);
        return consul2;
    }

    public Consulta ConsulColaboradores(Connection condf, String unidad, String empresa, String nombres, String region)
    {
        String var = "";
        String order = "  ORDER BY nombre";
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, digito_ver, nombre, unidad, empresa FROM eje_ges_trabajador  where unidad='")).append(unidad).append("' and empresa='").append(empresa).append("' ")));
        if(nombres != null && !"".equals(nombres))
            var = String.valueOf(String.valueOf((new StringBuilder("and nombre like%'")).append(nombres).append("'% ")));
        sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(var).append(order)));
        OutMessage.OutMessagePrint("SQL FINI ".concat(String.valueOf(String.valueOf(sql))));
        Consulta consul2 = new Consulta(condf);
        consul2.exec(sql);
        return consul2;
    }

    public Consulta ConsulColaboradores(Connection condf, String unidad)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, digito_ver, nombre, unidad, empresa FROM eje_ges_trabajador  where unidad='")).append(unidad).append("'  ORDER BY nombre")));
        consul = new Consulta(condf);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulColaboradores(Connection condf, String unidad, String nombres, int valorpaso)
    {
        String var = "";
        String order = "  ORDER BY nombre";
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, digito_ver, nombre, unidad, empresa FROM eje_ges_trabajador  where unidad='")).append(unidad).append("' ")));
        if(nombres != null && !"".equals(nombres))
            var = String.valueOf(String.valueOf((new StringBuilder("and nombre like%'")).append(nombres).append("'% ")));
        sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(var).append(order)));
        OutMessage.OutMessagePrint("SQL FINI ".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(condf);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulColaboradores(String unidad, String nombres, String region, int valorpaso)
    {
        String var = "";
        String order = " ORDER BY  nom  ";
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT  rut, dv,  uni_id,  nom  FROM   view_dotacion_region WHERE     (id_region = '")).append(region).append("')")));
        if(nombres != null && !"".equals(nombres))
            var = String.valueOf(String.valueOf((new StringBuilder("AND nom LIKE '%")).append(nombres).append("%' ")));
        sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(var).append(order)));
        OutMessage.OutMessagePrint("SQL Empleados Region\n".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public Consulta ConsulColaboradores(String unidad, String nombres, String region, int valorpaso, String empresa)
    {
        String var = "";
        String order = " ORDER BY  nom  ";
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, dv,  uni_id,  nom  FROM view_dotacion_region WHERE (id_region = '")).append(region).append("') ").append("and (uni_empresa='").append(empresa).append("') ")));
        if(nombres != null && !"".equals(nombres))
            var = String.valueOf(String.valueOf((new StringBuilder("AND nom LIKE '%")).append(nombres).append("%' ")));
        sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append(var).append(order)));
        OutMessage.OutMessagePrint("SQL Empleados Empresa\n".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(con);
        consul.exec(sql);
        return consul;
    }

    public void GetDatosRegion(String region)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT cant_suc, cant_colab, cost_total, descrip as region FROM eje_ges_regiones WHERE (region = '")).append(region).append("')")));
        OutMessage.OutMessagePrint("SQL FINI ".concat(String.valueOf(String.valueOf(sql))));
        consul = new Consulta(con);
        consul.exec(sql);
        if(consul.next())
        {
            tot_unidades = consul.getString(1);
            tot_empleados = consul.getString(2);
            tot_costos = consul.getString(3);
            regiond = consul.getString(4);
            if(tot_unidades == null)
                tot_unidades = "";
            if(tot_unidades == null)
                tot_empleados = "";
            if(tot_unidades == null)
                tot_costos = "";
            if(regiond == null)
                regiond = "";
        }
    }

    public String getTotalUnidades()
    {
        return tot_unidades;
    }

    public String getTotalEmpleaos()
    {
        return tot_empleados;
    }

    public String getTotalCosto()
    {
        return tot_costos;
    }

    public String getRegion()
    {
        return regiond;
    }

    private Connection con;
    private String mensajeError;
    private Consulta consul;
    private String tot_unidades;
    private String tot_empleados;
    private String tot_costos;
    private String regiond;
}