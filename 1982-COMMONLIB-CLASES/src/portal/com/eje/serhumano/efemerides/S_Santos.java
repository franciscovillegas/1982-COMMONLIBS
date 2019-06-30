package portal.com.eje.serhumano.efemerides;

import java.io.IOException;
import java.sql.Connection;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.datos.Consulta;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import portal.com.eje.tools.OutMessage;
import portal.com.eje.tools.Tools;
import portal.com.eje.tools.Validar;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleList;

// Referenced classes of package portal.com.eje.serhumano.efemerides:
//            Efemerides_Manager

public class S_Santos extends MyHttpServlet {

    public S_Santos() {
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        doGet(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
    	
        Usuario user = SessionMgr.rescatarUsuario(req); 
//    	MMA 20170111
//    	Connection Conexion =  connMgr.getConnection(user.getJndi());
    	Connection Conexion =  getConnMgr().getConnection(user.getJndi());
        
        if(Conexion != null) {
            if(user.esValido()) {
            	MuestraDatos(req, resp, Conexion, user);
            }
            else {
            	super.mensaje.devolverPaginaSinSesion(resp, "Busqueda Gr\341fica", "Tiempo de Sesi\363n expirado...");
            }
            insTracking(req, "Santos".intern(), null);
        } 
        else {
            super.mensaje.devolverPaginaMensage(resp, "Problemas T\351cnicos", "Errores en la Conexi\363n.");
        }
        
//    	MMA 20170111
//      connMgr.freeConnection(user.getJndi(), Conexion);
        getConnMgr().freeConnection(user.getJndi(), Conexion);

    }

    public void MuestraDatos(HttpServletRequest req, HttpServletResponse resp, Connection Conexion, Usuario user)
        throws ServletException, IOException {
        OutMessage.OutMessagePrint("\n**** Inicio MuestraDatos: ".concat(getClass().getName()));
        SimpleHash modelRoot = new SimpleHash();
        SimpleList simplelist = new SimpleList();
        String strFechaHoy = "";
        GregorianCalendar Fecha = new GregorianCalendar();
        int dia = Fecha.get(5);
        int mes = Fecha.get(2) + 1;
        if(user.esValido()) {
        	ResourceBundle proper = ResourceBundle.getBundle("db");
            String dominio = proper.getString("portal.domain");
            Validar valida = new Validar();
            String santos = "";
            String santo = "";
            Efemerides_Manager datasantos = new Efemerides_Manager(Conexion);
            Consulta info = datasantos.GetSantos(dia, mes);
            while(info != null && info.next()) {
            	if(santos.indexOf(info.getString("santo")) == -1) {
            		if(santos.length() > 1) {
            			santos+=",";
            		}
            		
            		santos+=info.getString("santo");
            	}
            }
           
            info.close();
            
            Consulta info2;
            for(info2 = datasantos.GetSantos(dia, mes); info2.next();) {
                santo = info2.getString("santo");
                OutMessage.OutMessagePrint("\nSanto: " + santo);
                SimpleHash simplehash1;
                for(info = datasantos.GetDeSantos(santo); info.next(); simplelist.add(simplehash1)) {
                    simplehash1 = new SimpleHash();
                    simplehash1.put("rut", info.getString("rut"));
                    simplehash1.put("nombre", info.getString("nombre"));
                    simplehash1.put("anex", valida.validarDato(info.getString("anexo"), " "));
                    simplehash1.put("fec", String.valueOf(String.valueOf((new StringBuilder(String.valueOf(String.valueOf(String.valueOf(dia))))).append("/").append(String.valueOf(mes)))));
                    simplehash1.put("uni", info.getString("unidad"));
                    
                    if(info.getString("e_mail")!= null && !"".equals(info.getString("e_mail"))) {
                    	simplehash1.put("mail", info.getString("e_mail") );
                    }
                    
                    if(info.getString("mail")!= null && !"".equals(info.getString("mail"))) {
                    	simplehash1.put("mail", info.getString("mail") );
                    }
                }

            }
            info2.close();

            strFechaHoy = String.valueOf(String.valueOf((new StringBuilder("Hoy ")).append(dia).append(" de ").append(Tools.RescataMes(mes)).append(": ")));
            if(!"xxx".equals(santos)) {
            	modelRoot.put("hoy", String.valueOf(strFechaHoy) + String.valueOf(santos));
            }
            else {
            	modelRoot.put("hoy", strFechaHoy);
            }
            modelRoot.put("santos", santos);
            modelRoot.put("detalle", simplelist);
            
            IOClaseWeb io = new IOClaseWeb(this, req, resp);
            super.retTemplate(io,"efemerides/santos.htm",modelRoot);
            OutMessage.OutMessagePrint("\n**** Fin MuestraDatos: ".concat(String.valueOf(String.valueOf(getClass().getName()))));
        } 
        else {
            super.mensaje.devolverPaginaSinSesion(resp, "E-Mail", "Tiempo de Sesi\363n expirado...");
        }
    }
}