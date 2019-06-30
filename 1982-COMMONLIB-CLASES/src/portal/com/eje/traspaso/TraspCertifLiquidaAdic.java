package portal.com.eje.traspaso;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package com.eje.traspaso:
//            Proceso

public class TraspCertifLiquidaAdic extends Proceso
{

    public TraspCertifLiquidaAdic(Connection conOrigen, Connection conDestino, String fecha_desde)
    {
        super(conOrigen, conDestino);
        this.fecha_desde = fecha_desde;
        historico = false;
        periodoOri = periodoOri;
    }

    public TraspCertifLiquidaAdic(Connection conOrigen, Connection conDestino, String agno, String mes, String periDest)
    {
        super(conOrigen, conDestino);
        this.agno = agno;
        this.mes = mes;
        this.periDest = periDest;
        historico = true;
        periodoOri = periodoOri;
    }

    public boolean Run(int empresa, int periodo)
    {
        String tot_imponible = null;
        String afecto_imponible = null;
        String ndias_trab = null;
        String uf_mes = null;
        Dato dato = new Dato();
        Consulta consulOri = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        Consulta consulAux = new Consulta(conOrigen);
        Consulta consulAux2 = new Consulta(conOrigen);
        Consulta consulAux3 = new Consulta(conOrigen);
        Consulta consulAux4 = new Consulta(conOrigen);
        OutMessage.OutMessagePrint("\nActualizando Data Liquidacion Adicional de Remuneraciones");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            Date fecha_actual = new Date();
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = simpledateformat.format(fecha_actual);
            int regProcesados = 0;
            int totReg = 0;
            String sqlOrig = " SELECT HL.COD_EMPRESA, HL.NRO_TRABAJADOR, HL.DV_TRABAJADOR,  HL.ANO_PERIODO, HL.MES_PERIODO, HL.COD_TIPO_PROCESO,  HL.NRO_CONTR_VIG_PROC, HL.COD_PLANTA, HL.COD_SUCURSAL,  HL.FEC_INI_CONTR_VIGE, HL.FEC_FIN_CONTR, HL.NRO_CONVENIO,  HL.SDO_BASE_CONTRACTU, HL.SDOS_ADIC_ACUMULAD, HL.NRO_CARGAS_NORMALE,  HL.NRO_CARGAS_MATERNA, HL.NRO_CARGAS_DUPLO, HL.VALOR_TRAMO_CARGA,  HL.CODIGO_TRAMO, HL.DOMICILIO, HL.COD_ESCALA_SUELDO, HL.COD_ESTADO_CIVIL,  HL.COD_CARGO, HL.COD_CENTRO_COSTO, HL.COD_TIPO_TRABAJADO,  HL.COD_TIPO_JORNADA, HL.SUELDO_GANADO_MES, HL.VALOR_ADIC_GANADO,  HL.SOBRETIEMPO_MES, HL.ASIGNACION_FAMILIA, HL.ASIG_FAMIL_RETROAC,  HL.VALOR_TOTAL_HABERE, HL.HAB_NO_IMP_NO_TRIB, HL.AFECTO_DL3501,  HL.VAL_ALCANCE_LIQUID, HL.VALOR_ANTICIPO, HL.TOT_DESC_CTA_CTE,  HL.RET_JUDIC_RESCONTA, HL.TOT_DCTOS_NO_LEGAL, HL.TOT_DCTOS_LEGALES,  HL.VAL_LIQUIDO_PAGO, HL.SOBREGIROS_MES, HL.TOT_IMPON_SIN_TOPE,  HL.TOT_IMPONIBLE, HL.COD_INSTIT_PREVISI, HL.MTO_CANCELA_PREVIS,  HL.MTO_PREV_VOLUNTARI, HL.MTO_AHORRO_VOLUNTA, HL.COD_INSTIT_SALUD,  HL.MONTO_SALUD, HL.MTO_SALUD_LEGAL, HL.VALOR_PACTADO_ISAP,  HL.VAL_VOLUNT_ISAPRE, HL.MONTO_DL18566, HL.MTO_PAGADO_CCAF,  HL.CAJA_COMP_ANTIGUA, HL.MTO_ANTIG_PREVISIO, HL.PJE_COTZADO_CAJA,  HL.VAL_LEYES_SOCIALES, HL.AFECTO_IMPTO, HL.ASIGNACION_ZONA,  HL.TASA_MTO_IMPTO, HL.REBAJA_IMPUESTO, HL.MONTO_CANCEL_IMPTO,  HL.APORTE_PATRON_MUTU, HL.APORTE_PATRON_CCAF, HL.APORTE_PATRONAL_1,  HL.APORTE_PATRONAL_2, HL.NRO_CHEQ_LIQUID_ME, HL.NRO_DIAS_ASISTIDOS,  HL.NRO_DIAS_ENFERMO, HL.NRO_DIAS_VACACIONE, HL.NRO_DIAS_AUSENTE,  HL.GRATIF_MENS_ANTICI, HL.HABERES_GRATIFICAC, HL.PREV_Y_SEG_GRATIFI,  HL.SALUD_GRATIFICACIO, HL.ASIG_ZONA_GRATIFIC, HL.IMPUESTO_GRATIFICA,  HL.ABONOS_MANUALES, HL.SALDO_CTA_CTE, HL.TOT_TRIBUTABLE, HL.HAB_X_ADM_DELEGADA, HL.TOT_CARGAS_ESCOLAR, HL.NRO_PAPELET_LIQUID, HL.VAL_CAL_BASE_SOBRE, HL.VALOR_IMPONIBLE, HL.VAL_TOT_TOPE_IMPON, HL.VAL_TOTAL_DESCTOS, HL.VALOR_DESAHUCIO, HL.PJE_DESAHUCIO, HL.TOT_IMP_NO_PROP, HL.MTO_TOPE60_DFL44, HL.MTO_DFL44, HL.AFECTO_COTIZACION, HL.TOTAL_IMPONI_LEY, HL.ADICIONAL_ISAPRE, HL.APORTE_PATRONAL_3, HL.APORTE_PATRONAL_4, HL.ASIG_REINTEGRO, HL.SDO_BASE_NO_PROP, HL.TOT_LEY_15386, HL.VAL_DSCTO_LEG_RET,  HL.SUELDO_DIARIO, HL.PJE_COTIZ_PREVIS, HL.PJE_COTIZ_SALUD, HL.NO_TRIBUTABLE_IMPO, HL.HABERES_EXENTOS, HL.NRO_HORA_ATRASO, HL.VAL_REBAJA_ATRASOS, HL.MTO_SEG_INV_SOBREV, HL.MTO_COTIZ_OBLIGATO, HL.PJE_SEG_INV_SOBREV, HL.PJE_COTIZ_OBLIGATO, HL.MTO_SALUD_REAL, HL.APORTE_CONVENIDO, HL.APORTES_INP_CAJA, HL.CONVENIOS_CAJA, HL.LEASING_CAJA, HL.SEGUROS_CAJA, HL.OTROS_CAJA, HL.DESCUENTO_CAJA, HL.NRO_HORAS_ATRASO, HL.NRO_HORAS_PERMISO, HL.NRO_MIN_ATRASO, HL.NRO_MIN_PERMISO, HL.NRO_HORAS_EXTRAS, HL.INVENTARIO,  HL.HABERES_VARIOS, HL.MOVILIZACION, HL.HABER_ASIGNADO, HL.BIENESTAR, HL.NRO_DIAS_AUS_HABIL, HL.MONTO_TOT_506, HL.NRO_DIASNOCUBI, HL.TOT_IMPTO_RELIQ, HL.NRO_EXTRAS_EXCESO, HL.IMPTO_VOLUNTARIO, HL.SUELDO_MENSUAL, HL.GEN_ANT_MUTUAL, HL.ADICIONAL_REAL, HL.COD_PAGO_PREVISION, HL.COD_UNIDAD_ADMINIS, HL.COD_SINDICATO, HL.MONTO_SEGURO_DES, HL.MONTO_AHORRO_PREV, HL.MTO_SEG_CTA_INDIDU, HL.MTO_SEG_CTA_FONDOS, HL.NRO_DIAS_AUSEN_CON, HL.MONTO_IMPON_SEGURO, HL.MTO_TRAB_TR_PESADO, HL.MTO_APOR_TR_PESADO, HL.PORC_APL_TR_PESADO, HL.COD_AFP_AFC, PER.COD_FORMA_PAGO, PER.COD_UNIDAD_ADMINIS, F.FORMA_PAGO FROM HISTORICO_LIQUIDAC AS HL, PERSONAL AS PER, PLANTA AS PLA, FORMA_PAGO AS F WHERE HL.COD_TIPO_PROCESO IN ('LG','LG1','LG2','LG3','LG4','LG5','LG05','LG_X') AND PER.COD_FORMA_PAGO = F.CODIGO_FORMA_PAGO  AND HL.NRO_TRABAJADOR <> 16098105  AND HL.NRO_TRABAJADOR = PER.NRO_TRABAJADOR  AND HL.COD_EMPRESA = PER.COD_EMPRESA  AND PER.COD_PLANTA = PLA.COD_PLANTA  AND PER.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_EMPRESA = F.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND (PER.FEC_FIN_CONTR_VIGE > '" + fecha + "' " + " OR PER.FEC_FIN_CONTR_VIGE is null) " + " AND HL.COD_EMPRESA = " + empresa + " AND HL.ANO_PERIODO*100+HL.MES_PERIODO >= " + Integer.parseInt(fecha_desde) + "";
            OutMessage.OutMessagePrint("\nActualizando Data Liquidacion Adicional Cabecera : " + sqlOrig);
            consulOri.exec(sqlOrig);
            String Empresa = "";
            do
            {
                if(!consulOri.next())
                    break;
                totReg++;
                int rut = consulOri.getInt("NRO_TRABAJADOR");
                String tipo_proceso = consulOri.getString("COD_TIPO_PROCESO");
                String sqlAux4 = "SELECT TOT_IMPONIBLEAN, AFECTO_IMPONIBLE, NDIAS_TRAB, UF_MES  FROM LIQUIDACION  WHERE  COD_EMPRESA = " + consulOri.getString("COD_EMPRESA") + " AND COD_PLANTA = " + consulOri.getString("COD_PLANTA") + " AND NRO_TRABAJADOR = " + consulOri.getString("NRO_TRABAJADOR") + " AND ANO_PERIODO = " + consulOri.getString("ANO_PERIODO") + " AND MES_PERIODO = " + consulOri.getString("MES_PERIODO");
                consulAux4.exec(sqlAux4);
                if(consulAux4.next())
                {
                    tot_imponible = consulAux4.getString("TOT_IMPONIBLEAN");
                    afecto_imponible = consulAux4.getString("AFECTO_IMPONIBLE");
                    ndias_trab = consulAux4.getString("NDIAS_TRAB");
                    uf_mes = consulAux4.getString("UF_MES");
                }
                String n_banco = null;
                String nro_cta_cte = null;
                String sqlAux = "SELECT P.COD_BANCO, B.BANCO, P.NRO_CTA_CTE_BANCAR FROM PERSONAL AS P, BANCO AS B, PLANTA as PLA WHERE B.COD_BANCO = P.COD_BANCO AND P.COD_VIGEN_TRABAJAD = 'S' AND P.COD_EMPRESA = PLA.COD_EMPRESA AND P.COD_PLANTA = PLA.COD_PLANTA  AND PLA.COD_VIGENTE <> 'N' AND (P.FEC_FIN_CONTR_VIGE > '" + fecha + "' " + "OR P.FEC_FIN_CONTR_VIGE is null ) AND P.NRO_TRABAJADOR = " + rut; 
                consulAux.exec(sqlAux);
                if(consulAux.next())
                {
                    n_banco = consulAux.getString("BANCO");
                    nro_cta_cte = consulAux.getString("NRO_CTA_CTE_BANCAR");
                }
                String n_empresa = null;
                sqlAux = "SELECT RTRIM(EMPRESA) AS EMPRESA  FROM EMPRESA  WHERE COD_EMPRESA = " + consulOri.getString("COD_EMPRESA");
                consulAux.exec(sqlAux);
                if(consulAux.next())
                    n_empresa = consulAux.getString("EMPRESA");
                String mes = consulOri.getString("MES_PERIODO");
                if(mes.length() == 1)
                    mes = "0" + mes;
                String delHistory = "delete from eje_ges_certif_histo_liquidacion_cabecera_adic where  periodo = " + dato.objectToDato(consulOri.getString("ANO_PERIODO") + mes) + " AND empresa = " + dato.objectToDatoComillas(consulOri.getString("COD_EMPRESA")) + " AND rut = " + rut;
                consulDest.insert(delHistory);
                String sqlDest = "INSERT INTO eje_ges_certif_histo_liquidacion_cabecera_adic (periodo,empresa, unidad, rut,tipo_proceso,causa_pago, fec_pago, id_forma_pago,forma_pago, cuenta, imp_tribut, imp_no_tribut, no_imp_tribut,no_imp_no_tribut, reliq_rentas, tot_haberes, tot_desctos,liquido, tope_imp, val_uf, dctos_varios, dctos_legales,dctos_impagos,banco, wp_cod_empresa, wp_cod_planta, wp_tot_imponible, wp_afecto_imponible, wp_ndias_trab) VALUES (" + dato.objectToDato(consulOri.getString("ANO_PERIODO") + mes) + "," + dato.objectToDatoComillas(consulOri.getString("COD_EMPRESA")) + "," + dato.objectToDatoComillas(consulOri.getString("COD_UNIDAD_ADMINIS")) + ", " + rut + ", " + dato.objectToDatoComillas(tipo_proceso) + ", null" + ", null" + ", null" + ", null" + ", " + dato.objectToDatoComillas(nro_cta_cte) + "," + dato.objectToDato(consulOri.getString("VALOR_IMPONIBLE")) + ", null" + ", null" + "," + dato.objectToDato(consulOri.getString("HAB_NO_IMP_NO_TRIB")) + ", null" + "," + dato.objectToDato(consulOri.getString("VALOR_TOTAL_HABERE")) + "," + dato.objectToDato(consulOri.getString("VAL_TOTAL_DESCTOS")) + "," + dato.objectToDato(consulOri.getString("VAL_LIQUIDO_PAGO")) + "," + dato.objectToDato(consulOri.getString("TOT_IMPONIBLE")) + "," + dato.objectToDato(uf_mes) + "," + dato.objectToDato(consulOri.getString("TOT_DCTOS_NO_LEGAL")) + "," + dato.objectToDato(consulOri.getString("TOT_DCTOS_LEGALES")) + ", null" + "," + dato.objectToDatoComillas(n_banco) + "," + dato.objectToDato(consulOri.getString("COD_EMPRESA")) + "," + dato.objectToDato(consulOri.getString("COD_PLANTA")) + "," + dato.objectToDato(tot_imponible) + "," + dato.objectToDato(afecto_imponible) + "," + dato.objectToDato(ndias_trab) + ")";
                if(consulDest.insert(sqlDest))
                {
                    if(++regProcesados % 50 == 0)
                        System.out.print(".");
                } else
                {
                    System.out.print("-");
                }
            } while(true);
            consulOri.close();
            consulDest.close();
            consulAux.close();
            consulAux2.close();
            consulAux3.close();
            consulAux4.close();
            OutMessage.OutMessagePrint("Registros Procesados (" + regProcesados + " de " + totReg + ")");
            OutMessage.OutMessagePrint("Fin de Traspaso Data Liquidacion Adicional de Remuneraciones");
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