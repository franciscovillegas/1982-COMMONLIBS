package portal.com.eje.traspaso;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Rut;
import portal.com.eje.tools.Validar;

// Referenced classes of package com.eje.traspaso:
//            Proceso

public class TraspCertifLiquidaDetalleDescuentoAdic extends Proceso
{

    public TraspCertifLiquidaDetalleDescuentoAdic(Connection conOrigen, Connection conDestino, String fecha_desde)
    {
        super(conOrigen, conDestino);
        this.fecha_desde = fecha_desde;
        historico = true;
        periodoOri = periodoOri;
    }

    public boolean Run(int empresa, int periodo)
    {
        Rut rut = null;
        Dato dato = new Dato();
        Validar validar = new Validar();
        Consulta consulOri2 = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        Consulta consulDest2 = new Consulta(conDestino);
        Consulta consulAux = new Consulta(conOrigen);
        Consulta consulAux2 = new Consulta(conOrigen);
        Date fecha_actual = new Date();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = simpledateformat.format(fecha_actual);
        OutMessage.OutMessagePrint("\nActualizando Data Liquidacion Adicional de Remuneraciones Descuentos");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            int regProcesados = 0;
            int totReg = 0;
            String sqlOrig2 = "SELECT LD.COD_EMPRESA, LD.COD_PLANTA, LD.COD_DESCUENTO, LD.NRO_TRABAJADOR, LD.DV_TRABAJADOR, LD.ANO_PERIODO, LD.MES_PERIODO,LD.COD_TIPO_PROCESO, LD.CORRELATIVO, LD.VALOR_TRANSACCION, LD.COD_UNIDAD_MONEDA, LD.VALOR_TRANSAC_PESO FROM LIQUIDA_DESCUENTOS AS LD, PLANTA AS PLA, PERSONAL AS PER WHERE LD.COD_TIPO_PROCESO IN ('LG','LG1','LG2','LG3','LG4','LG5','LG05','LG_X') AND LD.NRO_TRABAJADOR <> 16098105  AND LD.NRO_TRABAJADOR = PER.NRO_TRABAJADOR  AND LD.COD_EMPRESA = PER.COD_EMPRESA  AND PER.COD_PLANTA = PLA.COD_PLANTA  AND PER.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND (PER.FEC_FIN_CONTR_VIGE > '" + fecha + "' " + " OR PER.FEC_FIN_CONTR_VIGE is null) " + " AND LD.ANO_PERIODO*100+LD.MES_PERIODO >= " + Integer.parseInt(fecha_desde) + " AND LD.COD_EMPRESA = " + empresa + " and not LD.VALOR_TRANSAC_PESO is null";
            OutMessage.OutMessagePrint(" PROCESO INSERTA DESCUENTOS  " + sqlOrig2);
            consulOri2.exec(sqlOrig2);
            String delHistory = "delete from eje_ges_certif_histo_liquidacion_detalle_adic where    empresa = " + empresa + " AND periodo >= " + Integer.parseInt(fecha_desde) + " and id_tp = 'D'";
            consulDest.insert(delHistory);
            OutMessage.OutMessagePrint(" Borrando RUT desde periodo " + delHistory);
            String Empresa = "";
            do
            {
                if(!consulOri2.next())
                    break;
                totReg++;
                String val_haber = "0";
                String val_descuento = "0";
                String glosa_haber = "nn";
                String glosa_descuento = "nn";
                String tipo = "";
                String orden = "0";
                String indic_papeleta = "";
                String cod_empresa = consulOri2.getString("COD_EMPRESA");
                String cod_planta = consulOri2.getString("COD_PLANTA");
                String tipo_proceso = consulOri2.getString("COD_TIPO_PROCESO");
                String cod_movimiento = consulOri2.getString("COD_DESCUENTO");
                String sqlAux = "SELECT RTRIM(DESCRIPCION) AS DESCRIPCION, PRIORIDAD_DESCUENT,INDIC_PAPELETA FROM DESCUENTO WHERE COD_EMPRESA = " + cod_empresa + " AND COD_PLANTA = " + cod_planta + " AND COD_DESCUENTO = " + cod_movimiento;
                val_descuento = consulOri2.getString("VALOR_TRANSAC_PESO");
                consulAux.exec(sqlAux);
                if(consulAux.next())
                {
                    orden = consulAux.getString("PRIORIDAD_DESCUENT");
                    glosa_descuento = consulAux.getString("DESCRIPCION");
                    tipo = "D";
                    indic_papeleta = consulAux.getString("INDIC_PAPELETA");
                }
                String mes = consulOri2.getString("MES_PERIODO");
                if(mes.length() == 1)
                    mes = "0" + mes;
                String sqlDest2 = "INSERT INTO eje_ges_certif_histo_liquidacion_detalle_adic (periodo, empresa, unidad, rut,tipo_proceso, tip_unidad, id_tp,glosa_haber,val_haber, glosa_descuento,val_descuento, orden, wp_cod_empresa, wp_cod_planta,wp_indic_papeleta) VALUES (" + dato.objectToDato(consulOri2.getString("ANO_PERIODO") + mes) + ", " + dato.objectToDatoComillas(consulOri2.getString("COD_EMPRESA")) + ", " + dato.objectToDatoComillas(consulOri2.getString("COD_UNIDAD_MONEDA")) + ", " + dato.objectToDato(consulOri2.getString("NRO_TRABAJADOR")) + ", " + dato.objectToDatoComillas(tipo_proceso) + ", " + dato.objectToDato(consulOri2.getString("VALOR_TRANSACCION")) + ", " + dato.objectToDatoComillas(tipo) + ", " + dato.objectToDatoComillas(glosa_haber) + ", " + dato.objectToDato(val_haber) + ", " + dato.objectToDatoComillas(glosa_descuento) + ", " + dato.objectToDato(val_descuento) + ", " + validar.validarDato(orden, "9999") + ", " + dato.objectToDatoComillas(consulOri2.getString("COD_EMPRESA")) + ", " + dato.objectToDatoComillas(consulOri2.getString("COD_PLANTA")) + ", " + dato.objectToDatoComillas(indic_papeleta) + ")";
                if(consulDest2.insert(sqlDest2))
                {
                    if(++regProcesados % 100 == 0)
                        System.out.print(".");
                } else
                {
                    System.out.print("-");
                }
            } while(true);
            consulOri2.close();
            consulAux.close();
            consulAux2.close();
            consulDest.close();
            consulDest2.close();
            OutMessage.OutMessagePrint("Registros Procesados (" + regProcesados + " de " + totReg + ")");
            OutMessage.OutMessagePrint("Fin de Traspaso Data Liquidacion Adicional de Remuneraciones Descuentos");
        }
        consulAuxx.close();
        return true;
    }

    private String fecha_desde;
    private boolean historico;
    private String agno;
    private String mes;
    private String periDest;
    private String periodoOri;
}
