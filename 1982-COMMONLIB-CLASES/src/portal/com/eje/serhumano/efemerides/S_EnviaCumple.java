package portal.com.eje.serhumano.efemerides;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import portal.com.eje.serhumano.httpservlet.MyHttpServlet;
import portal.com.eje.serhumano.user.SessionMgr;
import portal.com.eje.serhumano.user.Usuario;
import repository.RepositoryManager;
import cl.ejedigital.tool.correo.CorreoDispatcher;
import cl.ejedigital.tool.correo.CorreoProcessPropertie;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;

import com.google.gson.JsonObject;

public class S_EnviaCumple extends MyHttpServlet implements javax.servlet.Servlet {
   
	private static final long serialVersionUID = 1L;
	private Usuario user;
	
	public S_EnviaCumple() {
		super();
	}   	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			user = SessionMgr.rescatarUsuario(request);
			if (!user.esValido()) {
				super.mensaje.devolverPaginaSinSesion(response,"ERROR de Sesi&oacute;n","Su sesi&oacute;n ha expirado o no se ha logeado.");
				return;
			}
			else {
				response.setContentType("application/json");
				String asunto = request.getParameter("asunto");
				String email = request.getParameter("email");
				
				String dedicatoria = request.getParameter("comments");
				//Connection Conexion = connMgr.getConnection(user.getJndi());
				
				System.out.println(asunto);
				System.out.println(email);
				System.out.println(dedicatoria);
				
				
				boolean ok = false;
				ICorreoBuilder cb = new CorreoDedicatoria(asunto,email,dedicatoria,user.getName());
				ICorreoProcess cp = new CorreoProcessPropertie(cb); 
				//cp.setMailDesde(user.getEmail(Conexion, user.getRutId()));
				//cp.setNombreDesde(user.getName());
				try {
					ok = CorreoDispatcher.getInstance().sendMail(cp);
				} 
				catch (MessagingException e) {
					e.printStackTrace();
					ok = false;
				}
				//connMgr.freeConnection(user.getJndi(), Conexion);
				
				String msgResultado = ok ? "La Dedicatoria fue enviada con &eacute;xito..." : "La Dedicatoria no pudo ser enviada, intentelo en un momento...";
				JsonObject jObject = new JsonObject();
				RepositoryManager rmng = new RepositoryManager();
				jObject.addProperty("mensaje",msgResultado);
				response.getOutputStream().print(jObject.toString());
				
				response.getOutputStream().flush();
			}
		}
		catch (Exception ex) {      
			ex.printStackTrace();
		}
	}  	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}  
	
}


class CorreoDedicatoria implements ICorreoBuilder {
	private String emailDestinatario;
	private String dedicatoriaDestinatario;
	private String nombreAmigo;
	private String asunto;
	private VoDestinatario vo;
	
	public CorreoDedicatoria(String asunto, String email, String mensaje, String amigo) {
		this.emailDestinatario = email;
		this.dedicatoriaDestinatario = mensaje;
		this.nombreAmigo = amigo;
		this.vo = new VoDestinatario("Festejado", email, RecipientType.TO);
		this.asunto = asunto;
	}
	
	public String getAsunto() {
		return asunto;
	}

	public String getBody() {
		String msg = "Estimado(a):<br><br>Tu compañero(a) <b>" + this.nombreAmigo + 
					"</b> te a enviado una dedicatoria:<br><br><b><i><ul>\"" +this.dedicatoriaDestinatario + 
					"\"</ul></i></b><br><br> Felicidades.";
		return msg;
	}

	public List<File> getArchivosAdjuntos() {
		return null;
	}

	public List<IVoDestinatario> getDestinatarios() {
		List<IVoDestinatario> lista = new ArrayList<IVoDestinatario>();
		lista.add(vo);
		return lista;
	}
	
}
