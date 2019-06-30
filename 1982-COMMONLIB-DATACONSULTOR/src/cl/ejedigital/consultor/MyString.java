package cl.ejedigital.consultor;

/**
 * Copia al 2016-03-31
 * Se dejó como default porque el paqueta la necesitaba
 * */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @deprecated Se debe usar el del otro paquete
 * */
public class MyString {

	public MyString() {
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
}
