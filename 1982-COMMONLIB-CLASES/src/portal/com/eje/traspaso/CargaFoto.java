package portal.com.eje.traspaso;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class CargaFoto extends Proceso
{

    public CargaFoto(Connection conOrigen, Connection conDestino, String extension)
    {
        super(conOrigen, conDestino);
        this.extension = extension;
    }

    public boolean Run(int empresa, int periodos)
    {
        Dato dato = new Dato();
        Consulta consulOri = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando nombre de fotos");
        Date fecha_actual = new Date();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = simpledateformat.format(fecha_actual);
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
        	String sqlDest = "DELETE FROM eje_ges_foto_trab WHERE wp_cod_empresa = " + empresa + " ";
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
        	String sqlOrig = "SELECT PER.NRO_TRABAJADOR, PER.COD_EMPRESA, PER.COD_PLANTA,  PER.DV_TRABAJADOR, PER.RUT_TRABAJADOR, PER.DV_RUT_TRABAJADOR  FROM PERSONAL as PER, PLANTA as PLA  WHERE PER.COD_VIGEN_TRABAJAD = 'S'  AND PER.COD_EMPRESA = PLA.COD_EMPRESA  AND PER.COD_PLANTA = PLA.COD_PLANTA  AND PLA.COD_VIGENTE <> 'N'  AND PER.COD_EMPRESA = " + empresa + " AND (PER.FEC_FIN_CONTR_VIGE > '" + fecha + "' " + " OR PER.FEC_FIN_CONTR_VIGE is null " + ")";
            OutMessage.OutMessagePrint("Query: " + sqlOrig);
            consulOri.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            String Empresa = "";
            String foto = null;
            do
            {
                if(!consulOri.next())
                    break;
                totReg++;
                foto = consulOri.getString("RUT_TRABAJADOR") + "." + extension;
                sqlDest = "INSERT INTO eje_ges_foto_trab (rut, id_foto, wp_cod_empresa, wp_cod_planta)  VALUES ( " + dato.objectToDato(consulOri.getString("RUT_TRABAJADOR")) + ", " + dato.objectToDatoComillas(foto.toLowerCase()).toLowerCase() + ", " + dato.objectToDato(consulOri.getString("COD_EMPRESA")) + ", " + dato.objectToDato(consulOri.getString("COD_PLANTA")) + ")";
                if(consulDest.insert(sqlDest) && ++regProcesados % 200 == 0)
                    System.out.print(".");
            } while(true);
            consulOri.close();
            consulDest.close();
            OutMessage.OutMessagePrint("Registros Procesados (" + regProcesados + " de " + totReg + ")");
            OutMessage.OutMessagePrint("Fin de Carga Fotos");
        }
        consulAuxx.close();
        return true;
    }

    private String periodoOri;
    private String extension;
}