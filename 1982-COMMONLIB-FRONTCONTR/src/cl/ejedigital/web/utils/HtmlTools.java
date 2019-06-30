package cl.ejedigital.web.utils;

import java.util.Map;

import org.apache.commons.lang.WordUtils;

import cl.ejedigital.consultor.ConsultaData;

public class HtmlTools {
	private static HtmlTools instance;
	
	private HtmlTools() {
		
	}
	
	public static HtmlTools getInstance() {
		if(instance == null) {
			synchronized (HtmlTools.class) {
				if(instance == null) {
					instance = new HtmlTools();
				}
			}
		}
		
		return instance;
	}
	
	public String toHTMLList(String subTitulo, ConsultaData data, String fieldName, boolean camelCase) {
		StringBuilder html = new StringBuilder(subTitulo != null? subTitulo + ":" : "");
		if(data != null && fieldName != null) {
			int pos = data.getPosition();
			data.toStart();
			html.append("<ul>");
			while(data != null && data.next()) {
				html.append("<li>");
				String word = data.getString(fieldName);
				if(camelCase) {
					word = WordUtils.capitalizeFully(word);
					
				}
				html.append(word);
				html.append("</li>");
			}
			html.append("<ul>");
			data.setPosition(pos);
		}
		
		return html.toString();
	}
	
	public String toHTMLList_withA(String subTitulo, ConsultaData data, Map<String, String> mappingZones, boolean camelCase) {
		StringBuilder html = new StringBuilder(subTitulo != null? subTitulo + ":" : "");
		if(data != null && mappingZones != null) {
			int pos = data.getPosition();
			data.toStart();
			html.append("<ul>");
			while(data != null && data.next()) {
				html.append("<li>");
				String word 	= data.getString(mappingZones.get("text"));
				String href		= data.getString(mappingZones.get("href"));
				String onclick  = data.getString(mappingZones.get("onclick"));
				if(camelCase) {
					word = WordUtils.capitalizeFully(word);
				}
				String a = "<a href=\""+href+"\" onclick=\""+onclick+"\" >"+word+"</a>";
				html.append(a);
				html.append("</li>");
			}
			html.append("<ul>");
			data.setPosition(pos);
		}
		
		return html.toString();
	}
	
	public String toHTMLines(ConsultaData data, String fieldName, boolean camelCase) {
		StringBuilder html = new StringBuilder();
		if(data != null && fieldName != null) {
			int pos = data.getPosition();
			data.toStart();
			 
			while(data != null && data.next()) {
				 
				String word = data.getString(fieldName);
				if(camelCase) {
					word = WordUtils.capitalizeFully(word);
					
				}
				html.append(word);
				html.append("<br/>");
			}
			 
			data.setPosition(pos);
		}
		
		return html.toString();
	}
}
