package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.indicadores.Periodo;
import portal.com.eje.tools.Dato;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Rut;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspTrabHaberes extends Proceso
{

    public TraspTrabHaberes(Connection conOrigen, Connection conDestino, String agno, String mes)
    {
        super(conOrigen, conDestino);
        periDatos = new Periodo(agno, mes);
        this.agno = agno;
        this.mes = mes;
        historico = false;
    }

    public TraspTrabHaberes(Connection conOrigen, Connection conDestino, String fecha_desde, String agno, String mes)
    {
        super(conOrigen, conDestino);
        this.fecha_desde = fecha_desde;
        historico = true;
    }

    public boolean Run(int empresa, int periodos)
    {
        String sqlBorrar = "";
        Rut rut = null;
        Dato dato = new Dato();
        Consulta consulOri = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Detalle de Haberes");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlOrig = "SELECT H.COD_EMPRESA, H.COD_PLANTA, H.COD_HABER,  H.NRO_TRABAJADOR, H.VALOR_TRANSAC_PESO, H.ANO_PERIODO, H.MES_PERIODO,  H.COD_UNIDAD_MONEDA, H.VALOR_TRANSACCION FROM HABERES_CONTRACTUA H, PLANTA P WHERE H.COD_TIPO_PROCESO = 'LQ'  AND H.COD_PLANTA = P.COD_PLANTA  AND H.COD_EMPRESA = P.COD_EMPRESA  AND P.COD_VIGENTE <> 'N'  AND H.ANO_PERIODO*100+H.MES_PERIODO >= " + Integer.parseInt(fecha_desde) + " AND H.NRO_TRABAJADOR <> 16098105 " + " AND H.COD_EMPRESA = " + empresa + " ";
            String sqlDest = "DELETE FROM eje_ges_trab_haberes_historia  WHERE wp_cod_empresa = " + empresa + " AND periodo >= " + Integer.parseInt(fecha_desde) + " ";
            OutMessage.OutMessagePrint("Eliminando Trab Haberes: " + sqlDest);
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            OutMessage.OutMessagePrint("Query: " + sqlOrig);
            consulOri.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            String Empresa = "";
            do
            {
                if(!consulOri.next())
                    break;
                totReg++;
                String mes = consulOri.getString("MES_PERIODO");
                if(mes.length() == 1)
                    mes = "0" + mes;
                String periodo = consulOri.getString("ANO_PERIODO") + mes;
                String monto = "0";
                if(consulOri.getString("COD_UNIDAD_MONEDA").substring(0, 1).equals("$"))
                    monto = dato.objectToDato(consulOri.getString("VALOR_TRANSACCION"));
                else
                    monto = dato.objectToDato(consulOri.getString("VALOR_TRANSAC_PESO"));
                sqlDest = "INSERT INTO eje_ges_trab_haberes_historia (empresa, periodo, rut, haber, monto, wp_cod_empresa, wp_cod_planta)  VALUES (" + empresa + ", " + periodo + ", " + dato.objectToDato(consulOri.getString("NRO_TRABAJADOR")) + ", " + dato.objectToDato(consulOri.getString("COD_HABER")) + ", " + monto + ", " + dato.objectToDato(consulOri.getString("COD_EMPRESA")) + ", " + dato.objectToDato(consulOri.getString("COD_PLANTA")) + ")";
                if(consulDest.insert(sqlDest) && ++regProcesados % 200 == 0)
                    System.out.print(".");
            } while(true);
            consulOri.close();
            consulDest.close();
            OutMessage.OutMessagePrint("Registros Procesados (" + regProcesados + " de " + totReg + ")");
            OutMessage.OutMessagePrint("Fin de Detalle de Haberes_historia");
        }
        consulAuxx.close();
        return true;
    }

    private String periodoOri;
    private Periodo periDatos;
    private String fecha_desde;
    private boolean historico;
    private String agno;
    private String mes;
}