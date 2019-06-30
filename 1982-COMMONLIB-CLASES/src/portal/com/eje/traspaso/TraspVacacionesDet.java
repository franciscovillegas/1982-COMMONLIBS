package portal.com.eje.traspaso;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspVacacionesDet extends Proceso
{

    public TraspVacacionesDet(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Dato dato = new Dato("dd/MM/yyyy");
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
            String sqlDest = "DELETE FROM eje_ges_vacaciones_det  where empresa =" + empresa;
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            Date fecha_actual = new Date();
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = simpledateformat.format(fecha_actual);
            String sqlOrig = "SELECT HFT.NRO_TRABAJADOR, HFT.COD_EMPRESA, HFT.COD_PLANTA,  HFT.NUM_DIAS_FERIADOS,HFT.FEC_INICIO_FERIADO, HFT.FECHA_FIN_FERIADO  FROM MOVIMIENT_FERIADOS as HFT, PERSONAL as PER, PLANTA as PLA  WHERE PER.COD_VIGEN_TRABAJAD = 'S'  AND HFT.NRO_TRABAJADOR = PER.NRO_TRABAJADOR  AND HFT.COD_EMPRESA = PER.COD_EMPRESA  AND HFT.COD_PLANTA = PER.COD_PLANTA  AND HFT.TIPO_MOVIMIENTO = 'CARGO'  AND HFT.COD_FERIADO_SOLICI = 'U'  AND PER.COD_EMPRESA = PLA.COD_EMPRESA  AND PER.COD_PLANTA = PLA.COD_PLANTA  AND PLA.COD_VIGENTE <> 'N'  AND PER.COD_EMPRESA = " + empresa + " AND (PER.FEC_FIN_CONTR_VIGE > '" + fecha + "' " + " OR PER.FEC_FIN_CONTR_VIGE is null) ";
            consulOrig.exec(sqlOrig);
            OutMessage.OutMessagePrint(sqlOrig);
            do
            {
                if(!consulOrig.next())
                    break;
                totReg++;
                System.out.println(dato.objectToDato(consulOrig.getString("NRO_TRABAJADOR")));
                System.out.println(consulOrig.getDate("FEC_INICIO_FERIADO"));
                System.out.println(consulOrig.getString("NUM_DIAS_FERIADOS"));
                sqlDest = "INSERT INTO eje_ges_vacaciones_det  (rut, periodo, desde, hasta,   dias_normales, dias_progresivos, tipo_vac, empresa) VALUES (" + dato.objectToDato(consulOrig.getString("NRO_TRABAJADOR")) + ", " + 0 + ", " + dato.fechaToDatoComillas(consulOrig.getValor("FEC_INICIO_FERIADO")) + ", " + dato.fechaToDatoComillas(consulOrig.getValor("FECHA_FIN_FERIADO")) + ", " + dato.objectToDato(consulOrig.getString("NUM_DIAS_FERIADOS")) + ", " + 0 + ", " + 0 + ", " + consulOrig.getInt("COD_EMPRESA") + ")";
                OutMessage.OutMessagePrint(sqlDest);
                if(consulDest.insert(sqlDest) && ++regProcesados % 300 == 0)
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