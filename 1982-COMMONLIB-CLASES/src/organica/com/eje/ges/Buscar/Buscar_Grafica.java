package organica.com.eje.ges.Buscar;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.DatosRut.Rut;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

// Referenced classes of package com.eje.ges.Buscar:
//            FotosPersonalUnidad

public class Buscar_Grafica extends MyHttpServlet
{

    public Buscar_Grafica()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        doGet(req, resp);
        connMgr.freeConnection("portal", Conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        if(Usuario.rescatarUsuario(req).esValido())
            DespResultado(req, resp, Conexion);
        else
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        connMgr.freeConnection("portal", Conexion);
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        user = Usuario.rescatarUsuario(req);
        SimpleHash modelRoot = new SimpleHash();
        String unidad = req.getParameter("unidad");
        String empresa = req.getParameter("empresa");
        OutMessage.OutMessagePrint("Unidad : ".concat(String.valueOf(String.valueOf(unidad))));
        OutMessage.OutMessagePrint("Empresa: ".concat(String.valueOf(String.valueOf(empresa))));
        fotos = new FotosPersonalUnidad();
        modelRoot.put("varios", fotos.GetFotosPersonalUnidad(Conexion, unidad, empresa));
        userRut = new Rut(Conexion, fotos.RutSup);
        modelRoot.put("rut", fotos.RutSup);
        modelRoot.put("foto", userRut.Foto);
        modelRoot.put("cargosup", userRut.Cargo);
        modelRoot.put("nomsup", userRut.Nombres);
        modelRoot.put("unidadsup", userRut.Unidad);
        modelRoot.put("emailsup", userRut.Email);
        modelRoot.put("mailsup", userRut.Mail);
        modelRoot.put("anexosup", userRut.Anexo);
        modelRoot.put("mision", fotos.MisionUnidad);
        modelRoot.put("unid_desc", fotos.DescUnidad);
        super.retTemplate(resp,"Gestion/Buscar/busq_organica.htm",modelRoot);
    }

    private Usuario user;
    private Rut userRut;
    private FotosPersonalUnidad fotos;
    private Mensaje mensaje;
}