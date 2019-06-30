package cl.eje.mail.modulos;

import cl.ejedigital.web.FreemakerTool;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import portal.com.eje.frontcontroller.IOClaseWeb;
import portal.com.eje.frontcontroller.resobjects.ResourceHtml;

public class CorreoRecuperaClaveFallida extends CorreoRecuperaClave {
	protected String nuevoCorreo;
	protected String nuecoFono;
	private Template template;
	private String nombre;
	private String nombreAdmin;
 
	
	public CorreoRecuperaClaveFallida(IOClaseWeb cw, String rutDestinatario,
			Template template,
			String nombreAdmin,
			String nombre, 
			String email,
			String password, String nuevoCorreo, String nuevoFono) {
		super(cw, rutDestinatario, nombre, email, password);
		
		this.nombreAdmin = nombreAdmin;
		this.nombre = nombre;
		this.nuecoFono = nuevoFono;
		this.nuevoCorreo = nuevoCorreo;
		this.template = template;
	}
 
	public String getBody() {
		FreemakerTool free = new FreemakerTool();
		ResourceHtml html = new ResourceHtml();

		SimpleHash modelRoot = new SimpleHash();
		modelRoot.put("nombreAdmin", this.nombreAdmin );
		modelRoot.put("nombre", this.nombre );
		modelRoot.put("rut", rutDestinatario);
		
		modelRoot.put("auto", "true" );
		modelRoot.put("usuario", rutDestinatario );
		modelRoot.put("clave", pass );
		
		modelRoot.put("adicional", "La persona adicionalmente entrego su correo :"+cw.getParamString("correo", "No definido") +" y teléfono :"+ cw.getParamString("fono", "No definido"));
		modelRoot.put("correo",cw.getParamString("correo", "No definido"));
		modelRoot.put("fono",cw.getParamString("fono", "No definido"));
		
		String url = cw.getReq().getRequestURL().toString();
		url = url.substring(0, url.lastIndexOf("/"));
		url = url.substring(0, url.lastIndexOf("/"));
		
		modelRoot.put("servidor", url);
		
		
		return free.templateProcess(template , modelRoot);
 
	}
}