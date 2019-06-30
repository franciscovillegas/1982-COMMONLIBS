package portal.com.eje.traspaso;

import java.sql.Connection;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package portal.com.eje.traspaso:
//            Proceso

public class GenerarClaves extends Proceso
{

    public GenerarClaves(Connection conOrigen, Connection conDestino)
    {
        super(conOrigen, conDestino);
    }

    public boolean Run(int empresa, int periodo)
    {
        Consulta consulDest = new Consulta(conDestino);
        Consulta consulDestAux = new Consulta(conDestino);
        OutMessage.OutMessagePrint("\nActualizando Usuario");
        long password = 0L;
        int regProcesados = 0;
        int regPosiblesProcesados = 0;
        int totReg = 0;
        String sqlDestAux = "SELECT RUT_USUARIO FROM eje_ges_USUARIO WHERE WP_COD_EMPRESA = " + empresa;
        consulDestAux.exec(sqlDestAux);
        do
        {
            if(!consulDestAux.next())
                break;
            totReg++;
            do
                password = Math.round(Math.random() * 98765432D);
            while(password < 0x98967fL);
            String sqlDest = "update eje_ges_usuario set password_usuario = " + password + " WHERE rut_usuario = " + consulDestAux.getString("RUT_USUARIO") + " AND wp_cod_empresa = " + empresa;
            if(consulDest.insert(sqlDest))
            {
                OutMessage.OutMessagePrint("Actualizada clave : " + sqlDest);
                if(++regProcesados % 50 == 0)
                    System.out.print(".");
            } else
            {
                OutMessage.OutMessagePrint("Error al intentar actualizar claves");
            }
        } while(true);
        OutMessage.OutMessagePrint("\nRegistros Procesados (" + regProcesados + " de " + totReg + ")");
        consulDest.close();
        consulDestAux.close();
        OutMessage.OutMessagePrint("\nFin Usuarios");
        return true;
    }
}