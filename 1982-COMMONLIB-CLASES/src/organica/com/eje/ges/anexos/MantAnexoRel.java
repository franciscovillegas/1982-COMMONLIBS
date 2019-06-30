package organica.com.eje.ges.anexos;

import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

// Referenced classes of package com.eje.ges.anexos:
//            ManAnexo

public class MantAnexoRel extends MyHttpServlet
{

    public MantAnexoRel()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de MantAnexoRel");
        user = Usuario.rescatarUsuario(req);
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion_chica.htm", "Mantener Anexos", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermiso("df_mant_anexo"))
        {
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion_chica.htm", "Mantener Anexos", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            ManAnexo mantenerAnexo = new ManAnexo(conexion);
            String paramAccion = req.getParameter("accion");
            if(paramAccion == null)
                paramAccion = "";
            else
                paramAccion = paramAccion.trim();
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("--> paramAccion '")).append(paramAccion).append("'"))));
            if(paramAccion.equals("EAR"))
            {
                String arrAnexoRel[] = req.getParameterValues("anexo_rel");
                String paramAnexo = req.getParameter("anexo");
                String paramUnidad = req.getParameter("unidad");
                int largo_arrAnexoRel = 0;
                if(arrAnexoRel != null)
                    largo_arrAnexoRel = arrAnexoRel.length;
                for(int x = 0; x < largo_arrAnexoRel; x++)
                {
                    Vector anexoRel = Tools.separaLista(arrAnexoRel[x], ",");
                    String unidad_rel = (String)anexoRel.get(1);
                    String anexo_rel = (String)anexoRel.get(0);
                    mantenerAnexo.quitarAnexoRelacion(null, paramUnidad, paramAnexo, unidad_rel, anexo_rel);
                }

            }
            if(paramAccion.equals("AAR"))
            {
                String arrAnexoRel[] = req.getParameterValues("anexo_rel");
                String paramAnexo = req.getParameter("anexo");
                String paramUnidad = req.getParameter("unidad");
                String paramUnidadSel = req.getParameter("unidad_sel");
                int largo_arrAnexoRel = 0;
                if(arrAnexoRel != null)
                    largo_arrAnexoRel = arrAnexoRel.length;
                for(int x = 0; x < largo_arrAnexoRel; x++)
                    mantenerAnexo.agregarAnexoRelacion(null, paramUnidad, paramAnexo, paramUnidadSel, arrAnexoRel[x]);

            }
            mantenerAnexo.close();
            getPagAnexRel(req, resp, conexion);
        }
        OutMessage.OutMessagePrint("Fin de doPost");
        connMgr.freeConnection("portal", conexion);
    }

    private void devolverPaginaBusqueda(HttpServletResponse resp, boolean tienePermiso)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("puede_mant", tienePermiso);
        super.retTemplate(resp,"Gestion/Anexos/main_anexos.htm",modelRoot);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doGet de MantAnexoRel");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion_chica.htm", "Mantener Anexos", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermiso("df_mant_anexo"))
        {
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion_chica.htm", "Mantener Anexos", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            String queVer = valida.validarDato(req.getParameter("ver"), "AR");
            if("AU".equals(queVer))
                getPagAnexoUnidad(req, resp, conexion);
            else
                getPagAnexRel(req, resp, conexion);
        }
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", conexion);
    }

    public void getPagAnexRel(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        ManAnexo mantenerAnexo = new ManAnexo(conexion);
        ManAnexo manAnexo = new ManAnexo(conexion);
        SimpleHash modelRoot = new SimpleHash();
        String paramEmpresa = req.getParameter("empresa");
        String paramUnidad = req.getParameter("unidad");
        String paramAnexo = req.getParameter("anexo");
        modelRoot.put("empresa", paramEmpresa);
        modelRoot.put("unidad", paramUnidad);
        modelRoot.put("anexo", paramAnexo);
        modelRoot.put("unidad_desc", req.getParameter("unidad_desc"));
        modelRoot.put("anexos_rel", manAnexo.getAnexosRelacionados(paramUnidad, paramAnexo));
        manAnexo.close();
        super.retTemplate(resp,"Gestion/Anexos/MantAnexoRel.htm",modelRoot);
        mantenerAnexo.close();
    }

    public void getPagAnexoUnidad(HttpServletRequest req, HttpServletResponse resp, Connection conexion)
        throws IOException, ServletException
    {
        ManAnexo mantenerAnexo = new ManAnexo(conexion);
        ManAnexo manAnexo = new ManAnexo(conexion);
        SimpleHash modelRoot = new SimpleHash();
        String paramEmpresa = req.getParameter("empresa");
        String paramUnidad = req.getParameter("unidad_sel");
        Consulta consul = new Consulta(conexion);
        String sql = "SELECT DISTINCT unid_id AS id, unid_desc AS descrip FROM eje_ges_unidades where vigente = 'S' order by descrip";
        consul.exec(sql);
        modelRoot.put("unidades", consul.getSimpleList());
        modelRoot.put("empresa", paramEmpresa);
        modelRoot.put("unidad_sel", paramUnidad);
        modelRoot.put("unidad", req.getParameter("unidad"));
        modelRoot.put("unidad_desc", req.getParameter("unidad_desc"));
        modelRoot.put("anexo", req.getParameter("anexo"));
        OutMessage.OutMessagePrint("unidad_sel: ".concat(String.valueOf(String.valueOf(paramUnidad))));
        if(paramUnidad != null)
            modelRoot.put("anexos", manAnexo.getAnexosExistentes(paramUnidad));
        consul.close();
        manAnexo.close();
        super.retTemplate(resp,"Gestion/Anexos/SelAnexoUnidad.htm",modelRoot);
        mantenerAnexo.close();
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}