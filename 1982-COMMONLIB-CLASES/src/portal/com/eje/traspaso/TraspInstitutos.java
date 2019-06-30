package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspInstitutos extends Proceso
{

    public TraspInstitutos(Connection conOrigen, Connection conDestino, String periodoOri)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Tools tool = new Tools();
        Consulta consul = new Consulta(conOrigen);
        Consulta consulTraspaso = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Institutos");
        String sqlDest = "DELETE FROM eje_ges_institutos ";
        if(consulTraspaso.insert(sqlDest))
            OutMessage.OutMessagePrint("\nDatos de destino Borrados");
        else
            OutMessage.OutMessagePrint("\nError al intentar borrar datos de destino");
        String sqlOrig = "SELECT COD_INSTIT_ESTUDIO, INSTITUCION_ESTUDI FROM INSTITUCION_ESTUDI";
        OutMessage.OutMessagePrint(" :) ---> Actualizando Institutos \nQuery: " + sqlOrig);
        consul.exec(sqlOrig);
        int regProcesados = 0;
        int totReg = 0;
        do
        {
            if(!consul.next())
                break;
            totReg++;
            sqlDest = "INSERT INTO eje_ges_institutos (instituto, descrip) VALUES ('" + consul.getString("COD_INSTIT_ESTUDIO") + "', '" + Tools.remplaza(consul.getString("INSTITUCION_ESTUDI"), "'", "''") + "')";
            if(consulTraspaso.insert(sqlDest) && ++regProcesados % 200 == 0)
                System.out.print(".");
        } while(true);
        OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
        consul.close();
        consulTraspaso.close();
        OutMessage.OutMessagePrint("\nFin Institutos");
        return true;
    }

    private String periodoOri;
}