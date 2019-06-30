package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.md5.ManagerMD5;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class Pass2MD5 extends Proceso
{

    public Pass2MD5(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consulDest = new Consulta(conDestino);
        Consulta consulAux = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Usuario");
        String sqlAux = "SELECT * FROM eje_ges_USUARIO  WHERE MD5 = 'N' AND WP_COD_EMPRESA = " + empresa;
        consulAux.exec(sqlAux);
        OutMessage.OutMessagePrint(" :) ---> Actualizando Usuarios \nQuery: " + sqlAux);
        consulAux.exec(sqlAux);
        int regProcesados = 0;
        int regPosiblesProcesados = 0;
        int totReg = 0;
        do
        {
            if(!consulAux.next())
                break;
            totReg++;
            String sqlDest = "UPDATE eje_ges_usuario SET password_usuario = '" + ManagerMD5.encrypt(consulAux.getString("password_usuario")) + "', md5 = 'S' " + " WHERE rut_usuario = " + consulAux.getString("rut_usuario");
            if(consulDest.insert(sqlDest) && ++regProcesados % 50 == 0)
                System.out.print(".");
        } while(true);
        OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
        consulDest.close();
        consulAux.close();
        OutMessage.OutMessagePrint("\nFin Usuarios");
        return true;
    }
}