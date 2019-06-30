package portal.com.eje.datosPlantilla;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;

// Referenced classes of package com.eje.datosPlantilla:
//            AdminEjecucionPlantillaMgr

public class ServletGeneraInforme extends MyHttpServlet
{

    public ServletGeneraInforme()
    {
        try
        {
            jbInit();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doPost: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        user = SessionMgr.rescatarUsuario(req);
        if(!user.esValido())
        {
            super.mensaje.devolverPaginaSinSesion(resp, "ERROR de Sesi&oacute;n", "Su sesi&oacute;n ha expirado o no se ha logeado.");
        } else
        {
            conexion = super.connMgr.getConnection("portal");
            java.io.InputStream is = getClass().getResourceAsStream("/db.properties");
            Properties fileProps = new Properties();
            try
            {
                fileProps.load(is);
            }
            catch(Exception e)
            {
                OutMessage.OutMessagePrint("No se ha podido leer el PATH para crear archivos");
            }
            String path = fileProps.getProperty("pathInforme");
            periodo = req.getParameter("periodo");
            archivo = "informeGestion_" + user.getRutId() + "_" + periodo;
            File informe = new File(path + archivo + ".xls");
            if(!informe.exists())
            {
                AdminEjecucionPlantillaMgr obj = new AdminEjecucionPlantillaMgr(conexion);
                boolean campos = obj.datosTablaCampoAsignado();
                if(!campos)
                    obj.generaCampos();
                ccosto = obj.obtieneCentroCostoUsuario(user.getRutId());
                empresa = obj.obtieneEmpresaUsuario(user.getRutId());
                if(ccosto != "" && empresa != "")
                {
                    String select = obj.determinaQueryDatosGestion(periodo, ccosto, empresa);
                    boolean tx = obj.ejecutaSelectAndGeneraDataForTablaTempPlantilla(1, 1, select);
                    if(tx)
                    {
                        String query = "SELECT * FROM #TEMP_DATA_PLANTILLA" + obj.determinaStringOrderBy();
                        boolean tx2 = obj.generaInformeGestion(query, archivo, path, periodo);
                        if(tx2)
                            resp.sendRedirect("GeneraInformeGestion3?informe=" + archivo);
                        else
                            resp.sendRedirect("Tool?htm=informegestion/informeerror.htm");
                    } else
                    {
                        resp.sendRedirect("Tool?htm=informegestion/informeerror.htm");
                    }
                } else
                {
                    resp.sendRedirect("Tool?htm=informegestion/usuariosinccostoyemp.htm");
                }
            } else
            {
                resp.sendRedirect("GeneraInformeGestion3?informe=" + archivo);
            }
        }
        super.connMgr.freeConnection("portal", conexion);
        OutMessage.OutMessagePrint("\n**** FIN doPost: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private void jbInit()
        throws Exception
    {
    }

    private Usuario user;
    private Connection conexion;
    private String periodo;
    private String archivo;
    private String ccosto;
    private String empresa;
}