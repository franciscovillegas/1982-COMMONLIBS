package portal.com.eje.portal.parametro;

public class ParametroKey   {
	private double idParam;
	private String nemo;
	private double idModulo;

	public ParametroKey(double idParam, String nemo, double idModulo) {
		super();
		this.idParam = idParam;
		this.nemo = nemo;
		this.idModulo = idModulo;
	}

	public double getIdParam() {
		return idParam;
	}

	public String getNemo() {
		return nemo;
	}

	public double getIdModulo() {
		return idModulo;
	}

}
