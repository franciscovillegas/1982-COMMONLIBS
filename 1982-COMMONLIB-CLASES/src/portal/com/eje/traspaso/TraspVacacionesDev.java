package portal.com.eje.traspaso;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspVacacionesDev extends Proceso
{

    public TraspVacacionesDev(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Dato dato = new Dato();
        Consulta consulOrig = new Consulta(conOrigen);
        Consulta consulAux = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Devengo de Vacaciones");
        int regProcesados = 0;
        int totReg = 0;
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "DELETE FROM eje_ges_feriad_devengados where cod_empresa = " + empresa;
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            Date fecha_actual = new Date();
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = simpledateformat.format(fecha_actual);
            String sqlOrig = "SELECT FD.COD_EMPRESA,FD.COD_PLANTA,FD.NRO_TRABAJADOR,FD.DV_TRABAJADOR,FD.ANO_PERIODO,FD.MES_PERIODO,FD.DIAS_DEVENGADOS,FD.FEC_DEVENGADOS FROM FERIAD_DEVENGADOS as FD,PERSONAL as PER, PLANTA as PLA  WHERE PER.COD_VIGEN_TRABAJAD = 'S'  AND FD.NRO_TRABAJADOR = PER.NRO_TRABAJADOR  AND FD.COD_EMPRESA = PER.COD_EMPRESA  AND PER.COD_EMPRESA = PLA.COD_EMPRESA  AND PER.COD_PLANTA = PLA.COD_PLANTA  AND FD.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND PER.COD_EMPRESA = " + empresa + " AND (PER.FEC_FIN_CONTR_VIGE > '" + fecha + "' " + " OR PER.FEC_FIN_CONTR_VIGE is null) ";
            SimpleDateFormat mesFormat = new SimpleDateFormat("MM");
            SimpleDateFormat agnoFormat = new SimpleDateFormat("yyyy");
            OutMessage.OutMessagePrint(sqlOrig);
            consulOrig.exec(sqlOrig);
            do
            {
                if(!consulOrig.next())
                    break;
                totReg++;
                sqlDest = "INSERT INTO eje_ges_feriad_devengados  (cod_empresa,cod_planta,rut_cliente,dv_cliente,ano_periodo,mes_periodo,dias_devengados,fec_devengados ) VALUES (" + dato.objectToDato(consulOrig.getString("COD_EMPRESA")) + ", " + dato.objectToDato(consulOrig.getString("COD_PLANTA")) + ", " + dato.objectToDato(consulOrig.getString("NRO_TRABAJADOR")) + ", '" + dato.objectToDato(consulOrig.getString("DV_TRABAJADOR")) + "', " + dato.objectToDato(consulOrig.getString("ANO_PERIODO")) + ", " + dato.objectToDato(consulOrig.getString("MES_PERIODO")) + ", " + dato.objectToDato(consulOrig.getString("DIAS_DEVENGADOS")) + ", " + dato.fechaToDatoComillas(consulOrig.getValor("FEC_DEVENGADOS")) + ")";
                if(consulDest.insert(sqlDest) && ++regProcesados % 50 == 0)
                    System.out.print(".");
            } while(true);
        }
        OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
        consulOrig.close();
        consulDest.close();
        OutMessage.OutMessagePrint("\nFin Devengo de Vacaciones");
        consulAuxx.close();
        return true;
    }
}