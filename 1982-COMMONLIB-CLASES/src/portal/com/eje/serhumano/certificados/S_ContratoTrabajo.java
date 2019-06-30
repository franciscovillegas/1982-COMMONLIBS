package portal.com.eje.serhumano.certificados;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;

public class S_ContratoTrabajo extends MyHttpServlet
    implements Servlet
{

    public S_ContratoTrabajo()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String MPREFIX = "[doGet]";
        System.out.println("[doGet] Entrando...");
        user = SessionMgr.rescatarUsuario(request);
        java.sql.Connection Conexion = super.connMgr.getConnection("portal");
        if(Conexion != null)
        {
            loadContrato(request, response);
            insTracking(request, "Contrato de Trabajo".intern(), user.getRutId());
        } else
        {
            mensaje.devolverPaginaMensage(response, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        super.connMgr.freeConnection("portal", Conexion);
        System.out.println("[doGet] Saliendo...");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        doGet(request, response);
    }

    public void loadContrato(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            java.io.InputStream is = getClass().getResourceAsStream("/db.properties");
            Properties dbProps = new Properties();
            dbProps.load(is);
            String url = dbProps.getProperty("sid_url");
            String url2 = url;
            System.out.println("------------------------>" + url);
            if(url != null && !"".equals(url))
            {
                String rut_usuario = user.getRutId();
                String user_password = user.getPassWord();
                url = url + "?P1=" + rut_usuario;
                url = url + "&P2=" + user_password;
                java.sql.Connection winper2Conn = super.connMgr.getConnection("winper2");
                java.sql.Connection portalConn = super.connMgr.getConnection("portal");
                user.updatePasswdDBGlobal(winper2Conn, portalConn, rut_usuario, user_password);
                Map parameter = new HashMap();
                parameter.put("P1", rut_usuario);
                parameter.put("P2", user_password);
                doPostURLRequest(url2, parameter);
                response.sendRedirect(url);
            } else
            {
                response.sendRedirect(request.getContextPath() + "/window_close.html");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    static boolean doPostURLRequest(String strURL, Map parameter)
    {
        int i = 1;
        try
        {
            URL url = new URL(strURL);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            int n = parameter.size();
            for(Iterator it = parameter.entrySet().iterator(); it.hasNext();)
            {
                java.util.Map.Entry entry = (java.util.Map.Entry)it.next();
                String key = (String)entry.getKey();
                String value = (String)entry.getValue();
                out.print(key + "=" + value);
                if(i < n)
                    out.print("&");
                i++;
            }

            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while((inputLine = in.readLine()) != null) 
                System.out.println(inputLine);
            in.close();
        }
        catch(MalformedURLException e)
        {
            System.out.println("Url mal formado :" + e.toString());
            return false;
        }
        catch(IOException e)
        {
            System.out.println("Conexion no establecida : ");
            return false;
        }
        catch(Exception e)
        {
            System.out.println("Algun tipo de error : ");
            return false;
        }
        return true;
    }

    private Usuario user;
    private Mensaje mensaje;
    private ResourceBundle proper;
}