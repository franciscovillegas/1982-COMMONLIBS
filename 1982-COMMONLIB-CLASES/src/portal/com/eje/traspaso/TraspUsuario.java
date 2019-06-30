package portal.com.eje.traspaso;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class TraspUsuario extends Proceso
{

    public TraspUsuario(Connection conOrigen, Connection conDestino, String periodoOri)
    {
        super(conOrigen, conDestino);
        this.periodoOri = periodoOri;
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consul = new Consulta(conOrigen);
        Consulta consulTraspaso = new Consulta(conDestino);
        Consulta consulAux = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Usuario");
        Consulta consulAuxx = new Consulta(conOrigen);
        String sqlAuxx = "SELECT TOP 1 EMP.*  FROM EMPRESA as EMP, PLANTA as PLA  WHERE EMP.COD_EMPRESA = PLA.COD_EMPRESA  AND PLA.COD_VIGENTE <> 'N'  AND EMP.COD_EMPRESA = " + empresa;
        consulAuxx.exec(sqlAuxx);
        if(consulAuxx.next())
        {
            Date fecha_actual = new Date();
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = simpledateformat.format(fecha_actual);
            String sqlOrig = "SELECT PER.NRO_TRABAJADOR, PER.RUT_TRABAJADOR, PER.COD_EMPRESA, PER.COD_PLANTA  FROM PERSONAL as PER, PLANTA as PLA  WHERE PER.COD_VIGEN_TRABAJAD = 'S'  AND PER.COD_EMPRESA = PLA.COD_EMPRESA  AND PER.COD_PLANTA = PLA.COD_PLANTA  AND PLA.COD_VIGENTE <> 'N'  AND PER.RUT_TRABAJADOR <> 16098105  AND PER.COD_EMPRESA = " + empresa + " AND (PER.FEC_FIN_CONTR_VIGE > '" + fecha + "' " + " OR PER.FEC_FIN_CONTR_VIGE is null " + ")";
            OutMessage.OutMessagePrint(" :) ---> Actualizando Usuarios \nQuery: " + sqlOrig);
            consul.exec(sqlOrig);
            int regProcesados = 0;
            int regPosiblesProcesados = 0;
            int totReg = 0;
            for(; consul.next(); System.out.print("-"))
            {
                totReg++;
                String rut = consul.getString("RUT_TRABAJADOR");
                String sqlAux = "SELECT * FROM eje_ges_USUARIO WHERE RUT_USUARIO = " + rut + " AND WP_COD_EMPRESA = " + empresa;
                consulAux.exec(sqlAux);
                if(!consulAux.next())
                {
                    String sqlDest = "INSERT INTO eje_ges_usuario (login_usuario, password_usuario, rut_usuario, wp_cod_empresa, wp_cod_planta, md5) VALUES ('" + rut + "'" + ", " + "'" + rut + "'" + ", " + rut + ", " + consul.getString("COD_EMPRESA") + ", " + consul.getString("COD_PLANTA") + ",'N' " + ")";
                    if(consulTraspaso.insert(sqlDest) && ++regProcesados % 50 == 0)
                        System.out.print(".");
                }
                sqlAux = "SELECT * FROM eje_ges_USER_APP WHERE RUT_USUARIO = " + rut + " AND WP_COD_EMPRESA = " + empresa;
                consulAux.exec(sqlAux);
                if(!consulAux.next())
                {
                    String sqlDest = "INSERT INTO eje_ges_user_app (app_id, rut_usuario, wp_cod_empresa, wp_cod_planta) VALUES ('sh', " + rut + ", " + consul.getString("COD_EMPRESA") + ", " + consul.getString("COD_PLANTA") + ")";
                    if(consulTraspaso.insert(sqlDest) && ++regProcesados % 100 == 0)
                        System.out.print(".");
                }
            }

            OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
            consul.close();
            consulTraspaso.close();
            consulAux.close();
            OutMessage.OutMessagePrint("\nFin Usuarios");
        }
        consulAuxx.close();
        return true;
    }

    private String periodoOri;
}