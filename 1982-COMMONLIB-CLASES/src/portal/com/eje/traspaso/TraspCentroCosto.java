package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspCentroCosto extends Proceso
{

    public TraspCentroCosto(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consulOrig = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        Dato dato = new Dato();
        Validar validar = new Validar();
        OutMessage.OutMessagePrint("Actualizando Centro de Costos");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "DELETE FROM eje_ges_centro_costo WHERE wp_cod_empresa = " + empresa;
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            OutMessage.OutMessagePrint("Error");
            String sqlOrig = "SELECT COD_EMPRESA, COD_CENTRO_COSTO, RTRIM(CENTRO_COSTO) AS CENTRO_COSTO, COD_VIGENTE FROM CENTRO_COSTO WHERE COD_EMPRESA = " + empresa;
            OutMessage.OutMessagePrint(" :) ---> Actualizando Centros de Costos \nQuery: " + sqlOrig);
            consulOrig.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            String Empresa = "";
            do
            {
                if(!consulOrig.next())
                    break;
                totReg++;
                String vigente = consulOrig.getString("COD_VIGENTE");
                if(vigente == null)
                    vigente = "N";
                sqlDest = "INSERT INTO eje_ges_centro_costo (centro_costo, descrip, vigente, wp_cod_empresa) VALUES ( " + dato.objectToDato(consulOrig.getString("COD_CENTRO_COSTO")) + ", " + "'";
                if(consulOrig.getString("CENTRO_COSTO").indexOf("'") < 0)
                {
                    OutMessage.OutMessagePrint("Inserte a  " + validar.validarDato(consulOrig.getString("CENTRO_COSTO"), "SD"));
                    sqlDest = sqlDest + validar.validarDato(consulOrig.getString("CENTRO_COSTO"), "SD");
                } else
                {
                    OutMessage.OutMessagePrint("Inserte b  " + dato.objectToDatoComillas(validar.validarDato(consulOrig.getString("CENTRO_COSTO"), "SD").replaceAll("'", "\264")));
                    sqlDest = sqlDest + dato.objectToDatoComillas(validar.validarDato(consulOrig.getString("CENTRO_COSTO"), "SD").replaceAll("'", "\264"));
                }
                sqlDest = sqlDest + "', " + dato.objectToDatoComillas(vigente) + ", " + dato.objectToDato(consulOrig.getString("COD_EMPRESA")) + ")";
                OutMessage.OutMessagePrint("Inserte " + sqlDest);
                if(consulDest.insert(sqlDest) && ++regProcesados % 200 == 0)
                    System.out.print(".");
            } while(true);
            OutMessage.OutMessagePrint("\nRegistros Procesados(1) (" + regProcesados + " de " + totReg + ")");
            consulOrig.close();
            consulDest.close();
            OutMessage.OutMessagePrint("Fin Centro Costo");
        }
        consulAuxx.close();
        return true;
    }

    private String periodoOri;
}