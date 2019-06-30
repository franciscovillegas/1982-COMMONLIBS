package portal.com.eje.serhumano.user;

import java.io.IOException;
import java.sql.Connection;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.datos.Consulta;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.admin.CapManager;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.md5.ManagerMD5;
import freemarker.template.SimpleHash;

public class S_CambiarClave extends MyHttpServlet  {

    public S_CambiarClave() {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
    	
        proper = ResourceBundle.getBundle("db");
        String usarDBexterna = proper.getString("winper.enable");
        String usarDBexterna2 = proper.getString("winper2.enable");
//    	MMA 20170111
//        Connection ConSh = connMgr.getConnection("portal");
//        Connection ConQSDiario = connMgr.getConnection("qs_diario");
//        Connection ConQSNoticias = connMgr.getConnection("qs_noticias");
//        Connection ConQSRevista = connMgr.getConnection("qs_revista");
//        Connection ConQSBenef = connMgr.getConnection("qs_benef");
//        Connection ConQSNormas = connMgr.getConnection("qs_normas");
        Connection ConSh = getConnMgr().getConnection("portal");
        Connection ConQSDiario = null; //getConnMgr().getConnection("qs_diario");
        Connection ConQSNoticias = null; //getConnMgr().getConnection("qs_noticias");
        Connection ConQSRevista = null; //getConnMgr().getConnection("qs_revista");
        Connection ConQSBenef = null; //getConnMgr().getConnection("qs_benef");
        Connection ConQSNormas = null; //getConnMgr().getConnection("qs_normas");
        
        Connection conexUser = null;
        Connection conexUser2 = null;
        if(usarDBexterna.equals("S")) {
//        	MMA 20170111
//        	conexUser = connMgr.getConnection("winper");
        	conexUser = getConnMgr().getConnection("winper");
        }
        if(usarDBexterna.equals("S") && usarDBexterna2.equals("S")) {
//        	MMA 20170111
//        	conexUser2 = connMgr.getConnection("winper2");
        	conexUser2 = getConnMgr().getConnection("winper2");
        }
        Usuario user = SessionMgr.rescatarUsuario(req);
        if(ConSh != null) {
        	Actualiza(req, resp, ConSh, conexUser, conexUser2, ConQSDiario, ConQSNoticias, ConQSRevista, ConQSBenef, ConQSNormas);
        }
        else {
        	mensaje.devolverPaginaMensage(resp, "", "Error de conexi\363n a la BD");
        }
//    	MMA 20170111
//        connMgr.freeConnection("portal", ConSh);
//        connMgr.freeConnection("qs_diario", ConQSDiario);
//        connMgr.freeConnection("qs_noticias", ConQSNoticias);
//        connMgr.freeConnection("qs_revista", ConQSRevista);
//        connMgr.freeConnection("qs_benef", ConQSBenef);
//        connMgr.freeConnection("qs_normas", ConQSNormas);
        getConnMgr().freeConnection("portal", ConSh);
        getConnMgr().freeConnection("qs_diario", ConQSDiario);
        getConnMgr().freeConnection("qs_noticias", ConQSNoticias);
        getConnMgr().freeConnection("qs_revista", ConQSRevista);
        getConnMgr().freeConnection("qs_benef", ConQSBenef);
        getConnMgr().freeConnection("qs_normas", ConQSNormas);
        if(usarDBexterna.equals("S")) {
//        	MMA 20170111
//        	connMgr.freeConnection("winper", conexUser);
        	getConnMgr().freeConnection("winper", conexUser);
        }
        if(usarDBexterna.equals("S") && usarDBexterna2.equals("S")) {
//        	MMA 20170111
//        	connMgr.freeConnection("winper2", conexUser2);
        	getConnMgr().freeConnection("winper2", conexUser2);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
//    	MMA 20170111
//        Connection Conexion = connMgr.getConnection("portal");
        Connection Conexion = getConnMgr().getConnection("portal");
        if(Conexion != null) {
        	GeneraDatos(req, resp, Conexion);
        }
        else {
        	mensaje.devolverPaginaMensage(resp, "", "Error de conexi\363n a la BD");
        }
//    	MMA 20170111
//        connMgr.freeConnection("portal", Conexion);
        getConnMgr().freeConnection("portal", Conexion);
    }

    public void Actualiza(HttpServletRequest req, HttpServletResponse resp, Connection Consh, Connection conexUser, 
    		Connection conexUser2, Connection ConQSDiario, Connection ConQSNoticias, 
            Connection ConQSRevista, Connection ConQSBenef, Connection ConQSNormas)
    	throws ServletException, IOException {
        Usuario user = SessionMgr.rescatarUsuario(req);
        insTracking(req, "Actualización".intern(), null);
        String pRut = null;
        String antigua = null;
        String nueva = null;
        String nueva_no_md5 = null;
        String pass = null;
        String md5 = null;
        Consulta consul = null;
        boolean ok = false;
        SimpleHash modelRoot = new SimpleHash();
        proper = ResourceBundle.getBundle("db");
        String usarDBexterna = proper.getString("winper.enable");
        String usarDBexterna2 = proper.getString("winper2.enable");
        CapManager cm = new CapManager(Consh);
        if(user.esValido()) {
            nueva = req.getParameter("nClave");
            nueva_no_md5 = req.getParameter("nClave");
            if(user.tieneApp("adm") || user.tieneApp("adm_clave")) {
                pRut = req.getParameter("pRut");
                consul = cm.getDatosUserPass(pRut);
                if(consul.next()) {
                    pass = consul.getString("password_usuario").trim();
                    md5 = consul.getString("md5");
                }
                antigua = pass;
                if(md5.equals("S")) {
                	nueva = ManagerMD5.encrypt(nueva);
                }
            } 
            else {
                pRut = user.getRutId();
                consul = cm.getDatosUserPass(pRut);
                if(consul.next()) {
                    pass = consul.getString("password_usuario").trim();
                    md5 = consul.getString("md5");
                }
                antigua = req.getParameter("aClave");
                if(md5.equals("S")) {
                    nueva = ManagerMD5.encrypt(nueva);
                    antigua = ManagerMD5.encrypt(antigua);
                }
            }
            if(pass != null) {
                if(pass.equals(antigua)) {
                    UserMgr usermgr = new UserMgr(conexUser);
                    UserMgr usermgr2 = new UserMgr(conexUser2);
                    String m_pass = "La Clave se actualiz\363 en :<br>";
                    if(conexUser != null && usarDBexterna.equals("S")) {
                        String passAux;
                        if(md5.equals("S")) {
                        	passAux = nueva;
                        }
                        else {
                        	passAux = ManagerMD5.encrypt(nueva);
                        }
                        ok = usermgr.updateUserWinper(pRut, passAux);
                        if(ok) {
                        	m_pass = String.valueOf(String.valueOf(m_pass)).concat("");
                        }
                        else {
                        	m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Problemas en conexion<br>");
                        }
                    } 
                    else {
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("");
                    }
                    if(conexUser2 != null && usarDBexterna.equals("S") && usarDBexterna2.equals("S")) {
                        String passAux = nueva_no_md5;
                        Consulta consul4 = usermgr.selectUserWinper(pRut, passAux);
                        if(consul4.next()) {
                            String codigo = consul4.getString("codigouser");
                            ok = usermgr2.updateUserWinper2(codigo.trim(), passAux.trim());
                            if(ok) {
                                OutMessage.OutMessagePrint("\n5");
                                m_pass = String.valueOf(String.valueOf(m_pass)).concat("");
                            } 
                            else {
                                m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Problemas en conexion");
                            }
                        }
                        consul4.close();
                    } 
                    else {
                        m_pass = String.valueOf(String.valueOf(m_pass)).concat("");
                    }
                    if(pRut.equals("99999999") && !user.getRutId().equals(pRut)) {
                        modelRoot.put("error_pass", true);
                        modelRoot.put("error_rut", false);
                        modelRoot.put("cambiar", false);
                    } 
                    else {
                        if(cm.UpdateUser(pRut, nueva)) {
                        	m_pass = String.valueOf(String.valueOf(m_pass)).concat("  - Portal RRHH<br>");
                        	
                        	System.out.println("GRABAR CLAVE S_ActualizaClave");
                     	    user.grabaPass(Consh, pRut, req.getParameter("nClave"));
                        }
                        modelRoot.put("passok", m_pass);
                        user.setPassWord(nueva);
                        modelRoot.put("cambiar", true);
                    }
                } 
                else {
                    modelRoot.put("error_pass", true);
                    modelRoot.put("error_rut", false);
                    modelRoot.put("cambiar", false);
                }
            } 
            else {
                modelRoot.put("error_pass", false);
                modelRoot.put("error_rut", true);
                modelRoot.put("cambiar", false);
            }
            modelRoot.put("pRut", req.getParameter("pRut"));
            modelRoot.put("pDig", req.getParameter("pDig"));
            modelRoot.put("usuario", user.toHash());
            super.retTemplate(resp,"user/user_cambio_clave.htm",modelRoot);
            consul.close();
        } 
        else {
            mensaje.devolverPaginaMensage(resp, "Time Out", "El tiempo de sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

    public void GeneraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion)
        throws ServletException, IOException {
        Usuario user = SessionMgr.rescatarUsuario(req);
        insTracking(req, "Cambio Clave".intern(), null);
        String rut = "";
        String dig = "";
        SimpleHash modelRoot = new SimpleHash();
        if(user.esValido()) {
            rut = user.getRutId();
            dig = user.getRut().getDig();
            
            String regla = req.getParameter("regla");
            proper = ResourceBundle.getBundle("db");
            if(!"0".equals(regla)) {
            	String msgClave = "";
            	try {
            		msgClave = proper.getString("clave.mensaje"+regla);
            	}
            	catch(Exception e) {
            		
            	}
            	
            	modelRoot.put("msgClave", msgClave);
            }
            
            modelRoot.put("usuario", user.toHash());
            modelRoot.put("pRut", rut);
            modelRoot.put("pDig", dig);
            
            IOClaseWeb io = new IOClaseWeb(this, req, resp);
            super.retTemplate(io,"user/user_cambio_clave.htm",modelRoot);
        } 
        else {
            mensaje.devolverPaginaSinSesion(resp, "Cambiar Clave", "El tiempo de sesi\363n ha expirado.Por favor ingrese nuevamente.");
        }
    }

    private ResourceBundle proper;
}