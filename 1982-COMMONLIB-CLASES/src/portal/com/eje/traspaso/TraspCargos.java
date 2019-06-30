package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspCargos extends Proceso
{

    public TraspCargos(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int cod_empresa, int periodo)
    {
        Consulta consul = new Consulta(conOrigen);
        Consulta consulTraspaso = new Consulta(conDestino);
        OutMessage.OutMessagePrint("Actualizando Cargos");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + cod_empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "DELETE FROM eje_ges_cargos WHERE empresa = " + cod_empresa;
            if(consulTraspaso.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            String sqlOrig = "SELECT COD_CARGO, CARGO_TRABAJADOR, COD_VIGENTE FROM CARGO_TRABAJADOR WHERE COD_EMPRESA = " + cod_empresa;
            OutMessage.OutMessagePrint(" :) ---> Actualizando Cargos \nQuery: " + sqlOrig);
            consul.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            String empresa = "";
            do
            {
                if(!consul.next())
                    break;
                totReg++;
                String cargo = consul.getString("CARGO_TRABAJADOR").replaceAll("'", "\264");
                sqlDest = "INSERT INTO eje_ges_cargos (empresa, cargo, descrip, vigente) VALUES ('" + cod_empresa + "', '" + consul.getString("COD_CARGO") + "', '" + cargo + "', '" + consul.getString("COD_VIGENTE") + "')";
                if(consulTraspaso.insert(sqlDest) && ++regProcesados % 200 == 0)
                    System.out.print(".");
            } while(true);
            OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
            OutMessage.OutMessagePrint("Fin Cargos");
            consul.close();
            consulTraspaso.close();
        }
        consulAuxx.close();
        return true;
    }
}