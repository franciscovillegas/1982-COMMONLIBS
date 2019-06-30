package portal.com.eje.serhumano.usermanager;

import java.sql.Connection;
import java.sql.PreparedStatement;

import portal.com.eje.tools.OutMessage;

public class RolManager
{

    public RolManager(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }

    public boolean AddRol(String codigo, String nombre, String nemo)
    {
        boolean valor = false;
        mensajeError = "";
        String sql = "INSERT INTO userjs.eje_js_roles (rol, rol_glosa, rol_desc) VALUES     (?, ?, ?)";
        try
        {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, codigo);
            pstmt.setString(2, nemo);
            pstmt.setString(3, nombre);
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

    public boolean DeleteRol()
    {
        boolean valor = false;
        return valor;
    }

    private Connection con;
    private String mensajeError;
}