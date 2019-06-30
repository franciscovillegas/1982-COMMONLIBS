package cl.ejedigital.tool.strings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.text.Normalizer;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import org.apache.commons.lang.StringEscapeUtils;

import portal.com.eje.portal.factory.Util;

public class MyString {
	public static String cadena_alfanumerica = "abcdefghijklmnopqkstuvwxyz1234567890";
	
	public MyString() {
	}
	
	public static MyString getInstance() {
		return Util.getInstance(MyString.class);
	}
	
	public String replaceCadena(String palabra,String busca,String reemplaza){
		String resultado = null;
		try {
			Pattern patron = Pattern.compile(busca);
			Matcher encaja = patron.matcher(palabra);
			resultado = encaja.replaceAll(reemplaza);
		} catch (Exception e) {
			System.out.println("Ha ocurrido un error en \"replaceCadena\"");
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	public String rellenaCadena(String str,char relleno,int largo){
		String pal=str;
		while(pal.length()<largo){
			pal = relleno + pal;
		}
		return pal;
	}
	
	public String getRandomString(String charsPermitidos,int largo){
		
		String pal = "";
	
		while(pal.length() < largo) {
			int rnd = (int) (Math.random() * (charsPermitidos.length()-1)); 
			pal += String.valueOf(charsPermitidos.toCharArray()[rnd]);
		}
		
		return pal;
	}
	
	public String getRandomString(int largo){
		return getRandomString("0123456789", largo);
	}
	
	public StringBuilder readFile(File file, String encodeFile ) throws FileNotFoundException {
		StringBuilder text = new StringBuilder();
		Scanner scanner = new Scanner(new FileInputStream(file), encodeFile);
		try {
			while(scanner.hasNextLine()) {
				text.append(scanner.nextLine() + "\n");
			}
		}
		finally {
			scanner.close();
		}
		
		return text;
	}
	
	public String cortarString(String dato, int largo)
    {
        if(dato == null)
            return "";
        dato = dato.trim();
        if(dato.length() > largo)
            dato = String.valueOf(String.valueOf(dato.substring(0, largo>3? largo - 3: largo))).concat("...");
        return dato;
    }
	
	public String cortarStringSS(String dato, int largo)
    {
        if(dato == null)
            return "";
        dato = dato.trim();
        if(dato.length() > largo)
            dato = String.valueOf(String.valueOf(dato.substring(0, largo>3? largo - 3: largo)));
        return dato;
    }

	
	  public String quitaEspacios(String texto) {
	        java.util.StringTokenizer tokens = new java.util.StringTokenizer(texto);
	        StringBuilder buff = new StringBuilder();
	        while (tokens.hasMoreTokens()) {
	            buff.append(" ").append(tokens.nextToken());
	        }
	        return buff.toString().trim();
	    }
	  
	public String quitaCEspeciales(String nombre) {
		return quitaCEspeciales(nombre, "_");
	}

	public String quitaCEspeciales(String nombre, String reemplazo) {
		if (nombre != null) {
			nombre = nombre.replaceAll("\\ ", reemplazo);
			nombre = (nombre.replaceAll("[^\\dA-Za-z]", reemplazo));
			/* Otro Regex */
			nombre = (nombre.replaceAll("[\\W]|_", reemplazo));
			/* Otro Regex */
			nombre = (nombre.replaceAll("[^a-zA-Z0-9]", reemplazo));
		}

		return nombre;
	}
	
	public String normalizaString(String original) {
		String cadenaNormalize = Normalizer.normalize(original, Normalizer.Form.NFD);   
		String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
		return cadenaSinAcentos;
	}

	
	public String normalizaStringFromXml(String html) {
		final StringBuilder sb = new StringBuilder();
		HTMLEditorKit.ParserCallback parserCallback = new HTMLEditorKit.ParserCallback() {
		    public boolean readyForNewline;

		    @Override
		    public void handleText(final char[] data, final int pos) {
		        String s = new String(data);
		        sb.append(s.trim());
		        readyForNewline = true;
		    }

		    @Override
		    public void handleStartTag(final HTML.Tag t, final MutableAttributeSet a, final int pos) {
		        if (readyForNewline && (t == HTML.Tag.DIV || t == HTML.Tag.BR || t == HTML.Tag.P)) {
		            sb.append("\n");
		            readyForNewline = false;
		        }
		    }

		    @Override
		    public void handleSimpleTag(final HTML.Tag t, final MutableAttributeSet a, final int pos) {
		        handleStartTag(t, a, pos);
		    }
		};
		String retorno = null;
		try {
			new ParserDelegator().parse(new StringReader(html), parserCallback, false);
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return normalizaString(sb.toString());
	}
		
		public String convertFromUTF8(String s) {
	        String out = null;
	        try {
	            out = new String(s.getBytes("ISO-8859-1"), "UTF-8");

	        } catch (java.io.UnsupportedEncodingException e) {
	            return null;
	        }
	        return out;
	    }
	 
	    // convert from internal Java String format -> UTF-8
	    public String convertToUTF8(String s) {
	        String out = null;
	        try {
	            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
	        } catch (java.io.UnsupportedEncodingException e) {
	            return null;
	        }
	        return out;
	    }
	    
	    public String getUniversalFromUTF8(String s) {
	    	 String out = convertFromUTF8(s);
	    	 
	    	  out = StringEscapeUtils.escapeJava(out);
	            out = out.replace("\\u00A0", " "); 
	            
	            return out;
	    }
	    
	/**
	 * Se entiende que el numero fue un punto de montaje temporal mientras una
	 * versión estaba en jdk11 y la otra en jdk6
	 */
	public String quitaNumeros(String montaje) {
		if (montaje != null) {
			montaje = montaje.replaceAll("[0-9]", "");
		}

		return montaje;
	}
	
	
	
}
