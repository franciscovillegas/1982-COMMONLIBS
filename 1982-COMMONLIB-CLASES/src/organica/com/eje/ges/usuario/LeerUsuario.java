package organica.com.eje.ges.usuario;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.cargo.VerCargo;
import organica.com.eje.ges.usuario.unidad.VerUnidad;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Validar;
import organica.tools.enviaCorreo;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import freemarker.template.SimpleHash;

// Referenced classes of package com.eje.ges.usuario:
//            Usuario, InfoUsuario, UsuariosConectados, ControlAcceso

public class LeerUsuario extends MyHttpServlet
{

    public LeerUsuario()
    {
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("Entro al doPost de LeerUsuario");
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
//    	MMA 20170112
//	    java.sql.Connection Conexion = connMgr.getConnection("portal");
	    Connection Conexion = getConnMgr().getConnection("portal");
        OutMessage.OutMessagePrint("\nIngreso a LeerUsuario");
		OutMessage.OutMessagePrint("\nIngreso a LeerUsuario");
		OutMessage.OutMessagePrint("\tgetRemoteAddr --> ".concat(String.valueOf(String.valueOf(req.getRemoteAddr()))));
        Validar valida = new Validar();
        OutMessage.OutMessagePrint("-->Leer Usuario!!!!!");
        if(Conexion == null)
        {
            resp.sendRedirect("Tool?htm=Gestion/pagina_error_tecnico.htm");
        } else
        {
            String modo = req.getParameter("modo");
            int queApp = 0;
            Usuario user = new Usuario();
            if(modo != null)
                queApp = Integer.parseInt(modo);
            OutMessage.OutMessagePrint("--> Modo = ".concat(String.valueOf(String.valueOf(queApp))));
            switch(queApp)
            {
            case 1: // '\001'
            default:
                break;

            case 2: // '\002'
                OutMessage.OutMessagePrint("\t---->Entra por Ingreso a App:Certificados");
                user.getDatosCertif(Conexion, req.getParameter("rut"), req.getParameter("clave"));
                break;

            case 3: // '\003'
                OutMessage.OutMessagePrint("--> INGRESO A App:Anexos");
                user.getDatosAnexos(Conexion, req.getParameter("rut"), req.getParameter("clave"));
                break;

            case 0: // '\0'
                OutMessage.OutMessagePrint("--> INGRESO A DATAfOLDER");
                user.getDatos(Conexion, req.getParameter("rut"), req.getParameter("clave"));
                VerCargo cargosver[] = user.getCargosVer();
                for(int x = 0; x < cargosver.length; x++)
                    System.err.println("---->Cargo Ver: ".concat(String.valueOf(String.valueOf(cargosver[x].toString()))));

                VerUnidad unidadesver[] = user.getUnidadesVer();
                for(int x = 0; x < unidadesver.length; x++)
                    System.err.println("---->Unidad Ver: ".concat(String.valueOf(String.valueOf(unidadesver[x].toString()))));

                break;

            case 4: // '\004'
                OutMessage.OutMessagePrint("--> INGRESO A App:RegistroVisitas");
                user.getDatosVigilantes(Conexion, req.getParameter("rut"), req.getParameter("clave"));
                break;
            }
            if(user.esValido())
            {
                //user.infoUsuario = new InfoUsuario(user.getRutUsuario(), req.getRemoteAddr());
                
                ControlAcceso control = new ControlAcceso(user);
				SessionMgr.guardarUsuarioOrganica(req,user);
                OutMessage.OutMessagePrint("\t---->Permiso Certificados?: ".concat(String.valueOf(String.valueOf(control.tienePermiso("df_certif")))));
                if(queApp == 3)
                {
                    OutMessage.OutMessagePrint("\t----->Entra por Ingreso a Anexos");
                    resp.sendRedirect("/Anexos/frameanexos.htm");
                } else
                if(queApp == 2)
                {
                    OutMessage.OutMessagePrint("\t---->Entra por Ingreso a Certificados");
                    resp.sendRedirect("Tool?htm=Certificados/framesetresul.htm");
                } else
                if(queApp == 4)
                {
                    resp.sendRedirect("Tool?htm=Gestion/RegistroVisitas/frameset2.htm");
                } else
                {
                    resp.sendRedirect("Tool?htm=Gestion/bottom.htm");
                    if(req.getParameter("NroAcc") != null && req.getParameter("NroTot") != null && req.getParameter("HoraLla") != null)
                    {
                        Runtime rt = Runtime.getRuntime();
                        long memoriaLibre = rt.freeMemory();
                        long memoriaTotal = rt.totalMemory();
                    }
                }
              
            } else
            {
                Consulta consul = new Consulta(Conexion);
                String sql = "SELECT valor FROM eje_ges_parametros WHERE (id = 2)";
                consul.exec(sql);
                if(consul.next())
                    if(user.getErrores > consul.getInt("valor"))
                    {
                        OutMessage.OutMessagePrint("Usuario Bloqueado");
                        sql = "SELECT valor FROM eje_ges_parametros WHERE (id = 4)";
                        consul.exec(sql);
                        if(consul.next())
                        {
                            String Enviar = valida.validarDato(consul.getString("valor"), "").trim();
                            OutMessage.OutMessagePrint("Enviar E-mail: ".concat(String.valueOf(String.valueOf(Enviar))));
                            if("S".equals(Enviar))
                            {
                                sql = "SELECT valor FROM eje_ges_parametros WHERE (id = 5)";
                                consul.exec(sql);
                                if(consul.next())
                                {
                                    String para = consul.getString("valor");
                                    String de = para;
                                    SimpleHash modelRoot = new SimpleHash();
                                    modelRoot.put("rut", req.getParameter("rut"));
                                    modelRoot.put("intento", String.valueOf(user.getErrores));
                                    new enviaCorreo(para, String.valueOf(String.valueOf((new StringBuilder("(DataFolder) Bloqueo de Usuario '")).append(req.getParameter("rut")).append("'"))), de, getTemplate("/organica/templates/Gestion/Usuarios/mailBloqueo.htm"), modelRoot, "Bloqueo.htm");
                                    OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("Envio de e-mail para ")).append(para).append("..."))));
                                } else
                                {
                                    OutMessage.OutMessagePrint("E-mail no definido...");
                                }
                            }
                        }
                        resp.sendRedirect("Tool?htm=Gestion/bloquearacceso.htm");
                    } else
                    {
                        devolverPaginaMensage(resp, user.getErrores, user.getError(), queApp);
                    }
                consul.close();
            }
        }
//    	MMA 20170112
//      connMgr.freeConnection("portal", Conexion);
        getConnMgr().freeConnection("portal", Conexion);
    }

    private void devolverPaginaMensage(HttpServletResponse resp, int intentos, String msg, int cualApp)
        throws IOException, ServletException
    {
        String htm = "";
        switch(cualApp)
        {
        case 0: // '\0'
            htm = "Gestion/ingreso.htm";
            break;

        case 2: // '\002'
            htm = "Certificados/index.html";
            break;

        case 3: // '\003'
            htm = "Gestion/Anexos/index.html";
            break;

        case 4: // '\004'
            htm = "Gestion/RegistroVisitas/index.html";
            break;
        }
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("mensaje", msg);
        modelRoot.put("intentos", String.valueOf(intentos));
        super.retTemplate(resp,htm,modelRoot);
    }

    private static int TIME_OUT = -1;

}