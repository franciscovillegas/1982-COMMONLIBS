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

public class TraspEmpleado extends Proceso
{

    public TraspEmpleado(Connection conOrigen, Connection conDestino, String periodoOri)
    {
        super(conOrigen, conDestino);
        this.periodoOri = periodoOri;
    }

    public boolean Run(int empresa, int periodo)
    {
        Dato dato = new Dato();
        Validar validar = new Validar();
        Date fecha_actual = new Date();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat mesFormat = new SimpleDateFormat("MM");
        SimpleDateFormat agnoFormat = new SimpleDateFormat("yyyy");
        String fecha = simpledateformat.format(fecha_actual);
        String mes = mesFormat.format(fecha_actual);
        String agno = agnoFormat.format(fecha_actual);
        Consulta consulOri = new Consulta(conOrigen);
        Consulta consulOriAux = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Empleados");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
        	String sqlDest = "delete from eje_ges_trabajador where empresa =" + empresa;
        	if(consulDest.insert(sqlDest))
        		OutMessage.OutMessagePrint("datos de eje_ges_trabajador eliminados ");
            String sqlOrig = "SELECT  PER.RUT_TRABAJADOR, PER.DV_RUT_TRABAJADOR, (RTRIM(PER.NOMBRE) + ' ' + RTRIM(PER.APE_PATERNO_TRABAJ) +' '+RTRIM(PER.APE_MATERNO_TRABAJ)) as nomcom,  RTRIM(PER.NOMBRE) as NOMBRE, RTRIM(PER.APE_PATERNO_TRABAJ) as APE_PATERNO_TRABAJ, RTRIM(PER.APE_MATERNO_TRABAJ) as APE_MATERNO_TRABAJ, PER.COD_SEXO,  PER.FEC_NACIMIENTO, PER.COD_ESTADO_CIVIL, PER.NACIONALIDAD, PER.COD_CARGO, PER.FEC_INI_CONTRATO, PER.FEC_FIN_CONTR_VIGE,  PER.COD_SINDICATO,  PER.COD_AFP, PER.FEC_INCORPORAC_AFP,  PER.COD_ISAPRE, PER.FEC_INI_ISAPRE, PER.UNID_COB_MTO_PACTA, PER.ADICIONAL_ISAPRE, PER.MONE_VAL_ADIC_SALU, PER.MTO_PACTADO_ISAPRE,  PER.COD_BANCO, PER.NRO_CTA_CTE_BANCAR,  PER.COD_EMPRESA, PER.COD_PLANTA, PER.COD_SUCURSAL, PER.COD_CENTRO_COSTO, PER.COD_UNIDAD_ADMINIS,  PER.MTO_DCTO_CTA_AHORR, PER.UNID_COBRO_CTA_AHO,  PER.MTO_COTIZ_VOLUNTAR, PER.UNID_COB_MTO_VOLUN,  PER.SUELDO_MENSUAL, PER.MONEDA_SUELDO,  PER.NUM_CARGAS_NORMALE, PER.NUM_CARGAS_DUPLO, PER.NUM_CARGAS_MATERNA, PER.CASILLA_E_MAIL,   PER.FEC_ING_SIST_PREVI, PER.FEC_ANTIGUEDAD,  PER.COD_LUGPRESTACION, PER.APLICA_SEGURO_DES, PER.FEC_INI_SEGURO_DES, PER.CODIGO_TIPO_CONTRA,RTRIM(PER.CONDIC_PREVISIONAL) AS CONDIC_PREVISIONAL,PER.COD_CAJA_PREVISION,PER.COD_TIPO_TRABAJADO,PER.VALOR_ADIC_1,PER.VALOR_ADIC_2,PER.COD_TIPO_SEGURO,PER.EXENTO_SEGURO,PER.EXENTO_FONDO FROM PERSONAL as PER, PLANTA as PLA  WHERE PER.COD_VIGEN_TRABAJAD = 'S'  AND PER.COD_EMPRESA = PLA.COD_EMPRESA  AND PER.COD_PLANTA = PLA.COD_PLANTA  AND PLA.COD_VIGENTE <> 'N'  AND PER.COD_EMPRESA = " + empresa + " AND (PER.FEC_FIN_CONTR_VIGE > '" + fecha + "' " + " OR PER.FEC_FIN_CONTR_VIGE is null " + ") " + " AND PER.NRO_TRABAJADOR <> 16098105 ";
            OutMessage.OutMessagePrint(" :) ---> Actualizando Empleados \nQuery: " + sqlOrig);
            consulOri.exec(sqlOrig);
            if(mes.length() == 1)
                mes = "0" + mes;
            String periodo_hoy = agno + mes;
            int regProcesados = 0;
            int totReg = 0;
            String SQL = "";
            while(consulOri.next())
            {
                totReg++;
                Rut rut = new Rut(consulOri.getString("RUT_TRABAJADOR"));
                String d_v = consulOri.getString("DV_RUT_TRABAJADOR");
                String nom_completo = consulOri.getString("nomcom");
                String nombre = consulOri.getString("NOMBRE");
                String apellido_pat = consulOri.getString("APE_PATERNO_TRABAJ");
                String apellido_amt = consulOri.getString("APE_MATERNO_TRABAJ");
                String id_sexo = validar.validarDato(consulOri.getString("COD_SEXO"), "n");
                String id_estado_civil = validar.validarDato(consulOri.getString("COD_ESTADO_CIVIL"), "n");
                String id_nacion = validar.validarCortarString(consulOri.getString("NACIONALIDAD"), 3, "nnnnnnn").toUpperCase();
                Object fec_naci = consulOri.getValor("FEC_NACIMIENTO");
                String id_cargo = consulOri.getString("COD_CARGO");
                String tipo_contrato = consulOri.getString("CODIGO_TIPO_CONTRA");
                String condic_previsional = validar.validarDato(consulOri.getString("CONDIC_PREVISIONAL"),"N");
                String caja_prevision = consulOri.getString("COD_CAJA_PREVISION");
                String exento_seguro = consulOri.getString("EXENTO_SEGURO");
                exento_seguro = (exento_seguro==null || exento_seguro.equals(" ") || exento_seguro.equals("N"))? "N":"S";
                String exento_fondo = consulOri.getString("EXENTO_FONDO");
                exento_fondo = (exento_fondo==null || exento_fondo.equals(" ") || exento_fondo.equals("N"))? "N":"S";
                String valor_adic_1 =  validar.validarDato(consulOri.getString("VALOR_ADIC_1"),"N");
                String valor_adic_2 =  validar.validarDato(consulOri.getString("VALOR_ADIC_2"),"N");
                String cod_tipo_trabajado = validar.validarDato(consulOri.getString("COD_TIPO_TRABAJADO"),"N");
                String desc_tipo_contrato = null;
                valor_adic_1 = ("N".equals(valor_adic_1) || "0".equals(valor_adic_1)) ? "0":"$" + valor_adic_1 + " unidad tiempo presencial";
                valor_adic_2 = ("N".equals(valor_adic_2) || "0".equals(valor_adic_2)) ? "0":"$" + valor_adic_2 + " unidad tiempo ausente";
                cod_tipo_trabajado = "D".equals(cod_tipo_trabajado) ? "D" : cod_tipo_trabajado; 
                if(tipo_contrato.equals("F"))
                    desc_tipo_contrato = "Plazo Fijo";
                else
                if(tipo_contrato.equals("P"))
                    desc_tipo_contrato = "Plazo Indefinido";
                Object fec_inicio_cargo = null;
                Object fec_ingreso = consulOri.getValor("FEC_INI_CONTRATO");
                Object fec_fin_contrato = consulOri.getValor("FEC_FIN_CONTR_VIGE");
                String id_sindicato = consulOri.getString("COD_SINDICATO");
                String sueldo_base = consulOri.getString("SUELDO_MENSUAL");
                String moneda_sueldo_base = consulOri.getString("MONEDA_SUELDO");
                String email = consulOri.getString("CASILLA_E_MAIL");
                Object fec_ing_sist_previ = consulOri.getValor("FEC_ING_SIST_PREVI");
                Object fec_antiguedad = consulOri.getValor("FEC_ANTIGUEDAD");
                String cod_lug_prestacion = consulOri.getString("COD_LUGPRESTACION");
                String aplica_seguro_des = consulOri.getString("APLICA_SEGURO_DES");
                Object fec_ini_seguro_des = consulOri.getValor("FEC_INI_SEGURO_DES");
                if("f".equalsIgnoreCase(id_sexo))
                    id_sexo = "F";
                if("m".equalsIgnoreCase(id_sexo))
                    id_sexo = "M";
                String id_afp = consulOri.getString("COD_AFP");
                Object fec_inicio_afp = consulOri.getValor("FEC_INCORPORAC_AFP");
                String sqlAux = "SELECT PJE_COTIZ_PREVISIO,PJE_EX_SEG_INVALID FROM AFP WHERE COD_VIGENTE = 'S' AND COD_AFP = " + consulOri.getString("COD_AFP");
                consulOriAux.exec(sqlAux);
                float AfpCotiza = 0.0F;
                if(consulOriAux.next()) {
                	AfpCotiza = "J".equals(condic_previsional) ? consulOriAux.getFloat("PJE_EX_SEG_INVALID") : consulOriAux.getFloat("PJE_COTIZ_PREVISIO");
                	AfpCotiza = "S".equals(exento_fondo) ? 0.0f : AfpCotiza; 
                }
                float AfpCotizaM = consulOri.getFloat("SUELDO_MENSUAL");
                String AfpCotizaMoneda = consulOri.getString("UNID_COBRO_CTA_AHO");
                float AfpCotizaAd = consulOri.getFloat("MTO_COTIZ_VOLUNTAR");
                float AfpCotizaAdM = 0.0F;
                String AfpCotizaAdMoneda = consulOri.getString("UNID_COB_MTO_VOLUN");
                float AfpAhorroVo = consulOri.getFloat("MTO_DCTO_CTA_AHORR");
                float AfpAhorroVoM = consulOri.getFloat("SUELDO_MENSUAL");
                String AfpAhorroVoMoneda = consulOri.getString("UNID_COBRO_CTA_AHO");
                float AfpDepConve = 0.0F;
                float AfpDepConveM = 0.0F;
                String AfpDepConveMoneda = null;
                String id_isapre = consulOri.getString("COD_ISAPRE");
                Object fec_inicio_isapre = consulOri.getValor("FEC_INI_ISAPRE");
                String IsapreCotiza = consulOri.getString("MTO_PACTADO_ISAPRE");
                String IsapreCotizaM = "0";
                String IsapreCotizaMoneda = consulOri.getString("UNID_COB_MTO_PACTA");
                String IsapreCotizaAd = consulOri.getString("ADICIONAL_ISAPRE");
                String IsapreCotizaAdM = "0";
                String IsapreCotizaAdMoneda = consulOri.getString("MONE_VAL_ADIC_SALU");
                String seg_sal_com = "N";
                String cod_tipo_seguro = validar.validarDato( consulOri.getString("COD_TIPO_SEGURO") );
                if( cod_tipo_seguro.equals("1") )
                    seg_sal_com = "S";
                String cod_empresa = consulOri.getString("COD_EMPRESA");
                String cod_planta = consulOri.getString("COD_PLANTA");
                String cod_sucursal = consulOri.getString("COD_SUCURSAL");
                String id_ccosto = consulOri.getString("COD_CENTRO_COSTO");
                String id_unidad = consulOri.getString("COD_UNIDAD_ADMINIS");
                String n_empresa = null;
                sqlAux = "SELECT RTRIM(EMPRESA) AS EMPRESA  FROM EMPRESA  WHERE COD_EMPRESA = " + cod_empresa;
                consulOriAux.exec(sqlAux);
                if(consulOriAux.next())
                    n_empresa = consulOriAux.getString("EMPRESA");
                String nro_cargas_normales = validar.validarDato(consulOri.getString("NUM_CARGAS_NORMALE"),"0");
                String nro_cargas_duplo = validar.validarDato(consulOri.getString("NUM_CARGAS_DUPLO"),"0");
                String nro_cargas_materna = validar.validarDato(consulOri.getString("NUM_CARGAS_MATERNA"),"0");
                String direccion = null;
                String num_via = null;
                String id_comuna = null;
                String comuna = null;
                String ciudad = null;
                String telefono = null;
                String tipo_via = null;
                String depto = null;
                sqlAux = "SELECT RTRIM(PERSONAL.DIRECCION) as DIRECCION,  PERSONAL.COD_COMUNA,  PERSONAL.COD_CIUDAD,  RTRIM(PERSONAL.FONO) as FONO, RTRIM(PERSONAL.FONO2) as FONO2,  RTRIM(COMUNA.COMUNA) as COMUNA,  RTRIM(CIUDAD.CIUDAD) as CIUDAD  FROM PERSONAL, COMUNA, CIUDAD WHERE (PERSONAL.COD_COMUNA = COMUNA.COD_COMUNA) AND (PERSONAL.COD_CIUDAD = CIUDAD.COD_CIUDAD) AND (RUT_TRABAJADOR = '" + rut.getRut() + "')";
                consulOriAux.exec(sqlAux);
                if(consulOriAux.next())
                {
                    direccion = consulOriAux.getString("DIRECCION");
                    id_comuna = consulOriAux.getString("COD_COMUNA");
                    comuna = consulOriAux.getString("COMUNA");
                    ciudad = consulOriAux.getString("CIUDAD");
                    telefono = consulOriAux.getString("FONO") + " - " + consulOriAux.getString("FONO2");
                }
                String horas_jornada = "";
                String dias_jornada = "";
                String tip_jor_lab = null;
                sqlAux = "SELECT RTRIM(J_T.JORNADA) as JORNADA,  J_T.HORAS_SEMANALES, J_T.DIAS_SEMANALES  FROM PERSONAL as P, JORNADA_TRABAJO as J_T WHERE (P.COD_JORNADA = J_T.COD_JORNADA) AND (P.RUT_TRABAJADOR = '" + rut.getRut() + "')" + "AND (P.COD_EMPRESA = " + empresa + ")";
                consulOriAux.exec(sqlAux);
                if(consulOriAux.next())
                {
                    tip_jor_lab = consulOriAux.getString("JORNADA");
                    horas_jornada = consulOriAux.getString("HORAS_SEMANALES");
                    dias_jornada = consulOriAux.getString("Dias_semanales");
                }
                String est_civil = null;
                if("F".equalsIgnoreCase(id_sexo))
                    if("C".equalsIgnoreCase(id_estado_civil))
                        est_civil = "Casada";
                    else
                    if("S".equalsIgnoreCase(id_estado_civil))
                        est_civil = "Soltera";
                if("M".equalsIgnoreCase(id_sexo))
                    if("C".equalsIgnoreCase(id_estado_civil))
                        est_civil = "Casado";
                    else
                    if("S".equalsIgnoreCase(id_estado_civil))
                        est_civil = "Soltero";
                String banco = null;
                String cta_cte = null;
                sqlAux = "SELECT PERSONAL.NRO_CTA_CTE_BANCAR, RTRIM(BANCO.BANCO) as BANCO FROM PERSONAL, BANCO WHERE (PERSONAL.COD_BANCO = BANCO.COD_BANCO) AND (BANCO.COD_VIGENTE = 'S') AND (PERSONAL.COD_EMPRESA = " + cod_empresa + " ) " + "AND (PERSONAL.COD_PLANTA = " + cod_planta + " ) " + "AND (PERSONAL.RUT_TRABAJADOR = '" + rut.getRut() + "')";
                consulOriAux.exec(sqlAux);
                if(consulOriAux.next())
                {
                    banco = consulOriAux.getString("BANCO");
                    cta_cte = consulOriAux.getString("NRO_CTA_CTE_BANCAR");
                }
                //sqlDest = "INSERT INTO eje_ges_trabajador (periodo, rut, nombre, fecha_nacim, estado_civil, empresa, sueldo,  nombres, ape_paterno,  ape_materno, digito_ver, cargo,  fecha_cargo, fecha_ingreso, fec_ter_cont, domicilio,  comuna, ciudad, telefono, sindicato, afp, isapre, pais,  corporacion,  sexo, fec_ing_cargo,  fec_ing_afp, fec_ing_isap,  cot_afp, mo_cot_afp, cot_adic, mo_cot_adic, ah_volunt, mo_ah_volunt,  dep_conven, mon_dep_conven, cot_salud, mon_salud, adic_salud, mon_adic_salud,  segsalcom,  banco, cta_cte,  wp_cod_empresa, wp_cod_planta, wp_cod_sucursal, sociedad,  wp_num_cargas_normale,wp_num_cargas_duplo, wp_num_cargas_materna, sueldo_base_mensual, moneda_sueldo_base_mensual,  fec_ing_hold, fecha_corporacion,  fec_afi_sist, cod_lug_prestacion, aplica_seguro_des, fec_ini_seguro_des, desc_tip_contrato,  ccosto, unidad, tip_jor_lab,jubilado,valor_adic_1,valor_adic_2,cod_tipo_trabajado,e_mail ) VALUES (" + dato.objectToDato(periodo_hoy) + ", " + dato.objectToDato(rut.getRut()) + ", " + dato.objectToDatoComillas(nom_completo) + ", " + dato.fechaToDatoComillas(fec_naci) + ", " + dato.objectToDatoComillas(est_civil) + ", " + dato.objectToDatoComillas(cod_empresa) + ", " + dato.objectToDato(sueldo_base) + ", " + dato.objectToDatoComillas(nombre) + ", " + dato.objectToDatoComillas(apellido_pat) + ", " + dato.objectToDatoComillas(apellido_amt) + ", " + dato.objectToDatoComillas(d_v) + ", " + dato.objectToDatoComillas(id_cargo) + ", " + dato.fechaToDatoComillas(fec_inicio_cargo) + ", " + dato.fechaToDatoComillas(fec_ingreso) + ", " + dato.fechaToDatoComillas(fec_fin_contrato) + ", " + dato.objectToDatoComillas(direccion) + ", " + dato.objectToDatoComillas(comuna) + ", " + dato.objectToDatoComillas(ciudad) + ", " + dato.objectToDatoComillas(telefono) + ", " + dato.objectToDatoComillas(id_sindicato) + ", " + dato.objectToDatoComillas(id_afp) + ", " + dato.objectToDatoComillas(id_isapre) + ", " + dato.objectToDatoComillas(id_nacion) + ", " + dato.objectToDato(null) + ", " + dato.objectToDatoComillas(id_sexo) + ", " + dato.fechaToDatoComillas(fec_inicio_cargo) + ", " + dato.fechaToDatoComillas(fec_inicio_afp) + ", " + dato.fechaToDatoComillas(fec_inicio_isapre) + ", " + AfpCotiza + ", " + dato.objectToDatoComillas(AfpCotizaMoneda) + ", " + AfpCotizaAd + ", " + dato.objectToDatoComillas(AfpCotizaAdMoneda) + ", " + AfpAhorroVo + ", " + dato.objectToDatoComillas(AfpAhorroVoMoneda) + ", " + AfpDepConve + ", " + dato.objectToDatoComillas(AfpDepConveMoneda) + ", " + dato.objectToDato(IsapreCotiza) + ", " + dato.objectToDatoComillas(IsapreCotizaMoneda) + ", " + dato.objectToDato(IsapreCotizaAd) + ", " + dato.objectToDatoComillas(IsapreCotizaAdMoneda) + ", " + dato.objectToDatoComillas(seg_sal_com) + ", " + dato.objectToDatoComillas(banco) + ", " + dato.objectToDatoComillas(cta_cte) + ", " + dato.objectToDato(cod_empresa) + ", " + dato.objectToDato(cod_planta) + ", " + dato.objectToDato(cod_sucursal) + ", " + dato.objectToDato(cod_planta) + ", " + dato.objectToDato(nro_cargas_normales) + ", " + dato.objectToDato(nro_cargas_duplo) + ", " + dato.objectToDato(nro_cargas_materna) + ", " + dato.objectToDato(sueldo_base) + ", " + dato.objectToDatoComillas(moneda_sueldo_base) + ", " + dato.fechaToDatoComillas(fec_antiguedad) + ", " + dato.fechaToDatoComillas(fec_antiguedad) + ", " + dato.fechaToDatoComillas(fec_ing_sist_previ) + ", " + dato.objectToDato(cod_lug_prestacion) + ", " + dato.objectToDatoComillas(aplica_seguro_des) + ", " + dato.fechaToDatoComillas(fec_ini_seguro_des) + ", " + dato.objectToDatoComillas(desc_tipo_contrato) + ", " + dato.objectToDato(id_ccosto) + ", " + dato.objectToDatoComillas(id_unidad) + ", " + dato.objectToDatoComillas(tip_jor_lab)  + "," + dato.objectToDatoComillas(condic_previsional) + "," + dato.objectToDatoComillas(valor_adic_1) + "," + dato.objectToDatoComillas(valor_adic_2) + "," + dato.objectToDatoComillas(cod_tipo_trabajado) + "," + dato.objectToDatoComillas(email) + ")";
                sqlDest = String.valueOf(new StringBuilder("INSERT INTO eje_ges_trabajador  (periodo, rut, nombre, fecha_nacim, estado_civil, empresa, ")
                		.append("sueldo,  nombres, ape_paterno,  ape_materno, digito_ver, cargo,  fecha_cargo, fecha_ingreso, fec_ter_cont, domicilio, ")
                		.append("comuna, ciudad, telefono, sindicato, afp, isapre, pais,  corporacion,  sexo, fec_ing_cargo,  fec_ing_afp, fec_ing_isap, ")
                		.append("cot_afp, mo_cot_afp, cot_adic, mo_cot_adic, ah_volunt, mo_ah_volunt,  dep_conven, mon_dep_conven, cot_salud, mon_salud, ")
                		.append("adic_salud, mon_adic_salud,  segsalcom,  banco, cta_cte,  wp_cod_empresa, wp_cod_planta, wp_cod_sucursal, sociedad,  ")
                		.append("wp_num_cargas_normale,wp_num_cargas_duplo, wp_num_cargas_materna, sueldo_base_mensual, moneda_sueldo_base_mensual,  ")
                		.append("fec_ing_hold, fecha_corporacion,  fec_afi_sist, cod_lug_prestacion, aplica_seguro_des, fec_ini_seguro_des, ")
                		.append("desc_tip_contrato,  ccosto, unidad, tip_jor_lab,jubilado,e_mail ) VALUES (")
                		.append(dato.objectToDato(periodo_hoy)).append(", ").append(dato.objectToDato(rut.getRut())).append(", ")
                		.append(dato.objectToDatoComillas(nom_completo)).append(", ").append(dato.fechaToDatoComillas(fec_naci)).append(", ")
                		.append(dato.objectToDatoComillas(est_civil)).append(", ").append(dato.objectToDatoComillas(cod_empresa)).append(", ")
                		.append(dato.objectToDato(sueldo_base)).append(", ").append(dato.objectToDatoComillas(nombre)).append(", ")
                		.append(dato.objectToDatoComillas(apellido_pat)).append(", ").append(dato.objectToDatoComillas(apellido_amt)).append(", ")
                		.append(dato.objectToDatoComillas(d_v)).append(", ").append(dato.objectToDatoComillas(id_cargo)).append(", ")
                		.append(dato.fechaToDatoComillas(fec_inicio_cargo)).append(", ").append(dato.fechaToDatoComillas(fec_ingreso)).append(", ")
                		.append(dato.fechaToDatoComillas(fec_fin_contrato)).append(", ").append(dato.objectToDatoComillas(direccion)).append(", ")
                		.append(dato.objectToDatoComillas(comuna)).append(", ").append(dato.objectToDatoComillas(ciudad)).append(", ")
                		.append(dato.objectToDatoComillas(telefono)).append(", ").append(dato.objectToDatoComillas(id_sindicato)).append(", ")
                		.append(dato.objectToDatoComillas(id_afp)).append(", ").append(dato.objectToDatoComillas(id_isapre)).append(", ")
                		.append(dato.objectToDatoComillas(id_nacion)).append(", ").append(dato.objectToDato(null)).append(", ")
                		.append(dato.objectToDatoComillas(id_sexo)).append(", ").append(dato.fechaToDatoComillas(fec_inicio_cargo)).append(", ")
                		.append(dato.fechaToDatoComillas(fec_inicio_afp)).append(", ").append(dato.fechaToDatoComillas(fec_inicio_isapre)).append(", ")
                		.append(AfpCotiza).append(", ").append(dato.objectToDatoComillas(AfpCotizaMoneda)).append(", ")
                		.append(AfpCotizaAd).append(", ").append(dato.objectToDatoComillas(AfpCotizaAdMoneda)).append(", ")
                		.append(AfpAhorroVo).append(", ").append(dato.objectToDatoComillas(AfpAhorroVoMoneda)).append(", ")
                		.append(AfpDepConve).append(", ").append(dato.objectToDatoComillas(AfpDepConveMoneda)).append(", ")
                		.append(dato.objectToDato(IsapreCotiza)).append(", ").append(dato.objectToDatoComillas(IsapreCotizaMoneda)).append(", ")
                		.append(dato.objectToDato(IsapreCotizaAd)).append(", ").append(dato.objectToDatoComillas(IsapreCotizaAdMoneda)).append(", ")
                		.append(dato.objectToDatoComillas(seg_sal_com)).append(", ").append(dato.objectToDatoComillas(banco)).append(", ")
                		.append(dato.objectToDatoComillas(cta_cte)).append(", ").append(dato.objectToDato(cod_empresa)).append(", ")
                		.append(dato.objectToDato(cod_planta)).append(", ").append(dato.objectToDato(cod_sucursal)).append(", ")
                		.append(dato.objectToDato(cod_planta)).append(", ").append(dato.objectToDato(nro_cargas_normales)).append(", ")
                		.append(dato.objectToDato(nro_cargas_duplo)).append(", ").append(dato.objectToDato(nro_cargas_materna)).append(", ")
                		.append(dato.objectToDato(sueldo_base)).append(", ").append(dato.objectToDatoComillas(moneda_sueldo_base)).append(", ")
                		.append(dato.fechaToDatoComillas(fec_antiguedad)).append(", ").append(dato.fechaToDatoComillas(fec_antiguedad)).append(", ")
                		.append(dato.fechaToDatoComillas(fec_ing_sist_previ)).append(", ").append(dato.objectToDato(cod_lug_prestacion)).append(", ")
                		.append(dato.objectToDatoComillas(aplica_seguro_des)).append(", ").append(dato.fechaToDatoComillas(fec_ini_seguro_des)).append(", ")
                		.append(dato.objectToDatoComillas(desc_tipo_contrato)).append(", ").append(dato.objectToDato(id_ccosto)).append(", ")
                		.append(dato.objectToDatoComillas(id_unidad)).append(", ").append(dato.objectToDatoComillas(tip_jor_lab)).append(",")
                		.append(dato.objectToDatoComillas(condic_previsional)).append(",")
                        .append(dato.objectToDatoComillas(email)).append(")"));
                
                OutMessage.OutMessagePrint("\n" + sqlDest + "\n");
                if(consulDest.insert(sqlDest))
                {
                    regProcesados++;
                    System.out.print(".");
                }
            }
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