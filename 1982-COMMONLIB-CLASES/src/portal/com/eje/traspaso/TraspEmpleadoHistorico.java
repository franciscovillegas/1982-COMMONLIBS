package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspEmpleadoHistorico extends Proceso
{

    public TraspEmpleadoHistorico(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Dato dato = new Dato();
        Validar validar = new Validar();
        Consulta consulOri = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Empleados Historicos");
        int regProcesados = 0;
        int totReg = 0;
        String sqlOri = " SELECT periodo, rut, nombre, fecha_nacim, estado_civil, empresa, sueldo,  nombres, ape_paterno,  ape_materno, digito_ver, cargo,  fecha_cargo, fecha_ingreso, fec_ter_cont, domicilio,  comuna, ciudad, telefono, sindicato, afp, isapre, pais,  corporacion,  sexo, fec_ing_cargo,  fec_ing_afp, fec_ing_isap,  cot_afp, mo_cot_afp, cot_adic, mo_cot_adic, ah_volunt, mo_ah_volunt,  dep_conven, mon_dep_conven, cot_salud, mon_salud, adic_salud, mon_adic_salud,  segsalcom,  banco, cta_cte,  wp_cod_empresa, wp_cod_planta, wp_cod_sucursal, sociedad,  wp_num_cargas_normale, wp_num_cargas_duplo, wp_num_cargas_materna, sueldo_base_mensual, moneda_sueldo_base_mensual,  fec_ing_hold, fecha_corporacion,  fec_afi_sist, cod_lug_prestacion, aplica_seguro_des, fec_ini_seguro_des, desc_tip_contrato,  ccosto, unidad, tip_jor_lab  FROM eje_ges_trabajador  WHERE wp_cod_empresa = " + empresa;
        consulOri.exec(sqlOri);
        do
        {
            if(!consulOri.next())
                break;
            totReg++;
            String delHistory = "delete from eje_ges_trabajador_historia where  periodo = " + dato.objectToDato(consulOri.getString("periodo")) + " AND empresa = " + dato.objectToDatoComillas(consulOri.getString("empresa")) + " AND rut = " + dato.objectToDato(consulOri.getString("rut"));
            consulDest.insert(delHistory);
            String sqlDest = "INSERT INTO eje_ges_trabajador_historia  (periodo, rut, nombre, fecha_nacim, estado_civil, empresa, sueldo,  nombres, ape_paterno,  ape_materno, digito_ver, cargo,  fecha_cargo, fecha_ingreso, fec_ter_cont, domicilio,  comuna, ciudad, telefono, sindicato, afp, isapre, pais,  corporacion,  sexo, fec_ing_cargo,  fec_ing_afp, fec_ing_isap,  cot_afp, mo_cot_afp, cot_adic, mo_cot_adic, ah_volunt, mo_ah_volunt,  dep_conven, mon_dep_conven, cot_salud, mon_salud, adic_salud, mon_adic_salud,  segsalcom,  banco, cta_cte,  wp_cod_empresa, wp_cod_planta, wp_cod_sucursal, sociedad,  wp_num_cargas_normale, wp_num_cargas_duplo, wp_num_cargas_materna, sueldo_base_mensual, moneda_sueldo_base_mensual,  fec_ing_hold, fecha_corporacion,  fec_afi_sist, cod_lug_prestacion, aplica_seguro_des, fec_ini_seguro_des, desc_tip_contrato,  ccosto, unidad, tip_jor_lab ) VALUES (" + dato.objectToDato(consulOri.getString("periodo")) + ", " + dato.objectToDato(consulOri.getString("rut")) + ", " + dato.objectToDatoComillas(consulOri.getString("nombre")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("fecha_nacim")) + ", " + dato.objectToDatoComillas(consulOri.getString("estado_civil")) + ", " + dato.objectToDatoComillas(consulOri.getString("empresa")) + ", " + dato.objectToDato(consulOri.getString("sueldo")) + ", " + dato.objectToDatoComillas(consulOri.getString("nombres")) + ", " + dato.objectToDatoComillas(consulOri.getString("ape_paterno")) + ", " + dato.objectToDatoComillas(consulOri.getString("ape_materno")) + ", " + dato.objectToDatoComillas(consulOri.getString("digito_ver")) + ", " + dato.objectToDatoComillas(consulOri.getString("cargo")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("fecha_cargo")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("fecha_ingreso")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("fec_ter_cont")) + ", " + dato.objectToDatoComillas(consulOri.getString("domicilio")) + ", " + dato.objectToDatoComillas(consulOri.getString("comuna")) + ", " + dato.objectToDatoComillas(consulOri.getString("ciudad")) + ", " + dato.objectToDatoComillas(consulOri.getString("telefono")) + ", " + dato.objectToDatoComillas(consulOri.getString("sindicato")) + ", " + dato.objectToDatoComillas(consulOri.getString("afp")) + ", " + dato.objectToDatoComillas(consulOri.getString("isapre")) + ", " + dato.objectToDatoComillas(consulOri.getString("pais")) + ", " + dato.objectToDato(consulOri.getString("corporacion")) + ", " + dato.objectToDatoComillas(consulOri.getString("sexo")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("fec_ing_cargo")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("fec_ing_afp")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("fec_ing_isap")) + ", " + dato.objectToDato(consulOri.getString("cot_afp")) + ", " + dato.objectToDatoComillas(consulOri.getString("mo_cot_afp")) + ", " + dato.objectToDato(consulOri.getString("cot_adic")) + ", " + dato.objectToDatoComillas(consulOri.getString("mo_cot_adic")) + ", " + dato.objectToDato(consulOri.getString("ah_volunt")) + ", " + dato.objectToDatoComillas(consulOri.getString("mo_ah_volunt")) + ", " + dato.objectToDato(consulOri.getString("dep_conven")) + ", " + dato.objectToDatoComillas(consulOri.getString("mon_dep_conven")) + ", " + dato.objectToDato(consulOri.getString("cot_salud")) + ", " + dato.objectToDatoComillas(consulOri.getString("mon_salud")) + ", " + dato.objectToDato(consulOri.getString("adic_salud")) + ", " + dato.objectToDatoComillas(consulOri.getString("mon_adic_salud")) + ", " + dato.objectToDatoComillas(consulOri.getString("segsalcom")) + ", " + dato.objectToDatoComillas(consulOri.getString("banco")) + ", " + dato.objectToDatoComillas(consulOri.getString("cta_cte")) + ", " + dato.objectToDato(consulOri.getString("wp_cod_empresa")) + ", " + dato.objectToDato(consulOri.getString("wp_cod_planta")) + ", " + dato.objectToDato(consulOri.getString("wp_cod_sucursal")) + ", " + dato.objectToDato(consulOri.getString("sociedad")) + ", " + dato.objectToDato(consulOri.getString("wp_num_cargas_normale")) + ", " + dato.objectToDato(consulOri.getString("wp_num_cargas_duplo")) + ", " + dato.objectToDato(consulOri.getString("wp_num_cargas_materna")) + ", " + dato.objectToDato(consulOri.getString("sueldo_base_mensual")) + ", " + dato.objectToDatoComillas(consulOri.getString("moneda_sueldo_base_mensual")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("fec_ing_hold")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("fecha_corporacion")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("fec_afi_sist")) + ", " + dato.objectToDato(consulOri.getString("cod_lug_prestacion")) + ", " + dato.objectToDatoComillas(consulOri.getString("aplica_seguro_des")) + ", " + dato.fechaToDatoComillas(consulOri.getValor("fec_ini_seguro_des")) + ", " + dato.objectToDatoComillas(consulOri.getString("desc_tip_contrato")) + ", " + dato.objectToDato(consulOri.getString("ccosto")) + ", " + dato.objectToDatoComillas(consulOri.getString("unidad")) + ", " + dato.objectToDatoComillas(consulOri.getString("tip_jor_lab")) + ")";
            OutMessage.OutMessagePrint("\n" + sqlDest + "\n");
            if(consulDest.insert(sqlDest))
            {
                regProcesados++;
                System.out.print(".");
            }
        } while(true);
        consulOri.close();
        consulDest.close();
        OutMessage.OutMessagePrint("Registros Procesados (" + regProcesados + " de " + totReg + ")");
        OutMessage.OutMessagePrint("Fin de Trapasa Empleados");
        return true;
    }

    private String periodoOri;
}