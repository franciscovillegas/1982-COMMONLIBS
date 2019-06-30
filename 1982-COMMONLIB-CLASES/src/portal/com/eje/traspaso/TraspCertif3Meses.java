package portal.com.eje.traspaso;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspCertif3Meses extends Proceso
{

    public TraspCertif3Meses(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Dato dato = new Dato();
        Validar valida = new Validar();
        Consulta consulOri = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        Consulta consulAux = new Consulta(conOrigen);
        Consulta consulAux2 = new Consulta(conOrigen);
        OutMessage.OutMessagePrint("\nActualizando Certificado Antiguedad");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "delete from eje_ges_certif_antiguedad where empresa = " + empresa;
            OutMessage.OutMessagePrint("--->eLIMINANDO DATOS...: " + sqlDest);
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("datos de certif_antiguedad eliminados");
            Date fecha_actual = new Date();
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = simpledateformat.format(fecha_actual);
            int regProcesados = 0;
            int totReg = 0;
            String sqlOrig = " SELECT HL.MONTO_CANCEL_IMPTO, HL.MTO_CANCELA_PREVIS, HL.MONTO_SALUD, HL.NRO_TRABAJADOR, HL.NRO_DIAS_ASISTIDOS,  HL.NRO_CARGAS_NORMALE, HL.NRO_CARGAS_DUPLO, HL.NRO_CARGAS_MATERNA, V.VAL_MONEDA_UNIDAD AS UF,  HL.MES_PERIODO, HL.ANO_PERIODO, HL.COD_PLANTA, HL.COD_EMPRESA FROM HISTORICO_LIQUIDAC AS HL, PERSONAL AS PER, PLANTA AS PLA, VALOR_MONEDA AS V WHERE HL.NRO_TRABAJADOR <> 16098105  AND HL.NRO_TRABAJADOR = PER.NRO_TRABAJADOR  AND PER.COD_EMPRESA = PLA.COD_EMPRESA  AND HL.COD_EMPRESA = PER.COD_EMPRESA  AND HL.COD_PLANTA = PER.COD_PLANTA  AND PER.COD_PLANTA = PLA.COD_PLANTA  AND HL.MES_PERIODO = DATEPART(mm,V.FEC_VALOR_MONEDA) AND HL.ANO_PERIODO = DATEPART(yyyy,V.FEC_VALOR_MONEDA) AND V.COD_UNIDAD_COBRO = 'UF'  AND PLA.COD_VIGENTE <> 'N'  AND (PER.FEC_FIN_CONTR_VIGE > '" + fecha + "' " + " OR PER.FEC_FIN_CONTR_VIGE is null) " + " AND HL.COD_EMPRESA = " + empresa + " ORDER BY HL.NRO_TRABAJADOR ";
            OutMessage.OutMessagePrint("\nActualizando certificado antiguedad : " + sqlOrig);
            consulOri.exec(sqlOrig);
            do
            {
                if(!consulOri.next())
                    break;
                totReg++;
                int rut = consulOri.getInt("NRO_TRABAJADOR");
                String mes_periodo = valida.validarDato(consulOri.getString("MES_PERIODO"), "00");
                String agno_periodo = consulOri.getString("ANO_PERIODO");
                String cod_planta = consulOri.getString("COD_PLANTA");
                String cod_empresa = consulOri.getString("COD_EMPRESA");
                String impto = valida.validarDato(consulOri.getString("MONTO_CANCEL_IMPTO"), "0");
                String impos_salud = valida.validarDato(consulOri.getString("MONTO_SALUD"), "0");
                String impos_previs = valida.validarDato(consulOri.getString("MTO_CANCELA_PREVIS"), "0");
                String cargas_normales = valida.validarDato(consulOri.getString("NRO_CARGAS_NORMALE"), "0");
                String cargas_duplos = valida.validarDato(consulOri.getString("NRO_CARGAS_DUPLO"), "0");
                String cargas_maternas = valida.validarDato(consulOri.getString("NRO_CARGAS_MATERNA"), "0");
                String dias_trabajados = valida.validarDato(consulOri.getString("NRO_DIAS_ASISTIDOS"), "0");
                String uf = valida.validarDato(consulOri.getString("UF"), "0");
                
                String mes = null;
                if(mes_periodo.length() == 1) {  mes = String.valueOf(new StringBuilder("0").append(mes_periodo)); }
                else {  mes = mes_periodo; }
                
                String periodo_string = agno_periodo + mes;
                String sqlAux = "SELECT COD_HABER, VALOR_TRANSAC_PESO  FROM LIQUIDACIO_HABERES  WHERE  COD_EMPRESA = " + cod_empresa + " AND COD_PLANTA = " + cod_planta + " AND NRO_TRABAJADOR = " + rut + " AND ANO_PERIODO = " + agno_periodo + " AND MES_PERIODO = " + mes_periodo;
                String comision_1 = "0";
                String comision_2 = "0";
                consulAux.exec(sqlAux);
                do
                {
                    if(!consulAux.next())
                        break;
                    String cod_haber = consulAux.getString("COD_HABER");
                    String valor = consulAux.getString("VALOR_TRANSAC_PESO");
                    if(cod_haber.equals("163"))
                        comision_1 = valor;
                    else
                    if(cod_haber.equals("166"))
                        comision_2 = valor;
                } while(true);
                String sqlAux2 = "SELECT COD_HABER, VALOR_TRANSAC_PESO  FROM LIQUIDACIO_HABERES  WHERE  COD_EMPRESA = " + cod_empresa + " AND COD_PLANTA = " + cod_planta + " AND NRO_TRABAJADOR = " + rut + " AND ANO_PERIODO = " + agno_periodo + " AND MES_PERIODO = " + mes_periodo;
                String sueldo = "0";
                String bono_vacaciones = "0";
                String bono_navidad = "0";
                String bono_fiestas_patrias = "0";
                consulAux2.exec(sqlAux2);
                do
                {
                    if(!consulAux2.next())
                        break;
                    String cod_haber = consulAux2.getString("COD_HABER");
                    String valor = consulAux2.getString("VALOR_TRANSAC_PESO");
                    if(cod_haber.equals("1"))
                        sueldo = valor;
                    else
                    if(cod_haber.equals("553"))
                        bono_vacaciones = valor;
                    else
                    if(cod_haber.equals("556"))
                        bono_navidad = valor;
                    else
                    if(cod_haber.equals("558"))
                        bono_fiestas_patrias = valor;
                } while(true);
                sqlDest = "INSERT INTO eje_ges_certif_antiguedad (periodo,empresa, planta, rut, dias_trabajados,cargas_normales, cargas_duplos, cargas_maternas,  impos_salud, impos_previs, mto_cancel_impto, uf,  comision_1, comision_2, sueldo_base, bono_vacaciones, bono_navidad, bono_fiestas_patrias) VALUES ( " + periodo_string + ", " + cod_empresa + ", " + cod_planta + ", " + rut + ", " + dias_trabajados + ", " + cargas_normales + ", " + cargas_duplos + ", " + cargas_maternas + ", " + impos_salud + ", " + impos_previs + ", " + impto + ", " + uf + ", " + comision_1 + ", " + comision_2 + ", " + sueldo + ", " + bono_vacaciones + ", " + bono_navidad + ", " + bono_fiestas_patrias + ")";
                if(consulDest.insert(sqlDest))
                {
                    if(++regProcesados % 50 == 0)
                        System.out.print(".");
                } else
                {
                    System.out.print("-");
                }
            } while(true);
            consulAux.close();
            consulAux2.close();
            consulOri.close();
            consulDest.close();
            OutMessage.OutMessagePrint("Registros Procesados (" + regProcesados + " de " + totReg + ")");
            OutMessage.OutMessagePrint("Fin de Traspaso Data Certificado Antiguedad");
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