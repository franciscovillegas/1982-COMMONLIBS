package organica.com.eje.ges.carpelect;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.SimpleHash;
import organica.datos.Consulta;
import organica.tools.OutMessage;
import organica.tools.Tools;
import organica.tools.Validar;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.Mensaje;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.menu.bean.FichaPersonalBean;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;

public class MuestraInfoRut extends MyHttpServlet {

    public MuestraInfoRut() {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
//    	MMA 20170112
//        Connection conexion = connMgr.getConnection("portal");
    	Connection conexion = getConnMgr().getConnection("portal");
        user = SessionMgr.rescatarUsuario(req);
        if(user.esValido()) {
            if(conexion != null) {
            	GeneraDatos(req, resp, conexion);
            }
            else {
            	devolverPaginaError(resp, "Error de conexi\363n a la BD");
            }
        } 
        else {
            mensaje.devolverPaginaMensage(resp, "Time Out", "El tiempo de su sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
//    	MMA 20170112
//        connMgr.freeConnection("portal", conexion);
        getConnMgr().freeConnection("portal", conexion);
    }

    public void GeneraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws IOException, ServletException {
        String rut = req.getParameter("rut");
        if(rut == null || rut.equals("")) {
        	rut = user.getRut().getRut();
        }
        if(!user.esValido()) {
            mensaje.devolverPaginaSinSesion(resp, "Datos Personales", "Tiempo de Sesi\363n expirado...");
        } 
    	else  {
            String consulta = null;
            String digito = "";
           
            String edad = null;
            String Ncargas = null;
            int anos = 0;
            Consulta info = new Consulta(Conexion);
            Validar valida = new Validar();
            Consulta cargas = new Consulta(Conexion);
            consulta = "select count(nombre) as cargas from eje_ges_grupo_familiar where (es_carga = 'S') and rut=" + rut;
            cargas.exec(consulta);
           
            FichaPersonalBean fp = FichaPersonalBean.getInstance();
            SimpleHash modelRoot = fp.getMuestraInfoRut(Conexion, rut);
            modelRoot.put("formacion", fp.getFormacion(Conexion,rut));
            
            Consulta infoDatosPersonales = new Consulta(Conexion);
            consulta = "select digito_ver,nombre,id_foto,cargo from view_ges_InfoRut where rut = '" + rut + "'";
            OutMessage.OutMessagePrint("---->consulta: " + consulta);
            info.exec(consulta);
            if(infoDatosPersonales.next()) {
	            digito = info.getString("digito_ver");
	            modelRoot.put("rut2", Tools.setFormatNumber(rut) + "-" + digito);
	            modelRoot.put("nombre", info.getString("nombre"));
	            modelRoot.put("cargo", info.getString("cargo"));
                modelRoot.put("foto", info.getString("id_foto")); 
            }
            IOClaseWeb io = new IOClaseWeb(this, req, resp);
            io.retTemplate("CarpetaElectronica/InfoBasicaRut.htm",modelRoot);
        }
    }

    private void devolverPaginaError(HttpServletResponse resp, String msg) {
        try {
            PrintWriter printwriter = resp.getWriter();
            resp.setContentType("text/html");
            printwriter.println("<html>");
            printwriter.println("<head>");
            printwriter.println("<title>Valores recogidos en el formulario</title>");
            printwriter.println("</head>");
            printwriter.println("<body>");
            printwriter.println(msg);
            printwriter.println("</body>");
            printwriter.println("</html>");
            printwriter.flush();
            printwriter.close();
        }
        catch(IOException e) {
            OutMessage.OutMessagePrint("Error al botar la pagina");
        }
    }

    private Usuario user;
    private Tools tool;
    private Mensaje mensaje;
}