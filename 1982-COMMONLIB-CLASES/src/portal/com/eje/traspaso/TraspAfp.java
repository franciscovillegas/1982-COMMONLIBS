package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspAfp extends Proceso
{

    public TraspAfp(Connection conOrigen, Connection conDestino, String periodoOri)
    {
        super(conOrigen, conDestino);
        this.periodoOri = periodoOri;
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consul = new Consulta(conOrigen);
        Consulta consulTraspaso = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\n\nActualizando AFP");
        String sqlDest = "DELETE FROM eje_ges_afp";
        if(consulTraspaso.insert(sqlDest))
            OutMessage.OutMessagePrint("Datos de destino Borrados");
        else
            OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
        String sqlOrig = "SELECT COD_AFP, AFP FROM AFP WHERE COD_VIGENTE = 'S'";
        OutMessage.OutMessagePrint(" :) ---> Actualizando AFP \nQuery: " + sqlOrig);
        consul.exec(sqlOrig);
        int regProcesados = 0;
        int totReg = 0;
        do
        {
            if(!consul.next())
                break;
            totReg++;
            sqlDest = "INSERT INTO eje_ges_afp (afp, descrip) VALUES ('" + consul.getString("COD_AFP") + "', '" + consul.getString("AFP") + "')";
            if(consulTraspaso.insert(sqlDest) && ++regProcesados % 200 == 0)
                System.out.print(".");
        } while(true);
        OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
        consul.close();
        consulTraspaso.close();
        OutMessage.OutMessagePrint("\nFin AFP");
        return true;
    }

    private String periodoOri;
}