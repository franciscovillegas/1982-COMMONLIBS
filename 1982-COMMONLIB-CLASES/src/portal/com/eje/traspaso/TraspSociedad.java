package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspSociedad extends Proceso
{

    public TraspSociedad(Connection conOrigen, Connection conDestino, String periodoOri)
    {
        super(conOrigen, conDestino);
        this.periodoOri = periodoOri;
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consul = new Consulta(conOrigen);
        Consulta consulTraspaso = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Sociedades/Plantas");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "DELETE FROM eje_ges_sociedad where wp_cod_empresa = " + empresa;
            if(consulTraspaso.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            String sqlOrig = "SELECT COD_EMPRESA, COD_PLANTA, PLANTA, DIRECCION, COD_COMUNA, FONO_PLANTA, FAX_PLANTA FROM PLANTA WHERE COD_VIGENTE = 'S' AND COD_EMPRESA = " + empresa;
            OutMessage.OutMessagePrint(" :) ---> Actualizando Sociedades/Plantas \nQuery: " + sqlOrig);
            consul.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            do
            {
                if(!consul.next())
                    break;
                totReg++;
                sqlDest = "INSERT INTO eje_ges_sociedad (codigo, descripcion, direccion, fono, fax, wp_cod_empresa, wp_cod_comuna) VALUES (" + consul.getString("COD_PLANTA") + ", " + "'" + consul.getString("PLANTA") + "'" + ", " + "'" + consul.getString("DIRECCION") + "'" + ", " + "'" + consul.getString("FONO_PLANTA") + "'" + ", " + "'" + consul.getString("FAX_PLANTA") + "'" + ", " + consul.getString("COD_EMPRESA") + ", " + consul.getString("COD_COMUNA") + ")";
                if(consulTraspaso.insert(sqlDest) && ++regProcesados % 200 == 0)
                    System.out.print(".");
            } while(true);
            OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
            consul.close();
            consulTraspaso.close();
            OutMessage.OutMessagePrint("\nFin Sociedades/Pantas");
        }
        consulAuxx.close();
        return true;
    }

    private String periodoOri;
}