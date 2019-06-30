package portal.com.eje.portal.eje_generico_util;

import portal.com.eje.portal.factory.Util;

public class RutTool {

	public static RutTool getInstance() {
		return Util.getInstance(RutTool.class);
	}

	public String calculaDigitoVerificador(int rutAux) {
		String dv = "";
		try {
			int m = 0, s = 1;
			for (; rutAux != 0; rutAux /= 10) {
				s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
			}
			dv = "" + ((char) (s != 0 ? s + 47 : 75));

		} catch (java.lang.NumberFormatException e) {
		} catch (Exception e) {
		}
		return dv;
	}

	public static void main(String[] params) {
		System.out.println(RutTool.getInstance().calculaDigitoVerificador(12660954));
	}
}
