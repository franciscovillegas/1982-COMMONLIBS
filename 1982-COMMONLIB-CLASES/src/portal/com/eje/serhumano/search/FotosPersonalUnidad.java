package portal.com.eje.serhumano.search;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import freemarker.template.SimpleList;

public class FotosPersonalUnidad
{

    public FotosPersonalUnidad()
    {
        RutSup = null;
        MisionUnidad = null;
        DescUnidad = null;
    }

    public SimpleList GetFotosPersonalUnidad(Connection Conexion, String unidad, String empresa)
    {
        Consulta Buscar = new Consulta(Conexion);
        String consul = "select max(peri_id) as maxi from eje_ges_periodo ";
        Buscar.exec(consul);
        Buscar.next();
        String periodo = Buscar.getString("maxi");
        consul = String.valueOf(String.valueOf((new StringBuilder("SELECT eje_ges_unidad_encargado.rut_encargado, eje_ges_unidad_encargado.mision, eje_sh_unidades.unid_desc FROM eje_ges_unidad_encargado LEFT OUTER JOIN eje_sh_unidades ON eje_ges_unidad_encargado.unid_empresa = eje_sh_unidades.unid_empresa  AND eje_ges_unidad_encargado.unid_id = eje_sh_unidades.unid_id WHERE (eje_ges_unidad_encargado.unid_id = '")).append(unidad).append("') AND ").append("(eje_ges_unidad_encargado.periodo = ").append(periodo).append(") AND ").append("(eje_ges_unidad_encargado.unid_empresa = '").append(empresa).append("')  ")));
        Buscar.exec(consul);
        OutMessage.OutMessagePrint("------>SELECT Organica\n ".concat(String.valueOf(String.valueOf(consul))));
        consul = String.valueOf(String.valueOf((new StringBuilder("SELECT rtrim(desc_cargo) as desc_cargo,foto,costo ,rut,Rtrim(nombre) as nombre FROM view_dotacion_directa WHERE (empresa = '")).append(empresa).append("') AND (tipo = 'U') AND   (unidad = '").append(unidad).append("') order by costo desc")));
        String strRut = null;
        if(Buscar.next())
        {
            strRut = Buscar.getString("rut_encargado");
            DescUnidad = Buscar.getString("unid_desc");
            MisionUnidad = Buscar.getString("mision");
            RutSup = strRut;
        }
        Buscar.exec(consul);
        return Buscar.getSimpleList();
    }

    public String RutSup;
    public String MisionUnidad;
    public String DescUnidad;
}