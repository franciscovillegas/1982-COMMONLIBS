package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspGrupoFamiliar extends Proceso
{

    public TraspGrupoFamiliar(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Dato dato = new Dato();
        Consulta consulOri = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("Actualizando Grupo Familiar");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "delete from eje_ges_grupo_familiar where wp_cod_empresa = " + empresa;
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("datos de eje_ges_grupo_familiar eliminados");
            String sqlOrig = "SELECT NUCLEO_FAMILIAR.COD_EMPRESA, NUCLEO_FAMILIAR.NRO_TRABAJADOR,  NUCLEO_FAMILIAR.DV_TRABAJADOR, NUCLEO_FAMILIAR.CORREL_FAMILIAR,  NUCLEO_FAMILIAR.APE_PATERNO_FAMILI,  NUCLEO_FAMILIAR.APE_MATERNO_FAMILI, NUCLEO_FAMILIAR.NOMBRE_FAMILIAR,  (RTRIM(NUCLEO_FAMILIAR.APE_PATERNO_FAMILI) + ' ' + RTRIM(NUCLEO_FAMILIAR.APE_MATERNO_FAMILI) + ', ' + RTRIM(NUCLEO_FAMILIAR.NOMBRE_FAMILIAR)) AS nombre,  NUCLEO_FAMILIAR.NRO_RUT_FAMILIAR, NUCLEO_FAMILIAR.DV_RUT_FAMILIAR,  NUCLEO_FAMILIAR.FEC_NACIM_FAMILIAR, NUCLEO_FAMILIAR.COD_SEXO_FAMILIAR,  NUCLEO_FAMILIAR.COD_NIVEL_ESCOLAR, NUCLEO_FAMILIAR.ES_CARGA,  NUCLEO_FAMILIAR.TIPO_CARGA_FAMILIA, NUCLEO_FAMILIAR.ESTADO_FAMILIAR,  NUCLEO_FAMILIAR.ES_BENEFICIARIO, NUCLEO_FAMILIAR.DIRECCION_FAMILIAR,  NUCLEO_FAMILIAR.COD_COMUNA, NUCLEO_FAMILIAR.COD_ISAPRE,  NUCLEO_FAMILIAR.COD_ACTIVIDAD, NUCLEO_FAMILIAR.FEC_INI_VIGENCIA,  NUCLEO_FAMILIAR.FEC_FIN_VIGENCIA, NUCLEO_FAMILIAR.CARGA_NOR_SMEDICO,  NUCLEO_FAMILIAR.CARGA_ESP_SMEDICO, NUCLEO_FAMILIAR.BENEFICIO_BBMM,  NUCLEO_FAMILIAR.NUM_CARGA_ISAPRE,   PARENTESCO.PARENTESCO FROM NUCLEO_FAMILIAR INNER JOIN  PARENTESCO ON NUCLEO_FAMILIAR.COD_PARENTESCO = PARENTESCO.COD_PARENTESCO  WHERE NUCLEO_FAMILIAR.COD_EMPRESA = " + empresa + " ORDER BY NUCLEO_FAMILIAR.CORREL_FAMILIAR ";
            OutMessage.OutMessagePrint(" :) ---> Actualizando Grupo Familiar \nQuery: " + sqlOrig);
            consulOri.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            do
            {
                if(!consulOri.next())
                    break;
                totReg++;
                sqlDest = "INSERT INTO eje_ges_grupo_familiar (rut, numero, nombre, parentesco, fecha_nacim, rut_carga, dv_carga, es_carga, es_carga_salud, grupo_sanguineo, num_matrim, actividad, sexo, fecha_inicio, fecha_termino, fec_aviso, f_cancelaviso, wp_cod_empresa) VALUES (" + dato.objectToDato(consulOri.getString("NRO_TRABAJADOR")) + ", " + dato.objectToDato(consulOri.getString("CORREL_FAMILIAR")) + ", " + dato.objectToDatoComillas(consulOri.getString("nombre")) + ", " + dato.objectToDatoComillas(consulOri.getString("PARENTESCO")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("FEC_NACIM_FAMILIAR")) + ", " + dato.objectToDato(consulOri.getString("NRO_RUT_FAMILIAR")) + ", " + dato.objectToDatoComillas(consulOri.getString("DV_RUT_FAMILIAR")) + ", " + dato.objectToDatoComillas(consulOri.getString("ES_CARGA")) + ", " + dato.objectToDatoComillas(null) + ", " + dato.objectToDato(null) + ", " + dato.objectToDato(null) + ", " + dato.objectToDatoComillas(null) + ", " + dato.objectToDatoComillas(consulOri.getString("COD_SEXO_FAMILIAR")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("FEC_INI_VIGENCIA")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("FEC_FIN_VIGENCIA")) + ", " + dato.objectToDato(null) + ", " + dato.objectToDato(null) + ", " + dato.objectToDato(consulOri.getString("COD_EMPRESA")) + ")";
                if(consulDest.insert(sqlDest) && ++regProcesados % 100 == 0)
                    System.out.print(".");
            } while(true);
            consulOri.close();
            consulDest.close();
            OutMessage.OutMessagePrint("Registros Procesados (" + regProcesados + " de " + totReg + ")");
        }
        consulAuxx.close();
        return true;
    }
}