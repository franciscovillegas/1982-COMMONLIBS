package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.md5.ManagerMD5;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class Pass2MD5QS extends Proceso
{

    public Pass2MD5QS(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consulDest = new Consulta(conDestino);
        Consulta consulAux = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Usuario");
        String sqlAux = "SELECT * FROM WSG_USR_USUARIO  WHERE MD5 = 'N' AND WP_COD_EMPRESA = " + empresa;
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
            String sqlDest = "UPDATE WSG_USR_USUARIO SET password = '" + ManagerMD5.encrypt(consulAux.getString("password")) + "', md5 = 'S' " + " WHERE rut = " + consulAux.getString("rut");
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