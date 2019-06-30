// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:03:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   verVacaciones.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones.beans;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import java.sql.Connection;

public class verVacaciones
{

    public verVacaciones()
    {
        de_vaca = false;
        desde = "";
        hasta = "";
    }

    public verVacaciones(Connection connection, String s)
    {
        String s1 = "";
        Validar validar = new Validar();
        if(connection != null)
        {
            Consulta consulta = new Consulta(connection);
            String s2 = String.valueOf(String.valueOf((new StringBuilder("SELECT rut, periodo, desde, hasta FROM eje_ges_vacaciones_det WHERE (rut = ")).append(s).append(") ").append("AND (YEAR(desde) = YEAR(GETDATE())) ").append("AND (YEAR(hasta) = YEAR(GETDATE())) ").append("AND (GETDATE() >= desde) ").append("AND (GETDATE() <= hasta)")));
            OutMessage.OutMessagePrint("---->Ver si esta de Vacaciones:\n".concat(String.valueOf(String.valueOf(s2))));
            consulta.exec(s2);
            if(consulta.next())
            {
                OutMessage.OutMessagePrint("---->esta de Vacaciones");
                de_vaca = true;
                desde = validar.validarFecha(consulta.getValor("desde"));
                hasta = validar.validarFecha(consulta.getValor("hasta"));
            } else
            {
                OutMessage.OutMessagePrint("---->NO esta de Vacaciones");
            }
        } else
        {
            OutMessage.OutMessagePrint("en Class verVacaciones : Conexion es null ");
        }
    }

    public boolean deVacaciones()
    {
        return de_vaca;
    }

    public String getInicioVaca()
    {
        return desde;
    }

    public String getFinVaca()
    {
        return hasta;
    }

    private boolean de_vaca;
    private String desde;
    private String hasta;
}