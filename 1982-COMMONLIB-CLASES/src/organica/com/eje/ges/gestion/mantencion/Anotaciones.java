package organica.com.eje.ges.gestion.mantencion;

import organica.datos.Consulta;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import java.sql.Connection;
import organica.tools.OutMessage;
import organica.tools.Validar;

public class Anotaciones
{

    public Anotaciones()
    {
    }

    public SimpleList getAnotaciones(Connection Conexion, String rut, String empresa, int filtro)
    {
        Validar valida = new Validar();
        SimpleList simplelist = new SimpleList();
        Consulta info = new Consulta(Conexion);
        String consulta = null;
        if(filtro == 0)
            consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT fecha,tipo_obs,observacion,nombre from view_hoja_vida where rut=")).append(rut).append(" and empresa='").append(empresa).append("' order by codigo ")));
        else
            consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT fecha,tipo_obs,observacion,nombre from view_hoja_vida where rut=")).append(rut).append(" and empresa='").append(empresa).append("' and tipo_obs=").append(filtro).append(" order by codigo ")));
        OutMessage.OutMessagePrint("---->consulta: ".concat(String.valueOf(String.valueOf(consulta))));
        info.exec(consulta);
        SimpleHash simplehash1;
        for(; info.next(); simplelist.add(simplehash1))
        {
            simplehash1 = new SimpleHash();
            simplehash1.put("fecha", valida.validarFecha(info.getString("fecha")));
            simplehash1.put("tipo_obs", info.getString("tipo_obs"));
            simplehash1.put("obs", info.getString("observacion"));
            simplehash1.put("nombre", info.getString("nombre"));
        }

        info.close();
        return simplelist;
    }

    public SimpleList getAnotacionesAll(Connection Conexion, String rut, String empresa)
    {
        Validar valida = new Validar();
        Consulta info = new Consulta(Conexion);
        String consulta = "SELECT tipo_obs,nombre from eje_ges_hoja_vida_tipoobs ";
        info.exec(consulta);
        SimpleList simplelist1 = new SimpleList();
        SimpleHash simplehash1 = new SimpleHash();
        int x = 0;
        simplehash1.put("varios", getAnotaciones(Conexion, rut, empresa, 0));
        simplehash1.put("nombre", "Todas la Anotaciones");
        simplehash1.put("Layer", "0");
        simplelist1.add(simplehash1);
        for(; info.next(); simplelist1.add(simplehash1))
        {
            x++;
            simplehash1 = new SimpleHash();
            simplehash1.put("varios", getAnotaciones(Conexion, rut, empresa, info.getInt("tipo_obs")));
            simplehash1.put("nombre", info.getString("nombre"));
            simplehash1.put("Layer", (new Integer(x)).toString());
        }

        return simplelist1;
    }
}