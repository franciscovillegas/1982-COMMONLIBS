package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspEmpresa extends Proceso
{

    public TraspEmpresa(Connection conOrigen, Connection conDestino, String periodoOri)
    {
        super(conOrigen, conDestino);
        this.periodoOri = periodoOri;
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consul = new Consulta(conOrigen);
        Consulta consulTraspaso = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Empresas");
        String sqlOrig = "SELECT DISTINCT EMP.COD_EMPRESA, EMP.EMPRESA, EMP.RUT_EMPRESA, EMP.DV_RUT_EMPRESA, EMP.ACTIVIDAD_ECONOMIC, EMP.GIRO_EMPRESA  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N' ";
        OutMessage.OutMessagePrint(" :) ---> Actualizando Empresas \nQuery: " + sqlOrig);
        consul.exec(sqlOrig);
        int regProcesados = 0;
        int totReg = 0;
        do
        {
            if(!consul.next())
                break;
            totReg++;
            String sqlDest = "DELETE FROM eje_ges_empresa where empresa = " + consul.getString("COD_EMPRESA");
            if(consulTraspaso.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            sqlDest = "INSERT INTO eje_ges_empresa (empresa, descrip, empre_rut, id_act_econom, empre_giro) VALUES (" + consul.getString("COD_EMPRESA") + ", '" + consul.getString("EMPRESA") + "', '" + consul.getString("RUT_EMPRESA") + "-" + consul.getString("DV_RUT_EMPRESA") + "', " + consul.getString("ACTIVIDAD_ECONOMIC") + ", '" + consul.getString("GIRO_EMPRESA") + "')";
            if(consulTraspaso.insert(sqlDest) && ++regProcesados % 200 == 0)
                System.out.print(".");
        } while(true);
        OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
        consul.close();
        consulTraspaso.close();
        OutMessage.OutMessagePrint("\nFin Empresas");
        return true;
    }

    private String periodoOri;
}