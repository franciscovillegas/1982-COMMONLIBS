package portal.com.eje.serhumano.misdatos;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;

public class Prev_Manager
{

    public Prev_Manager(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }

    public Consulta GetDataPrevisional(String rut)
    {
        consul = new Consulta(con);
        String sql = "select nombre,afp,fec_afi_sist,fec_ing_afp,cargo,cot_afp,cot_adic,mo_cot_adic,ah_volunt,mo_ah_volunt,jubilado,dep_conven,mon_dep_conven,afp_histo,id_foto,isapre,fec_ing_isap,plan_salud,fec_con_salud,venc_salud,mon_salud,cot_salud,adic_salud,isap_histo,cambio_plan,e_mail,anexo,unidad from view_InfoRut where rut=".concat(String.valueOf(String.valueOf(rut)));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDataPrevisional(String rut, String empresa, String unidad)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT TOP 12 periodo, glosa_descuento, val_descuento FROM view_liquidacion_descuentos WHERE (rut = ")).append(rut).append(") ").append("AND (orden = 30 OR orden = 50) ").append("AND (empresa = '").append(empresa).append("') ").append("AND (unidad = '").append(unidad).append("') ").append("ORDER BY periodo DESC")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDataFondoPension(String rut, String empresa, String unidad, String periodo)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo, glosa_descuento, val_descuento FROM view_liquidacion_descuentos WHERE (rut = ")).append(rut).append(") and (periodo=").append(periodo).append(") ").append("AND (orden = 30) ").append("AND (empresa = '").append(empresa).append("') ").append("AND (unidad = '").append(unidad).append("') ").append("ORDER BY periodo DESC")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDataSalud(String rut, String empresa, String unidad, String periodo)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT periodo, glosa_descuento, val_descuento FROM view_liquidacion_descuentos WHERE (rut = ")).append(rut).append(") and (periodo=").append(periodo).append(") ").append("AND (orden = 50) ").append("AND (empresa = '").append(empresa).append("') ").append("AND (unidad = '").append(unidad).append("') ").append("ORDER BY periodo DESC")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetPeriodos(String rut, String empresa, String unidad)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT distinct top 12 periodo,unidad,empresa FROM view_liquidacion_descuentos WHERE (rut = ")).append(rut).append(") ").append("AND (empresa = '").append(empresa).append("') ").append("AND (unidad = '").append(unidad).append("') ").append("ORDER BY periodo DESC")));
        System.err.println("GetPeriodos\n".concat(String.valueOf(String.valueOf(sql))));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetCargas(String rut)
    {
        consul = new Consulta(con);
        String sql = "select count(nombre) as cargas from eje_ges_grupo_familiar where rut=".concat(String.valueOf(String.valueOf(rut)));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetDetalleCargas(String rut)
    {
        consul = new Consulta(con);
        String sql = "select nombre,parentesco from eje_ges_grupo_familiar where (es_carga = 'S') and rut=".concat(String.valueOf(String.valueOf(rut)));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetHistoAFP(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select * from eje_ges_historico_afp where rut=")).append(rut).append(" order by fecha_ing desc")));
        consul.exec(sql);
        return consul;
    }

    public Consulta GetHistoIsapre(String rut)
    {
        consul = new Consulta(con);
        String sql = String.valueOf(String.valueOf((new StringBuilder("select * from eje_ges_historico_isapre where rut=")).append(rut).append(" order by fecha_ing")));
        consul.exec(sql);
        return consul;
    }

    public String getError()
    {
        return mensajeError;
    }

    private Connection con;
    private Consulta consul;
    private String mensajeError;
}