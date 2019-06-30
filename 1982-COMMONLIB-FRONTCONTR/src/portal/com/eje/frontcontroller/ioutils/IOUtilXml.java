package portal.com.eje.frontcontroller.ioutils;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

import portal.com.eje.frontcontroller.IIOClaseWebLight;
import portal.com.eje.frontcontroller.pages.PageBasic;
import portal.com.eje.portal.factory.Weak;
import portal.com.eje.portal.pdf.HttpFlow;

public class IOUtilXml extends IOUtil {

	public static IOUtilXml getIntance() {
		return Weak.getInstance(IOUtilXml.class);
	}
	
	/**
	 * Retorna un xml en una página dentro de tag pre, el parametro xml no debe estar con Escape
	 * Error no tabula
	 * 
	 * @author Pancho
	 * @since 23-05-2018
	 */
	
	public boolean retXml(IIOClaseWebLight  io, Document doc) {
		io.getResp().setContentType("text/html");
		PageBasic basic = new PageBasic();
		
		OutputFormat format = new OutputFormat(doc);
		format.setLineWidth(1000);
		format.setIndenting(true);
		format.setIndent(4);
		
		StringWriter outxml = new StringWriter();
		XMLSerializer serializer = new XMLSerializer(outxml, format);
		try {
			serializer.serialize(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		return io.retTexto(basic.getPre(StringEscapeUtils.escapeHtml(outxml.toString())));
	}
	
	public boolean retXml(IIOClaseWebLight  io, HttpFlow xml) {
		io.getResp().setContentType("text/html");
		PageBasic basic = new PageBasic();
		
		return io.retTexto(basic.getPre(StringEscapeUtils.escapeHtml(xml.getFlow())));
	}
}
