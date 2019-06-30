package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspDependencia extends Proceso
{

    public TraspDependencia(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consulOrig = new Consulta(conDestino);
        Consulta consulTraspaso = new Consulta(conOrigen);
        Consulta consulAuxx = new Consulta(conOrigen);
        OutMessage.OutMessagePrint("Actualizando Dependencias");
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "DELETE FROM eje_ges_dependencia where wp_cod_empresa =" + empresa;
            if(consulOrig.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            String sqlOrig = "SELECT COD_EMPRESA, COD_DEPENDE_DE, DEPENDENCIA FROM DEPENDENCIAS WHERE COD_EMPRESA = " + empresa;
            OutMessage.OutMessagePrint(" :) ---> Actualizando Dependencias \nQuery: " + sqlOrig);
            consulTraspaso.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            Dato dato = new Dato();
            do
            {
                if(!consulTraspaso.next())
                    break;
                totReg++;
                sqlDest = "INSERT INTO eje_ges_dependencia (codigo, descripcion, wp_cod_empresa) VALUES (" + dato.objectToDato(consulTraspaso.getString("COD_DEPENDE_DE")) + ", " + dato.objectToDatoComillas(consulTraspaso.getString("DEPENDENCIA")) + ", " + dato.objectToDato(consulTraspaso.getString("COD_EMPRESA")) + ")";
                if(consulOrig.insert(sqlDest) && ++regProcesados % 200 == 0)
                    System.out.print(".");
            } while(true);
            OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
            consulOrig.close();
            consulTraspaso.close();
            OutMessage.OutMessagePrint("\nFin Dependencias");
        }
        consulAuxx.close();
        return true;
    }

    private String periodoOri;
}