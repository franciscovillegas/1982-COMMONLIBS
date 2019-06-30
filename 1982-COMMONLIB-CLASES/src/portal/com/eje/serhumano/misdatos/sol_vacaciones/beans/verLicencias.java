// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:03:09
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   verLicencias.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones.beans;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import java.sql.Connection;

public class verLicencias
{

    public verLicencias()
    {
        con_licencia = false;
        desde = "";
        hasta = "";
    }

    public verLicencias(Connection connection, String s)
    {
        Validar validar = new Validar();
        if(connection != null)
        {
            Consulta consulta = new Consulta(connection);
            String s1 = String.valueOf(String.valueOf((new StringBuilder("SELECT rut,fecha_inicio as desde,fecha_termino as hasta,dias FROM eje_ges_licencias_medicas WHERE (rut = ")).append(s).append(") ").append("AND (YEAR(fecha_inicio) = YEAR(GETDATE())) ").append("AND (GETDATE() >= fecha_inicio) ").append("AND (GETDATE() <= fecha_termino)")));
            OutMessage.OutMessagePrint("---->Ver si esta con Licencia:\n".concat(String.valueOf(String.valueOf(s1))));
            consulta.exec(s1);
            if(consulta.next())
            {
                OutMessage.OutMessagePrint("---->esta con Licencia");
                con_licencia = true;
                desde = validar.validarFecha(consulta.getValor("desde"));
                hasta = validar.validarFecha(consulta.getValor("hasta"));
            } else
            {
                OutMessage.OutMessagePrint("---->NO esta con Licencia");
            }
        } else
        {
            OutMessage.OutMessagePrint("en Class verVacaciones : Conexion es null ");
        }
    }

    public boolean conLicencia()
    {
        return con_licencia;
    }

    public String getInicioLicencia()
    {
        return desde;
    }

    public String getFinLicencia()
    {
        return hasta;
    }

    private boolean con_licencia;
    private String desde;
    private String hasta;
}