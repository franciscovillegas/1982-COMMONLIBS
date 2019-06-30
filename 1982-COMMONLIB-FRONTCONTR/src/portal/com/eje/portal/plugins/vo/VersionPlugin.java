package portal.com.eje.portal.plugins.vo;

import cl.ejedigital.tool.validar.Validar;

public class VersionPlugin {
	private int a;
	private int b;
	private int c;
	private String modificador;

	public VersionPlugin(int a, int b, int c) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public VersionPlugin(int a, int b, int c, String modificador) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		this.modificador = modificador;
	}

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public int getC() {
		return c;
	}

	public String getModificador() {
		return modificador;
	}
	
	public String getVersion() {
		return toString();
	}
	
	public double getEspecificacion() {
		return Validar.getInstance().validarDouble(String.valueOf(a) + "."+String.valueOf(b),-1);
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append(a).append(".").append(b).append(".").append(c).append(Validar.getInstance().validarDato(modificador,"")).toString();
	}

}
