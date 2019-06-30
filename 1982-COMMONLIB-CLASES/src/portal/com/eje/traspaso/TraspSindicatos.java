package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspSindicatos extends Proceso
{

    public TraspSindicatos(Connection conOrigen, Connection conDestino, String periodoOri)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Dato dato = new Dato();
        Consulta consulOri = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Sindicatos");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "DELETE FROM eje_ges_sindicatos WHERE wp_cod_empresa = " + empresa;
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("datos de eje_ges_sindicatos eliminados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            String sqlOrig = "SELECT COD_EMPRESA, COD_PLANTA, COD_SINDICATO, SINDICATO FROM SINDICATO WHERE COD_EMPRESA = " + empresa;
            OutMessage.OutMessagePrint(" :) ---> Actualizando Sindicatos \nQuery: " + sqlOrig);
            consulOri.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            do
            {
                if(!consulOri.next())
                    break;
                totReg++;
                sqlDest = "INSERT INTO eje_ges_sindicatos (sindicato, descrip, wp_cod_empresa, wp_cod_planta) VALUES (" + dato.objectToDato(consulOri.getString("COD_SINDICATO")) + ", " + dato.objectToDatoComillas(consulOri.getString("SINDICATO")) + ", " + dato.objectToDato(consulOri.getString("COD_EMPRESA")) + ", " + dato.objectToDato(consulOri.getString("COD_PLANTA")) + ")";
                if(consulDest.insert(sqlDest) && ++regProcesados % 200 == 0)
                    System.out.print(".");
            } while(true);
            consulOri.close();
            consulDest.close();
            OutMessage.OutMessagePrint("Registros Procesados (" + regProcesados + " de " + totReg + ")");
        }
        consulAuxx.close();
        return true;
    }

    private String periodoOri;
}