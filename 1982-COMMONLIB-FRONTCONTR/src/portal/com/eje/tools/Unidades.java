package portal.com.eje.tools;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import freemarker.template.SimpleList;

public class Unidades
{

    public Unidades()
    {
    }

    public String getNombreUnidad(Connection conn, String empresa, String unidad)
    {
        String unid = "";
        Consulta unidades = new Consulta(conn);
        String sql = "SELECT DISTINCT unid_id, unid_desc, empresa FROM view_busqueda WHERE  (empresa = '" + empresa + "') and unid_id='" + unidad + "'";
        unidades.exec(sql);
        if(unidades.next())
            unid = unidades.getString("unid_desc");
        return unid;
    }

    public SimpleList getUnidades(Connection conn, String empresa)
    {
        Consulta unidades = new Consulta(conn);
        String sql = "SELECT unid_empresa AS empresa, unid_id, unid_desc FROM eje_sh_unidades WHERE (unid_empresa = '" + empresa + "') and vigente='S' " + " ORDER BY unid_desc";
        unidades.exec(sql);
        return unidades.getSimpleList();
    }

    public SimpleList getUnidades(Connection conn, String empresa, String unidad, String tipo)
    {
        Consulta unidades = new Consulta(conn);
        String sql = "SELECT DISTINCT unid_id, unid_desc, tipo, empresa FROM view_busqueda WHERE (empresa = '" + empresa + "') AND (tipo = '" + tipo + "') and (unidad = '" + unidad + "')";
        unidades.exec(sql);
        return unidades.getSimpleList();
    }
}