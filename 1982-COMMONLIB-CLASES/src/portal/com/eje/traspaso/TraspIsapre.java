package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspIsapre extends Proceso
{

    public TraspIsapre(Connection conOrigen, Connection conDestino, String periodoOri)
    {
        super(conOrigen, conDestino);
        this.periodoOri = periodoOri;
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consul = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando ISAPRE");
        String sqlDest = "DELETE FROM eje_ges_isapres";
        if(consulDest.insert(sqlDest))
            OutMessage.OutMessagePrint("Datos de destino Borrados");
        else
            OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
        String sqlOrig = "SELECT COD_ISAPRE, ISAPRE FROM ISAPRE WHERE COD_VIGENCIA = 'S'";
        OutMessage.OutMessagePrint(" ---> Actualizando ISAPRE \nQuery: " + sqlOrig);
        consul.exec(sqlOrig);
        int regProcesados = 0;
        int totReg = 0;
        do
        {
            if(!consul.next())
                break;
            totReg++;
            sqlDest = "INSERT INTO eje_ges_isapres (isapre, descrip) VALUES ('" + consul.getString("COD_ISAPRE") + "','" + consul.getString("ISAPRE") + "')";
            if(consulDest.insert(sqlDest) && ++regProcesados % 10 == 0)
                System.out.print(".");
        } while(true);
        OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
        consul.close();
        consulDest.close();
        OutMessage.OutMessagePrint("\nFin ISAPRE");
        return true;
    }

    private String periodoOri;
}