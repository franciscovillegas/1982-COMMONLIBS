package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspTP extends Proceso
{

    public TraspTP(Connection conOrigen, Connection conDestino, String periodoOri)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Dato dato = new Dato();
        Consulta consulOri = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando tp_wp");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "delete from eje_ges_tp_wp WHERE wp_cod_empresa =" + empresa;
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("datos de eje_ges_tp_wp");
            String sqlOrig = "SELECT COD_EMPRESA, COD_PLANTA, COD_CONCEP_CONTABL, CONCEPTO_CONTABLE FROM CONCEPTO_CONTABLE WHERE VIGENCIA_CONCEPTO = 'S' AND COD_EMPRESA = " + empresa;
            OutMessage.OutMessagePrint(" :) ---> Query: " + sqlOrig);
            consulOri.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            do
            {
                if(!consulOri.next())
                    break;
                totReg++;
                sqlDest = "INSERT INTO eje_ges_tp_wp  (id_tp, n_tp, wp_cod_empresa, wp_cod_planta)VALUES ('" + consulOri.getString("COD_CONCEP_CONTABL") + "', '" + consulOri.getString("CONCEPTO_CONTABLE") + "', '" + consulOri.getString("COD_EMPRESA") + "', '" + consulOri.getString("COD_PLANTA") + "')";
                if(consulDest.insert(sqlDest) && ++regProcesados % 200 == 0)
                    System.out.print(".");
            } while(true);
            consulOri.close();
            consulDest.close();
            OutMessage.OutMessagePrint("Registros Procesados (" + regProcesados + " de " + totReg + ")");
            OutMessage.OutMessagePrint("Fin de TP");
        }
        consulAuxx.close();
        return true;
    }

    private String periodoOri;
}