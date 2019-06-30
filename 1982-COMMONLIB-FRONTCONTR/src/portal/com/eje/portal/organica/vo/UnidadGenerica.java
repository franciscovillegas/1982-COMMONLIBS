package portal.com.eje.portal.organica.vo;

public class UnidadGenerica extends UnidadGenericaBase {

	protected UnidadGenerica() {
		super();
		
	}

	public UnidadGenerica(String name, boolean sumValuesToParent) {
		super(name, sumValuesToParent);
		
	}

	public UnidadGenerica(String name, String pathImage, boolean sumValuesToParent) {
		super(name, pathImage, sumValuesToParent);
		
	}

	public UnidadGenerica(String name, String pathImage) {
		super(name, pathImage);
		
	}

	public UnidadGenerica(String name) {
		super(name);
		
	}

	public static IUnidadGenerica newRoot() {
		return new UnidadGenerica(DIOS_STR);
	}
	
}
