package organica.com.eje.ges.gestion.mantencion;

import organica.datos.Consulta;
import java.sql.Connection;
import organica.tools.OutMessage;

public class MakeObs
{

    public MakeObs()
    {
    }

    public boolean getMakeObs(Connection Conexion, String unidad, String rut_emp, String empresa)
    {
        boolean si = false;
        Consulta info = new Consulta(Conexion);
        String consulta = String.valueOf(String.valueOf((new StringBuilder("SELECT trab.rut, trab.nombres,trab.ape_paterno,trab.ape_materno FROM eje_ges_trabajador trab INNER JOIN eje_ges_unidad_rama ur ON trab.empresa = ur.empresa AND trab.unidad = ur.uni_rama WHERE (ur.empresa = '")).append(empresa).append("') AND ").append("(ur.unidad = '").append(unidad).append("') and (ur.uni_rama<> '").append(unidad).append("') AND (ur.tipo = 'R') and trab.rut=").append(rut_emp)));
        OutMessage.OutMessagePrint("---->consulta: ".concat(String.valueOf(String.valueOf(consulta))));
        info.exec(consulta);
        if(info.next())
            si = true;
        return si;
    }
}