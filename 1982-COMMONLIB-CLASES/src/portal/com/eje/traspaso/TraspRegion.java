package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspRegion extends Proceso
{

    public TraspRegion(Connection conOrigen, Connection conDestino, String periodoOri)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consul = new Consulta(conOrigen);
        Consulta consulTraspaso = new Consulta(conDestino);
        OutMessage.OutMessagePrint("Actualizando Regiones");
        String sqlDest = "DELETE FROM eje_ges_regiones";
        if(consulTraspaso.insert(sqlDest))
            OutMessage.OutMessagePrint("Datos de destino Borrados");
        else
            OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
        String sqlOrig = "SELECT COD_REGION, REGION FROM REGION";
        OutMessage.OutMessagePrint(" :) ---> Actualizando Regiones \nQuery: " + sqlOrig);
        consul.exec(sqlOrig);
        int regProcesados = 0;
        int totReg = 0;
        do
        {
            if(!consul.next())
                break;
            totReg++;
            sqlDest = "INSERT INTO eje_ges_regiones (region, descrip) VALUES ('" + consul.getString("COD_REGION") + "', '" + consul.getString("REGION").replaceAll("'", "\264") + "')";
            if(consulTraspaso.insert(sqlDest) && ++regProcesados % 200 == 0)
                System.out.print(".");
        } while(true);
        OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
        consul.close();
        consulTraspaso.close();
        OutMessage.OutMessagePrint("\nFin Regiones");
        return true;
    }

    private String periodoOri;
}