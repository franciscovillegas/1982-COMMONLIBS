package organica.com.eje.ges.anexos;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

// Referenced classes of package com.eje.ges.anexos:
//            ManAnexo

public class CreaAnexoUnidad extends MyHttpServlet
{

    public CreaAnexoUnidad()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de CreaAnexoUnidad");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion_chica.htm", "Crear Anexos", "Tiempo de Sesi\363n expirado...");
        if(!control.tienePermiso("df_mant_anexo"))
        {
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion_chica.htm", "Crear Anexos", "Usted no tiene permiso para Realizar esta Acci\363n...");
        } else
        {
            ManAnexo mantenerAnexo = new ManAnexo(conexion);
            String msg = "R";
            String paramAccion = valida.validarDato(req.getParameter("accion"), "CA");
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("--> paramAccion '")).append(paramAccion).append("'"))));
            SimpleHash modelRoot = new SimpleHash();
            boolean resul = false;
            String paramNuevo = req.getParameter("nuevo");
            String paramPublico = valida.validarDato(req.getParameter("publico"), "S");
            String paramUnidad = req.getParameter("unidad");
            String paramEmpresa = req.getParameter("empresa");
            if("CAN".equals(paramAccion))
                resul = mantenerAnexo.crearAnexo(paramEmpresa, paramNuevo, paramUnidad, paramPublico);
            else
            if("CFO".equals(paramAccion))
                resul = mantenerAnexo.crearFono(paramEmpresa, paramNuevo, paramUnidad, paramPublico);
            else
            if("CFA".equals(paramAccion))
                resul = mantenerAnexo.crearFax(paramEmpresa, paramNuevo, paramUnidad);
            else
            if("MAN".equals(paramAccion))
                resul = mantenerAnexo.modifAnexo(paramEmpresa, paramNuevo, paramUnidad, paramPublico);
            else
            if("MFO".equals(paramAccion))
                resul = mantenerAnexo.modifFono(paramEmpresa, paramNuevo, paramUnidad, paramPublico);
            if(!resul)
                msg = "NR";
            mantenerAnexo.close();
            generaPagina(req, resp, msg, user, conexion);
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doGet de CreaAnexos");
        user = Usuario.rescatarUsuario(req);
        String strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion_chica.htm", "Crear Anexos", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermiso("df_mant_anexo"))
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion_chica.htm", "Crear Anexos", "Usted no tiene permiso para Realizar esta Acci\363n...");
        else
            generaPagina(req, resp, "", user, conexion);
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", conexion);
    }

    public void generaPagina(HttpServletRequest req, HttpServletResponse resp, String msg, Usuario user, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro a generaPagina");
        Validar valida = new Validar();
        ManAnexo mantenerAnexo = new ManAnexo(conexion);
        String paramAccion = valida.validarDato(req.getParameter("accion"), "CA");
        String paramUnidad = req.getParameter("unidad");
        SimpleHash modelRoot = new SimpleHash();
        if("CAN".equals(paramAccion))
            modelRoot.put("existentes", mantenerAnexo.getAnexosExistentes(paramUnidad));
        else
        if("CFO".equals(paramAccion))
            modelRoot.put("existentes", mantenerAnexo.getFonosExistentes(paramUnidad));
        else
        if("CFA".equals(paramAccion))
            modelRoot.put("existentes", mantenerAnexo.getFaxExistentes(paramUnidad));
        else
        if("MAN".equals(paramAccion) || "MFO".equals(paramAccion))
        {
            modelRoot.put("valor", req.getParameter("valor"));
            modelRoot.put("publico", req.getParameter("publico"));
        }
        if(msg.equals("R"))
            modelRoot.put("refrescar", "1");
        modelRoot.put("unidad", req.getParameter("unidad"));
        modelRoot.put("unidad_desc", req.getParameter("unidad_desc"));
        modelRoot.put("accion", paramAccion);
        modelRoot.put("msg", msg);
        super.retTemplate(resp,"Gestion/Anexos/CreaAnexoUnidad.htm",modelRoot);
        mantenerAnexo.close();
        OutMessage.OutMessagePrint("Fin de generaPagina");
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
    private static final String TEM_CREAR_ANEXOS = "CreaAnexoUnidad.htm";
    private static final String PAG_MSG_ERROR = "Anexos/msgCreaAnexo.htm";
}