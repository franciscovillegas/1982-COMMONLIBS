package portal.com.eje.serhumano.rrhh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import portal.com.eje.datos.Consulta;
import portal.com.eje.tools.OutMessage;

public class publicador_Manager
{

    public publicador_Manager(Connection conex)
    {
        con = conex;
        mensajeError = "";
    }

    public boolean AddPublicacion(String titulo, String contenido, String imagen, String url)
    {
        boolean valor = false;
        String sql = "";
        try
        {
            double id_publi = GetMaxId();
            sql = "INSERT INTO eje_ges_publicaciones (id_publi, titulo, contenido, fec_actualiza,imagen,url_detalle) VALUES (?,?,?, GETDATE(),?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setDouble(1, id_publi);
            pstmt.setString(2, titulo);
            pstmt.setString(3, contenido);
            pstmt.setString(4, imagen);
            pstmt.setString(5, url);
            pstmt.executeUpdate();
            System.err.println("-------->Insertando Publicacion: ".concat(String.valueOf(String.valueOf(pstmt.toString()))));
            valor = true;
        }
        catch(SQLException e)
        {
            mensajeError = e.getMessage();
            excepcion = e;
            OutMessage.OutMessagePrint("Insert Error(AddSupply): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean updatePublicacion(double id, String titulo, String contenido, String imagen, String url)
    {
        boolean valor = false;
        String sql = "";
        try
        {
            sql = "UPDATE eje_ges_publicaciones SET titulo = ?, contenido = ?,fec_actualiza = GETDATE(), imagen = ?, url_detalle = ? WHERE (id_publi = ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, titulo);
            pstmt.setString(2, contenido);
            pstmt.setString(3, imagen);
            pstmt.setString(4, url);
            pstmt.setDouble(5, id);
            pstmt.executeUpdate();
            System.err.println("-------->Actualizando Publicacion: ".concat(String.valueOf(String.valueOf(pstmt.toString()))));
            valor = true;
        }
        catch(SQLException e)
        {
            mensajeError = e.getMessage();
            excepcion = e;
            OutMessage.OutMessagePrint("Update Error(AddSupply): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public boolean delPublicacion(double id)
    {
        boolean valor = false;
        String sql = "";
        try
        {
            sql = "DELETE FROM eje_ges_publicaciones WHERE (id_publi = ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setDouble(1, id);
            pstmt.executeUpdate();
            System.err.println("-------->Eliminando Publicacion: ".concat(String.valueOf(String.valueOf(pstmt.toString()))));
            valor = true;
        }
        catch(SQLException e)
        {
            mensajeError = e.getMessage();
            excepcion = e;
            OutMessage.OutMessagePrint("DELETE Error(AddSupply): ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        return valor;
    }

    public double GetMaxId()
    {
        consul = new Consulta(con);
        String sql = "SELECT ISNULL(MAX(id_publi), 1000) AS maximo FROM eje_ges_publicaciones";
        OutMessage.OutMessagePrint(sql);
        consul.exec(sql);
        consul.next();
        return Double.parseDouble(consul.getString("maximo")) + 1.0D;
    }

    public Consulta getPublicaciones()
    {
        consul = new Consulta(con);
        String sql = "SELECT id_publi, titulo, contenido,fec_actualiza, imagen, url_detalle as url FROM eje_ges_publicaciones ORDER BY titulo";
        consul.exec(sql);
        return consul;
    }

    public Consulta getDataPubli(String id)
    {
        consul = new Consulta(con);
        String sql = "SELECT id_publi, titulo, contenido,fec_actualiza, imagen, url_detalle as url FROM eje_ges_publicaciones WHERE id_publi=".concat(String.valueOf(String.valueOf(id)));
        consul.exec(sql);
        return consul;
    }

    public String getError()
    {
        return mensajeError;
    }

    public Throwable getExcepcion()
    {
        return excepcion;
    }

    private Connection con;
    private Consulta consul;
    private String mensajeError;
    private Throwable excepcion;
}