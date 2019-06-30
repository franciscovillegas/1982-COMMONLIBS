package portal.com.eje.serhumano.usermanager;

import java.sql.Connection;
import java.sql.PreparedStatement;

import portal.com.eje.tools.OutMessage;

public class AccessManager
{

    public AccessManager(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }

    public boolean AddAccess(String codigo, String nombre)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "INSERT INTO userjs.eje_js_acceso  (acc_id, acc_glosa) VALUES   (?, ?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, codigo);
            pstmt.setString(2, nombre);
            pstmt.executeUpdate();
            valor = true;
        }
        catch(Exception e)
        {
            mensajeError = e.getMessage();
            OutMessage.OutMessagePrint("Error de Preparestatment --> ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public String getError()
    {
        return mensajeError;
    }

    public boolean DeleteAccess()
    {
        boolean valor = false;
        return valor;
    }

    private Connection con;
    private String mensajeError;
}