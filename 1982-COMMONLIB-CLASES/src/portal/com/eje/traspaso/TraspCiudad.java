package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspCiudad extends Proceso
{

    public TraspCiudad(Connection conOrigen, Connection conDestino, String periodoOri)
    {
        super(conOrigen, conDestino);
        this.periodoOri = periodoOri;
    }

    public boolean Run(int empresa, int periodo)
    {
        Tools tool = new Tools();
        Consulta consul = new Consulta(conOrigen);
        Consulta consulTraspaso = new Consulta(conDestino);
        OutMessage.OutMessagePrint("Actualizando Ciudades");
        String sqlDest = "DELETE FROM eje_ges_ciudades";
        if(consulTraspaso.insert(sqlDest))
            OutMessage.OutMessagePrint("Datos de destino Borrados");
        else
            OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
        String sqlOrig = "SELECT COD_CIUDAD, CIUDAD FROM CIUDAD";
        OutMessage.OutMessagePrint(" :) ---> Actualizando Ciudades \nQuery: " + sqlOrig);
        consul.exec(sqlOrig);
        int regProcesados = 0;
        int totReg = 0;
        do
        {
            if(!consul.next())
                break;
            totReg++;
            sqlDest = "INSERT INTO eje_ges_ciudades (ciudad, descrip) VALUES (" + consul.getString("COD_CIUDAD") + ", '" + consul.getString("CIUDAD").replaceAll("'", "\264") + "')";
            if(consulTraspaso.insert(sqlDest) && ++regProcesados % 200 == 0)
                System.out.print(".");
        } while(true);
        OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
        consul.close();
        consulTraspaso.close();
        OutMessage.OutMessagePrint("Fin Ciudades");
        return true;
    }

    private String periodoOri;
}