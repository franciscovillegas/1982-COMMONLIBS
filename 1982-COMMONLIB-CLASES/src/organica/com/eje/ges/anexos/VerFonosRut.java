package organica.com.eje.ges.anexos;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.DatosRut.Rut;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

// Referenced classes of package com.eje.ges.anexos:
//            ManAnexo

public class VerFonosRut extends MyHttpServlet
{

    public VerFonosRut()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doGet de VerFonosRut");
        user = Usuario.rescatarUsuario(req);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Asignar Anexos", "Tiempo de Sesi\363n expirado...");
        else
            generaPagina(req, resp, "", user, conexion);
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", conexion);
    }

    public void generaPagina(HttpServletRequest req, HttpServletResponse resp, String msg, Usuario user, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro a generaPagina");
        ManAnexo mantenerAnexo = new ManAnexo(conexion);
        String paramRutTrab = req.getParameter("rut");
        String html = req.getParameter("htm");
        if(html == null)
            html = "Gestion/Anexos/VerFonosRut.htm";
        else
            html = "Gestion/Anexos/" + html;
        OutMessage.OutMessagePrint("html --> ".concat(String.valueOf(String.valueOf(html))));
        SimpleHash modelRoot = new SimpleHash();
        Rut userRut = new Rut(conexion, paramRutTrab);
        modelRoot.put("rut", paramRutTrab);
        modelRoot.put("nombre", userRut.Nombres);
        modelRoot.put("cargo", userRut.Cargo);
        modelRoot.put("unidad_desc", userRut.Unidad);
        modelRoot.put("foto", userRut.Foto);
        modelRoot.put("email", userRut.Email);
        modelRoot.put("mail", userRut.Mail);
        Consulta consul = new Consulta(conexion);
        modelRoot.put("anexos", mantenerAnexo.getRutAnexos(paramRutTrab));
        modelRoot.put("fonos", mantenerAnexo.getRutFonos(paramRutTrab));
        modelRoot.put("faxes", mantenerAnexo.getRutFaxes(paramRutTrab));
        super.retTemplate(resp,html,modelRoot);
        consul.close();
        mantenerAnexo.close();
        OutMessage.OutMessagePrint("Fin de generaPagina");
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
    private static final String TEM_CREAR_ANEXOS = "VerFonosRut.htm";
    private static final String PAG_MSG_ERROR = "Anexos/msgCreaAnexo.htm";
}