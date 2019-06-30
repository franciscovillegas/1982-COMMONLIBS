package portal.com.eje.orgpeople.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.orgpeople.manager.orgManager;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;

public class S_verOrganica extends MyHttpServlet
{

    public S_verOrganica()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        Connection Conexion = super.connMgr.getConnection("portal");
        if(Conexion != null)
            muestraPag(req, resp, Conexion);
        else
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        super.connMgr.freeConnection("portal", Conexion);
    }

    public void muestraPag(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException
    {
        OutMessage.OutMessagePrint("\n**** Inicio generaTab: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        String rut = req.getParameter("rut");

        SimpleHash modelRoot = new SimpleHash();
        Validar vld = new Validar();
        String pHtm = vld.validarDato(req.getParameter("pHtm"), "orgpeople/org.html");
        String strRut = vld.validarDato(req.getParameter("rut"), "1");
        orgManager om = new orgManager();
        ResourceBundle proper = ResourceBundle.getBundle("app");
        String strUnidad = "";
        String strEmpresa = "";
        String strHttp = "";
        try
        {
            strUnidad = proper.getString("org.UnidadRRHH");
            strEmpresa = proper.getString("org.EmpRRHH");
            strHttp = proper.getString("org.httpFotoRut");
            Consulta rs = om.getEncargadoUnidad(strEmpresa, strUnidad, Conexion);
            if(rs.next())
                strRut = vld.validarDato(rs.getString("rut_encargado"), "0");
            rs.close();
        }
        catch(MissingResourceException e)
        {
            System.out.println("Exepcion : ".concat(String.valueOf(String.valueOf(e.getMessage()))));
        }
        modelRoot.put("list", om.getColaboradorRaiz(strEmpresa, strRut, "", Conexion, 0));
        modelRoot.put("httpFoto", strHttp);
        super.retTemplate(resp,pHtm,modelRoot);
        OutMessage.OutMessagePrint("\n**** Fin generaTab: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    private Tools tool;
}