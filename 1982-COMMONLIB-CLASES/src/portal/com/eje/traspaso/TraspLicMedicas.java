package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Rut;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspLicMedicas extends Proceso
{

    public TraspLicMedicas(Connection conOrigen, Connection conDestino, String fecha_desde)
    {
        super(conOrigen, conDestino);
        this.fecha_desde = fecha_desde;
        historico = true;
    }

    public TraspLicMedicas(Connection conOrigen, Connection conDestino, String agno, String mes, String periDest)
    {
        super(conOrigen, conDestino);
        this.agno = agno;
        this.mes = mes;
        this.periDest = periDest;
        historico = false;
    }

    public boolean Run(int empresa, int periodos)
    {
        Tools tool = new Tools();
        Validar valida = new Validar();
        Consulta consul = new Consulta(conOrigen);
        Consulta consulTraspaso = new Consulta(conDestino);
        Dato dato = new Dato();
        OutMessage.OutMessagePrint("\nActualizando Licencias Medicas");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "DELETE FROM eje_ges_licencias_medicas WHERE wp_cod_empresa = " + empresa;
            OutMessage.OutMessagePrint(sqlDest);
            if(consulTraspaso.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            String sqlOrig = "select distinct a.cod_empresa as empresa, a.cod_planta as planta, a.cod_planta as planta, a.nro_trabajador as rut, b.nro_tipo_licencia as id_tipo_licencia, rtrim(b.tipo_licencia) as grupo_enfermedad, rtrim(f.descrip_diagnostic) as diagnostico, a.nro_dias_licencia as dias, a.fec_desde_licencia as fecha_inicio, a.fec_hasta_licencia as fecha_termino, rtrim(a.cod_medico) as rut_profesional, rtrim(a.dv_medico) as dv_profesional ,(rtrim(d.nombre_medico) + ' ' + rtrim(d.ape_paterno_medico) + ' ' + rtrim(d.ape_materno_medico)) as profesional, rtrim(e.descrip_especialid) as espe_profesional from licencia a,tipo_licencia b,empresa c,medicos d,especialidades e,diagnostico f, planta p where a.nro_tipo_licencia = b.nro_tipo_licencia and a.cod_empresa=c.cod_empresa and a.cod_medico = d.cod_medico and d.cod_espec_medica=e.cod_espec_medica and a.cod_diagnostico = f.cod_diagnostico  and a.nro_trabajador <> 16098105  and a.cod_planta = p.cod_planta  and p.cod_empresa = a.cod_empresa  and p.cod_vigente <> 'N'  and a.cod_empresa = " + empresa;
            OutMessage.OutMessagePrint(" :) ---> Actualizando Historico Licencias Medicas \nQuery: ");
            consul.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            String Rut = "";
            String Empresa = "";
            do
            {
                if(!consul.next())
                    break;
                totReg++;
                Rut rut = new Rut(consul.getString("rut"));
                Rut = rut.getRut();
                String rut_profesional = "";
                String rut_medico = consul.getString("rut_profesional");
                String dv_rut_medico = consul.getString("dv_profesional");
                if(rut.equals("1"))
                    rut_profesional = "N.D.";
                else
                    rut_profesional = rut_medico + "-" + dv_rut_medico;
                sqlDest = "INSERT INTO eje_ges_licencias_medicas  (rut,fecha_inicio,fecha_termino,diagnostico,dias,grupo_enfermedad,rut_profesional,profesional,espe_profesional,id_tipo_licencia,tipo_licencia,wp_cod_empresa, wp_cod_planta) VALUES (" + Rut + ", " + dato.fechaToDatoComillas(consul.getValor("fecha_inicio")) + ", " + dato.fechaToDatoComillas(consul.getValor("fecha_termino")) + ", '" + consul.getString("diagnostico") + "', '" + consul.getString("dias") + "', '" + consul.getString("grupo_enfermedad") + "', '" + rut_profesional + "', '" + valida.validarDato(consul.getString("profesional")).replaceAll("'", "\264") + "', '" + consul.getString("espe_profesional") + "', '" + consul.getString("id_tipo_licencia") + "', '" + " " + "', " + consul.getString("empresa") + ", " + consul.getString("planta") + ")";
                if(consulTraspaso.insert(sqlDest) && ++regProcesados % 200 == 0)
                    System.out.print(".");
            } while(true);
            OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
            consul.close();
            consulTraspaso.close();
            OutMessage.OutMessagePrint("\nFin Licencias Medicas");
        }
        consulAuxx.close();
        return true;
    }

    private String fecha_desde;
    private boolean historico;
    private String agno;
    private String mes;
    private String periDest;
}