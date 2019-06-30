package organica.com.eje.ges.Buscar;

import java.io.IOException;
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

public class DetDotacion extends MyHttpServlet
{

    public DetDotacion()
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
        DespResultado(req, resp);
    }

    private void DespResultado(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        java.sql.Connection Conexion = connMgr.getConnection("portal");
        user = Usuario.rescatarUsuario(req);
        if(user.esValido())
        {
            SimpleHash modelRoot = new SimpleHash();
            String unidad = req.getParameter("unidad");
            String empresa = req.getParameter("empresa");
            OutMessage.OutMessagePrint("Unidad : ".concat(String.valueOf(String.valueOf(unidad))));
            OutMessage.OutMessagePrint("Empresa: ".concat(String.valueOf(String.valueOf(empresa))));
            fotos = new FotosPersonalUnidad();
            modelRoot.put("varios", fotos.GetFotosPersonalUnidad(Conexion, unidad, empresa));
            modelRoot.put("unid_desc", fotos.DescUnidad);
            modelRoot.put("unid_id", unidad);
            super.retTemplate(resp,"Gestion/Buscar/DetDotacion.htm",modelRoot);
        } else
        {
            mensaje.devolverPaginaSinSesion(resp, "", "Tiempo de Sesi\363n expirado...");
        }
        connMgr.freeConnection("portal", Conexion);
    }

    private Usuario user;
    private Rut userRut;
    private FotosPersonalUnidad fotos;
    private Mensaje mensaje;
}