package portal.com.eje.tools.fileupload.ifaces;

import java.util.List;

/**
 * Cambiado por su homologá en el paquete cl.ejedigital.web.
 * 
 * @deprecated 
 * 
 * */
public interface IParametrosGetter {

	/**
	 * Retorna un parametro en su forma String, en el caso de que el parametro sea nulo o no valido
	 * retorna el valor por defecto
	 * 
	 * @return String
	 * */
	public String getParamString(String paramName, String valDefecto);

	
	/**
	 * Retorna un parametro casteado a int, en el caso de que el parametro sea nulo o no valido
	 * retorna el valor por defecto
	 * 
	 * @return int
	 * */
	public int getParamInt(String paramName, int valDefecto);
	
	
	/**
	 * Retorna un parametro casteado a long, en el caso de que el parametro sea nulo o no valido
	 * retorna el valor por defecto
	 * 
	 * @return long
	 * */
	public long getParamLong(String paramName, long valDefecto);
	
	
	/**
	 * Retorna un parametro casteado a double, en el caso de que el parametro sea nulo o no valido
	 * retorna el valor por defecto
	 * 
	 * @return double
	 * */
	public double getParamDouble(String paramName, double valDefecto);
	
	
	/**
	 * Retorna un parametro casteado a String[], en el caso de que el parametro sea nulo o no valido
	 * retorna un arreglo de String (String[]) tan largo como el tercer parametro lo indique, este metodo utiliza el getParamValues. </br>
	 * El parametro valDefecto indica el valor con el que se rellenaran los bloques que falten
	 * para completar el largo del String[] devuelto.
	 * 
	 * @param 
	 * @return String[]
	 * */
	public String[] getParamValues(String paramName, String valDefecto, int largo);
	
	
	/**
	 * Exactamente lo mismo que request.getParamValues();
	 * 
	 * @param 
	 * @return String[]
	 * */
	public String[] getParamValues(String paramName, String valDefecto);

	
	/**
	 * Retorna una lista que contiene  objetos Integer (List&lt;Integer&gt;) cuyo valor indica
	 * el indice del archivo subido
	 * 
	 * @return List&lt;Integer&gt;
	 * */
	public List getFiles();
	
	public String getRelativePath();
	
}
