package portal.com.eje.serhumano.tracking;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

public class informeManager
{

    public informeManager(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }

    public String getError()
    {
        return mensajeError;
    }

    public Consulta consulta1()
    {
        consul = new Consulta(con);
        String sql = "SELECT t.rut AS rut, COUNT(*) AS num_vis, u.ape_paterno, u.ape_materno, u.nombres FROM eje_ges_tracking t INNER JOIN eje_ges_trabajador u ON u.rut = t.rut GROUP BY t.rut,u.ape_paterno, u.ape_materno, u.nombres ORDER BY u.ape_paterno, u.ape_materno, u.nombres";
        OutMessage.OutMessagePrint("Cumple: " + sql);
        consul.exec(sql);
        return consul;
    }

    private Connection con;
    private Consulta consul;
    private String mensajeError;
}