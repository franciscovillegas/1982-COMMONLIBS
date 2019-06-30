package organica.com.eje.ges.anexos;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import organica.bci.traspaso.tools.Rut;
import organica.com.eje.ges.usuario.ControlAcceso;
import organica.com.eje.ges.usuario.Usuario;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import freemarker.template.SimpleHash;

// Referenced classes of package com.eje.ges.anexos:
//            ManAnexo

public class MantAnexoUnidad extends MyHttpServlet
{

    public MantAnexoUnidad()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doPost de MantAnexoUnidad");
        user = Usuario.rescatarUsuario(req);
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion.htm", "Mantener Anexos", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermiso("df_mant_anexo"))
        {
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion.htm", "Mantener Anexos", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            ManAnexo mantenerAnexo = new ManAnexo(conexion);
            String paramAccion = req.getParameter("accion");
            if(paramAccion == null)
                paramAccion = "";
            else
                paramAccion = paramAccion.trim();
            OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("--> paramAccion '")).append(paramAccion).append("'"))));
            if(paramAccion.equals("CA"))
            {
                String arrRutTrab[] = req.getParameterValues("ruts");
                int largo_arrRutTrab = 0;
                if(arrRutTrab != null)
                    largo_arrRutTrab = arrRutTrab.length;
                for(int x = 0; x < largo_arrRutTrab; x++)
                {
                    String strRutTrab = arrRutTrab[x];
                    String strNuevoAnexo = req.getParameter("anexo_".concat(String.valueOf(String.valueOf(strRutTrab))));
                    if(strNuevoAnexo == null)
                        strNuevoAnexo = "";
                    else
                        strNuevoAnexo = strNuevoAnexo.trim();
                    mantenerAnexo.cambiarAnexo(null, strRutTrab, strNuevoAnexo);
                }

            }
            if(paramAccion.equals("EAN") || paramAccion.equals("EFO") || paramAccion.equals("EFA"))
            {
                String arrValores[] = req.getParameterValues("elim");
                String paramUnidad = req.getParameter("unidad");
                if(paramUnidad == null)
                    paramUnidad = "";
                else
                    paramUnidad = paramUnidad.trim();
                OutMessage.OutMessagePrint(String.valueOf(String.valueOf((new StringBuilder("--> paramUnidad '")).append(paramUnidad).append("'"))));
                int largo_arrValores = 0;
                if(arrValores != null)
                    largo_arrValores = arrValores.length;
                for(int x = 0; x < largo_arrValores; x++)
                    if(paramAccion.equals("EAN"))
                        mantenerAnexo.eliminarAnexo(null, paramUnidad, arrValores[x]);
                    else
                    if(paramAccion.equals("EFO"))
                        mantenerAnexo.eliminarFono(null, paramUnidad, arrValores[x]);
                    else
                    if(paramAccion.equals("EFA"))
                        mantenerAnexo.eliminarFax(null, paramUnidad, arrValores[x]);

            }
            mantenerAnexo.close();
            doGet(req, resp);
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
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doGet de MantAnexoUnidad");
        user = Usuario.rescatarUsuario(req);
        ControlAcceso control = new ControlAcceso(user);
        if(!user.esValido())
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion.htm", "Mantener Anexos", "Tiempo de Sesi\363n expirado...");
        else
        if(!control.tienePermiso("df_mant_anexo"))
        {
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion.htm", "Mantener Anexos", "Usted no tiene permiso para ver esta informaci\363n...");
        } else
        {
            ManAnexo mantenerAnexo = new ManAnexo(conexion);
            ManAnexo manAnexo = new ManAnexo(conexion);
            SimpleHash modelRoot = new SimpleHash();
            String paramEmpresa = req.getParameter("empresa");
            String paramUnidad = req.getParameter("unidad");
            int busca = 0;
            String paramRut = req.getParameter("rutb");
            String paramNom = req.getParameter("nomb");
            String paramPat = req.getParameter("patb");
            String paramMat = req.getParameter("matb");
            System.out.println(String.valueOf(String.valueOf((new StringBuilder("Unidad= ")).append(paramUnidad).append("\nRut= ").append(paramRut).append("\nNombre= ").append(paramNom).append("\nPaterno= ").append(paramPat).append("\nMaterno= ").append(paramMat))));
            if(!"".equals(paramUnidad) && paramRut == null && paramNom == null && paramPat == null && paramMat == null)
                busca = 1;
            else
            if(!"".equals(paramRut))
                busca = 2;
            else
            if(!"".equals(paramNom))
                busca = 3;
            else
            if(!"".equals(paramPat))
                busca = 4;
            else
            if(!"".equals(paramMat))
                busca = 5;
            Consulta consul = new Consulta(conexion);
            String sql = "SELECT DISTINCT unid_id AS id, unid_desc AS descrip FROM eje_ges_unidades where vigente = 'S' order by descrip";
            consul.exec(sql);
            modelRoot.put("unidades", consul.getSimpleList());
            modelRoot.put("empresa", paramEmpresa);
            switch(busca)
            {
            default:
                break;

            case 1: // '\001'
                modelRoot.put("anexos", manAnexo.getAnexosExistentes(paramUnidad));
                modelRoot.put("faxes", manAnexo.getFaxExistentes(paramUnidad));
                modelRoot.put("fonos", manAnexo.getFonosExistentes(paramUnidad));
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT tra.unidad, tra.empresa AS id_empresa,  emp.descrip AS empresa, tra.rut, tra.digito_ver, tra.nombre, tra.cargo FROM eje_ges_trabajador tra LEFT OUTER JOIN eje_ges_empresa emp ON tra.empresa = emp.empresa WHERE (tra.unidad = '")).append(paramUnidad).append("') ORDER BY emp.descrip, tra.nombre")));
                System.out.println("Buscar Unidad:----> ".concat(String.valueOf(String.valueOf(sql))));
                consul.exec(sql);
                modelRoot.put("ver1", "visible");
                modelRoot.put("ver2", "visible");
                modelRoot.put("ver3", "visible");
                modelRoot.put("unidad", paramUnidad);
                modelRoot.put("empleados", consul.getSimpleList());
                break;

            case 2: // '\002'
                Rut xrut = new Rut(paramRut);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT tra.unidad, tra.empresa AS id_empresa,  emp.descrip AS empresa, tra.rut, tra.digito_ver, tra.nombre, tra.cargo,tra.ape_paterno, tra.ape_materno FROM eje_ges_trabajador tra LEFT OUTER JOIN eje_ges_empresa emp ON tra.empresa = emp.empresa WHERE (tra.rut = ")).append(xrut.getRut()).append(") ").append("AND (tra.digito_ver = '").append(xrut.getDigVer()).append("') ")));
                if(!"".equals(paramNom))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (tra.nombre LIKE '%").append(paramNom).append("%') ")));
                    modelRoot.put("nomb", paramNom);
                }
                if(!"".equals(paramPat))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (tra.ape_paterno LIKE '%").append(paramPat).append("%') ")));
                    modelRoot.put("patb", paramPat);
                }
                if(!"".equals(paramMat))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (tra.ape_materno LIKE '%").append(paramMat).append("%') ")));
                    modelRoot.put("matb", paramMat);
                }
                sql = String.valueOf(String.valueOf(sql)).concat("ORDER BY emp.descrip, tra.nombre");
                System.out.println("Buscar RUT:----> ".concat(String.valueOf(String.valueOf(sql))));
                consul.exec(sql);
                modelRoot.put("rutb", paramRut);
                modelRoot.put("ver1", "hidden");
                modelRoot.put("ver2", "hidden");
                modelRoot.put("ver3", "hidden");
                modelRoot.put("empleados", consul.getSimpleList());
                break;

            case 3: // '\003'
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT tra.unidad, tra.empresa AS id_empresa,  emp.descrip AS empresa, tra.rut, tra.digito_ver, tra.nombre, tra.cargo,tra.ape_paterno, tra.ape_materno FROM eje_ges_trabajador tra LEFT OUTER JOIN eje_ges_empresa emp ON tra.empresa = emp.empresa WHERE (tra.nombre LIKE '%")).append(paramNom).append("%') ")));
                if(!"".equals(paramPat))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (tra.ape_paterno LIKE '%").append(paramPat).append("%') ")));
                    modelRoot.put("patb", paramPat);
                }
                if(!"".equals(paramMat))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (tra.ape_materno LIKE '%").append(paramMat).append("%') ")));
                    modelRoot.put("matb", paramMat);
                }
                sql = String.valueOf(String.valueOf(sql)).concat("ORDER BY emp.descrip, tra.nombre");
                System.out.println("Buscar Nombre:----> ".concat(String.valueOf(String.valueOf(sql))));
                consul.exec(sql);
                modelRoot.put("nomb", paramNom);
                modelRoot.put("ver1", "hidden");
                modelRoot.put("ver2", "hidden");
                modelRoot.put("ver3", "hidden");
                modelRoot.put("empleados", consul.getSimpleList());
                break;

            case 4: // '\004'
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT tra.unidad, tra.empresa AS id_empresa,  emp.descrip AS empresa, tra.rut, tra.digito_ver, tra.nombre, tra.cargo,tra.ape_paterno, tra.ape_materno FROM eje_ges_trabajador tra LEFT OUTER JOIN eje_ges_empresa emp ON tra.empresa = emp.empresa WHERE (tra.ape_paterno LIKE '%")).append(paramPat).append("%') ")));
                if(!"".equals(paramNom))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (tra.nombre LIKE '%").append(paramNom).append("%') ")));
                    modelRoot.put("nomb", paramNom);
                }
                if(!"".equals(paramMat))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (tra.ape_materno LIKE '%").append(paramMat).append("%') ")));
                    modelRoot.put("matb", paramMat);
                }
                sql = String.valueOf(String.valueOf(sql)).concat("ORDER BY emp.descrip, tra.nombre");
                System.out.println("Buscar Ape Paterno:----> ".concat(String.valueOf(String.valueOf(sql))));
                consul.exec(sql);
                modelRoot.put("patb", paramPat);
                modelRoot.put("ver1", "hidden");
                modelRoot.put("ver2", "hidden");
                modelRoot.put("ver3", "hidden");
                modelRoot.put("empleados", consul.getSimpleList());
                break;

            case 5: // '\005'
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT tra.unidad, tra.empresa AS id_empresa,  emp.descrip AS empresa, tra.rut, tra.digito_ver, tra.nombre, tra.cargo,tra.ape_paterno, tra.ape_materno FROM eje_ges_trabajador tra LEFT OUTER JOIN eje_ges_empresa emp ON tra.empresa = emp.empresa WHERE (tra.ape_materno LIKE '%")).append(paramMat).append("%') ")));
                if(!"".equals(paramNom))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (tra.nombre LIKE '%").append(paramNom).append("%') ")));
                    modelRoot.put("nomb", paramNom);
                }
                if(!"".equals(paramPat))
                {
                    sql = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sql)))).append("and (tra.ape_paterno LIKE '%").append(paramPat).append("%') ")));
                    modelRoot.put("patb", paramPat);
                }
                sql = String.valueOf(String.valueOf(sql)).concat("ORDER BY emp.descrip, tra.nombre");
                System.out.println("Buscar Ape Materno:----> ".concat(String.valueOf(String.valueOf(sql))));
                consul.exec(sql);
                modelRoot.put("matb", paramMat);
                modelRoot.put("ver1", "hidden");
                modelRoot.put("ver2", "hidden");
                modelRoot.put("ver3", "hidden");
                modelRoot.put("empleados", consul.getSimpleList());
                break;
            }
            consul.close();
            manAnexo.close();
            modelRoot.put("cerrar_sesion", req.getParameter("CS") != null);
            super.retTemplate(resp,"Gestion/Anexos/MantAnexoUnidad.htm",modelRoot);
            mantenerAnexo.close();
        }
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}