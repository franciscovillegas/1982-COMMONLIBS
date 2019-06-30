package portal.com.eje.traspaso;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspVacaciones extends Proceso
{

    public TraspVacaciones(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Dato dato = new Dato();
        Consulta consulOrig = new Consulta(conOrigen);
        Consulta consulAux = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Vacaciones");
        int regProcesados = 0;
        int totReg = 0;
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "DELETE FROM eje_ges_vacaciones_wp where cod_empresa = " + empresa;
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            Date fecha_actual = new Date();
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = simpledateformat.format(fecha_actual);
            String sqlOrig = "SELECT FT.NRO_TRABAJADOR, FT.COD_EMPRESA, FT.COD_PLANTA,  FT.DIAS_CON_DERECHO, FT.DIAS_PENDIENTES, FT.DIAS_DEL_PERIODO,  FT.DIAS_PROX_PERIODO, FT.SDO_ACTUAL_PERIODO, FT.DIAS_PROGRESIVOS,  FT.FEC_INI_ULT_PERIOD, FT.FEC_INI_ULT_PERIOD  FROM FERIAD_TRABAJADOR as FT, PERSONAL as PER, PLANTA as PLA  WHERE PER.COD_VIGEN_TRABAJAD = 'S'  AND FT.NRO_TRABAJADOR = PER.NRO_TRABAJADOR  AND FT.COD_EMPRESA = PER.COD_EMPRESA  AND PER.COD_EMPRESA = PLA.COD_EMPRESA  AND PER.COD_PLANTA = PLA.COD_PLANTA  AND FT.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND FT.FEC_INI_ULT_PERIOD is not null  AND PER.COD_EMPRESA = " + empresa + " AND (PER.FEC_FIN_CONTR_VIGE > '" + fecha + "' " + " OR PER.FEC_FIN_CONTR_VIGE is null) ";
            SimpleDateFormat mesFormat = new SimpleDateFormat("MM");
            SimpleDateFormat agnoFormat = new SimpleDateFormat("yyyy");
            OutMessage.OutMessagePrint(sqlOrig);
            consulOrig.exec(sqlOrig);
            do
            {
                if(!consulOrig.next())
                    break;
                totReg++;
                Date fecha_2 = (Date)consulOrig.getValor("FEC_INI_ULT_PERIOD");
                OutMessage.OutMessagePrint(consulOrig.getString("NRO_TRABAJADOR"));
                OutMessage.OutMessagePrint(fecha_2.toString());
                String mes = mesFormat.format(fecha_2);
                String agno = agnoFormat.format(fecha_2);
                String period = agno + mes;
                sqlDest = "INSERT INTO eje_ges_vacaciones_wp  (rut, periodo, cod_empresa, cod_planta,  dias_con_derecho, dias_pendientes, dias_del_periodo,  dias_prox_periodo, sdo_actual_periodo, dias_progresivos) VALUES (" + dato.objectToDato(consulOrig.getString("NRO_TRABAJADOR")) + ", " + dato.objectToDato(agno) + ", " + dato.objectToDato(consulOrig.getString("COD_EMPRESA")) + ", " + dato.objectToDato(consulOrig.getString("COD_PLANTA")) + ", " + dato.objectToDato(consulOrig.getString("DIAS_CON_DERECHO")) + ", " + dato.objectToDato(consulOrig.getString("DIAS_PENDIENTES")) + ", " + dato.objectToDato(consulOrig.getString("DIAS_DEL_PERIODO")) + ", " + dato.objectToDato(consulOrig.getString("DIAS_PROX_PERIODO")) + ", " + dato.objectToDato(consulOrig.getString("SDO_ACTUAL_PERIODO")) + ", " + dato.objectToDato(consulOrig.getString("DIAS_PROGRESIVOS")) + ")";
                if(consulDest.insert(sqlDest) && ++regProcesados % 50 == 0)
                    System.out.print(".");
            } while(true);
        }
        OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
        consulOrig.close();
        consulDest.close();
        OutMessage.OutMessagePrint("\nFin Vacaciones");
        consulAuxx.close();
        return true;
    }
}