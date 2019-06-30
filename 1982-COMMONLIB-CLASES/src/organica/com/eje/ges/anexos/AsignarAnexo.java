package organica.com.eje.ges.anexos;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import organica.DatosRut.Rut;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

// Referenced classes of package com.eje.ges.anexos:
//            ManAnexo

public class AsignarAnexo extends MyHttpServlet
{

    public AsignarAnexo()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de AsignarAnexo");
        user = Usuario.rescatarUsuario(req);
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
            String paramAccion = req.getParameter("accion");
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("--> paramAccion '")).append(paramAccion).append("'"))));
            SimpleHash modelRoot = new SimpleHash();
            boolean resul = false;
            String paramUnidad = req.getParameter("unidad");
            String paramVer = req.getParameter("ver");
            String paramEmpresa = req.getParameter("empresa");
            String paramRutTrab = req.getParameter("rut");
            if(paramAccion.equals("A"))
            {
                String arrValores[] = req.getParameterValues("disp");
                int largo_arrValores = 0;
                if(arrValores != null)
                    largo_arrValores = arrValores.length;
                for(int x = 0; x < largo_arrValores; x++)
                {
                    Vector anexos = Tools.separaLista(arrValores[x], ",");
                    String anx_unid = (String)anexos.get(1);
                    String anx_id = (String)anexos.get(0);
                    if("VAN".equals(paramVer))
                        mantenerAnexo.AsigRutAnexo(paramEmpresa, paramRutTrab, anx_id, anx_unid);
                    else
                    if("VFO".equals(paramVer))
                        mantenerAnexo.AsigRutFono(paramEmpresa, paramRutTrab, anx_id, anx_unid);
                    else
                    if("VFA".equals(paramVer))
                        mantenerAnexo.AsigRutFax(paramEmpresa, paramRutTrab, anx_id, anx_unid);
                }

            } else
            if(paramAccion.equals("Q"))
            {
                String arrValores[] = req.getParameterValues("asig");
                int largo_arrValores = 0;
                if(arrValores != null)
                    largo_arrValores = arrValores.length;
                for(int x = 0; x < largo_arrValores; x++)
                {
                    Vector anexos = Tools.separaLista(arrValores[x], ",");
                    String anx_unid = (String)anexos.get(1);
                    String anx_id = (String)anexos.get(0);
                    if("VAN".equals(paramVer))
                        mantenerAnexo.QuitarRutAnexo(paramEmpresa, paramRutTrab, anx_id, anx_unid);
                    else
                    if("VFO".equals(paramVer))
                        mantenerAnexo.QuitarRutFono(paramEmpresa, paramRutTrab, anx_id, anx_unid);
                    else
                    if("VFA".equals(paramVer))
                        mantenerAnexo.QuitarRutFax(paramEmpresa, paramRutTrab, anx_id, anx_unid);
                }

            }
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
        Connection Conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doGet de AsignarAnexo");
        user = Usuario.rescatarUsuario(req);
        String strRut = user.getRutConsultado();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion_chica.htm", "Crear Anexos", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermiso("df_mant_anexo"))
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion_chica.htm", "Asignar Anexos", "Usted no tiene permiso para Realizar esta Acci\363n...");
        else
            generaPagina(req, resp, "", user, Conexion);
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", Conexion);
    }

    public void generaPagina(HttpServletRequest req, HttpServletResponse resp, String msg, Usuario user, Connection conexion)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro a generaPagina");
        Validar valida = new Validar();
        ManAnexo mantenerAnexo = new ManAnexo(conexion);
        String paramVer = valida.validarDato(req.getParameter("ver"), "VAN");
        String paramUnidad = req.getParameter("unidad");
        String paramEmpresa = req.getParameter("empresa");
        String paramRutTrab = req.getParameter("rut");
        SimpleHash modelRoot = new SimpleHash();
        Rut userRut = new Rut(conexion, paramRutTrab);
        modelRoot.put("rut", paramRutTrab);
        modelRoot.put("nombre", userRut.Nombres);
        modelRoot.put("cargo", userRut.Cargo);
        modelRoot.put("foto", userRut.Foto);
        modelRoot.put("email", userRut.Email);
        modelRoot.put("mail", userRut.Mail);
        String sql = "";
        Consulta consul = new Consulta(conexion);
        boolean ejec_consul = false;
        if("VAN".equals(paramVer))
        {
            modelRoot.put("asignados", mantenerAnexo.getRutAnexos(paramRutTrab));
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unidad, anexo as id, publico FROM eje_ges_unidad_anexo WHERE (unidad = '")).append(paramUnidad).append("') AND NOT EXISTS").append(" (SELECT * FROM eje_ges_trabajadores_anexos").append(" WHERE (rut = ").append(paramRutTrab).append(") AND").append(" (anexo = eje_ges_unidad_anexo.anexo) AND").append(" (unidad = eje_ges_unidad_anexo.unidad))")));
            ejec_consul = true;
        } else
        if("VFO".equals(paramVer))
        {
            modelRoot.put("asignados", mantenerAnexo.getRutFonos(paramRutTrab));
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unidad, fono as id, publico FROM eje_ges_unidad_fono WHERE (unidad = '")).append(paramUnidad).append("') AND NOT EXISTS").append(" (SELECT * FROM eje_ges_trabajadores_fono").append(" WHERE (rut = ").append(paramRutTrab).append(") AND").append(" (fono = eje_ges_unidad_fono.fono) AND").append(" (unidad = eje_ges_unidad_fono.unidad))")));
            ejec_consul = true;
        } else
        if("VFA".equals(paramVer))
        {
            modelRoot.put("asignados", mantenerAnexo.getRutFaxes(paramRutTrab));
            sql = String.valueOf(String.valueOf((new StringBuilder("SELECT unidad, fax as id FROM eje_ges_unidad_fax WHERE (unidad = '")).append(paramUnidad).append("') AND NOT EXISTS").append(" (SELECT * FROM eje_ges_trabajadores_fax").append(" WHERE (rut = ").append(paramRutTrab).append(") AND").append(" (fax = eje_ges_unidad_fax.fax) AND").append(" (unidad = eje_ges_unidad_fax.unidad))")));
            ejec_consul = true;
        }
        if(ejec_consul)
        {
            OutMessage.OutMessagePrint(" Disp. --> ".concat(String.valueOf(String.valueOf(sql))));
            consul.exec(sql);
            modelRoot.put("disponibles", consul.getSimpleList());
        }
        modelRoot.put("empresa", paramEmpresa);
        modelRoot.put("unidad", paramUnidad);
        modelRoot.put("unidad_desc", req.getParameter("unidad_desc"));
        modelRoot.put("ver", paramVer);
        modelRoot.put("msg", msg);
        super.retTemplate(resp,"Gestion/Anexos/AsignarAnexo.htm",modelRoot);
        consul.close();
        mantenerAnexo.close();
        OutMessage.OutMessagePrint("Fin de generaPagina");
    }


    private Usuario user;
    private Tools tool;
    private static final String TEM_CREAR_ANEXOS = "AsignarAnexo.htm";
    private static final String PAG_MSG_ERROR = "Anexos/msgCreaAnexo.htm";
}