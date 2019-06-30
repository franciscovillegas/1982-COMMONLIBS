package portal.com.eje.portal.organica.vo;

public class UnidadCorrelativoLocator {
	private static UnidadCorrelativoLocator instance;
	private double unicNumber;
	
	private UnidadCorrelativoLocator() {
		
	}
	
	public static UnidadCorrelativoLocator getInstance() {
		if(instance == null) {
			synchronized (UnidadCorrelativoLocator.class) {
				if(instance == null) {
					instance = new UnidadCorrelativoLocator();
				}
			}
		}
		
		return instance;
	}
	
	public double getUnicNumber() {
		return unicNumber++;
	}
	
}
