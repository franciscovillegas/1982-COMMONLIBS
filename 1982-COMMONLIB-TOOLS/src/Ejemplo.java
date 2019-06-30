import java.math.BigDecimal;

import cl.ejedigital.tool.misc.Formatear;



public class Ejemplo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i = 0 ; i< 30 ; i++) {
			double num = (int) (Math.random() * 100000D);
			System.out.println(new BigDecimal(num) + " -->"+Formatear.getInstance().trasformaATexto(num));
		}
		
		

	}

}
