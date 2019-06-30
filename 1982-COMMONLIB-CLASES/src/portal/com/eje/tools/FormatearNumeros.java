package portal.com.eje.tools;

import java.text.NumberFormat;
import java.util.Locale;

/**Clase que permite dar formato local a un numero entero o flotante
* @author     EjeDigital
* @version     1.0, 27/04/07
*/
public class FormatearNumeros {

	/**
	 * Devuelve la representacion ENTERA en formato local de un número.
	 * @param numero	número a formatear.
	 * @return ret		valor formateado.
	 */
    public static String numero(int numero) {
        String ret = "0";
        ret = NumberFormat.getInstance(new Locale("es","CL")).format(new Integer(numero));
        return ret;
    }

	/**
	 * Devuelve la representacion DECIMAL en formato local de un número dado la cantidad de decimales
	 * @param numero	número a formatear.
	 * @param cantDec	cantidad de decimales.
	 * @return ret		número formateado.
	 */
    public static String numero(float numero, int cantDec) {
        String ret = "0";
        NumberFormat f = NumberFormat.getInstance(new Locale("es","CL"));
        f.setMinimumFractionDigits(cantDec);
        f.setMaximumFractionDigits(cantDec);
        ret = f.format(new Float(numero));
        return ret;
    }
}