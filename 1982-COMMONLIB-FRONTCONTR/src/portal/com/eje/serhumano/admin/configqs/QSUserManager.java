package portal.com.eje.serhumano.admin.configqs;

import java.sql.Connection;
import java.sql.PreparedStatement;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

public class QSUserManager
{

    public QSUserManager(Connection conexion)
    {
        this.conexion = conexion;
    }

    protected void setError(String msg, Throwable t)
    {
        mensajeError = msg;
        excepcion = t;
    }

    protected void setError(String msg)
    {
        setError(msg, null);
    }

    protected void setError()
    {
        setError("");
    }

    public String getError()
    {
        return mensajeError;
    }

    public Throwable getException()
    {
        return excepcion;
    }

    public boolean existUser(String login)
    {
        boolean exist = false;
        Consulta consul = new Consulta(conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT login FROM wsg_usr_usuario WHERE (login = '")).append(login).append("')")));
        consul.exec(sql);
        if(consul.next())
            exist = true;
        consul.close();
        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("existUser() --> ")).append(exist).append(" --> ").append(sql))));
        return exist;
    }

    public boolean addUser(String login, String password, String nombreComp, int rut, String dig, String tipo)
    {
        String sql = "INSERT INTO wsg_usr_usuario (login, password, nombre_comp, tipo_user, rut, dig) VALUES (?, ?, ?, ?, ?, ?)";
        boolean ok = true;
        setError();
        if(existUser(login))
            ok = false;
        if(ok)
            try
            {
                PreparedStatement pstmt = conexion.prepareStatement(sql);
                pstmt.setString(1, login);
                pstmt.setString(2, password);
                pstmt.setString(3, nombreComp);
                pstmt.setString(4, tipo);
                pstmt.setInt(5, rut);
                pstmt.setString(6, dig);
                pstmt.executeUpdate();
                pstmt.close();
                ok = true;
            }
            catch(Exception e)
            {
                setError(String.valueOf(String.valueOf((new StringBuilder("Error al Agregar Usuario (")).append(login).append(")"))), e);
                OutMessage.OutMessagePrint("Error en addUser() --> ".concat(String.valueOf(String.valueOf(mensajeError))));
            }
        return ok;
    }

    public boolean addUser(String login, String password, String nombreComp, int rut, String dig)
    {
        return addUser(login, password, nombreComp, rut, dig, "C");
    }

    public Consulta getUser(String login)
    {
        Consulta consul = new Consulta(conexion);
        String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT login, nombre_comp, password, rut, dig, url, tipo_user, pag_home FROM wsg_usr_usuario WHERE (login = '")).append(login).append("')")));
        consul.exec(sql);
        OutMessage.OutMessagePrint("getUser() --> ".concat(String.valueOf(String.valueOf(sql))));
        return consul;
    }

    public boolean setTipo(String login, String tipo)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE wsg_usr_usuario SET tipo_user=? WHERE (login = '")).append(login).append("')")));
        boolean ok = false;
        setError();
        try
        {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, tipo);
            pstmt.executeUpdate();
            pstmt.close();
            ok = true;
        }
        catch(Exception e)
        {
            setError(String.valueOf(String.valueOf((new StringBuilder("Error al Actualizar Usuario (")).append(login).append(")"))), e);
            OutMessage.OutMessagePrint("Error en setTipo() --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    public boolean updateUserPassword(String login, String password)
    {
        String sql = String.valueOf(String.valueOf((new StringBuilder("UPDATE wsg_usr_usuario SET password=? WHERE (login = '")).append(login).append("')")));
        boolean ok = false;
        setError();
        try
        {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, password);
            pstmt.executeUpdate();
            pstmt.close();
            ok = true;
        }
        catch(Exception e)
        {
            setError(String.valueOf(String.valueOf((new StringBuilder("Error al Actualizar Clave del Usuario (")).append(login).append(")"))), e);
            OutMessage.OutMessagePrint("Error en updateUserPassword() --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    public boolean deleteUser(String login)
    {
        String sql = "DELETE FROM wsg_usr_usuario WHERE (login = ?)";
        boolean ok = false;
        setError();
        try
        {
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, login);
            pstmt.executeUpdate();
            pstmt.close();
            ok = true;
        }
        catch(Exception e)
        {
            setError(String.valueOf(String.valueOf((new StringBuilder("Error al Eliminar Usuario (")).append(login).append(")"))), e);
            OutMessage.OutMessagePrint("Error en updateUserPassword() --> ".concat(String.valueOf(String.valueOf(mensajeError))));
        }
        return ok;
    }

    protected Connection conexion;
    protected String mensajeError;
    protected Throwable excepcion;
    public static final String TIPO_ADMIN = "A";
    public static final String TIPO_CLIENTE = "C";
}