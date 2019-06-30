package organica.com.eje.ges.usuario;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package com.eje.ges.usuario:
//            Usuario, ControlAcceso

public class ListaUsuarios extends MyHttpServlet
{

    public ListaUsuarios()
    {
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    private ResultSet consultaListaUsuarios(String query, Connection conn, String strUnidad, String strEmpresa)
    {
        ResultSet rs;
        try
        {
            String procedure = "{call " + query + "}";
            CallableStatement cs = conn.prepareCall(procedure);
            cs.setString(1, strUnidad);
            cs.setString(2, strEmpresa);
            rs = cs.executeQuery();
        }
        catch(SQLException e)
        {
            System.out.print("No pudo ejecutar el sp :" + query + " , " + e.toString());
            return null;
        }
        return rs;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\n>>--> Entro al doPost de ListaUsuarios");
        user = Usuario.rescatarUsuario(req);
        ControlAcceso control = new ControlAcceso(user);
        if(user.esValido())
        {
            if(!control.tienePermiso("df_man_usu"))
            {
                mensaje.devolverPaginaMensage(resp, "Historial", "Usted no tiene permiso para ver esta informaci\363n...");
            } else
            {
                String strUnidad = req.getParameter("unidad");
                String strUnidadDescrip = req.getParameter("descrip");
                String strEmpresa = req.getParameter("empresa");
                String sql;
                if(strUnidadDescrip == null || strUnidadDescrip.trim().equals(""))
                {
                    Consulta consul_unid = new Consulta(conexion);
                    sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unid_desc FROM eje_ges_unidades WHERE (unid_empresa = '")).append(strEmpresa).append("') AND (unid_id = '").append(strUnidad).append("')")));
                    consul_unid.exec(sql);
                    if(consul_unid.next())
                        strUnidadDescrip = consul_unid.getString("unid_desc");
                    consul_unid.close();
                }

                String unidad_administrativa = "";
                unidad_administrativa = getUnidadAdm();

                if(unidad_administrativa.equals("0"))
                    sql = String.valueOf(new StringBuilder("eje_ges_lista_usuarios_ua(?,?)"));
                else
                    sql = String.valueOf(new StringBuilder("eje_ges_lista_usuarios_cc(?,?)"));

                ResultSet rs = consultaListaUsuarios(sql, conexion, strUnidad, strEmpresa);
                OutMessage.OutMessagePrint("-->ListaUsuarios ".concat(String.valueOf(String.valueOf(sql))));
                SimpleHash modelRoot = new SimpleHash();
                String user_rol = "";
                String app_id = "";
                modelRoot.put("unid_desc", strUnidadDescrip);
                modelRoot.put("unid_id", strUnidad);
                modelRoot.put("emp_id", strEmpresa);
                SimpleList listaUsuarios = new SimpleList();
                SimpleList listaNoUsuarios = new SimpleList();
                try
                {
                    while(rs.next()) 
                    {
                        SimpleHash simplehash1 = new SimpleHash();
                        simplehash1.put("rut", rs.getString("rut"));
                        user_rol = rs.getString("rol_id") != null ? rs.getString("rol_id").toLowerCase() : "sin_rol";
                        app_id = rs.getString("app_id");
                        OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("**-->user_admin: ")).append(user_rol))));
                            if(isUsuarioDefinido(user_rol, app_id))
                            {
                                simplehash1.put("nombre", user_rol.concat(String.valueOf(String.valueOf(rs.getString("nombre")))));
                                listaUsuarios.add(simplehash1);
                            } else
                            {
                                simplehash1.put("nombre", rs.getString("nombre"));
                                listaNoUsuarios.add(simplehash1);
                            }
                    }
                }
                catch(SQLException e)
                {
                    System.out.print("No se pueden rescatar datos del sp :" + e.toString());
                }
                modelRoot.put("usuarios", listaUsuarios);
                modelRoot.put("no_usuarios", listaNoUsuarios);
                super.retTemplate(resp,"Gestion/Usuarios/ListaUsuarios.htm",modelRoot);
            }
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

	private String getUnidadAdm() {
		String unidad_administrativa = "";
		ResourceBundle proper = ResourceBundle.getBundle("DataFolder");
		try {
		  unidad_administrativa = proper.getString("portal.unidad_administrativa");
		}
		catch(MissingResourceException e) {
		        OutMessage.OutMessagePrint("Excepcion : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
		}
		return unidad_administrativa;
    }

    private boolean isUsuarioDefinido(String rol, String app_id)
    {
        boolean result = false;
		boolean privilegio = "df".equals( app_id ) || 
		                     "adm_cp".equals( app_id ) || 
		                     "adm_gp".equals( app_id );
        if( privilegio )
        {
            Consulta consul = new Consulta(connMgr.getConnection("portal"));
            String sql = String.valueOf(new StringBuilder("SELECT rol_id FROM eje_ges_roles WHERE rol_id in ('admin','publico')"));
            consul.exec(sql);
            while(consul.next()) 
            {
                String rol_id = consul.getString("rol_id");
                if(rol.equals(rol_id))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private boolean isUsuarioDefinido(String rol)
    {
        boolean result = false;
        Consulta consul = new Consulta(connMgr.getConnection("portal"));
        String sql = String.valueOf(new StringBuilder("SELECT rol_id FROM eje_ges_roles WHERE rol_id in ('admin','publico')"));
        consul.exec(sql);
        while(consul.next()) 
        {
            String rol_id = consul.getString("rol_id");
            if(rol.equals(rol_id))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    private void devolverPaginaMensage(HttpServletResponse resp, String titulo, String msg)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("titulo", titulo);
        modelRoot.put("mensaje", msg);
        super.retTemplate(resp,"Gestion/Usuarios/mensaje.htm",modelRoot);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}