package cl.eje.qsmcom.mantenedor;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringEscapeUtils;

import portal.com.eje.frontcontroller.AbsClaseWeb;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;
import portal.com.eje.permiso.PerfilMngr;
import portal.com.eje.tools.security.Encrypter;
import cl.eje.qsmcom.managers.ManagerTrabajador;
import cl.ejedigital.consultor.ConsultaData;
import cl.ejedigital.consultor.output.JSonDataOut;
import cl.ejedigital.tool.correo.CorreoDispatcher;
import cl.ejedigital.tool.correo.CorreoProcessPropertie;
import cl.ejedigital.tool.correo.ifaces.ICorreoBuilder;
import cl.ejedigital.tool.correo.ifaces.ICorreoProcess;
import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.correo.vo.VoDestinatario;
import cl.ejedigital.tool.validar.Validar;
import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;

public class DatosPersonales extends AbsClaseWeb {

	public DatosPersonales(IOClaseWeb ioClaseWeb) {
		super(ioClaseWeb);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doPost() throws Exception {
		doGet();
		
	}

	@Override
	public void doGet() throws Exception {
		String accion = super.getIoClaseWeb().getParamString("accion","show");
		String thing  = super.getIoClaseWeb().getParamString("thing","");
		String htm	  = super.getIoClaseWeb().getParamString("htm","mantenedor/mantCargo.html");
		
		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("accion",accion);
		PerfilMngr perfil = new PerfilMngr();
		
		if("show".equals(accion)) {
			showPagina();
		}
		else if("select".equals(accion)) {

		}
		else if("insert".equals(accion)) {

		}
		else if("update".equals(accion)) {
			if("trabajador".equals(thing)) {
				updateTrabajador();
			}
		}
		else if("delete".equals(accion)) {

		}
		else if("upload".equals(accion)) {
			
		}
		
		
	}
	
	private void showPagina() {
		int rut = Validar.getInstance().validarInt(super.getIoClaseWeb().getUsuario().getRutId(),-1);
		ConsultaData dataTrabajador = ManagerTrabajador.getInstance().getTrabajador(rut);
		SimpleHash modelRoot = new SimpleHash();
		
		modelRoot.put("rut", super.getIoClaseWeb().getUsuario().getRut().getRut());
		modelRoot.put("digito_ver", super.getIoClaseWeb().getUsuario().getRut().getDig());
		
		String options = super.getIoClaseWeb().getParamString("options", "");
		
		if(options.indexOf("h") != -1) { modelRoot.put("h", "true"); }
		if(options.indexOf("f") != -1) { modelRoot.put("f", "true"); }
		
		if(dataTrabajador != null && dataTrabajador.next()) {
			modelRoot.put("nombres", dataTrabajador.getString("nombres"));
			modelRoot.put("ape_paterno", dataTrabajador.getString("ape_paterno"));
			modelRoot.put("ape_materno", dataTrabajador.getString("ape_materno"));
			modelRoot.put("mail", dataTrabajador.getString("mail"));
			
			ConsultaData dataCorreoValido = ManagerTrabajador.getInstance().getConfirmacionCorreo(rut, dataTrabajador.getString("mail"));
			if(dataCorreoValido == null || !dataCorreoValido.next() ) {
				modelRoot.put("cuentaSinValidar", "true");
			}
		}
		
		super.getIoClaseWeb().retTemplate("mantenedor/mantDatosPersonales.html", modelRoot);
	}

	
	private void updateTrabajador() {
		int rut = Validar.getInstance().validarInt(super.getIoClaseWeb().getUsuario().getRutId(),-1);
		String n = super.getIoClaseWeb().getParamString("nombres", "Error");
		String p = super.getIoClaseWeb().getParamString("paterno", "Error");
		String m = super.getIoClaseWeb().getParamString("materno", "Error");
		String correo = super.getIoClaseWeb().getParamString("mail", "Error");
		boolean ok = true;
		
		if(ManagerTrabajador.getInstance().existeTrabajador(rut)) {
			ok = ManagerTrabajador.getInstance().updateTrabajador(rut, "nombres", super.getIoClaseWeb().getParamString("nombres", "Error"));
			ok &= ManagerTrabajador.getInstance().updateTrabajador(rut, "ape_materno", m);
			ok &= ManagerTrabajador.getInstance().updateTrabajador(rut, "ape_paterno", p);
			ok &= ManagerTrabajador.getInstance().updateTrabajador(rut, "mail", correo);
		}
		else {
			ok = ManagerTrabajador.getInstance().insertTrabajador(rut,
					super.getIoClaseWeb().getUsuario().getRut().getDig() , n +" "+p+" "+m, 
					n, p, m, null, null, null, null, null, null, correo);
		}
		
		
		
		
		HashMap<String, Object> resp = new HashMap<String, Object>();

		ICorreoBuilder cb = new CorreoValidacion(
				super.getIoClaseWeb(),
					rut,
						super.getIoClaseWeb().getParamString("nombres", "Error"),
							super.getIoClaseWeb().getParamString("mail", "Error"));
		
		ICorreoProcess cp = new CorreoProcessPropertie(cb);
		try {
			CorreoDispatcher.getInstance().sendMail(cp);
		} catch (MessagingException e) {
			e.printStackTrace();
			ok = false;
		}
		
		resp.put("ok", String.valueOf(ok));
		resp.put("confirmacionPendiente", "true");
		
		JSonDataOut out = new JSonDataOut(resp);
	
		super.getIoClaseWeb().retTexto(out.getListData());
	}
}

class CorreoValidacion implements ICorreoBuilder {
	private IOClaseWeb cw;
	private VoDestinatario vo;
	private String codigo;
	private String nombre;
	
	public CorreoValidacion(IOClaseWeb cw, int rutDestinatario, String nombre, String email ) {
		this.cw = cw;
		this.vo = new VoDestinatario(nombre, email, RecipientType.TO);
		
		StringBuffer str = new StringBuffer();
		str.append(rutDestinatario).append("|").append(vo.getEmail());
		
		Encrypter e = new Encrypter();
		
		try {
			this.codigo = URLEncoder.encode(e.encrypt(str.toString()), "ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		this.nombre = nombre;
	}
	
	public List<File> getArchivosAdjuntos() {
		List<File> f = new ArrayList<File>();
		return  f;
	}

	public String getAsunto() {
		return "QSM Comisiones - Validación de correo [".concat(nombre).concat("]");
	}

	public String getBody() {
		FreemakerTool free = new FreemakerTool();
		ResourceHtml html = new ResourceHtml();

		SimpleHash modelRoot = new SimpleHash();
		String url = cw.getReq().getRequestURL().toString();
		url = url.substring(0, url.lastIndexOf("/"));
		url = url.substring(0, url.lastIndexOf("/"));
		
		StringBuffer urlValidacion = new StringBuffer();
		urlValidacion.append(url).append("/servlet/EjeCoreI?claseweb=cl.eje.qsmcom.modulos.ValidaCorreo&accion=valida&thing=codigo&c=").append(codigo);
		
		
		modelRoot.put("servidor", url);
		modelRoot.put("codigo", codigo);
		modelRoot.put("urlValidacion",urlValidacion.toString() );
		
		
		try {
			return free.templateProcess(html.getTemplate("qsmcorreo/correoValidacion.html"), modelRoot);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public List<IVoDestinatario> getDestinatarios() {
		List<IVoDestinatario> lista = new ArrayList<IVoDestinatario>();
		lista.add(vo);
		return lista;
	}

	
}