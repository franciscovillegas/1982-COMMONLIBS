package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspComunas extends Proceso
{

    public TraspComunas(Connection conOrigen, Connection conDestino, String periodoOri)
    {
        super(conOrigen, conDestino);
        validar = new Validar();
        this.periodoOri = periodoOri;
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consul = new Consulta(conOrigen);
        Consulta consulTraspaso = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Comunas");
        String sqlDest = "DELETE FROM eje_ges_comunas";
        if(consulTraspaso.insert(sqlDest))
            OutMessage.OutMessagePrint("Datos de destino Borrados");
        else
            OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
        String sqlOrig = "SELECT COD_COMUNA, COMUNA FROM COMUNA";
        OutMessage.OutMessagePrint(" :) ---> Actualizando Comunas \nQuery: " + sqlOrig);
        consul.exec(sqlOrig);
        Tools tool = new Tools();
        int regProcesados = 0;
        int totReg = 0;
        do
        {
            if(!consul.next())
                break;
            totReg++;
            sqlDest = "INSERT INTO eje_ges_comunas (comuna, descrip) VALUES (" + consul.getString("COD_COMUNA") + ", '" + validar.validarDato(consul.getString("COMUNA")) + "')";
            OutMessage.OutMessagePrint("\n Comunas " + validar.validarDato(consul.getString("COMUNA")));
            if(consulTraspaso.insert(sqlDest) && ++regProcesados % 200 == 0)
                System.out.print(".");
        } while(true);
        OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
        consul.close();
        consulTraspaso.close();
        OutMessage.OutMessagePrint("\nFin Comunas");
        return true;
    }

    private String periodoOri;
    Validar validar;
}