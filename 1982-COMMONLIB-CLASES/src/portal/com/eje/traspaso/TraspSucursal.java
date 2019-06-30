package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspSucursal extends Proceso
{

    public TraspSucursal(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consulOrig = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Sucursales");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "DELETE FROM eje_ges_sucursal where wp_cod_empresa = " + empresa;
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            String sqlOrig = "SELECT COD_EMPRESA, COD_PLANTA, COD_SUCURSAL, SUCURSAL FROM SUCURSAL WHERE COD_VIGENTE = 'S' AND COD_EMPRESA = " + empresa;
            OutMessage.OutMessagePrint(" :) ---> Actualizando Sucursales \nQuery: " + sqlOrig);
            consulOrig.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            do
            {
                if(!consulOrig.next())
                    break;
                totReg++;
                sqlDest = "INSERT INTO eje_ges_sucursal (codigo, descripcion, wp_cod_empresa, wp_cod_planta) VALUES (" + consulOrig.getString("COD_SUCURSAL") + ", " + "'" + consulOrig.getString("SUCURSAL") + "'" + ", " + consulOrig.getString("COD_EMPRESA") + ", " + consulOrig.getString("COD_PLANTA") + ")";
                if(consulDest.insert(sqlDest) && ++regProcesados % 200 == 0)
                    System.out.print(".");
            } while(true);
            OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
            consulOrig.close();
            consulDest.close();
            OutMessage.OutMessagePrint("\nFin Sucursales");
        }
        consulAuxx.close();
        return true;
    }

    private String periodoOri;
}