package mis;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.datos.Consulta;
import portal.com.eje.serhumano.certificados.Certif_Manager;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;
import freemarker.template.Template;

public class ServletInitPeriodo extends MyHttpServlet {

    public ServletInitPeriodo() {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        Connection Conexion = super.connMgr.getConnection("mis");
        if(Conexion != null) {
        	 String rutOrg = req.getParameter("rut_org")==null?"false":req.getParameter("rut_org");
             MuestraDatos(req, resp, Conexion,rutOrg);
        } 
        else {
            mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        super.connMgr.freeConnection("mis", Conexion);
    }

    public void MuestraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion, String rutOrg)
    throws IOException, ServletException {
        Usuario user = SessionMgr.rescatarUsuario(req);

        SimpleHash modelRoot = new SimpleHash();
        if(user.esValido()) {
            Certif_Manager periodos = new Certif_Manager(Conexion);
        	ResourceBundle proper = ResourceBundle.getBundle("db");
        	String empresa = proper.getString("birt.empresa");
            Consulta info = periodos.GetPeriodosMis(empresa);
            SimpleList simplehash1 = getPeriodos(info);
            modelRoot.put("periodos", simplehash1);
            modelRoot.put("rutOrg", rutOrg);

            super.retTemplate(resp,"mis/periodos.html",modelRoot);
        } 
        else {
            mensaje.devolverPaginaSinSesion(resp, "Certificados", "Tiempo de Sesi\363n expirado...");
        }
        OutMessage.OutMessagePrint("\n**** Fin MuestraDatos: " + getClass().getName());
    }

    private SimpleList getPeriodos(Consulta p_periodosConsulta) {
        SimpleList periodos = new SimpleList();
        boolean primero = true;
        while (p_periodosConsulta.next()) {

            SimpleHash periodo = new SimpleHash();
            periodo.put("id", p_periodosConsulta.getString("peri_id"));

            String periodoAnio = p_periodosConsulta.getString("peri_ano");
            int periodoMes = p_periodosConsulta.getInt("peri_mes");
            periodo.put("desc", Tools.RescataMes(periodoMes) + " de " + periodoAnio);

          if (primero) {
              periodo.put("primero", "selected");
              primero = false;
          }

          periodos.add(periodo);
        }

        return periodos;
    }
}