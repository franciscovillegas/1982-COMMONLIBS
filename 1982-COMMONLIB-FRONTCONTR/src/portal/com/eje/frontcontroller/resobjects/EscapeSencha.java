package portal.com.eje.frontcontroller.resobjects;

import org.apache.commons.lang.StringEscapeUtils;

import cl.ejedigital.consultor.output.IEscape;

public class EscapeSencha implements IEscape {

	public String escape(String normal) {
		return StringEscapeUtils.escapeJava(normal);
	}
	
}
 
