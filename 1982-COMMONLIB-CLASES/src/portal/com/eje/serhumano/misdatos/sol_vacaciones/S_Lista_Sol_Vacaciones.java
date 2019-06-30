// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 24-10-2006 18:01:38
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   S_Lista_Sol_Vacaciones.java

package portal.com.eje.serhumano.misdatos.sol_vacaciones;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

public class S_Lista_Sol_Vacaciones extends MyHttpServlet
{

    public S_Lista_Sol_Vacaciones()
    {
        try
        {
            jbInit();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        doGet(httpservletrequest, httpservletresponse);
    }

    protected void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        Connection connection = connMgr.getConnection("portal");
        if(connection != null)
        {
            MuestraDatos(httpservletrequest, httpservletresponse, connection);
            insTracking(httpservletrequest, "Solicitud de Vacaciones", null);
        } else
        {
            mensaje.devolverPaginaMensage(httpservletresponse, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        connMgr.freeConnection("portal", connection);
    }

    public void MuestraDatos(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, Connection connection)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\n**** Inicio MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(httpservletrequest);
        String template = "misdatos/sol_vacaciones/lista_sol_vac.htm";
        Validar validar = new Validar();
        Connection ConDestino = super.connMgr.getConnection("portal");
        if(httpservletrequest.getParameter("htm") != null)
            template = httpservletrequest.getParameter("htm");
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        String fecha = null;
        Consulta consulta = new Consulta(ConDestino);
        String messol = httpservletrequest.getParameter("messol");
        String diasol = httpservletrequest.getParameter("diasol");
        String yearsol = httpservletrequest.getParameter("yearsol");
        String mesd = httpservletrequest.getParameter("mesd");
        String diad = httpservletrequest.getParameter("diad");
        String yeard = httpservletrequest.getParameter("yeard");
        String radioSearch = httpservletrequest.getParameter("search");
        if(radioSearch == null)
        {
            OutMessage.OutMessagePrint("--> valor del radio search !!!" + radioSearch);
            OutMessage.OutMessagePrint("--> inicio del buscador !!!");
            modelRoot.put("solicitudes", simplelist);
            super.retTemplate(httpservletresponse,template,modelRoot);
        } else
        {
            if(radioSearch.equals("1"))
            {
                fecha = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(diasol))).append("/").append(messol).append("/").append(yearsol)));
                String sql = "SELECT  eje_ges_solicitud_vacaciones.id_sol, eje_ges_trabajador.rut, eje_ges_trabajador.digito_ver, eje_ges_trabajador.nombre, eje_ges_solicitud_vacaciones.fecha_desde, eje_ges_solicitud_vacaciones.fecha_hasta, eje_ges_solicitud_vacaciones.dias, eje_ges_solicitud_vacaciones.fecha_sol FROM eje_ges_trabajador, eje_ges_solicitud_vacaciones WHERE eje_ges_trabajador.rut = eje_ges_solicitud_vacaciones.rut AND eje_ges_solicitud_vacaciones.estado ='L' AND eje_ges_solicitud_vacaciones.fecha_sol >= '" + fecha + "' ORDER BY eje_ges_solicitud_vacaciones.fecha_sol";
                consulta.exec(sql);
                SimpleHash simplehash1;
                for(; consulta.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("id_sol", consulta.getString("id_sol"));
                    simplehash1.put("rut2", consulta.getString("rut") + "-" + consulta.getString("digito_ver"));
                    simplehash1.put("nombre", consulta.getString("nombre"));
                    simplehash1.put("fecha_desde", validar.validarFecha(consulta.getValor("fecha_desde")));
                    simplehash1.put("fecha_hasta", validar.validarFecha(consulta.getValor("fecha_hasta")));
                    simplehash1.put("dias", consulta.getString("dias"));
                    simplehash1.put("fecha_sol", validar.validarFecha(consulta.getValor("fecha_sol")));
                }

                modelRoot.put("solicitudes", simplelist);
                consulta.close();
                super.retTemplate(httpservletresponse,template,modelRoot);
            }
            if(radioSearch.equals("2"))
            {
                fecha = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(diad))).append("/").append(mesd).append("/").append(yeard)));
                String sql = "SELECT  eje_ges_solicitud_vacaciones.id_sol, eje_ges_trabajador.rut, eje_ges_trabajador.digito_ver, eje_ges_trabajador.nombre, eje_ges_solicitud_vacaciones.fecha_desde, eje_ges_solicitud_vacaciones.fecha_hasta, eje_ges_solicitud_vacaciones.dias, eje_ges_solicitud_vacaciones.fecha_sol FROM eje_ges_trabajador, eje_ges_solicitud_vacaciones WHERE eje_ges_trabajador.rut = eje_ges_solicitud_vacaciones.rut AND eje_ges_solicitud_vacaciones.estado ='L' AND eje_ges_solicitud_vacaciones.fecha_desde = '" + fecha + "' ORDER BY eje_ges_solicitud_vacaciones.fecha_sol";
                consulta.exec(sql);
                SimpleHash simplehash1;
                for(; consulta.next(); simplelist.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("id_sol", consulta.getString("id_sol"));
                    simplehash1.put("rut2", consulta.getString("rut") + "-" + consulta.getString("digito_ver"));
                    simplehash1.put("nombre", consulta.getString("nombre"));
                    simplehash1.put("fecha_desde", validar.validarFecha(consulta.getValor("fecha_desde")));
                    simplehash1.put("fecha_hasta", validar.validarFecha(consulta.getValor("fecha_hasta")));
                    simplehash1.put("dias", consulta.getString("dias"));
                    simplehash1.put("fecha_sol", validar.validarFecha(consulta.getValor("fecha_sol")));
                }

                modelRoot.put("solicitudes", simplelist);
                consulta.close();
                super.retTemplate(httpservletresponse,template,modelRoot);
            }
        }
        OutMessage.OutMessagePrint("\n**** Fin MuestraSolicitudes: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private void jbInit()
        throws Exception
    {
    }

    private Usuario user;
    private ResourceBundle proper;
}