package portal.com.eje.tools;

import org.apache.commons.lang.StringEscapeUtils;

import cl.ejedigital.consultor.output.IEscape;

public class escapeJavascript implements IEscape{

	public String escape(String arg0) {
		return StringEscapeUtils.escapeJava(arg0);

	}

}
