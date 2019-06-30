package organica.com.eje.ges.anexos;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.com.eje.datos.InfoTipos;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.tools.Tools;
import organica.tools.Validar;

import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

// Referenced classes of package com.eje.ges.anexos:
//            ManAnexo

public class CreaAnexos extends MyHttpServlet
{

    public CreaAnexos()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = connMgr.getConnection("portal");
        System.err.println("\nEntro al doPost de CreaAnexos");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        String strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Crear Anexos", "Tiempo de Sesi\363n expirado...");
        if(!control.tienePermiso("df_mant_anexo"))
        {
            mensaje.devolverPaginaMensage(resp, "Anexos/msgCreaAnexo.htm", "Crear Anexos", "Usted no tiene permiso para Realizar esta Acci\363n...");
        } else
        {
            ManAnexo mantenerAnexo = new ManAnexo(conexion);
            String msg = "R";
            String paramAccion = valida.validarDato(req.getParameter("accion"), "C");
            System.err.println(String.valueOf(String.valueOf((new StringBuilder("--> paramAccion '")).append(paramAccion).append("'"))));
            boolean resul = false;
            String paramAnexo = req.getParameter("anexo");
            String paramNuevoAnexo = req.getParameter("nuevo_anexo");
            String paramFax = req.getParameter("fax");
            String paramUnidad = req.getParameter("unidad");
            String paramEmpresa = req.getParameter("empresa");
            if(paramAccion.equals("C"))
                resul = mantenerAnexo.crearAnexo(paramEmpresa, paramNuevoAnexo, paramUnidad, paramFax);
            else
                resul = mantenerAnexo.modificarAnexo(paramEmpresa, paramAnexo, paramNuevoAnexo, paramUnidad, paramFax);
            if(!resul)
                msg = "NR";
            mantenerAnexo.close();
            generaPagina(req, resp, msg, user, conexion);
        }
        System.err.println("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = connMgr.getConnection("portal");
        System.err.println("\nEntro al doGet de CreaAnexos");
        user = Usuario.rescatarUsuario(req);
        String strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaSinSesion(resp, "Crear Anexos", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermiso("df_mant_anexo"))
            mensaje.devolverPaginaMensage(resp, "Anexos/msgCreaAnexo.htm", "Crear Anexos", "Usted no tiene permiso para Realizar esta Acci\363n...");
        else
            generaPagina(req, resp, "", user, Conexion);
        System.err.println("Fin de doGet");
        connMgr.freeConnection("portal", Conexion);
    }

    public void generaPagina(HttpServletRequest req, HttpServletResponse resp, String msg, Usuario user, Connection conexion)
        throws IOException, ServletException
    {
        System.err.println("\nEntro a generaPagina");
        Validar valida = new Validar();
        ManAnexo mantenerAnexo = new ManAnexo(conexion);
        InfoTipos infoTipos = new InfoTipos(conexion);
        String paramAccion = valida.validarDato(req.getParameter("accion"), "C");
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("anexos_existentes", mantenerAnexo.getAnexosExistentes(user.getEmpresa()));
        modelRoot.put("unidades", infoTipos.getUnidadesEmpresa());
        modelRoot.put("empresas", infoTipos.getEmpresas());
        if(msg.equals("R"))
        {
            modelRoot.put("anexo", req.getParameter("nuevo_anexo"));
            paramAccion = "M";
            modelRoot.put("refrescar", "1");
        } else
        {
            modelRoot.put("anexo", req.getParameter("anexo"));
        }
        modelRoot.put("nuevo_anexo", req.getParameter("nuevo_anexo"));
        modelRoot.put("fax", req.getParameter("fax"));
        modelRoot.put("empresa", req.getParameter("empresa"));
        modelRoot.put("unidad", req.getParameter("unidad"));
        modelRoot.put("accion", paramAccion);
        modelRoot.put("msg", msg);
        super.retTemplate(resp,"Gestion/Anexos/CreaAnexos.htm",modelRoot);
        mantenerAnexo.close();
        infoTipos.close();
        System.err.println("Fin de generaPagina");
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
    private static final String TEM_CREAR_ANEXOS = "CreaAnexos.htm";
    private static final String PAG_MSG_ERROR = "Anexos/msgCreaAnexo.htm";
}