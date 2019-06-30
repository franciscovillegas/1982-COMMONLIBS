package organica.com.eje.ges.anexos;

import java.io.IOException;
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
import freemarker.template.SimpleList;

// Referenced classes of package com.eje.ges.anexos:
//            ManAnexo

public class BuscarAnexos extends MyHttpServlet
{

    public BuscarAnexos()
    {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\nEntro al doPost de BuscarAnexos");
        doGet(req, resp);
        OutMessage.OutMessagePrint("Fin de doPost");
    }

    private void devolverPaginaBusqueda(HttpServletResponse resp, boolean tienePermiso)
        throws IOException, ServletException
    {
        SimpleHash modelRoot = new SimpleHash();
        modelRoot.put("puede_mant", tienePermiso);
        super.retTemplate(resp,"main_anexos.htm",modelRoot);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        java.sql.Connection conexion = connMgr.getConnection("portal");
        OutMessage.OutMessagePrint("\nEntro al doGet de BuscarAnexos");
        user = Usuario.rescatarUsuario(req);
        Validar valida = new Validar();
        ControlAcceso control = new ControlAcceso(user);
        String paramAnexo = valida.validarDato(req.getParameter("anexo"), "");
        String paramRut = valida.validarDato(req.getParameter("rut"), "");
        if(!user.esValido())
            mensaje.devolverPaginaMensage(resp, "Anexos/sin_sesion.htm", "Buscar Anexos", "Tiempo de Sesi\363n expirado...");
        else
        if(paramAnexo.equals("") && paramRut.equals(""))
        {
            devolverPaginaBusqueda(resp, control.tienePermiso("df_mant_anexo"));
        } else
        {
            ManAnexo mantenerAnexo = new ManAnexo(conexion);
            String strMsg = "";
            String paramUnidad = req.getParameter("unidad");
            String html = req.getParameter("htm");
            if(html == null)
                html = "BuscaAnexos.htm";
            OutMessage.OutMessagePrint("html --> ".concat(String.valueOf(String.valueOf(html))));
            SimpleHash modelRoot = new SimpleHash();
            Consulta consul = new Consulta(conexion);
            String sqlTrab = "";
            SimpleList listaTrab = new SimpleList();
            OutMessage.OutMessagePrint("--> paramRut ".concat(String.valueOf(String.valueOf(paramRut))));
            if(paramRut != null && !paramRut.trim().equals(""))
            {
                String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT TOP 1 tr.rut, tr.nombre, ca.descrip AS cargo, tr_an.anexo, tr_an.unidad FROM eje_ges_trabajadores_anexos tr_an RIGHT OUTER JOIN eje_ges_trabajador tr ON  tr_an.rut = tr.rut LEFT OUTER JOIN eje_ges_cargos ca ON tr.cargo = ca.cargo AND  tr.empresa = ca.empresa WHERE (tr.rut = ")).append(paramRut).append(")")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                consul.exec(sql);
                if(consul.next())
                {
                    SimpleHash simplehash1 = new SimpleHash();
                    paramAnexo = consul.getString("anexo");
                    paramUnidad = consul.getString("unidad");
                    OutMessage.OutMessagePrint("--> paramAnexo ".concat(String.valueOf(String.valueOf(paramAnexo))));
                    simplehash1.put("anexo", paramAnexo);
                    String strRutTrabPaso = consul.getString("rut");
                    simplehash1.put("rut", strRutTrabPaso);
                    simplehash1.put("nombre", valida.cortarString(consul.getString("nombre"), 30));
                    simplehash1.put("cargo", valida.cortarString(consul.getString("cargo"), 30));
                    simplehash1.put("anexos", mantenerAnexo.getRutAnexos(strRutTrabPaso));
                    simplehash1.put("fonos", mantenerAnexo.getRutFonos(strRutTrabPaso));
                    simplehash1.put("faxes", mantenerAnexo.getRutFaxes(strRutTrabPaso));
                    listaTrab.add(simplehash1);
                } else
                {
                    strMsg = String.valueOf(String.valueOf((new StringBuilder("Trabajador con rut '")).append(paramRut).append("' no encontrado.\\n")));
                }
            }
            OutMessage.OutMessagePrint("--> paramAnexo ".concat(String.valueOf(String.valueOf(paramAnexo))));
            if(paramAnexo != null && !paramAnexo.trim().equals(""))
            {
                String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT DISTINCT ua.unidad, ua.anexo, u.unid_desc, ua.publico FROM eje_ges_unidad_anexo ua INNER JOIN eje_ges_unidades u ON ua.unidad = u.unid_id WHERE (ua.unidad = '")).append(paramUnidad).append("') AND (ua.anexo = ").append(paramAnexo).append(")")));
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sql))));
                consul.exec(sql);
                SimpleHash infoAnexo = consul.getSimpleHash();
                if(infoAnexo == null)
                    strMsg = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(strMsg)))).append("El anexo '").append(paramAnexo).append("' no existe.\\n")));
                else
                    modelRoot.put("infoanexo", infoAnexo);
                sqlTrab = String.valueOf(String.valueOf((new StringBuilder("SELECT tr.rut, tr.nombre, ca.descrip AS cargo FROM eje_ges_cargos ca RIGHT OUTER JOIN eje_ges_trabajador tr ON ca.cargo = tr.cargo AND ca.empresa = tr.empresa LEFT OUTER JOIN eje_ges_trabajadores_anexos ta ON tr.empresa = ta.empresa AND tr.rut = ta.rut WHERE (ta.anexo = '")).append(paramAnexo).append("') AND (ta.unidad = '").append(paramUnidad).append("')")));
                if(paramRut != null && !paramRut.trim().equals(""))
                    sqlTrab = String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(sqlTrab)))).append(" and (tr.rut <> ").append(paramRut).append(")")));
                sqlTrab = String.valueOf(String.valueOf(sqlTrab)).concat(" ORDER BY nombre");
                OutMessage.OutMessagePrint("--> ".concat(String.valueOf(String.valueOf(sqlTrab))));
                consul.exec(sqlTrab);
                String strRutTrabPaso = "";
                SimpleHash simplehash1;
                for(; consul.next(); listaTrab.add(simplehash1))
                {
                    simplehash1 = new SimpleHash();
                    strRutTrabPaso = consul.getString("rut");
                    simplehash1.put("rut", strRutTrabPaso);
                    simplehash1.put("nombre", valida.cortarString(consul.getString("nombre"), 30));
                    simplehash1.put("cargo", valida.cortarString(consul.getString("cargo"), 30));
                    simplehash1.put("anexos", mantenerAnexo.getRutAnexos(strRutTrabPaso));
                    simplehash1.put("fonos", mantenerAnexo.getRutFonos(strRutTrabPaso));
                    simplehash1.put("faxes", mantenerAnexo.getRutFaxes(strRutTrabPaso));
                }

                modelRoot.put("personas", listaTrab);
                modelRoot.put("anexos", mantenerAnexo.getAnexosRelacionados(paramUnidad, paramAnexo));
            } else
            {
                strMsg = String.valueOf(String.valueOf(strMsg)).concat("Anexo no especificado.");
            }
            modelRoot.put("personas", listaTrab);
            modelRoot.put("anexo", paramAnexo);
            modelRoot.put("rut", paramRut);
            modelRoot.put("msg", strMsg);
            modelRoot.put("unidad", paramUnidad);
            modelRoot.put("volver", req.getParameter("volver"));
            modelRoot.put("htm", html);
            super.retTemplate(resp,html,modelRoot);
            consul.close();
            mantenerAnexo.close();
        }
        OutMessage.OutMessagePrint("Fin de doGet");
        connMgr.freeConnection("portal", conexion);
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
    private static final String TEM_BUSCA_ANEXOS = "BuscaAnexos.htm";
    private static final String TEM_MANTENER_ANEXOS = "MantenerAnexos.htm";
}