package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Rut;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspHorasExtras extends Proceso
{

    public TraspHorasExtras(Connection conOrigen, Connection conDestino, String fecha_desde)
    {
        super(conOrigen, conDestino);
        this.fecha_desde = fecha_desde;
        historico = true;
    }

    public TraspHorasExtras(Connection conOrigen, Connection conDestino, String agno, String mes, String periDest)
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
        OutMessage.OutMessagePrint("\nActualizando Horas Extras");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "DELETE FROM eje_ges_sobretiempos where compania = " + empresa;
            if(consulTraspaso.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            String sqlOrig = "select a.cod_empresa as compania,b.nro_trabajador as rut,b.cantidad as num_horas, b.mes_cons_info_hist, b.anos_consev_histor, (convert(char (4), b.anos_consev_histor)) +'' + (convert(char (2), b.mes_cons_info_hist)) as periodo  from sobretiempo a, histor_sobretiempo b, planta p  where a.cod_sobretiempo = b.cod_sobretiempo  and b.nro_trabajador <> 16098105  and p.cod_empresa = a.cod_empresa  and p.cod_vigente <> 'N'  and a.cod_empresa = b.cod_empresa and a.cod_empresa = " + empresa + " " + "group by a.cod_empresa, b.nro_trabajador, b.cantidad, " + "b.anos_consev_histor, b.mes_cons_info_hist ";
            OutMessage.OutMessagePrint(" :) ---> Actualizando Horas Extras \nQuery: " + sqlOrig);
            consul.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            java.util.Date fec = null;
            String Empresa = "";
            do
            {
                if(!consul.next())
                    break;
                totReg++;
                Empresa = consul.getString("compania");
                agno = consul.getString("anos_consev_histor");
                String mes = consul.getString("mes_cons_info_hist");
                if(mes.length() == 1)
                    mes = "0" + mes;
                String periodo_2 = agno + mes;
                Rut rut = new Rut(consul.getString("rut"));
                sqlDest = "INSERT INTO eje_ges_sobretiempos(compania,rut,periodo,total_haberes,num_horas,valhoras,num_minutos,tipo) VALUES('" + Empresa + "'," + rut.getRut() + "," + periodo_2 + "," + 0 + "," + consul.getFloat("num_horas") + "," + 0 + "," + 0 + ",'' )";
                if(consulTraspaso.insert(sqlDest) && ++regProcesados % 200 == 0)
                    System.out.print(".");
            } while(true);
            OutMessage.OutMessagePrint("Registros Procesados (" + regProcesados + " de " + totReg + ")");
            consul.close();
            consulTraspaso.close();
            OutMessage.OutMessagePrint("Fin Horas Extras");
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