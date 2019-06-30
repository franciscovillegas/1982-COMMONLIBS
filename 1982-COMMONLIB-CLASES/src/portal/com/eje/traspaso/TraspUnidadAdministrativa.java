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

public class TraspUnidadAdministrativa extends Proceso
{

    public TraspUnidadAdministrativa(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consulOrig = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        Consulta consulAuxx = new Consulta(conOrigen);
        Validar validar = new Validar();
        OutMessage.OutMessagePrint("Actualizando Unidades");
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "DELETE FROM eje_sh_unidades WHERE unid_empresa = '" + empresa + "'";
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            
            String sqlOrig = "SELECT COD_EMPRESA, COD_UNIDAD_ADMINIS, RTRIM(UNIDAD_ADMINISTRAT) AS UNIDAD_ADMINISTRAT, COD_VIGENTE,COD_FICHA_JEFE FROM UNIDAD_ADMINISTRAT WHERE COD_EMPRESA = " + empresa;
            OutMessage.OutMessagePrint(" :) ---> Actualizando Unidades \nQuery: " + sqlOrig);
            consulOrig.exec(sqlOrig);
            int regProcesados = 0;
            int totReg = 0;
            String Empresa = "";
            Dato dato = new Dato();
            do
            {
                if(!consulOrig.next())
                    break;
                totReg++;
                String vigente = consulOrig.getString("COD_VIGENTE");
                if(vigente == null)
                    vigente = "N";
                sqlDest = "INSERT INTO eje_sh_unidades (unid_empresa, unid_id, unid_desc, area, vigente) VALUES ( '" + consulOrig.getString("COD_EMPRESA") + "'" + ", " + "'" + consulOrig.getString("COD_UNIDAD_ADMINIS") + "'" + ", " + "'";
                if(consulOrig.getString("UNIDAD_ADMINISTRAT").indexOf("'") < 0)
                {
                    OutMessage.OutMessagePrint("Inserte a  " + validar.validarDato(consulOrig.getString("UNIDAD_ADMINISTRAT"), "SD"));
                    sqlDest = sqlDest + validar.validarDato(consulOrig.getString("UNIDAD_ADMINISTRAT"), "SD");
                } else
                {
                    OutMessage.OutMessagePrint("Inserte b  " + dato.objectToDatoComillas(validar.validarDato(consulOrig.getString("UNIDAD_ADMINISTRAT"), "SD").replaceAll("'", "\264")));
                    sqlDest = sqlDest + dato.objectToDatoComillas(validar.validarDato(consulOrig.getString("UNIDAD_ADMINISTRAT"), "SD").replaceAll("'", "\264"));
                }
                sqlDest = sqlDest + "'" + ", " + null + ", " + "'" + vigente + "'" + ")";
                if(consulDest.insert(sqlDest) && ++regProcesados % 200 == 0)
                    System.out.print(".");
                
                Date fecha = new Date();
                SimpleDateFormat mesFormat = new SimpleDateFormat("MM");
                SimpleDateFormat agnoFormat = new SimpleDateFormat("yyyy");
                
                String mes = mesFormat.format(fecha);
                String agno = agnoFormat.format(fecha);
                String period = agno + mes;
                
                String rut_jefe = consulOrig.getString("COD_FICHA_JEFE");
                if( rut_jefe != null) {
                	sqlDest = "INSERT INTO eje_ges_unidad_encargado (unid_empresa,unid_id,periodo,estado,rut_encargado) VALUES('" + validar.validarDato(consulOrig.getString("COD_EMPRESA"),"-1") + "','" + validar.validarDato(consulOrig.getString("COD_UNIDAD_ADMINIS"),"0") + "'," + validar.validarDato(period) + ",1," + rut_jefe + ")";
                    consulDest.insert(sqlDest);
                }
                
            } while(true);
            OutMessage.OutMessagePrint("\nRegistros Procesados(1) (" + regProcesados + " de " + totReg + ")");
            consulOrig.close();
            consulDest.close();
            OutMessage.OutMessagePrint("Fin Unidades");
        }
        consulAuxx.close();
        return true;
    }

    private String periodoOri;
}