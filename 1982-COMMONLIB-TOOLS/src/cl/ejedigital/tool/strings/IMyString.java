package cl.ejedigital.tool.strings;


public interface IMyString {

	public String replaceCadena(String palabra,String busca,String reemplaza);	
	
	public String rellenaCadena(String str,char relleno,int largo);
	
	public String getRandomString(String charsPermitidos,int largo);
	
	public String getRandomString(int largo);
	
}
