package portal.com.eje.portal.correspondencia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.ejedigital.tool.correo.vo.IVoDestinatario;
import cl.ejedigital.tool.validar.Validar;
import portal.com.eje.portal.correspondencia.CorrespondenciaBuilder;
import portal.com.eje.tools.ListUtils;

public class CorrespondenciaTagged {
	private Map<String,Object> tags;
	private CorrespondenciaBuilder cb;
	
	
	public CorrespondenciaTagged(CorrespondenciaBuilder cb) {
		super();
		this.cb = cb;
		tags = new HashMap<String,Object>(); 
	}
	
	public void addTag(String key, Object value) {
		tags.put(key, value);
	}

	public Map<String, Object> getTags() {
		return tags;
	}

	public CorrespondenciaBuilder getCb() {
		return cb;
	}
	
	public String getCadenaDeDestinatarios() {
		StringBuilder str = new StringBuilder();
		
		for(IVoDestinatario ivo : cb.getDestinatarios() ) {
			StringBuilder mail = new StringBuilder();
			
			String mailDest = ivo.getEmail() != null? ivo.getEmail() : "nodefinido";
			
			mail.append( ivo.getNombre() ).append("<").append(mailDest).append(">");
			
			if(!str.toString().contentEquals(mail)) {
				if(!"".equals(str.toString())) {
					str.append(",");
				}

				str.append(mail);
			}
			
		}
		
		return str.toString();
	}
	
	public String getTagValues() {
		StringBuilder str = new StringBuilder();
		
		for(Object o : tags.values()) {
			String toString = String.valueOf(o);
			if(str.toString().indexOf(toString) == -1) {
				if(!"".equals(str.toString())) {
					str.append(",");
				}
				
				str.append(toString);
			}
		}
		
		return str.toString();
	}
	
}
