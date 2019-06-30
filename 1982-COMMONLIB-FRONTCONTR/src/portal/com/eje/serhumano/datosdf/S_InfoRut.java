package portal.com.eje.serhumano.datosdf;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.admin.CapManager;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.servlet.GetProp;
import freemarker.template.SimpleHash;

// Referenced classes of package portal.com.eje.serhumano.datosdf:
//            datosRut

public class S_InfoRut extends MyHttpServlet
{

    public S_InfoRut()
    {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        OutMessage.OutMessagePrint("\n**** Inicio doPost: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        java.sql.Connection conexion = super.connMgr.getConnection("portal");
        String html = req.getParameter("htm");
        if(html == null)
            html = "fichapersonal/fichapersonal.htm";
        SimpleHash modelRoot = new SimpleHash();
        user = SessionMgr.rescatarUsuario(req);
        if(conexion != null)
        {
            if(!user.esValido())
            {
                super.mensaje.devolverPaginaSinSesion(resp, "Informaci\363n Empleado", "Tiempo de Sesi\363n expirado...");
            } else
            {
            	CapManager cm = new CapManager(conexion);
                String strRut = req.getParameter("rut");
                datosRut userRut = new datosRut(conexion, strRut);
                OutMessage.OutMessagePrint("html --> ".concat(String.valueOf(String.valueOf(html))));
                Consulta consul = new Consulta(conexion);
                modelRoot.put("GetProp", new GetProp(ResourceBundle.getBundle("db")));
                modelRoot.put("rut", strRut);
                modelRoot.put("nom_empresa", userRut.EmpresaDescrip);
                modelRoot.put("nombre", userRut.Nombres);
                modelRoot.put("unidad", cm.getUnidadRut(strRut, conexion, user.getEmpresa()));
                modelRoot.put("cargo", userRut.Cargo);
                modelRoot.put("foto", String.valueOf(userRut.Foto).toLowerCase());
                modelRoot.put("email", userRut.Email);
                modelRoot.put("mail", userRut.Mail);
                String sql = String.valueOf(String.valueOf((new StringBuilder("SELECT ta.anexo, ta.unidad FROM  dbo.eje_ges_trabajadores_anexos ta WHERE  (ta.rut = ")).append(userRut.Rut).append(") ")));
                consul.exec(sql);
                modelRoot.put("anexo", consul.getSimpleList());
                modelRoot.put("rutsup", userRut.Sup_Rut);
                modelRoot.put("cargosup", userRut.Sup_Cargo);
                modelRoot.put("nomsup", userRut.Sup_Nombre);
                modelRoot.put("unidadsup", userRut.Sup_Unidad);
                modelRoot.put("emailsup", userRut.Sup_Email);
                modelRoot.put("mailsup", userRut.Sup_Mail);
                sql = String.valueOf(String.valueOf((new StringBuilder("SELECT ta.anexo, ta.unidad FROM  dbo.eje_ges_trabajadores_anexos ta WHERE  (ta.rut = ")).append(userRut.Sup_Rut).append(") ")));
                consul.exec(sql);
                modelRoot.put("anexosup", consul.getSimpleList());
                consul.close();
                super.retTemplate(resp,html,modelRoot);
            }
            insTracking(req, "Información de rut previa a modificar".intern(), null);
        } else
        {
            super.mensaje.devolverPaginaSinSesion(resp, "problemas T\351nicos", "Error en la Conexi\363n.");
        }
        try
        {
        	super.retTemplate(resp,html,modelRoot);
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
        super.connMgr.freeConnection("portal", conexion);
        OutMessage.OutMessagePrint("\n**** Fin doPost: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException
    {
        doPost(req, resp);
    }

    private Usuario user;
}