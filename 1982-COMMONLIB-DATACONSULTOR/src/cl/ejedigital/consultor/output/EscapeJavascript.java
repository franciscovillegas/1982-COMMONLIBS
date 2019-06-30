package cl.ejedigital.consultor.output;

import org.apache.commons.lang.StringEscapeUtils;

public class EscapeJavascript implements IEscape {

	public String escape(String normal) {
		return StringEscapeUtils.escapeJava(normal);
	}
	
	
}
