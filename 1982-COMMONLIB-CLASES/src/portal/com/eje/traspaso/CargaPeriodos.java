package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class CargaPeriodos extends Proceso
{

    public CargaPeriodos(Connection conOrigen, Connection conDestino, int yearInicio, String mesFin, String yearFin)
    {
        super(conOrigen, conDestino);
        this.yearInicio = yearInicio;
        finYear = Integer.parseInt(yearFin);
        finMes = Integer.parseInt(mesFin);
        historico = true;
    }

    public CargaPeriodos(Connection conOrigen, Connection conDestino, String periMmes, String periYear)
    {
        super(conOrigen, conDestino);
        finYear = Integer.parseInt(periYear);
        finMes = Integer.parseInt(periMmes);
        historico = false;
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consulDest = new Consulta(conDestino);
        Consulta consulOrig = new Consulta(conOrigen);
        OutMessage.OutMessagePrint("Cargando Periodos desde " + yearInicio + periMeses[0]);
        String sqlDest = "DELETE FROM eje_ges_periodo where peri_id >= " + String.valueOf(periodo);
        if(consulDest.insert(sqlDest))
            OutMessage.OutMessagePrint("Datos de destino Borrados");
        else
            OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
        float utm = 0.0F;
        float uf = 0.0F;
        int regProcesados = 0;
        int totReg = 0;
        for(int year = yearInicio; year <= finYear; year++)
        {
            for(int mes = 1; mes <= 12 && (year != finYear || mes <= finMes); mes++)
            {
                totReg++;
                String sqlDestAux = "INSERT INTO eje_ges_periodo (peri_id, peri_ano, peri_mes, peri_utm, peri_uf) VALUES (" + year + periMeses[mes - 1] + ", " + year + ", " + mes + ", " + utm + ", " + uf + ")";
                if(consulDest.insert(sqlDestAux) && ++regProcesados % 50 == 0)
                    System.out.print(".");
            }

        }

        OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
        OutMessage.OutMessagePrint("Fin Carga periodos");
        consulDest.close();
        consulOrig.close();
        return true;
    }

    private int yearInicio;
    private int finYear;
    private int finMes;
    private boolean historico;
    private String periMeses[] = {
        "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", 
        "11", "12"
    };
}