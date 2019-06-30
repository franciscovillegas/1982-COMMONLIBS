package portal.com.eje.traspaso;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class CargaUsuariosQS extends Proceso
{

    public CargaUsuariosQS(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consulOrig = new Consulta(conOrigen);
        Consulta consulDest = new Consulta(conDestino);
        Consulta consulDestAux = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Usuario");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            String sqlDest = "DELETE FROM wsg_usr_usuario WHERE rut <> 99999999 AND wp_cod_empresa = " + empresa;
            if(consulDest.insert(sqlDest))
                OutMessage.OutMessagePrint("Datos de destino Borrados");
            else
                OutMessage.OutMessagePrint("Error al intentar borrar datos de destino");
            Date fecha_actual = new Date();
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = simpledateformat.format(fecha_actual);
            String sqlOrig = "SELECT PER.RUT_TRABAJADOR, PER.DV_RUT_TRABAJADOR, PER.COD_EMPRESA, PER.COD_PLANTA  FROM PERSONAL as PER, PLANTA as PLA  WHERE PER.COD_VIGEN_TRABAJAD = 'S'  AND PER.COD_EMPRESA = PLA.COD_EMPRESA  AND PER.COD_PLANTA = PLA.COD_PLANTA  AND PLA.COD_VIGENTE <> 'N'  AND PER.COD_EMPRESA = " + empresa + " AND (PER.FEC_FIN_CONTR_VIGE > '" + fecha + "' " + " OR PER.FEC_FIN_CONTR_VIGE is null " + ")";
            OutMessage.OutMessagePrint(" :) ---> Actualizando Usuarios QS \nQuery: " + sqlOrig);
            consulOrig.exec(sqlOrig);
            int regProcesados = 0;
            int regPosiblesProcesados = 0;
            int totReg = 0;
            for(; consulOrig.next(); System.out.print("-"))
            {
                totReg++;
                String rut = consulOrig.getString("RUT_TRABAJADOR");
                String sqlDestAux = "SELECT * FROM WSG_USR_USUARIO WHERE RUT = " + rut + " AND WP_COD_EMPRESA = " + empresa;
                consulDestAux.exec(sqlDestAux);
                if(!consulDestAux.next())
                {
                    sqlDest = "INSERT INTO wsg_usr_usuario (login, password, rut, dig, tipo_user, wp_cod_empresa, wp_cod_planta) VALUES ('" + rut + "'" + ", " + "'" + rut + "'" + ", " + rut + ", " + "'" + consulOrig.getString("DV_RUT_TRABAJADOR") + "'" + ", " + "'C'" + ", " + consulOrig.getString("COD_EMPRESA") + ", " + consulOrig.getString("COD_PLANTA") + ")";
                    if(consulDest.insert(sqlDest) && ++regProcesados % 50 == 0)
                        System.out.print(".");
                }
            }

            OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
            consulOrig.close();
            consulDest.close();
            consulDestAux.close();
            OutMessage.OutMessagePrint("\nFin Usuarios");
        }
        consulAuxx.close();
        return true;
    }
}