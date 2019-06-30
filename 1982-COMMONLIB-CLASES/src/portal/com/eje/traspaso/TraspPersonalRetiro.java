package portal.com.eje.traspaso;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Rut;
import portal.com.eje.tools.Validar;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspPersonalRetiro extends Proceso
{

    public TraspPersonalRetiro(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Dato dato = new Dato();
        Validar validar = new Validar();
        Date fecha_actual = new Date();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = simpledateformat.format(fecha_actual);
        Consulta consulOri = new Consulta(conOrigen);
        Consulta consulOriAux = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Empleados");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "delete from eje_ges_personal_retirado where empresa ='" + empresa + "'";
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("datos de eje_ges_personal_retirado eliminados ");
            String sqlOrig = "SELECT  PER.RUT_TRABAJADOR, PER.DV_RUT_TRABAJADOR, (RTRIM(PER.NOMBRE) + ' ' + RTRIM(PER.APE_PATERNO_TRABAJ) +' '+RTRIM(PER.APE_MATERNO_TRABAJ)) as nomcom,  PER.FEC_NACIMIENTO, PER.COD_ESTADO_CIVIL, PER.NACIONALIDAD, PER.COD_CARGO, PER.FEC_INI_CONTRATO, PER.FEC_FIN_CONTR_VIGE,  PER.COD_PLANTA, PER.COD_UNIDAD_ADMINIS, PER.COD_CENTRO_COSTO, PER.COD_SUCURSAL, PER.COD_EMPRESA  FROM PERSONAL as PER  WHERE PER.COD_VIGEN_TRABAJAD = 'N'  AND PER.COD_EMPRESA = " + empresa + " AND PER.NRO_TRABAJADOR <> 16098105 ";
            OutMessage.OutMessagePrint(" :) ---> Actualizando Personal retirado \nQuery: " + sqlOrig);
            consulOri.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            String SQL = "";
            do
            {
                if(!consulOri.next())
                    break;
                totReg++;
                Rut rut = new Rut(consulOri.getString("RUT_TRABAJADOR"));
                String nom_completo = consulOri.getString("nomcom");
                Object fec_ingreso = consulOri.getValor("FEC_INI_CONTRATO");
                Object fec_fin_contrato = consulOri.getValor("FEC_FIN_CONTR_VIGE");
                String cod_empresa = consulOri.getString("COD_EMPRESA");
                String cod_planta = consulOri.getString("COD_PLANTA");
                String id_unidad = consulOri.getString("COD_UNIDAD_ADMINIS");
                sqlDest = "INSERT INTO eje_ges_personal_retirado  (rut, nombre, fec_ingreso, fec_retiro, unidad, empresa, wp_cod_planta) VALUES (" + dato.objectToDato(rut.getRut()) + ", " + dato.objectToDatoComillas(nom_completo) + ", " + validar.validarFecha(dato.fechaToDatoComillas(fec_ingreso), null) + ", " + validar.validarFecha(dato.fechaToDatoComillas(fec_fin_contrato), null) + ", " + dato.objectToDatoComillas(id_unidad) + ", " + dato.objectToDatoComillas(cod_empresa) + ", " + dato.objectToDato(cod_planta) + ")";
                if(consulDest.insert(sqlDest))
                {
                    regProcesados++;
                    System.out.print(".");
                }
            } while(true);
            consulOri.close();
            consulOriAux.close();
            consulDest.close();
            OutMessage.OutMessagePrint("Registros Procesados (" + regProcesados + " de " + totReg + ")");
            OutMessage.OutMessagePrint("Fin de Trapasa Empleados");
        }
        consulAuxx.close();
        return true;
    }

    private String periodoOri;
}